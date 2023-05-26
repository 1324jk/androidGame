package com.taewon.mygallag;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;

public class PauseDialog extends Dialog {         //일시정지 기능. Dialog클래스를 상속받음.
    // Dialog 클래스는 보통 대화상자(dialog)를 나타내는 데에 사용되는 JavaFX 클래스.
    // 사용자와의 상호작용을 통해 정보를 표시하고 입력받을 수 있는 창을 생성하기 위함.
                              // JavaFX는 GUI 및 멀티미디어 애플리케이션을 개발하기 위한 플랫폼.

    //라디오 그룹을 정의함
    RadioGroup bgMusicOnOff;  //배경음 OnOff
    RadioGroup effectSoundOnOff;  //효과음 OnOff


    //PauseDialog의 생성자를 정의
    public PauseDialog(@NonNull Context context){
        super(context);                           //부모클래스인 Dialog클래스의 생성자를 호출
        setContentView(R.layout.pause_dialog);   //현재 다이얼로그의 UI를 pause_dialog로 설정.
        bgMusicOnOff = findViewById(R.id.bgMusicOnOff);
        effectSoundOnOff = findViewById(R.id.effectSoundOnOff);   //pause_dialog의 UI요소를 찾아옴.
        init();    //init()메서드를 호출
    }

    public void init(){      //일시정지 다이얼로그 내에서 배경음악 onoff버튼의 동작을 처리함
        bgMusicOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //setOnCheckedChangeListener은 체크 상태 변경 이벤트를 감지하기 위해 사용되는 메소드

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //onCheckedChanged는 체크상태가 변경될때 호출되는 콜백 메소드.
                // 매개변수 group은 이벤트가 발생한 RadioGroup객체를 참조하고 checkedId는 선택된 라디오 버튼의 리소스 ID를 나타냄
                switch (checkedId) {
                    case R.id.bgMusicOn:      //bgMusicOn이라는 ID를 가진 라디오 버튼이 선택되었을 때
                        // MainActivity.bgMusic의 볼륨을 1로 설정
                        MainActivity.bgMusic.setVolume(1,1);  //왼쪽 스피커 볼륨, 오른쪽 스피커 볼륨
                        break;
                    case R.id.bgMusicOff:    //bgMusicOff라는 ID를 가진 라디오 버튼이 선택되었을 때
                        // MainActivity.bgMusic의 볼륨을 0으로 설정
                        MainActivity.bgMusic.setVolume(0,0);
                        break;
                }
            }
        });
                           //효과음 onoff버튼의 동작을 처리함
        effectSoundOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.effectSoundOn:
                        MainActivity.effectVolumn =1.0f;    //1.0f = 최대볼륨
                        // 배경음과 효과음은 볼륨을 설정하는 방식이 다르게 구현되어 있음.
                        break;
                    case R.id.effectSoundOff:
                        MainActivity.effectVolumn = 0;
                        break;
                }
            }
        });

        // 취소 버튼 눌렀을 때 현재 다이얼로그 종료하는 기능
        findViewById(R.id.dialogCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dismiss();}
            //dismiss()메소드는 다이얼로그를 강제로 닫음. 닫을때 실행되는 애니메이션 등의 효과를 건너뜀.
            //주로 다이얼로그를 정상적으로 닫을 때는 dismiss()를 사용하고 cancle()은 보통 다이얼로그를 백키로 닫거나 외부영역을 터치하여 취소할때 호출됨.

        });


        // Ok버튼 눌렀을 때 현재 다이얼로그 종료. 이전 액티비티가 나타남
        findViewById(R.id.dialogOkBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dismiss();}
        });
    }
}
