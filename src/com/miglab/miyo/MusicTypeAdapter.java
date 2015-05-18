package com.miglab.miyo;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        return null;
    }
}
