package com.miglab.miyo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.MusicType;

import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/18.
 */
public class MusicTypeAdapter extends BaseAdapter {
    private Activity ac;
    private List<MusicType> list;
    private MusicTypeHolder holder;
    public MusicTypeAdapter(Activity ac,List<MusicType> list){
        this.ac = ac;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public boolean isEnabled(int position) {
        if (list.get(position).getIsTitle()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            holder = new MusicTypeHolder();
            convertView = holder.parent;
            convertView.setTag(holder);
        }else{
            holder = (MusicTypeHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        if(list.get(position).getIsTitle()) {
            holder.tv_name.setBackgroundColor(0xffe6e6e6);
        }else {
            holder.tv_name.setBackgroundColor(0xffffffff);
        }
        return convertView;
    }

    private class MusicTypeHolder {
        public View parent;
        public TextView tv_name;
        public ImageView iv_playing;
        public MusicTypeHolder() {
            parent = LayoutInflater.from(ac).inflate(R.layout.ly_music_type_item,null);
            tv_name = (TextView) parent.findViewById(R.id.name);
            iv_playing = (ImageView) parent.findViewById(R.id.playing);
            parent.setTag(this);
        }
    }
}
