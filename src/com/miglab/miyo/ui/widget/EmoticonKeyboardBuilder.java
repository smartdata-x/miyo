package com.miglab.miyo.ui.widget;

import com.miglab.miyo.entity.EmoticonPageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonKeyboardBuilder {
    public Builder builder;

    public EmoticonKeyboardBuilder(Builder builder){
        this.builder = builder;
    }

    public static class Builder {

        List<EmoticonPageInfo> mEmoticonPageInfoList = new ArrayList<EmoticonPageInfo>();

        public Builder(){ }

        public List<EmoticonPageInfo> getEmoticonSetBeanList() { return mEmoticonPageInfoList; }

        public Builder setEmoticonPageInfoList(List<EmoticonPageInfo> list) { this.mEmoticonPageInfoList = list;  return this;}

        public EmoticonKeyboardBuilder build() { return new EmoticonKeyboardBuilder(this); }
    }
}
