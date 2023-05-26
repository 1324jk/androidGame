package com.taewon.mygallag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class ResultActivity extends AppCompatActivity {   // AppCompatActivity클래스를 상속받음
    //AppCompatActivity는 기본 클래스 중 하나. 향상된 기능을 제공하고 안드로이드의 이전 버전과의 호환성을 지원하기 위해 사용됨.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {  //액티비티가 생성될때 호출되는 생명주기 메소드. 초기화 작업 수행
                //savedInstanceState는 onCreate의 매개변수로 전달되는 Bundle객체.
                   // 액티비티가 재생성 되는 경우(예: 화면회전, 시스템 구성 변경 등)에 이전상태 정보를 저장하고 복원하는 데에 사용됨.

        super.onCreate(savedInstanceState);  //부모 클래스인 AppCompatActivity의 onCreate()메소드 호출.
        setContentView(R.layout.game_over_dialog); //이 액티비티에서 표시할 레이아웃 파일을 지정
        init();
    }

    private void init(){
        findViewById(R.id.goMainBtn).setOnClickListener(new View.OnClickListener() {
            //game_over_dialog의 goMainBtn을 가져옴
            //goMainBtn(처음으로 버튼)에 클릭 이벤트 리스너 설정
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, StartActivity.class);
                //현재 액티비티에서 다른 액티비티로 전환하기 위해 Intent객체를 생성하는 코드

                startActivity(intent);          //startActivity(intent)를 호출하여 전환 실행
                finish();                       //시작한 후 현재 ResultActivity를 종료
            }
        });
        ((TextView)findViewById(R.id.userFinalScoreText)).setText(getIntent().getIntExtra("score",0)+"");
    }       //findViewById() 메소드를 사용하여 userFinalScoreText 텍스트뷰를 찾아서, 이전 화면에서 전달받은 점수 데이터를 출력
            //getIntent()는 현재 액티비티로 전달된 Intent객체를 가져오는 메서드
            //getIntExtra는 Intent객체에서 정수형 데이터를 추출하는 메서드.
           // SpaceInvadersView의 endGame()
}
