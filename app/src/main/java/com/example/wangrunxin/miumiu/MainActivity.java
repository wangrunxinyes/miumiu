package com.example.wangrunxin.miumiu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wangrunxin.utils.HttpHelper;
import com.example.wangrunxin.utils.SoundHelper;
import com.example.wangrunxin.utils.StringTools;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static Handler myhandler;
    private PowerManager.WakeLock m_wakeLockObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        Button bt_test_call = (Button) findViewById(R.id.main_bt_test_call);
        bt_test_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.wangrunxin.com/site/miu/miumiu/1";
                String key = null;

                HttpHelper helper = new HttpHelper();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response;

                        try {
                            response = new HttpHelper().doGet("http://www.wangrunxin.com/site/miu/miumiu/1");
                        } catch (Exception e) {
                            response = e.getMessage();
                        }
                        System.out.println(response);
                    }
                });
                thread.start();


            }
        });

        myhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        AcquireWakeLock(5000);
                        SoundHelper player = new SoundHelper();
                        player.setPlayState(false);
                        player.play(MainActivity.this);
                        break;

                    case 2:

                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new MyThread()).start();

    }

    public void AcquireWakeLock(long milltime) {
        if (m_wakeLockObj == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            m_wakeLockObj = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "test");

            m_wakeLockObj.acquire(milltime);
        }
    }


    public void ReleaseWakeLock() {
        if (m_wakeLockObj != null && m_wakeLockObj.isHeld()) {
            m_wakeLockObj.release();
            m_wakeLockObj = null;
        }
    }

}


class MyThread implements Runnable {
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            try {
                Thread.sleep(2000);// 线程暂停10秒，单位毫秒
                String response;
                Boolean result = false;
                try {
                    response = new HttpHelper().doGet("http://www.wangrunxin.com/site/miu/miumiu/1");
                    result = true;
                } catch (Exception e) {
                    response = e.getMessage();
                }
                System.out.println(response);
                if(result){
                    Map<String, Object> map=  StringTools.getMap(response);
                    if(map != null) {
                        String state = (String) map.get("data");
                        System.out.println("data=" + state);
                        if (state.equals("1")) {
                            System.out.println("*****************");
                            Message msg = new Message();
                            msg.what = 1;
                            MainActivity.myhandler.sendMessage(msg);
                        }
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
