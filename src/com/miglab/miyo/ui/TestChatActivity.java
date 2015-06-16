package com.miglab.miyo.ui;

import android.widget.ListView;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.EmoticonPageInfo;
import com.miglab.miyo.ui.widget.EmoticonGroup;
import com.miglab.miyo.ui.widget.EmoticonKeyboardBuilder;
import com.miglab.miyo.util.EmoticonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class TestChatActivity extends BaseActivity{
    ListView lv_chat;
    EmoticonGroup emoticonGroup;
    private EmoticonKeyboardBuilder builder;

    @Override
    protected void init() {
        setContentView(R.layout.ac_test);
        findView();
    }

    private void findView() {
        emoticonGroup = (EmoticonGroup) findViewById(R.id.emoticon_group);
        lv_chat = (ListView) findViewById(R.id.listView);
        emoticonGroup.setBuilder(getBuilder());
    }

    public EmoticonKeyboardBuilder getBuilder() {
        builder = new EmoticonKeyboardBuilder.Builder()
                .setEmoticonPageInfoList(EmoticonUtil.getEmoticonPageInfoList())
                .build();
        return builder;
    }

}
