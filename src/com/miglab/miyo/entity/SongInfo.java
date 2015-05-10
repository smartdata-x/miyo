package com.miglab.miyo.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author song
 * @version 创建时间：2015-2-8 下午10:27:19
 * 类说明 歌曲信息
 */
public class SongInfo implements Serializable{

	private static final long serialVersionUID = -5482142792946768459L;
	public int id;
	public String name;
	public String album;
	public String artist;
	public String pic;
	public String hq_url;
	public String url;
	public int tid;
	public int type;
	public int clt;
	public int hot;
	public int like;//0未收藏，1已收藏
	
	public SongInfo(JSONObject json) {
		if (json != null) {
			id = json.optInt("id");
			name = json.optString("title");
			album = json.optString("album");
			artist = json.optString("artist");
			pic = json.optString("pic");
			hq_url = json.optString("hq_url");
			url = json.optString("url");
			type = json.optInt("type");
			tid = json.optInt("tid");
			type = json.optInt("type");
			clt = json.optInt("clt");
			hot = json.optInt("hot");
			like = json.optInt("like");
		}
	}

	@Override
	public String toString() {
		return "SongInfo [id=" + id + ", name=" + name + ", album=" + album
				+ ", artist=" + artist + ", pic=" + pic + ", hq_url=" + hq_url
				+ ", url=" + url + ", tid=" + tid + ", type=" + type + ", clt="
				+ clt + ", hot=" + hot + "]";
	}

	
}
