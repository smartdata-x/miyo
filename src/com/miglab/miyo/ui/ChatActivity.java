package com.miglab.miyo.ui;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.adapter.ChatListAdapter;
import com.miglab.miyo.entity.PushMessageInfo;

import java.util.*;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class ChatActivity extends BaseActivity {
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void init() {
        setContentView(R.layout.ac_chat);
        initViews();
        initListener();
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                uiHandler.postDelayed(new Runnable() {
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
        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView.setAdapter(new ChatListAdapter(this, new ArrayList<PushMessageInfo>()));
    }
}
