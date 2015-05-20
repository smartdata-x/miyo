package com.miglab.miyo.adapter;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.net.DimensionFMTask;
import com.miglab.miyo.ui.MainActivity;

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
    private Handler handler;
    private int typePosition = -1;
    public MusicTypeAdapter(Activity ac,List<MusicType> list, Handler handler){
        this.ac = ac;
        this.list = list;
        this.handler = handler;
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
            holder.parent.setBackgroundColor(ac.getResources().getColor(R.color.bg_my_fm_title));
            holder.tv_name.setTextColor(ac.getResources().getColor(R.color.my_fm_title_textColor));
            holder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ac.getResources().getDimensionPixelSize(R.dimen.my_fm_title_textSize));
        }else {
            holder.parent.setBackgroundResource(R.drawable.music_type_item_selector);//ac.getResources().getColor(R.color.bg_my_fm_item));
            holder.tv_name.setTextColor(ac.getResources().getColor(R.color.my_fm_item_textColor));
            holder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ac.getResources().getDimensionPixelSize(R.dimen.my_fm_item_textSize));
        }
        if(typePosition != -1 && typePosition == position){
            AnimationDrawable animationDrawable= (AnimationDrawable) holder.iv_playing.getDrawable();
            holder.iv_playing.setVisibility(View.VISIBLE);
            if(!animationDrawable.isRunning())
                 animationDrawable.start();
        }else{
            holder.iv_playing.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setShowPosition(int position) {
        this.typePosition = position;
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
