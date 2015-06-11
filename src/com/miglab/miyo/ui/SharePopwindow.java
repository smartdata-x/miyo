package com.miglab.miyo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.miglab.miyo.R;
import com.miglab.miyo.control.MusicManager;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.third.share.*;
import com.miglab.miyo.util.DisplayUtil;


/**
 * Created by tudou on 2015/5/25.
 */
public class SharePopwindow extends PopupWindow implements View.OnClickListener{
    private View parent;
    private LinearLayout bottom;
    private MainActivity ac;
    private GridView gridView;
    public SharePopwindow(MainActivity ac) {
        this.ac = ac;
        LayoutInflater inflater = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parent = inflater.inflate(R.layout.ppw_share,null);

        bottom = (LinearLayout) parent.findViewById(R.id.bottom);
        gridView = (GridView) parent.findViewById(R.id.gridview);
        gridView.setAdapter(new ShareAdapter());
        parent.setOnClickListener(this);
        DisplayUtil.setViewListener(parent, this);
        parent.startAnimation(AnimationUtils.loadAnimation(ac, R.anim.fade_in));
        bottom.startAnimation(AnimationUtils.loadAnimation(ac, R.anim.push_bottom_in));
        setContentView(parent);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0xb0000000));
        setOutsideTouchable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case R.drawable.friend_share:
                        friendsShare();
                        break;
                    case R.drawable.weixin_share:
                        weixinShare();
                        break;
                    case R.drawable.weibo_share:
                        weiboShare();
                        break;
                    case R.drawable.qq_share:
                        qqShare();
                        break;
                    case R.drawable.qqzone_share:
                        qqZoneShare();
                        break;

                }
                SharePopwindow.this.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.parent:
                dismiss();
                break;
        }
    }

    private void qqShare() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        if(songInfo == null)
            return;
        Share share = new Share2QQ(ac,songInfo);
        share.share();
    }

    private void qqZoneShare() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        if(songInfo == null)
            return;
        Share share = new Share2QQZone(ac,songInfo);
        share.share();
    }

    private void weixinShare() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        if(songInfo == null)
            return;
        Share2Weixin share = new Share2Weixin(ac,songInfo);
        share.bitmap = ac.getCurMusicBitmap();
        share.setReqScene(Share2Weixin.WEIXIN_SHARE);
        share.share();
    }

    private void friendsShare() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        if(songInfo == null)
            return;
        Share2Weixin share = new Share2Weixin(ac,songInfo);
        share.bitmap = ac.getCurMusicBitmap();
        share.setReqScene(Share2Weixin.FRIENDS_SHARE);
        share.share();
    }

    private void weiboShare() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        if(songInfo == null)
            return;
        Intent i = new Intent(ac,WBShareActivity.class);
        i.putExtra("songInfo", songInfo);
        i.putExtra("bitmap",ac.getCurMusicBitmap());
        i.putExtra("album", MusicManager.getInstance().getMusicType().getName());
        ac.startActivity(i);
//        Share2Weibo share = new Share2Weibo(ac,songInfo);
//        ac.setWeiboShareAPI(share.iWeiboShareAPI);
//        share.share();

    }

    private class ShareAdapter extends BaseAdapter {
        int[] imgRes = {R.drawable.friend_share, R.drawable.weixin_share, R.drawable.weibo_share,
                        R.drawable.qq_share, R.drawable.qqzone_share};
        int[] stringRes = {R.string.friends_share, R.string.weixin_share, R.string.weibo_share,
                        R.string.qq_share, R.string.qqzone_share};
        @Override
        public int getCount() {
            return imgRes.length;
        }

        @Override
        public Object getItem(int position) {
            return imgRes[position];
        }

        @Override
        public long getItemId(int position) {
            return imgRes[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null || convertView.getTag() == null) {
                convertView = LayoutInflater.from(ac).inflate(R.layout.ly_share_item,null);
                viewHolder = BuildHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_share.setText(stringRes[position]);
            viewHolder.iv_share.setImageResource(imgRes[position]);
            return convertView;
        }

        private ViewHolder BuildHolder(View convertView) {
            ViewHolder holder = new ViewHolder();
            holder.tv_share = (TextView) convertView.findViewById(R.id.shareText);
            holder.iv_share = (ImageView) convertView.findViewById(R.id.shareImg);
            return holder;
        }

        class ViewHolder {
            public TextView tv_share;
            public ImageView iv_share;
        }
    }
}
