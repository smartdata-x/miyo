package com.miglab.miyo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.Emoticon;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.ui.widget.IView;

import java.io.IOException;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    private List<Emoticon> list;
    private int mItemHeight = 0;
    private int mImgHeight = 0;

    private IView mOnItemListener;

    public EmoticonAdapter(Context context, List<Emoticon> list) {
        this.mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder viewHolder;
        if(convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.ly_emoticon_item, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mItemHeight));
            viewHolder.iv_face = (ImageView) convertView.findViewById(R.id.item_iv_face);
            viewHolder.ry_content = (RelativeLayout) convertView.findViewById(R.id.ry_content);
            viewHolder.ry_parent = (RelativeLayout) convertView.findViewById(R.id.ry_parent);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mImgHeight, mImgHeight);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            viewHolder.ry_content.setLayoutParams(params);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Emoticon emoticon = list.get(position);
        if (emoticon != null) {
            viewHolder.ry_parent.setBackgroundResource(R.drawable.emoticon_item_selector);
            ImageLoader.getInstance().displayImage(emoticon.iconUri, viewHolder.iv_face);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onItemClick(emoticon);
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_face;
        public RelativeLayout ry_parent;
        public RelativeLayout ry_content;
    }

    public void setHeight(int height, int padding) {
        mItemHeight = height;
        mImgHeight = mItemHeight - padding;
        notifyDataSetChanged();
    }

    public void setOnItemListener(IView listener) {
        this.mOnItemListener = listener;
    }
}
