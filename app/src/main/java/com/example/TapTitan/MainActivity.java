package com.example.TapTitan;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pre;
    private SharedPreferences.Editor edi;
    static float startX=-1,startY=-1,startX2=-1,startY2=-1,intervalx,intervaly;//x좌표 y좌표 초기화
    final static MusicService musicservice=new MusicService();
    int mlife,plife, gold, dpt,dps,lupg,dptadd,level,kill,count,bgmstart,temp,pet,charging;
    long pressedTime,aTimer,toTimer;//뒤로가기 버튼을 누른 시간
    CountDownTimer autoTimer;
    Button kstore, psetting,pstore,lvup;
    TextView blife,money,tdpt,tdps,autoBuff;
    ImageView attacked,attacked2,skill,Impet;
    FrameLayout backg;
    int back[]={R.drawable.taptitanbasic,R.drawable.taptitan1,R.drawable.taptitan2,R.drawable.taptitan3,R.drawable.taptitan4};
    Intent knight,petset,petst,getevery,bgm,call,music;
    String m,b,l,t,s;
    Thread knightthread;
    ProgressBar health;


    static BitmapDrawable attack=null;
    String path;
    static MediaPlayer mx,sward,skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pre= PreferenceManager.getDefaultSharedPreferences(this);
        edi=pre.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path= Environment.getExternalStorageDirectory().getPath()+"/florallife.mp3";
        setTitle("Tap Titan");
        getevery=getIntent();
        backg=(FrameLayout)findViewById((R.id.back));
        kstore=(Button)findViewById(R.id.knightStore);
        psetting=(Button)findViewById(R.id.petSetting);
        pstore=(Button)findViewById(R.id.petStore);
        lvup=(Button)findViewById(R.id.level);
        blife=(TextView)findViewById(R.id.bosslife);
        money=(TextView)findViewById(R.id.money);
        tdpt=(TextView)findViewById(R.id.dpt);
        tdps=(TextView)findViewById(R.id.dps);
        attack=(BitmapDrawable)getResources().getDrawable(R.drawable.attack);//지워야함
        attacked=(ImageView)findViewById(R.id.attacked);
        attacked2=(ImageView)findViewById(R.id.attacked2);
        skill=(ImageView)findViewById(R.id.skilled);
        Impet=(ImageView) findViewById(R.id.Impet);
        autoBuff=(TextView)findViewById(R.id.skilltimer);
        health=(ProgressBar)findViewById(R.id.health);

        bgm=new Intent(this, MusicService.class);
        knight=new Intent(getApplicationContext(),knightstoreActivity.class);
        music=new Intent(getApplicationContext(),MusicSetting.class);
        call=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:/01024276252"));
        petset=new Intent(getApplicationContext(),PetchoiceActivity.class);
        petst=new Intent(getApplicationContext(),PetstoreActivity.class);
        mx=new MediaPlayer();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);

        sward=MediaPlayer.create(this,R.raw.sward);

        skills=MediaPlayer.create(this,R.raw.skillsound);
        if(bgmstart==1){
        mlife=getevery.getIntExtra("mlife",100);//몬스터의 전체 체력
        plife=getevery.getIntExtra("plife",100);//몬스터의 남은 체력
        gold=getevery.getIntExtra("gold",0);//소지금
        dpt=getevery.getIntExtra("dpt",1);//터치당 공격력
        lupg=getevery.getIntExtra("lvupgold",1);//레벨업 비용
        dptadd=getevery.getIntExtra("dptadd",2);//레벨업당 공격력 증가량
        level=getevery.getIntExtra("level",1);//현재 레벨
        kill=getevery.getIntExtra("kill",0);//몬스터 잡은 횟수
        count=getevery.getIntExtra("count",0);
        dps=getevery.getIntExtra("dps",0);
        bgmstart=getevery.getIntExtra("bgmstart",0);
        toTimer=getevery.getLongExtra("toTimer",30000);
        temp=getevery.getIntExtra("temp",0);
        pet=getevery.getIntExtra("pet",0);}
        else{
            mlife=pre.getInt("mlife",100);//몬스터의 전체 체력
            plife=pre.getInt("plife",100);//몬스터의 남은 체력
            gold=pre.getInt("gold",0);//소지금
            dpt=pre.getInt("dpt",1);//터치당 공격력
            lupg=pre.getInt("lvupgold",1);//레벨업 비용
            dptadd=pre.getInt("dptadd",2);//레벨업당 공격력 증가량
            level=pre.getInt("level",1);//현재 레벨
            kill=pre.getInt("kill",0);//몬스터 잡은 횟수
            count=pre.getInt("count",0);
            dps=pre.getInt("dps",0);
            toTimer=pre.getLong("toTimer",30000);
            temp=pre.getInt("temp",0);
            pet=pre.getInt("pet",0);
        }

        aTimer=0;
        health.setProgress((int)((float)plife/(float)mlife*100));

        backg.setBackground(getDrawable(back[count]));

        if(bgmstart==0){
            startService(bgm);
            bgmstart=1;
        }

        m=getString(R.string.money);
        b=getString(R.string.life);
        l=getString(R.string.levelup);
        t=getString(R.string.dpt);
        s=getString(R.string.dps);

        money.setText(String.format(m,gold));
        blife.setText(String.format(b,plife,mlife));
        lvup.setText(String.format(l,level,lupg));
        tdpt.setText(String.format(t,dpt));
        if(pet == 1){
            Impet.setVisibility(View.VISIBLE);
        }
        if(dps>0)
            tdps.setText(String.format(s,dps));

        kstore.setOnClickListener(new View.OnClickListener() {//기사상점 클릭시
            @Override
            public void onClick(View v) {
                knight.putExtra("mlife",mlife);
                knight.putExtra("plife",plife);
                knight.putExtra("gold",gold);
                knight.putExtra("dpt",dpt);
                knight.putExtra("lupg",lupg);
                knight.putExtra("level",level);
                knight.putExtra("kill",kill);
                knight.putExtra("count",count);
                knight.putExtra("dps",dps);
                knight.putExtra("temp", temp);
                knight.putExtra("pet", pet);
                knight.putExtra("bgmstart",bgmstart);
                knight.putExtra("toTimer",aTimer);
                startActivity(knight);
            }
        });
        psetting.setOnClickListener(new View.OnClickListener() {//펫선택 클릭시
            @Override
            public void onClick(View v) {
                petset.putExtra("mlife",mlife);
                petset.putExtra("plife",plife);
                petset.putExtra("gold",gold);
                petset.putExtra("dpt",dpt);
                petset.putExtra("lupg",lupg);
                petset.putExtra("level",level);
                petset.putExtra("kill",kill);
                petset.putExtra("count",count);
                petset.putExtra("dps",dps);
                petset.putExtra("temp", temp);
                petset.putExtra("pet", pet);
                petset.putExtra("bgmstart",bgmstart);
                petset.putExtra("toTimer",aTimer);
                startActivity(petset);
            }
        });
        pstore.setOnClickListener(new View.OnClickListener() {//펫상점 클릭시
            @Override
            public void onClick(View v) {
                petst.putExtra("mlife",mlife);
                petst.putExtra("plife",plife);
                petst.putExtra("gold",gold);
                petst.putExtra("dpt",dpt);
                petst.putExtra("lupg",lupg);
                petst.putExtra("level",level);
                petst.putExtra("kill",kill);
                petst.putExtra("count",count);
                petst.putExtra("dps",dps);
                petst.putExtra("temp", temp);
                petst.putExtra("pet", pet);
                petst.putExtra("bgmstart",bgmstart);
                petst.putExtra("toTimer",aTimer);
                startActivity(petst);
            }
        });
        lvup.setOnClickListener(new View.OnClickListener() {//레벨업 클릭시
            @Override
            public void onClick(View v) {
                if(gold>=lupg){
                    gold-=lupg;
                    if(level>10){
                        dpt+=(2*(2*level/10));
                        lupg+=1*(2*level/10);}
                    else{
                        dpt+=2;
                        lupg+=1;
                    }
                    level++;
                    lvup.setText(String.format(l,level,lupg));
                    money.setText(String.format(m,gold));
                    tdpt.setText(String.format(t,dpt));
                }

            }
        });
        if(count>=1){//기사를 구매하여 배경화면이 바뀌어 있을 시 몬스터를 자동으로 공격하는 패시브를 실행
            knightthread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try{
                            plife-=dps;
                            if(plife<=0){
                                kill++;
                                mlife+=50*kill;
                                plife=mlife;
                                gold+=10*kill;
                            }
                            blife.setText(String.format(b,plife,mlife));
                            money.setText(String.format(m,gold));
                            Thread.sleep(1000);
                        }catch (Throwable t){

                        }
                    }
                }
            });
            knightthread.start();
        }
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br,filter);
    }
    BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act=intent.getAction();

            if(act.equals(Intent.ACTION_BATTERY_CHANGED)){
                int plug=intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
                    switch (plug){
                        case 0:
                            charging=0;
                            break;
                        case BatteryManager.BATTERY_PLUGGED_AC:
                            if(charging!=1)
                            Toast.makeText(getApplicationContext(),"충전중",Toast.LENGTH_SHORT).show();
                            charging=1;
                            break;
                        case BatteryManager.BATTERY_PLUGGED_USB:
                            if(charging!=2)
                            Toast.makeText(getApplicationContext(),"USB연결됨",Toast.LENGTH_SHORT).show();
                            charging=2;
                            break;
                        case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                            if(charging!=3)
                            Toast.makeText(getApplicationContext(),"무선충전중",Toast.LENGTH_SHORT).show();
                            charging=3;
                    }

            }
        }
    };



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                // setContentView(new attackView(this));
                startX=(float) event.getX();
                startY=(float) event.getY();
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
                attacked.setX(startX-140);
                attacked.setY(startY-550);
                attacked.setVisibility(View.VISIBLE);
                sward.start();
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
                sward.start();
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
                        skills.start();
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
                money.setText(String.format(m,gold));
                blife.setText(String.format(b,plife,mlife));
                attacked.setVisibility(View.INVISIBLE);
                skill.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,1,0,"음악 설정하기");
        menu.add(0,2,0,"개발자에게 전화하기");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case 1:
                music.putExtra("mlife",mlife);
                music.putExtra("plife",plife);
                music.putExtra("gold",gold);
                music.putExtra("dpt",dpt);
                music.putExtra("lupg",lupg);
                music.putExtra("level",level);
                music.putExtra("kill",kill);
                music.putExtra("count",count);
                music.putExtra("dps",dps);
                music.putExtra("temp", temp);
                music.putExtra("pet", pet);
                music.putExtra("bgmstart",bgmstart);
                music.putExtra("toTimer",aTimer);

                startActivity(music);

                break;
            case 2:
                startActivity(call);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {//뒤로가기버튼을 1.5초사이에 두번 누르면 프로그램 종료
        if(pressedTime==0){
            Toast.makeText(getApplicationContext(),"프로그램을 종료하려면 한번더 눌러주세요",Toast.LENGTH_SHORT).show();
            pressedTime=System.currentTimeMillis();}//처음키가 눌렸을때 시간 저장
        else {
            long time = System.currentTimeMillis() - pressedTime;//현재시간과 비교
            if(time>1500)//두번 누른시간의 차이가 1.5초이상 나면 처음 누른시간 초기화
                pressedTime=0;
            else{
                edi.putInt("mlife",mlife);
                edi.putInt("gold",gold);
                edi.putInt("dpt",dpt);
                edi.putInt("lvupgold",lupg);
                edi.putInt("level",level);
                edi.putInt("kill",kill);
                edi.putInt("count",count);
                edi.putInt("dps",dps);
                edi.putInt("temp",temp);
                edi.putInt("pet",pet);
                edi.putLong("toTimer",aTimer);
                edi.commit();
                Intent intent=new Intent(getApplicationContext(),Intro.class);
                startActivity(intent);//intro 화면으로 이동
                stopService(bgm);
                moveTaskToBack(true);//백그라운드로 이동
                finishAndRemoveTask();//액티비티종료하고  태스크리스트에서 지움
                android.os.Process.killProcess(android.os.Process.myPid());//앱프로세스 종료
            }

        }
    }

}