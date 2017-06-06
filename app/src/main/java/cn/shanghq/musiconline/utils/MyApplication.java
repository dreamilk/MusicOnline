package cn.shanghq.musiconline.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by dream on 2017/4/9.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
