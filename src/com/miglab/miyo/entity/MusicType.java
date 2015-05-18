package com.miglab.miyo.entity;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/18.
 */
public class MusicType extends BaseEntity {
    private boolean isTitle = false;
    private int id;
    private String name;
    private String dim;
    public MusicType(String name,boolean isTitle){
        this.name = name;
        this.isTitle = isTitle;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public boolean getIsTitle(){
        return isTitle;
    }

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }
}
