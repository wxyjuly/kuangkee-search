/**
 * Project Name:kuangkee-search
 * File Name:WechatOpenId.java
 * Package Name:com.kuangkee.search.pojo.vo
 * Date:2018年6月22日下午5:25:02
 * Copyright (c) 2018, 【Leon Xi】 All Rights Reserved.
 *
*/

package com.kuangkee.search.pojo.vo;
/**
 * 获取封装的数据：https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx503ea6e774b89a36&secret=ef490668f7f0f6c17034cdc6a11b6864&code=081u3vfV0tWivV1W4pfV0jIMfV0u3vff&grant_type=authorization_code
 * ClassName:WechatOpenId <br/>
 * Date:     2018年6月22日 下午5:25:02 <br/>
 * @author   Leon Xi
 * @version  v1.0
 * @see 	 
 */
public class WechatOpenId {
	
	private String accessToken ;
	
	private String expriredIn ;
	
	private String refreshToken ;
	
	private String openId ;
	
	private String scope ;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpriredIn() {
		return expriredIn;
	}

	public void setExpriredIn(String expriredIn) {
		this.expriredIn = expriredIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
	public static void main(String[] args) {
		
	}
}

