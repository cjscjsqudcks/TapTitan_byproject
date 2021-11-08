package com.example.TapTitan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;


public class Intro extends AppCompatActivity {
    Button start_Button;
    MediaPlayer mediaPlayer;
    Random rand = new Random();
    int randNum;
    long pressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tap Titan");
        setContentView(R.layout.intro);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        randNum = rand.nextInt(4 - 1 + 1) + 1;
        start_Button = (Button)findViewById(R.id.StartButton);

        switch (randNum){
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm1);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm2);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm3);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm4);
                break;
        }

        //mediaPlayer.setLooping(true); //무한재생

        mediaPlayer.start();


        start_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(Intro.this,MainActivity.class);
                mediaPlayer.stop();
                startActivity(start);
            }
        });

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
                Intent intent=new Intent(getApplicationContext(),Intro.class);
                startActivity(intent);//intro 화면으로 이동
                mediaPlayer.stop();
                moveTaskToBack(true);//백그라운드로 이동
                finishAndRemoveTask();//액티비티종료하고  태스크리스트에서 지움
                android.os.Process.killProcess(android.os.Process.myPid());//앱프로세스 종료
            }

        }
    }
}
