package cn.shanghq.musiconline.services;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.EmptyStackException;
import java.util.IdentityHashMap;
import java.util.List;

import cn.shanghq.musiconline.bean.Music;

/**
 * Created by dream on 2017/4/16.
 */

public class MusicUtil {
    private List<String> musics=null;
    private MediaPlayer mediaPlayer;
    private Context context;
    public static int index=0;
    public boolean isPlaying=false;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent=new Intent(MusicService.CAST_ACTION_UPDATE);
            intent.putExtra(MusicService.KEY_MUSIC_INDEX,index);
            intent.putExtra(MusicService.KEY_MUSIC_CURR,mediaPlayer.getCurrentPosition());
            intent.putExtra(MusicService.KEY_MUSIC_TOTAL,mediaPlayer.getDuration());
            context.sendBroadcast(intent);
            handler.sendEmptyMessageDelayed(1,500);
        }
    };
    public MusicUtil(Context context,List<String> musics){
        mediaPlayer=new MediaPlayer();
        this.musics=musics;
        this.context=context;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }

    public void play(){
        mediaPlayer.start();
        isPlaying=true;
        handler.sendEmptyMessage(1);
    }

    public void play(int index){
        MusicUtil.index=index;
        if(musics!=null){
            String music=musics.get(MusicUtil.index);
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(context, Uri.parse(music));
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying=true;
                handler.sendEmptyMessage(0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            isPlaying=false;
        }
    }

    public void pause(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            isPlaying=false;
        }
    }

    public void next() {
        if(mediaPlayer!=null){
            if(index==musics.size()-1){
                index=0;
            }else {
                ++index;
            }
            play(index);
        }
    }

    public void prev(){
        if(musics!=null){
            if(index==0){
                index=musics.size()-1;
            }else {
                --index;
            }
            play(index);
        }
    }

}