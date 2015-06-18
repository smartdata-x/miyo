package com.miglab.miyo.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.miglab.miyo.R;
import com.miglab.miyo.adapter.ChatListAdapter;
import com.miglab.miyo.entity.SocChatMsg;
import com.miglab.miyo.ui.widget.EmoticonGroup;
import com.miglab.miyo.ui.widget.EmoticonKeyboardBuilder;
import com.miglab.miyo.util.EmoticonUtil;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class ChatActivity extends FragmentActivity implements View.OnClickListener,EmoticonGroup.KeyBoardBarViewListener {
    private ListView chatListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_joinChat;
    private TextView tv_title;
    private ImageView iv_back;
    private RelativeLayout ry_chatBottom;
    private EmoticonGroup emoticonGroup;

    private RightFragment rightFragment;

    private ChatListAdapter chatListAdapter;
    private int curSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        initFragments();
        initViews();
        initListener();
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        rightFragment = new RightFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.right, rightFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "没有更多消息了", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);


            }
        });
    }

    private void initViews() {
        setContentView(R.layout.ac_chat);
        chatListView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        tv_joinChat = (TextView) findViewById(R.id.join);
        ry_chatBottom = (RelativeLayout) findViewById(R.id.chat_bottom);
        tv_title = (TextView) findViewById(R.id.title);
        iv_back = (ImageView) findViewById(R.id.back);

        tv_title.setText("聊天");
        iv_back.setOnClickListener(this);
        tv_joinChat.setOnClickListener(this);
        initEmoticon();
        chatListAdapter = new ChatListAdapter(this, new ArrayList<SocChatMsg>());
        chatListView.setAdapter(chatListAdapter);
    }

    private void initEmoticon() {
        emoticonGroup = (EmoticonGroup) findViewById(R.id.emoticon_group);
        EmoticonKeyboardBuilder builder = new EmoticonKeyboardBuilder.Builder()
                .setEmoticonPageInfoList(EmoticonUtil.getEmoticonPageInfoListFromAsset(this))
                .build();
        emoticonGroup.setBuilder(builder);
        emoticonGroup.setOnKeyBoardBarViewListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join:
                //todo socket 连接
                tv_joinChat.setVisibility(View.GONE);
                ry_chatBottom.setVisibility(View.VISIBLE);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void OnKeyBoardStateChange(int state, int height) {

    }

    //todo 发送消息
    @Override
    public void OnSendBtnClick(String msg) {
        if(!TextUtils.isEmpty(msg)){
            SocChatMsg socChatMsg = new SocChatMsg();
            socChatMsg.setMsg(msg);
            socChatMsg.setIsMe(true);
            chatListAdapter.addData(socChatMsg, true, false);
            curSelection = chatListAdapter.getCount();
            chatListView.setSelection(curSelection);
        }
        emoticonGroup.clearEditText();
    }

    public static class UIHandler extends Handler {
        private final WeakReference<ChatActivity> mActivity;

        public UIHandler(ChatActivity activity) {
            mActivity = new WeakReference<ChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChatActivity activity = mActivity.get();
            if (activity != null) {
                activity.doHandler(msg);
            }
        }
    }

    private void doHandler(Message msg) {
    }

    protected UIHandler handler = new UIHandler(this);
}
