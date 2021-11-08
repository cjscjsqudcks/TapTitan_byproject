package com.example.TapTitan;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MusicSetting extends AppCompatActivity {
    ListView mp3list;
    Button closebtn;
    ArrayList<String> mp3s;
    String path= Environment.getExternalStorageDirectory().getPath()+"/";
    MainActivity mainActivity=new MainActivity();
    MusicService musicService=new MusicService();
    FileInputStream inputStream;
    long empty2;

    int empty;
    Intent close,getevery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclist);
        closebtn=(Button)findViewById(R.id.closeBtn);

        close=new Intent(getApplicationContext(),MainActivity.class);
        getevery=getIntent();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);
        mp3s=new ArrayList<String>();
        File[] files=new File(path).listFiles();
        String fname,ename;
        for(File file:files){
            fname=file.getName();
            if(fname.endsWith("mp3"))
                mp3s.add(fname);
        }
        mp3list=(ListView)findViewById(R.id.mp3list);
        ArrayAdapter<String> a=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mp3s);
        mp3list.setAdapter(a);
        mp3list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mp3Path=path+mp3s.get(position);
                if(musicService.m.isPlaying())
                    musicService.m.reset();
                if(mainActivity.mx.isPlaying()) {
                    mainActivity.mx.stop();
                    mainActivity.mx.reset();
                }
                try {

                    File file = new File(mp3Path);
                    inputStream = new FileInputStream(file);

                    mainActivity.mx.setDataSource(inputStream.getFD());
                    mainActivity.mx.setLooping(true);
                    mainActivity.mx.prepare();
                    mainActivity.mx.start();
                    inputStream.close();
                }catch (IOException e){
                    Toast.makeText(getApplicationContext(),"ERROR!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empty=getevery.getIntExtra("mlife",100);
                close.putExtra("mlife",empty);
                empty=getevery.getIntExtra("plife",100);
                close.putExtra("plife",empty);
                empty=getevery.getIntExtra("dpt",1);
                close.putExtra("dpt",empty);
                empty=getevery.getIntExtra("gold",0);
                close.putExtra("gold",empty);
                empty=getevery.getIntExtra("lvupgold",1);
                close.putExtra("lvupgold",empty);
                empty=getevery.getIntExtra("level",1);
                close.putExtra("level",empty);
                empty=getevery.getIntExtra("kill",0);
                close.putExtra("kill",empty);
                empty=getevery.getIntExtra("dps",0);
                close.putExtra("dps",empty);
                empty=getevery.getIntExtra("count",0);
                close.putExtra("count",empty);
                empty=getevery.getIntExtra("bgmstart",0);
                close.putExtra("bgmstart",empty);
                empty=getevery.getIntExtra("temp",0);
                close.putExtra("temp", empty);
                empty=getevery.getIntExtra("pet",0);
                close.putExtra("pet", empty);
                empty2=getevery.getLongExtra("toTimer",30000);
                close.putExtra("toTimer",empty2);
                startActivity(close);
            }
        });
    }
    public void onBackPressed() {//뒤로가기버튼을 1.5초사이에 두번 누르면 프로그램 종료
        empty=getevery.getIntExtra("mlife",100);
        close.putExtra("mlife",empty);
        empty=getevery.getIntExtra("plife",100);
        close.putExtra("plife",empty);
        empty=getevery.getIntExtra("dpt",1);
        close.putExtra("dpt",empty);
        empty=getevery.getIntExtra("gold",0);
        close.putExtra("gold",empty);
        empty=getevery.getIntExtra("lvupgold",1);
        close.putExtra("lvupgold",empty);
        empty=getevery.getIntExtra("level",1);
        close.putExtra("level",empty);
        empty=getevery.getIntExtra("kill",0);
        close.putExtra("kill",empty);
        empty=getevery.getIntExtra("dps",0);
        close.putExtra("dps",empty);
        empty=getevery.getIntExtra("count",0);
        close.putExtra("count",empty);
        empty=getevery.getIntExtra("bgmstart",0);
        close.putExtra("bgmstart",empty);
        empty=getevery.getIntExtra("temp",0);
        close.putExtra("temp", empty);
        empty=getevery.getIntExtra("pet",0);
        close.putExtra("pet", empty);
        empty2=getevery.getLongExtra("toTimer",30000);
        close.putExtra("toTimer",empty2);
        startActivity(close);


    }
}
