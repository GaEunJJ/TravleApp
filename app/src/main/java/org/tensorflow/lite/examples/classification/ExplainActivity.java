package org.tensorflow.lite.examples.classification;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ExplainActivity extends AppCompatActivity {

    ImageView imageViewSet;    // 설명 화면에서 이미지 뷰
    TextView landmarkname;
    TextView explainlong;   // 설명 화면에서 설명 텍스트 뷰
    TextView edit_play;
    TextView shortExplain;
    TextView explainTip;
    int var = ((CameraActivity)CameraActivity.context_main).value;
    MediaPlayer mp_basils, mp_college, mp_san, mp_eiffel, mp_colosseum;

    // MediaPlayer 객체생성
    MediaPlayer mediaPlayer;

    // 재생버튼
    ImageButton listen;

    public void onBackPressed() {   // 뒤로가기시 음성 중지
        mediaPlayer.pause();
        finish();
    }



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        listen = (ImageButton) findViewById(R.id.listen);
        imageViewSet = (ImageView)findViewById(R.id.imageViewSet);
        landmarkname = (TextView)findViewById(R.id.landmarkname);
        shortExplain = (TextView)findViewById(R.id.shortExplain);
        explainlong = (TextView)findViewById(R.id.explainlong);
        edit_play = findViewById(R.id.edit_play);
        explainTip = findViewById(R.id.explainTip);

        if (var == 0) {
            listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // MediaPlayer 객체 할당
                    mediaPlayer = MediaPlayer.create(ExplainActivity.this, R.raw.saintbasils_rec);
                    if(mediaPlayer.isPlaying()){
                        edit_play.setText("설명 듣기");
                        mediaPlayer.pause();
                    }

                    else{

                        edit_play.setText("설명 일시정지");
                        mediaPlayer.start();
                    }
                }
            });
            imageViewSet.setImageResource(R.drawable.saint_basils_cathedral);
            landmarkname.setText(" 성 바실리 대성당 ");
            shortExplain.setText(" 독특한 외관을 가진 러시아의 대표적인 성당 ");
            explainlong.setText("성 바실리 대성당은 모스크바의 붉은 광장에 있는 러시아 정교회 성당이다.\n"
                    +"그 당시 모스크바 대공국의 대공이었던 이반 4세가 "
                    + "러시아에서 카잔 칸을 몰아낸 것을 기념하며 봉헌한 성당이다.\n"
                    + "1555년 건축을 시작하여 1561년 완공하였다.\n"
                    + "1600년에 이반 대제의 종탑이 완공되기 전까지는 러시아에서 가장 높은 건축물이기도 하였다.");
            explainTip.setText("√  러시아 미술 문화의 심장과 같은 극장 \'볼쇼이 극장\'도 방문해 보세요. \n"
                                + "√  모스크바의 유명한 쇼핑 명소인 \'아르바트 거리\'에 가셔서 멋진 기념품을 쇼핑해 보세요 .\n"
                                + "√  바로 옆 \'붉은 광장\' 구경도 빼놓지 마세요!");
        }
        else if (var == 1) {
            // MediaPlayer 객체 할당
            mediaPlayer = MediaPlayer.create(ExplainActivity.this, R.raw.hyupsung_rec);
            listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaPlayer.isPlaying()){
                        edit_play.setText("설명 듣기");
                        mediaPlayer.pause();
                    }

                    else{

                        edit_play.setText("설명 일시정지");
                        mediaPlayer.start();
                    }
                }
            });
            imageViewSet.setImageResource(R.drawable.college);
            landmarkname.setText(" 협성대학교 이공관 ");
            shortExplain.setText(" 협성대 공돌이들의 꿈과 희망이 가득한 곳 ");
            explainlong.setText("협성대학교 이공관은 협성대학교 이공대학의 6개학과인\n"
                    + "도시공학과, 건축공학과, 컴퓨터공학과, 보건관리학과,\n"
                    + "스마트시스템소프트웨어공학과, 생명공학과가 있으며 강의실, 교수 연구실 등이 있다.");
            explainTip.setText("√  1층에 위치한 카페에서 음료와 간식 구매가 가능합니다! \n"
                    + "√  공강 시간이 길다면 학교 정문에서 10분 거리인 CGV를 방문해 보세요.\n"
                    + "√  깨끗하고 편리한 시설을 갖춘 이공관을 편하게 구경해보세요.");

        }
        else if (var == 2) {
            // MediaPlayer 객체 할당
            mediaPlayer = MediaPlayer.create(ExplainActivity.this, R.raw.sanmarco_rec);
            listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaPlayer.isPlaying()){
                        edit_play.setText("설명 듣기");
                        mediaPlayer.pause();
                    }

                    else{

                        edit_play.setText("설명 일시정지");
                        mediaPlayer.start();
                    }
                }
            });
            imageViewSet.setImageResource(R.drawable.san_marco);
            landmarkname.setText(" 산 마르코 광장 ");
            shortExplain.setText(" 세상에서 가장 아름다운 응접실 ");
            explainlong.setText("산마르코 광장은 이탈리아 베네치아에 위치한 광장으로,\n" +
                    "베네치아의 가장 유명한 광장이며 베네치아의 정치적, 종교적 중심지 역할을 하던 광장이다.\n" +
                    "베네치아에서는 산 마르코 광장을 단순히 '광장' (la Piazza)라고 지칭한다.");
            explainTip.setText("√  한 해의 마지막 날엔 불꽃 놀이가 펼쳐집니다.\n"
                    + "√  광장 카페에서 커피 한잔의 여유를 가지면 좋아요.\n"
                    + "√  비오는 날엔 광장에 물이차니 유의해주세요.");

        }
        else if (var == 3) {
            // MediaPlayer 객체 할당
            mediaPlayer = MediaPlayer.create(ExplainActivity.this, R.raw.eiffel_rec);
            listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaPlayer.isPlaying()){
                        edit_play.setText("설명 듣기");
                        mediaPlayer.pause();
                    }

                    else{

                        edit_play.setText("설명 일시정지");
                        mediaPlayer.start();
                    }
                }
            });
            imageViewSet.setImageResource(R.drawable.eiffel_tower);
            landmarkname.setText(" 에펠탑 ");
            shortExplain.setText(" 파리 건축의 상징 ");
            explainlong.setText("에펠 탑은 1889년 파리 마르스 광장에 지어진 탑이다.\n" +
                    "프랑스의 대표 건축물인 이 탑은 격자 구조로 이루어져 파리에서 가장 높은 건축물이며," +
                    "매년 수백만 명이 방문할 만큼 세계적인 유료 관람지이다.\n" +
                    "이를 디자인한 프랑스 공학자 및 건축가 귀스타브 에펠의 이름에서 명칭을 얻었으며,\n" +
                    "1889년 프랑스 혁명 100주년 기념 세계 박람회의 출입 관문으로 건축되었다.");
            explainTip.setText("√  영업 시간 : 오전 10시 30분 ~ 오후 5시 30분.\n"
                    + "√  탑 전체를 가까이서 보고 싶다면 샤이오 궁전 앞이나 샹드마르 공원이 좋습니다.\n"
                    + "√  해가 지면 매 시간 정시에 5분간 화이트 에펠 조명이 켜집니다.");

        }
        else if (var == 4) {
            // MediaPlayer 객체 할당
            mediaPlayer = MediaPlayer.create(ExplainActivity.this, R.raw.colosseum_rec);
            listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaPlayer.isPlaying()){
                        edit_play.setText("설명 듣기");
                        mediaPlayer.pause();
                    }

                    else if (!mediaPlayer.isPlaying())
                    {
                        edit_play.setText("설명 일시정지");
                        mediaPlayer.start();
                    }
                }
            });
            imageViewSet.setImageResource(R.drawable.colosseum);
            landmarkname.setText(" 콜로세움 ");
            shortExplain.setText(" 로마를 대표하는 웅장한 건축물 ");
            explainlong.setText("콜로세움은 고대 로마 시대의 건축물 가운데 하나로 로마 제국 시대에 만들어진 원형 경기장이다.  " +
                    "석회암, 응회암, 콘크리트 등으로 지어져 있고, 5만 명의 관중을 수용할 수 있었다.\n" +
                    "로마의 중심지에 위치하여 있고, 현재는 로마를 대표하는 유명한 관광지로 탈바꿈하였다.\n" +
                    "콜로세움이라는 이름은 근처에 있었던 네로 황제의 동상(巨像:colossus)에서 유래한다.\n" +
                    "원래 이름은 플라비우스 원형 경기장으로, 서기 72년 베스파시아누스 황제가 착공해 8년 뒤에 아들인 티투스 황제가 완공했다.");
            explainTip.setText("√  인터넷으로 미리 예약을 하거나 로마패스를 사서 가는 것이 좋습니다. \n"
                    + "√  콜로세움 앞에 잡상인들이 많으니 조심하세요.\n"
                    + "√  밤에보는 콜로세움의 야경은 더욱 멋있습니다.");

        }
    }

    public void onClick1(View view) {
        mediaPlayer.stop();
        finish();
    }
}