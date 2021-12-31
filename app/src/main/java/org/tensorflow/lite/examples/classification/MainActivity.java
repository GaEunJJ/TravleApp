package org.tensorflow.lite.examples.classification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
// 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
// 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton go_camera = (ImageButton) findViewById(R.id.go_camera);
        ImageButton go_map = (ImageButton) findViewById(R.id.go_map);
        ImageButton go_cafe = (ImageButton) findViewById(R.id.go_cafe);
        ImageButton go_hotel = (ImageButton) findViewById(R.id.go_hotel);
        ImageButton go_tour = (ImageButton) findViewById(R.id.go_tour);
        ImageButton go_ExchangeRate = (ImageButton) findViewById(R.id.go_ExchangeRate);
        ImageButton go_login = (ImageButton) findViewById(R.id.go_login);
        // gif 옆 글자 누르면 넘어감
        Button button = findViewById(R.id.button);  // Text : 건축물 인식
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);// Text : 환전과 지출내역
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);// Text : 여행객 커뮤니티
        Button button6 = findViewById(R.id.button6);

        Glide.with(this).load(R.raw.backimage11).into(go_camera);
        Glide.with(this).load(R.raw.backimage22).into(go_ExchangeRate);
        Glide.with(this).load(R.raw.backimage33).into(go_login);

        go_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);
            }
        });

        go_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/search/%EC%9D%8C%EC%8B%9D%EC%A0%90/"));
                startActivity(intent);
            }
        });
        go_cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/search/%EC%B9%B4%ED%8E%98/"));
                startActivity(intent);
            }
        });
        go_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/search/%ED%98%B8%ED%85%94/"));
                startActivity(intent);
            }
        });

        go_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/search/%EA%B4%80%EA%B4%91%EB%AA%85%EC%86%8C/"));
                startActivity(intent);
            }
        });

        go_ExchangeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Exchange_Rate.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Exchange_Rate.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Exchange_Rate.class);
                startActivity(intent);
            }
        });

        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BagiccommunityActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "하단 우측의 로그인 버튼을 누르면 글을 작성할 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BagiccommunityActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "하단 우측의 로그인 버튼을 누르면 글을 작성할 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BagiccommunityActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "하단 우측의 로그인 버튼을 누르면 글을 작성할 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
}