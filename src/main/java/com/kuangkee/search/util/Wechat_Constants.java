/**
 * Project Name:kuangkee-search
 * File Name:Constants.java
 * Package Name:com.kuangkee.search.util
 * Date:2018年6月15日上午11:25:35
 * Copyright (c) 2018, 【Leon Xi】 All Rights Reserved.
 *
*/

package com.kuangkee.search.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kuangkee.common.utils.constant.Constants;

import com.kuangkee.common.pojo.req.UserSearchLogReq;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.pojo.Account;
import com.kuangkee.search.pojo.vo.UserInfo;

/**
 * ClassName:Wechat_Constants <br/>
 * Date:     2018年6月15日 上午11:25:35 <br/>
 * @author   Leon Xi
 * @version  v1.0
 * @see 	 
 */
public class Wechat_Constants {
	
	//Wechat info
	public static final String APP_ID = "wx506d706ee08ac22b" ;
	public static final String APP_SECRET = "b6664acf41e7e5be80cfd2fdcd74de61" ;
	
	public static final String APP_DOMAIN = "" ; //微信设置的域名
	
	//Wechat redirect url
	public static final String WECHAT_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
			+ APP_ID
			+ "&redirect_uri="
			+ APP_DOMAIN
			+ "/api/login&response_type=code&scope=snsapi_base&state=STATE123#wechat_redirect" ; //获取code
	
	public static final String WECHAT_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
			+ APP_ID
			+ "&secret="
			+ APP_SECRET
			+ "&grant_type=authorization_code" ;  // "&code=${params.code}" 动态获取
	
	public static final String WECHAT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
			+ "&appid="
			+ APP_ID
			+ "&secret="
			+ APP_SECRET ; //step01:获取token
	
	public static final String WECHAT_BATCH_OPENIDS_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" ; //step02:${token}
	
	
	/**
	 * 从session中获取用户,不存在走微信
	 * @param session
	 * @return account
	 */
	@RequestMapping(value="/userSession")
	public static UserInfo getAccount(HttpServletRequest request, UserSearchLogReq searchReq,
			long uId) {
		
		UserInfo userInfo = new UserInfo();
		
		Object userInfoSession = SessionUtils.getSessionValue(request, Constants.SysConstant.ACOUNT + "_"+  String.valueOf(uId)) ;
		
		if(MatchUtil.isEmpty(userInfoSession)) { //session无值，从数据库获取，并放入session
			
			SessionUtils.setSessionValue(request, Constants.SysConstant.ACOUNT + "_"+  String.valueOf(uId), userInfo);
			
		} else { //session有值，直接转换
			userInfo = (UserInfo) userInfoSession ;
		}
		return userInfo ;
	}
}

