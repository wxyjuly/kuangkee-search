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

import org.springframework.web.bind.annotation.RequestMapping;

import com.kuangkee.common.pojo.req.UserSearchLogReq;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.constant.Constants;
import com.kuangkee.common.utils.httpclient.HttpClientUtil;
import com.kuangkee.common.utils.json.JsonUtils;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.pojo.vo.AccessToken;
import com.kuangkee.search.pojo.vo.UserInfo;

/**
 * ClassName:Wechat_Constants <br/>
 * Date:     2018年6月15日 上午11:25:35 <br/>
 * @author   Leon Xi
 * @version  v1.0
 * @see 	 
 */
public class Wechat_Constants {
	
	//ACCESS_TOKEN
	public static final String ACCESS_TOKEN = "access_token" ;
	
	//Wechat info
	public static final String APP_ID = "wxa2700dcfca7c6d14" ;
	public static final String APP_SECRET = "9cbccfb6052a2a7c85051dc7c87b3717" ;
	
	public static final String APP_DOMAIN = "http://search.wajiyisheng.com" ; //微信设置的域名
	
	//Wechat redirect url
	public static final String WECHAT_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
			+ APP_ID
			+ "&redirect_uri="
			+ APP_DOMAIN
			+ "/login&response_type=code&scope=snsapi_base&state=STATE123#wechat_redirect" ; //获取code
	
	public static final String WECHAT_OPENID_URL = 
			"https://api.weixin.qq.com/sns/oauth2/access_token?appid="
			+ APP_ID
			+ "&secret="
			+ APP_SECRET
			+ "&grant_type=authorization_code" ;  // "&code=${params.code}" 动态获取
	
	public static final String WECHAT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
			+ "&appid="
			+ APP_ID
			+ "&secret="
			+ APP_SECRET ; //step01:获取access_token,2小时有效;
	
	
	public static final String WECHAT_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?" ; //access_token=Xxx&openid=oAmV1tw7iUbDt0px_z0NBLy164Dg:"
	
	
	
	public static final String WECHAT_BATCH_OPENIDS_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" ; //step02:${token}
	
	//用户相关前缀
	public static final String PRE_USER = "USER_" ;
	
	//登陆页面-重定向使用
	public static final String LOGIN_PAGE = "login" ;
	
	public static final String INDEX_PAGE = "index.html" ;
	
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
	
	/**
	 * 
	 * getAccessToken:获取token. <br/>
	 *
	 * @author Leon Xi
	 * @param token
	 * @return
	 */
	public static final Object getAccessToken(HttpServletRequest request) {
		Object accessToken = SessionUtils.getSessionValue(request, ACCESS_TOKEN) ; 
		
		if(MatchUtil.isEmpty(accessToken)) {
			refreshAccessToken(request) ; // 从新设置session
			accessToken = getAccessToken(request);
		}
		return accessToken ;
	}
	
	/**
	 * 
	 * refreshAccessToken:更新内容该. <br/>
	 * @author Leon Xi
	 * @param request
	 * @return
	 */
	public static final boolean refreshAccessToken(HttpServletRequest request) {
		boolean flag = false ;
		String data = HttpClientUtil.doPost(WECHAT_TOKEN_URL) ;
		
		AccessToken accessToken = JsonUtils.jsonToPojo(data, AccessToken.class) ;
		System.err.println("data->"+accessToken.getAccess_token()+";"+accessToken.getExpires_in());
		
		if(!MatchUtil.isEmpty(accessToken)) { //有值
			SessionUtils.setSessionValue(request, ACCESS_TOKEN, accessToken);
			flag = true ;
		} 
		return flag ;
	}
	
	//--还需要一个调度，定时刷新accessToken--
	public static void main(String[] args) {
		String data = HttpClientUtil.doPost(WECHAT_TOKEN_URL) ;
		System.err.println(data);
		AccessToken accessToken = JsonUtils.jsonToPojo(data, AccessToken.class) ;
		System.err.println("data->"+accessToken.getAccess_token()+";"+accessToken.getExpires_in());
	}
}

