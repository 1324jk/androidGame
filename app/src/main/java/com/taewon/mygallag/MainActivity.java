package com.taewon.mygallag;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {      //+ AppCompatActivity는 안드로이드 앱 개발에서 제공되는 기본 클래스 중 하나
                                                           // 이 클래스가 AppCompatActivit를 상속받아 메인 화면을 담당하는 클래스임을 나타냄

    //변수 선언
    private Intent userIntent;
    ArrayList<Integer> bgMusicList;
    public static SoundPool effectSound;
    public static float effectVolumn;
    ImageButton specialShotBtn;
    public static ImageButton fireBtn, reloadBtn;
    JoystickView joyStick;
    public static TextView scoreTv;
    LinearLayout gameFrame;
    ImageView pauseBtn;
    public static LinearLayout lifeFrame;
    SpaceInvadersView spaceInvadersView;
    public static MediaPlayer bgMusic;
    int bgMusicIndex;
    public static TextView bulletCount;
    private static ArrayList<Integer> effectSoundList;
    public static final int PLAYER_SHOT = 0;
    public static final int PLAYER_HURT = 1;
    public static final int PLAYER_RELOAD = 2;
    public static final int PLAYER_GET_ITEM = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //Activity가 생성될때 자동 실행됨. 초기화 작업 수행
        super.onCreate(savedInstanceState);         //savedInstanceState는 onCreate의 매개변수로, 이전에 저장된 액티비티 상태 정보를 담고 있음
        setContentView(R.layout.activity_main);     //setContentView 메소드 호출

        userIntent = getIntent();          //getIntent()는 인텐트 객체를 가져오는 메소드
        bgMusicIndex = 0;
        bgMusicList = new ArrayList<Integer>(); //음악넣을 arrylist생성    //bgMusicList 변수에 Integer형식의 ArrayList객체 생성
                                                                                        // bgMusicList에는 음악 리소스ID를 저장
        bgMusicList.add(R.raw.main_game_bgm1);      //add 메소드를 사용해서 리스트에 ID를 추가
        bgMusicList.add(R.raw.main_game_bgm2);
        bgMusicList.add(R.raw.main_game_bgm3);


        effectSound = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE, 0);  //SoundPool클래스는 효과음 재생을 구현하는데에 사용됨
                                                                                                    //인자= maxStreams: 동시에 재생할 수 있는 최대 스트림 수
                                                                                                    //     streamType: 오디오 스트림 유형
                                                                                                    //     srcQuality: 오디오 품질
        // srcQuality는 0또는 1의 값을 가질 수 있음. 0은 기본(낮은)품질, 1는 높은 품질, 높은 메모리 사용량


        effectVolumn = 1;   //효과음을 최대 볼륨으로 설정

        //findViewById는 레이아웃 파일에 정의된 View(뷰) 객체를 코드에서 사용하기 위해 참조하는 메소드
        specialShotBtn = findViewById(R.id.specialShotBtn); //번개
        joyStick = findViewById(R.id.joyStick);
        scoreTv = findViewById(R.id.score); //TextView
        fireBtn = findViewById(R.id.fireBtn); //ImageButton
        reloadBtn = findViewById(R.id.reloadBtn);//ImageButton
        gameFrame = findViewById(R.id.gameFrame); //첫번째 LinearLayout
        pauseBtn = findViewById(R.id.pauseBtn);//ImageButton
        lifeFrame = findViewById(R.id.lifeFrame);//윗부분 LinearLayout

        init();
        setBtnBehavior(); //조이스틱 작동함수 실행
    }

    @Override
    protected void onResume() {       //onResume()는 MainActivity가 활성화될 때 호출되는 콜백 메소드. onCreate() 다음에 호출됨.
        super.onResume();
        bgMusic.start();                    //배경음악 시작
        spaceInvadersView.resume();        //게임 화면을 다시 시작
    }

    private void init() {           // init()은 게임화면을 초기화 하고 시작하는 역할을 하는 메소드
        Display display = getWindowManager().getDefaultDisplay();  //view의 display를 얻어온다.
        // WindowManager객체를 가져와서 getDefaultDisplay()메소드를 호출하고 현재 화면의 Display객체를 가져옴
        Point size = new Point();   //Point 객체 생성
        display.getSize(size);    //Display객체의 getSize메소드를 호출하여 화면 크기를 size객체에 저장
        //??? 아이템 화면에 띄우기
        spaceInvadersView = new SpaceInvadersView(this, userIntent.getIntExtra("character", R.drawable.ship_0000), size.x, size.y);
        // spaceInvadersView는 게임 화면을 구성하는 뷰. this는 MainActivity를 가리키는 참조
        gameFrame.addView(spaceInvadersView); //프레임에 만든 아이템들 넣기
        //음악 바꾸기
        changeBgMusic();
        bgMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //음악이 끝나면 다음 배경음악 재생
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                changeBgMusic();
            }
        });

        bulletCount = findViewById(R.id.bulletCount);   //총알 개수

        // spaceInvadersView의 getPlayer() 구현
        bulletCount.setText(spaceInvadersView.getPlayer().getBulletsCount() + "/30");  // 30/30표시
        scoreTv.setText(Integer.toString(spaceInvadersView.getScore())); //score:0 표시

        effectSoundList = new ArrayList<>();
        effectSoundList.add(PLAYER_SHOT, effectSound.load(MainActivity.this, R.raw.player_shot_sound, 1));
        effectSoundList.add(PLAYER_HURT, effectSound.load(MainActivity.this, R.raw.player_hurt_sound, 1));
        effectSoundList.add(PLAYER_RELOAD, effectSound.load(MainActivity.this, R.raw.reload_sound, 1));
        effectSoundList.add(PLAYER_GET_ITEM, effectSound.load(MainActivity.this, R.raw.player_get_item_sound, 1));
        bgMusic.start();  //음악이 바뀌면서 재생
    }

    private void changeBgMusic() {
        bgMusic = MediaPlayer.create(this, bgMusicList.get(bgMusicIndex)); //음악하나 가져와 만들기
        bgMusic.start();
        bgMusicIndex++; //음악 바꾸기 위해 증가해 놓기
        bgMusicIndex = bgMusicIndex % bgMusicList.size(); //음악 개수만큼만 바뀌게 하기 위해
    }    //bgMusicIndex가 bgMusicList의 크기보다 크다면 다시 0으로 초기화해서 리스트에 있는 음악들을 무한 반복 재생

    @Override
    protected void onPause() {   //일시 정지시
        super.onPause();
        bgMusic.pause();
        spaceInvadersView.pause();      //배경음악과 View 일시정지. 백그라운드로 이동시에 진행X
    }

    public static void effectSound(int flag) {
        effectSound.play(effectSoundList.get(flag), effectVolumn, effectVolumn,
                0, 0, 1.0f);  //priority는 재생시 다른 소리와 충돌했을때 우선순위를 정하는 인자. loop는 반복재생 설정, rate는 재생속도
        //Soundpool 실행
    }


    private void setBtnBehavior() {       // 조이스틱 설정
        joyStick.setAutoReCenterButton(true);       // 조이스틱의 버튼이 원래 위치로 돌아가게끔 설정
        joyStick.setOnKeyListener(new View.OnKeyListener() {      //조이스틱의 이벤트 처리.
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("keycode", Integer.toString((i)));
                return false;
            }
        });

        //조이스틱 이동방향으로 비행기 이동하게 한다
        joyStick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d("angle", Integer.toString(angle));
                Log.d("force", Integer.toString(strength));
                if (angle > 67.5 && angle < 112.5) {
                    //위
                    spaceInvadersView.getPlayer().moveUp(strength / 10);
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle > 247.5 && angle < 292.5) {
                    //아래
                    spaceInvadersView.getPlayer().moveDown(strength / 10);
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle > 112.5 && angle < 157.5) {
                    //왼쪽 대각선 위
                    spaceInvadersView.getPlayer().moveUp(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveLeft(strength / 10 * 0.5);
                } else if (angle > 157.5 && angle < 202.5) {
                    //왼쪽
                    spaceInvadersView.getPlayer().moveLeft(strength / 10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle > 202.5 && angle < 247.5) {
                    //왼쪽 대각선 아래
                    spaceInvadersView.getPlayer().moveLeft(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength / 10 * 0.5);
                } else if (angle > 22.5 && angle < 67.5) {
                    //오른쪽 대각선 위
                    spaceInvadersView.getPlayer().moveUp(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveRight(strength / 10 * 0.5);
                } else if (angle > 337.5 || angle < 22.5) {
                    //오른쪽
                    spaceInvadersView.getPlayer().moveRight(strength / 10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle > 292.5 && angle < 337.5) {
                    //오른쪽 아래
                    spaceInvadersView.getPlayer().moveRight(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength / 10 * 0.5);
                }
            }
        });

        //총알 버튼 눌렀을 때
        fireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceInvadersView.getPlayer().fire();
            }
        });   //총알 발사 기능 구현


        //리로드 버튼 눌렀을 때
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceInvadersView.getPlayer().reloadBullets();
                //SpaceInvadersView -> StarshipSprite -> reloadButtlets()
            }
        });  //총알 재충전 기능 구현


        // 일시정지 버튼 눌렀을 때
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceInvadersView.pause();  //spaceInvadersView 일시정지
                PauseDialog pauseDialog = new PauseDialog(MainActivity.this);
                pauseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        spaceInvadersView.resume(); //spaceInvadersView 종료
                    }
                });
                pauseDialog.show(); //pauseDialog 띄움
            }
        }); //게임 화면 일시정지 시키고 pauseDialog를 띄움. 다이얼로그가 닫힐때 게임을 재개함.


        // 스페셜샷 버튼 눌렀을 때
        specialShotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spaceInvadersView.getPlayer().getSpecialShotCount() >= 0)
                    // 0보다 큰 경우에만 specizlShot()메소드를 호출해서 스페셜샷을 사용할 수 있음
                    spaceInvadersView.getPlayer().specialShot();
            }
        });

    }
}