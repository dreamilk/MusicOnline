package cn.shanghq.musiconline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.shanghq.musiconline.adapters.MyAdapter;
import cn.shanghq.musiconline.bean.Music;
import cn.shanghq.musiconline.services.MusicService;
import cn.shanghq.musiconline.utils.HttpAsyncRequest;
import cn.shanghq.musiconline.utils.LogUtil;

public class MainActivity extends AppCompatActivity {
    public static final String HTTP_ROOT="http://www.shanghq.cn/";
    private ListView listView;
    private SeekBar seekBar;
    private TextView textViewCurr;
    private TextView textViewTotal;
    private TextView textViewMusicName;

    private ImageButton imageButtonNext;
    private ImageButton imageButtonPrev;
    private ImageButton imageButtonPlay;

    private Context context;

    private MusicReceiver musicReceiver;
    private int musicIndex=-1;
    public static List<Music> musics;
    public static boolean isPlaying=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        listView= (ListView) findViewById(R.id.listView);
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(-999);

        textViewCurr= (TextView) findViewById(R.id.tv_curr_time);
        textViewTotal= (TextView) findViewById(R.id.tv_total_time);
        textViewMusicName= (TextView) findViewById(R.id.tv_music_name);

        imageButtonNext= (ImageButton) findViewById(R.id.bt_next);
        imageButtonPlay= (ImageButton) findViewById(R.id.bt_play);
        imageButtonPrev= (ImageButton) findViewById(R.id.bt_prev);

        ImgbtnClickListener clickListener=new ImgbtnClickListener();
        imageButtonNext.setOnClickListener(clickListener);
        imageButtonPrev.setOnClickListener(clickListener);
        imageButtonPlay.setOnClickListener(clickListener);

        musicReceiver=new MusicReceiver();
        IntentFilter mMusicReciverFilter=new IntentFilter(MusicService.CAST_ACTION_UPDATE);
        registerReceiver(musicReceiver,mMusicReciverFilter);

        HttpAsyncRequest httpAsyncRequest=new HttpAsyncRequest() {
            @Override
            protected void onComplete(String s) {
                try {
                    JSONArray array=new JSONArray(s);
                    musics=new ArrayList<Music>();
                    Music music;
                    for(int i=0;i<array.length();i++){
                        music=new Music();
                        JSONObject object=array.getJSONObject(i);
                        music.setTitle(object.getString("name"));
                        music.setPath(object.getString("mp3"));
                        music.setSinger(object.getString("singer"));
                        LogUtil.log(object.getString("name"));
                        musics.add(music);
                    }
                    ListAdapter adapter=new MyAdapter(musics,context);
                    listView.setAdapter(adapter);

                    Intent intent=new Intent(context,MusicService.class);
                    ArrayList<String> musicArrayList=new ArrayList<String>();
                    for(Music music2:musics){
                        musicArrayList.add(HTTP_ROOT+music2.getPath());
                    }
                    intent.putExtra(MusicService.KEY_COMMAND,MusicService.CMD_INIT);
                    intent.putStringArrayListExtra(MusicService.KEY_MUSIC_LIST,musicArrayList);
                    startService(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        httpAsyncRequest.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,MusicService.class);
                intent.putExtra(MusicService.KEY_COMMAND,MusicService.CMD_PLAY);
                intent.putExtra(MusicService.KEY_MUSIC_INDEX,position);
                startService(intent);
                isPlaying=true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicReceiver);
    }

    class ImgbtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context,MusicService.class);
            switch (v.getId()){
                case R.id.bt_next:
                    intent.putExtra(MusicService.KEY_COMMAND,MusicService.CMD_NEXT);
                    break;
                case R.id.bt_play:
                    if(isPlaying){
                        intent.putExtra(MusicService.KEY_COMMAND,MusicService.CMD_PAUSE);
                        isPlaying=false;
                    }else {
                        intent.putExtra(MusicService.KEY_COMMAND, MusicService.CMD_RESUME);
                        isPlaying=true;
                    }
                    break;
                case R.id.bt_prev:
                    intent.putExtra(MusicService.KEY_COMMAND,MusicService.CMD_PREV);
                    break;
                default:
                    break;
            }
            startService(intent);

        }
    }

    class MusicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int index=intent.getIntExtra(MusicService.KEY_MUSIC_INDEX,0);
            int curr=intent.getIntExtra(MusicService.KEY_MUSIC_CURR,0);
            int total=intent.getIntExtra(MusicService.KEY_MUSIC_TOTAL,0);

            if(musicIndex!=index){
                musicIndex=index;
                seekBar.setMax(total);
                textViewTotal.setText(format(total));
                textViewMusicName.setText(musics.get(musicIndex).getTitle());
            }
            seekBar.setProgress(curr);
            textViewCurr.setText(format(curr));
        }
    }

    public static String format(long time){
        time=time/1000;
        if(time<60){
            return "00"+formatLong("00",time);
        }else {
            return formatLong("00",time/60)+":"+formatLong("00",time%60);
        }
    }

    private static String formatLong(String format,long num) {
        return new DecimalFormat(format).format(num);
    }
}
