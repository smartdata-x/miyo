package com.miglab.miyo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.PushMessageInfo;
import com.miglab.miyo.third.universalimageloader.core.DisplayImageOptions;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.ui.widget.RoundImageView;

import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class ChatListAdapter extends BaseAdapter {
    private List<PushMessageInfo> list;
    private Activity ac;
    private LayoutInflater mInflater;
    private DisplayImageOptions options;

    public ChatListAdapter(Activity ac, List<PushMessageInfo> list) {
        this.ac = ac;
        this.list = list;
        mInflater = LayoutInflater.from(ac);
        initDisplayImageOptions();
    }

    private void initDisplayImageOptions() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(false)
                .showImageForEmptyUri(R.drawable.user_default)
                .build();
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean isMe = position%2 == 0;
        ViewHolder viewHolder = null;
        if(convertView == null || convertView.getTag() == null) {
            if(isMe) {
                convertView = mInflater.inflate(R.layout.ly_chatlist_item_right, null);
            } else {
                convertView = mInflater.inflate(R.layout.ly_chatlist_item_left, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.user_msg);
            viewHolder.iv_head = (RoundImageView) convertView.findViewById(R.id.user_icon);
        }
        if(isMe) {
            ImageLoader.getInstance().displayImage(MiyoUser.getInstance().getHeadUrl(),viewHolder.iv_head);
            viewHolder.tv_name.setText(MiyoUser.getInstance().getNickname());
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView tv_name;
        public TextView tv_msg;
        public RoundImageView iv_head;
        public boolean isComMsg = true;
    }
}
