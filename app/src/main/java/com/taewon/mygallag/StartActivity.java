package com.taewon.mygallag;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    int characterId, effectId;
    ImageButton startBtn;
    TextView guideTv;
    MediaPlayer mediaPlayer;
    ImageView imgView[] = new ImageView[8];
    Integer img_id[] = {R.id.ship_001, R.id.ship_002, R.id.ship_003, R.id.ship_004, R.id.ship_005, R.id.ship_006, R.id.ship_007, R.id.ship_008};
    Integer img[] = {R.drawable.ship_0000, R.drawable.ship_0001, R.drawable.ship_0002, R.drawable.ship_0003, R.drawable.ship_0004, R.drawable.ship_0005, R.drawable.ship_0006, R.drawable.ship_0007};
    SoundPool soundPool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);   //activity_start.xml의 레이아웃을 화면에 보여줌

        mediaPlayer = MediaPlayer.create(this, R.raw.robby_bgm);  //robby_bgm.mp3을 재생하기 위해 MediaPlayer객체 생성
        mediaPlayer.setLooping(true);              //반복재생 true
        mediaPlayer.start();     //음악 재생
        soundPool = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE, 0);
        //slundPool은 mediaPlayer와 비슷하지만 효과음을 처리하는 데에 더 적합함. 최대 5개 파일을 동시에 재생가능.
        effectId = soundPool.load(this, R.raw.reload_sound, 1); //reload_sound파일을 SoundPool객체에 로드
        startBtn = findViewById(R.id.startBtn); //시작버튼
        guideTv = findViewById(R.id.guideTv);   //마지막 TextView      // 버튼들을 찾아서 변수에 할당


        // 캐릭터 선택 기능 구현
        for (int i=0; i< imgView.length; i++){
            imgView[i] = findViewById(img_id[i]);
            int index = i; //선택한 이미지 번호 알기
            imgView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    characterId = img[index];
                    startBtn.setVisibility(View.VISIBLE);
                    startBtn.setEnabled(true);
                    startBtn.setImageResource(characterId); //버튼에 선택한 이미지 넣기
                    guideTv.setVisibility(View.INVISIBLE); //마지막 TextView 숨기기
                    soundPool.play(effectId,1,1,0,0,1.0f); //소리 재생
                }
            });
        }
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT); // 화면 세로로 돌리기
        init();
        }
        private void init(){   //게임 시작버튼 설정. 버튼 클릭됐을때 MainActivity로 이동
        findViewById(R.id.startBtn).setVisibility(View.GONE); //버튼 위치는 남겨두고 숨기기
        findViewById(R.id.startBtn).setEnabled(false);  //선택 안되게 하기
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {       // 선택한 캐릭터 ID를 전달
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("character", characterId); //선택한 이미지 넘기기
                startActivity(intent);
                finish(); //액티비티 종료
            }
        });
        }

    @Override
    protected void onDestroy() {    //액티비티 소멸직전 호출 mediaPlayer가 살아있으면 리소스 소멸시킨다.   //메모리 누수 방지
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer =null;
        }
    }
}
