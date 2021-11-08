package com.example.TapTitan;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static java.lang.Math.abs;

public class PetchoiceActivity extends AppCompatActivity {

    Button clbtn, pet1;
    int empty,count,dpt,dps,mlife,plife,gold,kill,temp,pet,petBuffNumber;
    long pressedTime,aTimer,toTimer;
    float startX,startX2,startY,startY2,intervalx,intervaly;
    String m,b,t,s;
    TextView money,blife,tdpt,tdps,autoBuff;
    Intent close, getevery;
    ImageView Impet,attacked,attacked2,skill;
    CountDownTimer autoTimer;
    Random petBuff = new Random();
    MainActivity mainActivity=new MainActivity();
    ProgressBar health;
    FrameLayout backg;
    int back[]={R.drawable.taptitanbasic,R.drawable.taptitan1,R.drawable.taptitan2,R.drawable.taptitan3,R.drawable.taptitan4};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tap Titan");
        setContentView(R.layout.petchoice);

        petBuffNumber = petBuff.nextInt(2 - 1 + 1) + 1;
        clbtn=(Button)findViewById(R.id.closebtn);
        pet1=(Button)findViewById(R.id.pet1);
        money=(TextView)findViewById(R.id.money);
        blife=(TextView)findViewById(R.id.bosslife);
        tdpt=(TextView)findViewById(R.id.dpt);
        tdps=(TextView)findViewById(R.id.dps);
        Impet=(ImageView) findViewById(R.id.Impet);
        autoBuff=(TextView)findViewById(R.id.skilltimer);
        attacked=(ImageView)findViewById(R.id.attacked);
        attacked2=(ImageView)findViewById(R.id.attacked2);
        skill=(ImageView)findViewById(R.id.skilled);
        health=(ProgressBar)findViewById(R.id.health);
        backg=(FrameLayout)findViewById((R.id.back));

        getevery=getIntent();

        close=new Intent(getApplicationContext(),MainActivity.class);

        gold=getevery.getIntExtra("gold",0);
        dpt=getevery.getIntExtra("dpt",1);
        dps=getevery.getIntExtra("dps",0);
        mlife=getevery.getIntExtra("mlife",100);
        plife=getevery.getIntExtra("plife",100);
        kill=getevery.getIntExtra("kill",0);
        count=getevery.getIntExtra("count",0);
        toTimer=getevery.getLongExtra("toTimer",30000);
        temp=getevery.getIntExtra("temp",0);
        pet=getevery.getIntExtra("pet",0);
        //펫버프 두번공격

        health.setProgress((int)((float)plife/(float)mlife*100));
        m=getString(R.string.money);
        b=getString(R.string.life);
        t=getString(R.string.dpt);
        s=getString(R.string.dps);
        backg.setBackground(getDrawable(back[count]));

        money.setText(String.format(m,gold));
        blife.setText(String.format(b,plife,mlife));
        tdpt.setText(String.format(t,dpt));
        if(dps>0)
        tdps.setText(String.format(s,dps));

        if(temp == 1){
            pet1.setVisibility(View.VISIBLE);
        }
        else{
            pet1.setVisibility(View.INVISIBLE);
        }
        if(pet == 1){
            Impet.setVisibility(View.VISIBLE);
        }

        clbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.putExtra("mlife",mlife);
                close.putExtra("plife",plife);
                close.putExtra("dpt",dpt);
                close.putExtra("gold",gold);
                empty=getevery.getIntExtra("lvupgold",1);
                close.putExtra("lvupgold",empty);
                empty=getevery.getIntExtra("level",1);
                close.putExtra("level",empty);
                close.putExtra("kill",kill);
                close.putExtra("dps",dps);
                close.putExtra("count",count);
                empty=getevery.getIntExtra("bgmstart",0);
                close.putExtra("bgmstart",empty);
                close.putExtra("temp", temp);
                close.putExtra("pet", pet);
                close.putExtra("toTimer",toTimer);
                startActivity(close);
            }
        });

        pet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Impet.setVisibility(View.VISIBLE);
                pet=1;
                dpt+=dpt;
                //pet = 2;

                tdpt.setText(String.format(t,dpt));
                temp = 2;
            }
        });
        if(toTimer!=30000){//자동터지버프유지
            aTimer=toTimer;
            autoTimer=new CountDownTimer(toTimer,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    aTimer-=1000;
                    autoBuff.setText("스킬쿨타임"+aTimer/1000+"초");
                }

                @Override
                public void onFinish() {
                    autoBuff.setText("");
                }
            }.start();
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX=(int) event.getX(0);
                startY=(int) event.getY(0);
                plife-=dpt;
                health.setProgress((int)((float)plife/(float)(mlife)*100));
                blife.setText(String.format(b,plife,mlife));
                attacked.setX(startX-140);
                attacked.setY(startY-500);
                attacked.setVisibility(View.VISIBLE);
                if(plife<=0){
                    kill++;
                    mlife+=50*kill;
                    plife=mlife;
                    gold+=10*kill;
                }
                money.setText(String.format(m,gold));


                //this.invalidate();
                mainActivity.sward.start();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                startX2=(float) event.getX(1);
                startY2=(float) event.getY(1);
                plife-=dpt;
                if(plife<=0){
                    kill++;
                    mlife+=50*kill;
                    plife=mlife;
                    gold+=10*kill;
                }
                money.setText(String.format(m,gold));
                blife.setText(String.format(b,plife,mlife));
                health.setProgress((int)((float)plife/(float)(mlife)*100));
                attacked2.setX(startX2-140);
                attacked2.setY(startY2-550);
                attacked2.setVisibility(View.VISIBLE);
                mainActivity.sward.start();
                if(event.getPointerCount()==2){
                    intervalx= 0;
                    intervaly= 0;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                attacked2.setVisibility(View.INVISIBLE);

                skill.setVisibility(View.INVISIBLE);

                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount()==2){
                    float nintervalx= abs(event.getX(0)-event.getX(1));
                    float nintervaly= abs(event.getY(0)-event.getY(1));
                    if(intervalx<nintervalx && intervaly<nintervaly && aTimer==0){
                        plife-=dpt*5;
                        skill.setVisibility(View.VISIBLE);
                        mainActivity.skills.start();
                        aTimer=toTimer;
                        autoTimer=new CountDownTimer(toTimer,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                aTimer-=1000;
                                autoBuff.setText("스킬쿨타임"+aTimer/1000+"초");
                            }
                            @Override
                            public void onFinish() {
                                autoBuff.setText("");
                            }
                        }.start();
                    }
                }
                if(plife<=0){
                    kill++;
                    mlife+=50*kill;
                    plife=mlife;
                    gold+=10*kill;
                }
                health.setProgress((int)((float)plife/(float)(mlife)*100));
                blife.setText(String.format(b,plife,mlife));
                money.setText(String.format(m, gold));
                if(event.getPointerCount()==1) {
                    attacked.setVisibility(View.INVISIBLE);
                }
                break;
            case MotionEvent.ACTION_UP:
                attacked.setVisibility(View.INVISIBLE);

                skill.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);

    }
    @Override
    public void onBackPressed() {//뒤로가기버튼을 1.5초사이에 두번 누르면 프로그램 종료
        close.putExtra("mlife",mlife);
        close.putExtra("plife",plife);
        close.putExtra("dpt",dpt);
        close.putExtra("gold",gold);
        empty=getevery.getIntExtra("lvupgold",1);
        close.putExtra("lvupgold",empty);
        empty=getevery.getIntExtra("level",1);
        close.putExtra("level",empty);
        close.putExtra("kill",kill);
        close.putExtra("dps",dps);
        close.putExtra("count",count);
        empty=getevery.getIntExtra("bgmstart",0);
        close.putExtra("bgmstart",empty);
        close.putExtra("temp", temp);
        close.putExtra("pet", pet);
        close.putExtra("toTimer",aTimer);

        startActivity(close);

    }

}