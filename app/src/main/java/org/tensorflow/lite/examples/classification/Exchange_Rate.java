package org.tensorflow.lite.examples.classification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Exchange_Rate extends AppCompatActivity {

    TextView convertFromDropdownTextView, convertToDropdownTextView, conversionRateText, text_detail, textView,text_date, text_sort, text_info, text_price;
    EditText amountToConvert;
    ArrayList<String> arrayList;
    Dialog fromDialog;
    Dialog toDialog;
    ImageButton convertButton;
    String convertFromValue, convertToValue;
    String[] country = {"EUR","GBP","USD","KRW","CNY","RUB","JPY"};  // 해당 사이트에서 참고 후 있다면 화폐 추가 가능
    SQLiteDatabase sqlDB;
    private RecyclerView mRv_todo;
    private FloatingActionButton mBtn_write;
    private ArrayList<TodoItem> mTodoItems;
    private DBHelper mDBHelper;
    public static Context context_main;
    public String conversionValue; // 다른 activity에서 접근할 변수 (conversionValue)
    private CustomAdapter mAdapter;
    public Double convertDouble;
    public Double sum = 0.0 ;
    int food, travel, souvenir, traffic, etc = 0;
    String btnnum;     // 버튼 항목구분하기 위해서

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange__rate);

        setInit();  // 얘를 환율 이후에 불러야하나

        convertFromDropdownTextView = findViewById(R.id.convert_from_dropdown_menu);    // xml : '나라를 선택하세요' -> 여행지역
        convertToDropdownTextView = findViewById(R.id.convert_to_dropdown_menu);        // xml : '나라를 선택하세요' -> 거주지역
        convertButton = findViewById(R.id.conversionButton);                            // xml : '변환'버튼
        conversionRateText = findViewById(R.id.conversionRateText);                     // xml : 입력값 변환 후 출력 (금액)
        amountToConvert = findViewById(R.id.amountToConvertValueEditText);              // xml : 사용자 입력 (금액)
        context_main = this;

        arrayList = new ArrayList<>();
        for(String i : country){
            arrayList.add(i);
        }

        convertFromDropdownTextView.setOnClickListener(new View.OnClickListener() {     // xml : '나라를 선택하세요' -> 여행지역
            @Override
            public void onClick(View v) {
                fromDialog = new Dialog(Exchange_Rate.this);
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(650,800);
                fromDialog.show();

                EditText editText = fromDialog.findViewById(R.id.edit_text);
                ListView listView = fromDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Exchange_Rate.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {   // 텍스트 입력시 변화와 동시에 처리해주고자 할때.
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {             // 화폐 리스트 목록 중 하나 선택시
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        convertFromDropdownTextView.setText(adapter.getItem(position));
                        fromDialog.dismiss();
                        convertFromValue = adapter.getItem(position);
                    }
                });
            }


        });

        convertToDropdownTextView.setOnClickListener(new View.OnClickListener() {                   // xml : '나라를 선택하세요' -> 거주지역
            @Override
            public void onClick(View v) {
                toDialog = new Dialog(Exchange_Rate.this);
                toDialog.setContentView(R.layout.to_spinner);
                toDialog.getWindow().setLayout(650, 800);
                toDialog.show();

                EditText editText = toDialog.findViewById(R.id.edit_text);
                ListView listView = toDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Exchange_Rate.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        convertToDropdownTextView.setText(adapter.getItem(position));
                        toDialog.dismiss();
                        convertToValue = adapter.getItem(position);
                    }
                });

            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double amountToConvert = Double.valueOf(Exchange_Rate.this.amountToConvert.getText().toString());
                    getConversionRate(convertFromValue, convertToValue, amountToConvert);
                }
                catch (Exception e){

                }
            }
        });


    }
    private void setInit(){     // 초기화 구문
        mDBHelper = new DBHelper(this);
        mRv_todo = findViewById(R.id.rv_todo);
        mBtn_write = findViewById(R.id.btn_write);  // + 버튼
        mTodoItems = new ArrayList<>();

        // load recent DB
        loadRecentDB();

        mBtn_write.setOnClickListener(new View.OnClickListener() {      // + 버튼
            @Override
            public void onClick(View v) {
                // 팝업창 띄우기
                Dialog dialog = new Dialog(Exchange_Rate.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_edit);
                EditText et_sort = dialog.findViewById(R.id.et_sort);
                EditText et_info = dialog.findViewById(R.id.et_info);
                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                btn_ok.setOnClickListener(new View.OnClickListener() {  // 확인 버튼 (항목, 사용내역)
                    @Override
                    public void onClick(View v) {
                        try {
                            // Insert Database
                            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 현재 날짜, 시간

                            convertDouble = Double.parseDouble(conversionValue);
                            sum += convertDouble;


                            //mDBHelper에 시간, 항목, 내용, 금액, 총 금액 넣음
                            mDBHelper.InsertTodo(currentTime, et_sort.getText().toString(), et_info.getText().toString(), conversionValue, sum);

                            // Insert UI

                            TodoItem item = new TodoItem();
                            item.setWriteDate(currentTime);
                            item.setSort(et_sort.getText().toString());
                            item.setUser(et_info.getText().toString());
                            item.setEx_money(conversionValue);
                            item.setTotal(sum);

                            mAdapter.addItem(item);

                            mRv_todo.smoothScrollToPosition(0);
                            dialog.dismiss();
                            Toast.makeText(Exchange_Rate.this, "지출내역에 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(Exchange_Rate.this, "금액, 항목, 내용을 다시 확인하세요!", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                dialog.show();


            }
        });

    }


    private void loadRecentDB() {
        // 저장되어있던 DB를 가져온다.
        mTodoItems = mDBHelper.getTodoList();
        if(mAdapter == null){
            mAdapter = new CustomAdapter(mTodoItems, this);
            mRv_todo.setHasFixedSize(true);
            mRv_todo.setAdapter(mAdapter);

        }
    }

    public String getConversionRate(String convertFrom, String convertTo, Double amountToConvert){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://free.currconv.com/api/v7/convert?q="+convertFrom+"_"+convertTo+"&compact=ultra&apiKey=c346b4ac08714f81bf59008e33944a38";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Double conversionRateValue = round(((Double) jsonObject.get(convertFrom + "_" + convertTo)), 2);
                    conversionValue = "" + round((conversionRateValue * amountToConvert), 2);
                    DecimalFormat myFormatter = new DecimalFormat("###,###.##");
                    String formattedStringPrice = myFormatter.format(Double.parseDouble(conversionValue));
                    conversionRateText.setText(formattedStringPrice +" "+ "(" +  convertToValue+ ")");                            // conversionValue가 api로 부터 가져온 값?

                    //convertDouble = Double.parseDouble(conversionValue);
                    //sum += convertDouble;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        return null;
    }
    public static double round(double value, int places){
        if(places<0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}