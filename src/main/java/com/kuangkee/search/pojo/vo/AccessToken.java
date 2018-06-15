/**
 * Project Name:kuangkee-search
 * File Name:AccessToken.java
 * Package Name:com.kuangkee.search.pojo.vo
 * Date:2018年6月15日下午5:17:15
 * Copyright (c) 2018, 【Leon Xi】 All Rights Reserved.
 *
*/

package com.kuangkee.search.pojo.vo;
/**
 * ClassName:AccessToken <br/>
 * Date:     2018年6月15日 下午5:17:15 <br/>
 * @author   Leon Xi
 * @version  v1.0
 * @see 	 
 */
public class AccessToken {
	
	private String access_token ;
	
	private String expires_in ;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	
	
	
}

