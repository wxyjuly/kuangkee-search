package com.kuangkee.search.pojo.vo;

/**
 * 接口中获取用户实体Bean
 * ClassName: User <br/>
 * date: 2018年2月6日 上午11:58:24 <br/>
 * @author Leon Xi
 * @version v1.0
 */
public class UserInfo {

	private String userId ;
	private String wechantID ;
	private String userName ;
	private String phone ;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWechantID() {
		return wechantID;
	}
	public void setWechantID(String wechantID) {
		this.wechantID = wechantID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}