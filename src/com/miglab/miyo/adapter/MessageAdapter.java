package com.miglab.miyo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.miglab.miyo.R;
import com.miglab.miyo.control.MusicManager;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/11.
 */
public class MessageAdapter extends BaseAdapter {
    private Activity ac;
    public MessageAdapter(Activity ac){
        this.ac = ac;
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = holder.parent;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
        public View parent;
        public TextView tv_name;
        public TextView tv_msg;
        public ImageView iv_head;
        public ViewHolder() {
            parent = LayoutInflater.from(ac).inflate(R.layout.ly_message_item,null);
            tv_name = (TextView) parent.findViewById(R.id.user_name);
            tv_msg = (TextView) parent.findViewById(R.id.msg_content);
            iv_head = (ImageView) parent.findViewById(R.id.head_icon);
            parent.setTag(this);
        }
    }
}
