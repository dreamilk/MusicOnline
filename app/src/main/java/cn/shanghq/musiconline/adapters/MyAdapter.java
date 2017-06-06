package cn.shanghq.musiconline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.shanghq.musiconline.R;
import cn.shanghq.musiconline.bean.Music;

/**
 * Created by dream on 2017/4/12.
 */

public class MyAdapter extends BaseAdapter {
    private List<Music> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyAdapter(List<Music> musics, Context context) {
        this.mData = musics;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.singer = (TextView) convertView.findViewById(R.id.tv_list_item_singer);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_list_item_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.singer.setText(mData.get(position).getSinger());
        viewHolder.title.setText(mData.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        TextView singer;
        TextView title;
    }
}
