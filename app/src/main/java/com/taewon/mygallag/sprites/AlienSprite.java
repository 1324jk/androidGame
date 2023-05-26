package com.taewon.mygallag.sprites;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;
import java.util.Random;

public class AlienSprite extends Sprite{    //외계인 스프라이트 관련 기능 구현
    private Context context;
    private SpaceInvadersView game;
    ArrayList<AlienShotSprite> alienShotSprites;
    Handler fireHandler = null;
    boolean isDestroyed = false;
    public AlienSprite(Context context, SpaceInvadersView game, int resId, int x, int y){
        //AlienSprite 클래스의 생성자
        //외계인 스프라이트를 생성하고 초기화
        super(context, resId, x, y);    //resId는 외계인 이미지의 리소스 아이디, x,y는 초기 위치
        this.context = context;
        this.game=game;
        alienShotSprites = new ArrayList<>();
        Random r = new Random();
        int randomDx = r.nextInt(5);
        int randomDy = r.nextInt(5);
        if(randomDy <= 0) dy=1;
        dx = randomDx; dy=randomDy;
        fireHandler = new Handler(Looper.getMainLooper());
        fireHandler.postDelayed(  //1초뒤에 실행되는 작업을 예약
                //delay주는 함수
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d("run", "동작");
                        Random r = new Random();         //랜덤한 확률로 외계인이 총알을 발사할지 결정 (30%확률로 ifFire이 true가 됨)
                        boolean isFire = r.nextInt(100)+1 <=30;   //nextInt(100)+1  1~100사이의 난수 생성
                        if(isFire && !isDestroyed){
                            fire();
                            fireHandler.postDelayed(this, 1000);  //1000=1초
                        }
                    }
                }, 1000);    // 1초마다 작업 반복
    }

    @Override
    public void move() {
        super.move();  //외계인 이동시키기
        if(((dx<0) && (x<10)) || ((dx>0) && (x>800))){           //외계인이 화면에 닿았는지 확인
            dx = -dx;                     //경계(좌10, 우800)에 닿으면 dx값을 반대로 바꿈.
            if(y > game.screenH){ game.removeSprite(this); }
            //y 좌표가 화면높이를 초과하면 game.removeSprite(this)를 호출해서 외계인을 제거
        }
    }

    @Override
    public void handleCollision(Sprite other) {  //외계인이 다른 스프라이트와 충돌했을때
        if(other instanceof ShotSprite){    //외계인과 총알이 충돌했는지 확인
            game.removeSprite(other);    //총알 스프라이트 제거
            game.removeSprite(this);     //외계인 스프라이트 제거
            destroyAlien();           //외계인 파괴
            return;
        }
        if (other instanceof SpecialshotSprite) {   //외계인과 스페셜샷이 충돌했는지 확인
            game.removeSprite(this);     // 외계인 스프라이트 제거
            destroyAlien();         //외계인 파괴
            return;
        }
    }
    private void destroyAlien() {    //외계인 스프라이트 파괴 수행
        isDestroyed = true;       //변수를 true로 설정해서 외계인 스프라이트가 파괴되었음을 표시
        game.setCurrEnemycount(game.getCurrEnemyCount()-1);   // 현재 외계인 수 1감소
        for (int i=0; i<alienShotSprites.size(); i++)
            game.removeSprite(alienShotSprites.get(i)); //alienShotSprites리스트에 저장된 총알 스프라이트 제거
        spawnHealItem();    //회복 아이템 생성
        spawnPowerItem();   //공격력 강화 아이템 생성
        spawnSpeedItem();   //이속 증가 아이템 생성
        game.setScore(game.getScore() + 1);    //점수 1 증가
        MainActivity.scoreTv.setText(Integer.toString(game.getScore()));  //화면에 현재 점수 표시
    }
    private void fire(){  //외계인이 공격하는 기능
        AlienShotSprite alienShotSprite = new AlienShotSprite(context, game,
                getX(), getY()+30, 16);  //AlienShotSprite객체를 생성해서 외계인의 총알 스프라이트 생성
                                               //총알의 초기위치는 외계인의 위치에서 약간 아래에
        alienShotSprites.add(alienShotSprite);  //생성된 총알 스프라이트를 총알 스프라이트 리스트에 추가
        game.getSprites().add(alienShotSprite);  //생성된 총알 스프라이트를 게임 스프라이트 리스트에 추가(화면에 표시될 수 있게)
    }
    private void spawnSpeedItem(){    //외계인이 죽을때 속도 아이템을 생성
        Random r = new Random();
        int speedItemDrop = r.nextInt(100) + 1;   //1~100사이의 랜덤값 생성
        if(speedItemDrop <= 5){          //값이 5이하일 경우 속도 아이템 생성
            int dx = r.nextInt(10)+1;       //랜덤으로 아이템의 이동속도를 결정(x축 y축 따로)
            int dy = r.nextInt(10) + 5;
            game.getSprites().add(new SpeedItemSprite(context,game,(int) this.getX(), (int) this.getY(), dx, dy));
            //생성된 속도 아이템 스프라이트를 게임 스프라이트 리스트에 추가.(화면에 보이게)
        }
    }

    private void spawnPowerItem() {
        Random r = new Random();
        int powerItemDrop = r.nextInt(100) +1;
        if(powerItemDrop <= 3) {
            int dx = r.nextInt(10) +1;
            int dy = r.nextInt(10) +10;
            game.getSprites().add(new PowerItemSprite(context, game, (int)this.getX(), (int)this.getY(), dx, dy));
        }
    }

    private void spawnHealItem(){
        Random r = new Random();
        int powerItemDrop = r.nextInt(100) +1;
        if(powerItemDrop <= 1) {
            int dx = r.nextInt(10) +1;
            int dy = r.nextInt(10) + 10;
            game.getSprites().add(new HealitemSprite(context, game,
                    (int) this.getX(), (int)this.getY(), dx, dy));
        }
    }
}
