package com.kuangkee.search.pojo.vo;

/**
 * 接口中获取用户实体Bean
 * ClassName: User <br/>
 * date: 2018年2月6日 上午11:58:24 <br/>
 * @author Leon Xi
 * @version v1.0
 */
public class UserInfo {

    private Long id; //userId

    private Long version;

    private String phone;  //userPhone

    private String tagidList;

    private String province;

    private Integer sex;

    private Integer subscribe;

    private String nickname;  //userName

    private String groupid;

    private String language;

    private Long subscribeTime;

    private String openid;  //wechat

    private String city;

    private String remark;

    private String unionid;

    private String country;

    private String headimgurl;

    //extends
    private String longitude;

    private String latitude;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTagidList() {
		return tagidList;
	}

	public void setTagidList(String tagidList) {
		this.tagidList = tagidList;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Long getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
    
}