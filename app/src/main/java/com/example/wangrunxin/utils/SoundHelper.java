package com.example.wangrunxin.utils;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.wangrunxin.miumiu.R;

import java.io.IOException;

/**
 * Created by wangrunxin on 15/11/9.
 */
public class SoundHelper extends Activity {

    private MediaPlayer mediaPlayer;
    private Boolean play;

    public void setPlayState(Boolean state){
        play = state;
    }

    public void play(Context context) {

        if(play)
            return;

        boolean createState = false;
        if (mediaPlayer == null) {
            mediaPlayer = createLocalMp3(context);
            createState = true;
        }
        //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
        //以便其他应用程序可以使用该资源:
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();//释放音频资源
                play = false;
            }
        });

        try {
            //在播放音频资源之前，必须调用Prepare方法完成些准备工作
            if (createState) mediaPlayer.prepare();
            //开始播放音频
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public MediaPlayer createLocalMp3(Context context){
            /**
             * 创建音频文件的方法：
             * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
             * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
             *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
             */
            Uri uri = Uri.parse("android.resource://com.example.wangrunxin/raw/test.mp3");
            MediaPlayer mp = MediaPlayer.create(context, R.raw.test);
            mp.stop();
            return mp;
        }

}
