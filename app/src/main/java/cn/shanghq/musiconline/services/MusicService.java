package cn.shanghq.musiconline.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.animation.ValueAnimatorCompat;

import java.util.List;

/**
 * Created by dream on 2017/4/16.
 */

public class MusicService extends Service {
    public static final String KEY_COMMAND="k_command";
    public static final String KEY_MUSIC_NAME="k_music_name";
    public static final String KEY_MUSIC_PATH="k_music_path";
    public static final String KEY_MUSIC_INDEX="k_music_index";
    public static final String KEY_MUSIC_LIST="k_music_list";
    public static final String KEY_MUSIC_TOTAL="k_music_total";
    public static final String KEY_MUSIC_CURR="k_music_curr";
    public static final String CAST_ACTION_UPDATE="cn.shanghq.musiconline.MUSIC_TIME_UPDATE";

    public static final int CMD_INIT=1000;
    public static final int CMD_PLAY=1001;
    public static final int CMD_PAUSE=1002;
    public static final int CMD_NEXT=1003;
    public static final int CMD_PREV=1004;
    public static final int CMD_STOP=1005;
    public static final int CMD_RESUME=1006;

    private MusicUtil musicUtil;
    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId) {
        int command=intent.getIntExtra(KEY_COMMAND,-1);
        switch (command){
            case CMD_INIT:
                List<String> musicList=intent.getStringArrayListExtra(KEY_MUSIC_LIST);
                musicUtil=new MusicUtil(context,musicList);
                break;
            case CMD_PLAY:
                int index=intent.getIntExtra(KEY_MUSIC_INDEX,0);
                musicUtil.play(index);
                break;
            case CMD_PAUSE:
                musicUtil.pause();
                break;
            case CMD_RESUME:
                musicUtil.play();
                break;
            case CMD_NEXT:
                musicUtil.next();
                break;
            case CMD_PREV:
                musicUtil.prev();
                break;
            case CMD_STOP:
                musicUtil.stop();
                break;

            default:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
