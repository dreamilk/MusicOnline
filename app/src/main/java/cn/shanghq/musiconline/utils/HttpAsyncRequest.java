package cn.shanghq.musiconline.utils;

import android.content.Entity;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dream on 2017/4/16.
 */

public abstract class HttpAsyncRequest extends AsyncTask<HttpAsyncRequest,Integer,String> {
    @Override
    protected String doInBackground(HttpAsyncRequest... params) {
        LogUtil.log("-----------任务开始，进入后台执行-----------");
        String result=null;
        HttpClient client=new DefaultHttpClient();
        try {
            HttpResponse response=client.execute(new HttpGet("http://www.shanghq.cn"));
            result= EntityUtils.toString(response.getEntity(),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onComplete(s);
    }

    protected abstract void onComplete(String s);
}
