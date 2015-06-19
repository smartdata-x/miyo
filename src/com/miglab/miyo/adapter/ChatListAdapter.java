package com.miglab.miyo.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.PushMessageInfo;
import com.miglab.miyo.entity.SocChatMsg;
import com.miglab.miyo.third.universalimageloader.core.DisplayImageOptions;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.ui.widget.RoundImageView;
import com.miglab.miyo.util.EmoticonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class ChatListAdapter extends BaseAdapter {
    private List<SocChatMsg> list;
    private Activity ac;
    private LayoutInflater mInflater;
    private DisplayImageOptions options;
    private int fontHeight;

    public ChatListAdapter(Activity ac, List<SocChatMsg> list) {
        this.ac = ac;
        this.list = list;
        mInflater = LayoutInflater.from(ac);
        initDisplayImageOptions();
    }

    private void initDisplayImageOptions() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .showImageForEmptyUri(R.drawable.user_default)
                .build();
    }

    /**
     * @param socChatMsg 消息实体
     * @param isNotify 是否刷新界面
     * @param isHead 是否从头部插入
     */
    public void addData(SocChatMsg socChatMsg, boolean isNotify, boolean isHead) {
        if(socChatMsg == null)
            return;
        if(list == null)
            list = new ArrayList<SocChatMsg>();
        if(isHead)
            list.add(0,socChatMsg);
        else
            list.add(socChatMsg);
        if(isNotify)
            notifyDataSetChanged();
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SocChatMsg socChatMsg = list.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null || convertView.getTag() == null) {
            if(socChatMsg.isMe()) {
                convertView = mInflater.inflate(R.layout.ly_chatlist_item_right, null);
            } else {
                convertView = mInflater.inflate(R.layout.ly_chatlist_item_left, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.user_msg);
            viewHolder.iv_head = (RoundImageView) convertView.findViewById(R.id.user_icon);
            if(fontHeight == 0)
                fontHeight = getFontHeight(viewHolder.tv_msg);
        }
        if(socChatMsg.isMe()) {
            ImageLoader.getInstance().displayImage(MiyoUser.getInstance().getHeadUrl(),viewHolder.iv_head,options);
            viewHolder.tv_name.setText(MiyoUser.getInstance().getNickname());
        }
        viewHolder.tv_msg.setText(matchEmoticon(socChatMsg.getMsg()));
        return convertView;
    }

    private SpannableString matchEmoticon(String msg) {
        SpannableString spannableString = new SpannableString(msg);
        Pattern pattern = Pattern.compile("\\[\\\\\\d+\\]");
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            Bitmap bitmap = EmoticonUtil.getBitmap(ac,key);
            int start = matcher.start();
            int end = matcher.end();
            if(bitmap != null) {
                Drawable drawable = new BitmapDrawable(ac.getResources(), bitmap);
                drawable.setBounds(0, 0, fontHeight+5, fontHeight+5);
                ImageSpan imageSpan = new ImageSpan(drawable);
                spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    static class ViewHolder {
        public TextView tv_name;
        public TextView tv_msg;
        public RoundImageView iv_head;
        public boolean isComMsg = true;
    }

    private int getFontHeight(TextView textView) {
        Paint paint = new Paint();
        paint.setTextSize(textView.getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }
}
