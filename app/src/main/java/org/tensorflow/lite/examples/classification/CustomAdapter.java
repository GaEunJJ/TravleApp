package org.tensorflow.lite.examples.classification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<TodoItem> mTodoItems;
    private Context mContext;
    private DBHelper mDBHelper;
    public double convertDouble;

    String conversionValue;

    public CustomAdapter(ArrayList<TodoItem> mTodoItems, Context mContext) {
        this.mTodoItems = mTodoItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(holder);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.tv_writeDate.setText(mTodoItems.get(position).getWriteDate());
        holder.tv_sort.setText(mTodoItems.get(position).getSort());
        holder.tv_info.setText(mTodoItems.get(position).getUser());
        holder.tv_price.setText(String.valueOf(mTodoItems.get(position).getEx_money()));

    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_writeDate;
        private TextView tv_sort;
        private TextView tv_info;
        private TextView tv_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_writeDate = itemView.findViewById(R.id.tv_date);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_info = itemView.findViewById(R.id.tv_info);
            tv_price = itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(new View.OnClickListener() {    //item_list의 하나
                @Override
                public void onClick(View v) {
                    int curPos = getAdapterPosition();  // 현재 리스트 아이템 위치
                    TodoItem todoItem = mTodoItems.get(curPos);

                    String[] strChoiceItems = {"수정하기", "삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {

                            if(position==0){
                                // 수정하기
                                // 팝업창 띄우기
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);
                                EditText et_sort = dialog.findViewById(R.id.et_sort);
                                EditText et_info = dialog.findViewById(R.id.et_info);
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                                et_sort.setText(todoItem.getSort());
                                et_info.setText(todoItem.getUser());
                                // 수정할때 커서 뒤쪽으로 이동
                                et_sort.setSelection(et_sort.getText().length() - 1);


                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        // update table
                                        String sort = et_sort.getText().toString();
                                        String info = et_info.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 날짜, 시간 (수정 시점)
                                        String beforeTime = todoItem.getWriteDate();    // 이전 등록된 시간
                                        Double total = todoItem.getTotal(); //////////
                                        String conversionValue = todoItem.getEx_money();

                                        //mDBhelper에 시간, 항목, 내용, 금액 넣음
                                        mDBHelper.UpdateTodo(currentTime, sort, info, conversionValue, total, beforeTime);

                                        // update UI
                                        todoItem.setWriteDate(currentTime);
                                        todoItem.setSort(sort);
                                        todoItem.setUser(info);
                                        todoItem.setEx_money(conversionValue);
                                        todoItem.setTotal(total);
                                        notifyItemChanged(curPos, todoItem);
                                        dialog.dismiss();   //dialog 꺼지게함
                                        Toast.makeText(mContext, "목록 수정이 완료 되었습니다", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                dialog.show();


                            }
                            else if(position==1){   // 삭제하기
                                String beforeTime = todoItem.getWriteDate();
                                String conversionValue = todoItem.getEx_money();
                                mDBHelper.deleteTodo(beforeTime);
                                //delete UI
                                mTodoItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                convertDouble = Double.parseDouble(conversionValue);
                                todoItem.setTotal(todoItem.getTotal()-convertDouble);
                                notifyItemChanged(curPos, todoItem);
                                Toast.makeText(mContext, "목록이 제거되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    builder.show();

                }
            });

        }
    }

    // 액티비티에서 호출되는 함수이며, 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가하는 목적이다.
    public void addItem(TodoItem _item){
        mTodoItems.add(0, _item);   // 역순으로 정렬되도록
        notifyItemChanged(0);
    }
}
