package com.miglab.miyo.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonPageInfo {
    /**
     * ���鼯����(������Ψһ)
     */
    private String name;
    /**
     * ÿҳ����
     */
    private int line;
    /**
     * ÿҳ����
     */
    private int row;
    /**
     * ���鼯ͼ��·��
     */
    private String iconUri;
    /**
     * ���鼯ͼ������
     */
    private String iconName;
    /**
     * �Ƿ���ÿҳ���һ����ʾɾ����ť
     */
    private boolean isShowDelBtn;
    /**
     * �����ڼ��
     */
    private int itemPadding;
    /**
     * �����м��
     */
    private int horizontalSpacing;
    /**
     * �����м��
     */
    private int verticalSpacing;
    /**
     * ���鼯����Դ
     */
    private List<Emoticon> emoticonList;

    public EmoticonPageInfo(String name , int line , int row) {
        this.name = name;
        this.line = line;
        this.row = row;
    }

    public EmoticonPageInfo(String name , int line , int row , String iconUri , String iconName , boolean isShowDelBtn ,
                           int itemPadding , int horizontalSpacing , int verticalSpacing , List<Emoticon> emoticonList){
        this.name = name;
        this.line = line;
        this.row = row;
        this.iconUri = iconUri;
        this.iconName = iconName;
        this.isShowDelBtn = isShowDelBtn;
        this.itemPadding = itemPadding;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.emoticonList = emoticonList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public boolean isShowDelBtn() {
        return isShowDelBtn;
    }

    public void setIsShowDelBtn(boolean isShowDelBtn) {
        this.isShowDelBtn = isShowDelBtn;
    }

    public int getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(int itemPadding) {
        this.itemPadding = itemPadding;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public List<Emoticon> getEmoticonList() {
        return emoticonList;
    }

    public void setEmoticonList(ArrayList<Emoticon> emoticonList) {
        this.emoticonList = emoticonList;
    }
}
