package cn.shanghq.musiconline.utils;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by dream on 2017/4/9.
 */

public class LogUtil {
    private static final String tag="myLog";
    private static final boolean isOpen=true;

    public static void log(String log){
        if(isOpen){
            Log.d(tag,log);
        }
    }

    public static void toast(String message){
        Toast.makeText(MyApplication.getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
