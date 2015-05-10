package com.miglab.miyo;

/**
 * @author ZhaoLongQuan
 * @version 创建时间：2015-1-28 上午11:30:33
 * 类说明
 */
public class Account {

	public int type; // 登陆来源 0:快速登录 1 新浪微博 2 微信 3 QQ 4 手机登陆
	public String id; // 帐号平台的id
	public String token; // 第三方平台返回唯一凭据
	public String nickname;
	public int gender; // 0 女 1男
	public String headUrl;
	public int birthday;
	public String location;
	public String imei;
	public long expires;
	
	@Override
	public String toString() {
		return "Account [type=" + type + ", id=" + id + ", token=" + token
				+ ", nickname=" + nickname + ", gender=" + gender
				+ ", headUrl=" + headUrl + ", birthday=" + birthday
				+ ", location=" + location + ", imei=" + imei + "]";
	}
}
