/**
 * Project Name:kuangkee-search
 * File Name:WechatUtil.java
 * Package Name:com.kuangkee.search.util
 * Date:2018年7月3日下午2:20:54
 * Copyright (c) 2018, 【Leon Xi】 All Rights Reserved.
 *
*/

package com.kuangkee.search.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuangkee.common.pojo.common.wechat.AccessTokenInfo;
import com.kuangkee.common.pojo.common.wechat.Wechat_Constants;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.httpclient.HttpClientUtil;
import com.kuangkee.common.utils.json.JsonUtils;

/**
 * ClassName:WechatUtil <br/>
 * 操作微信
 * Date:     2018年7月3日 下午2:20:54 <br/>
 * @author   Leon Xi
 * @version  v1.0
 * @see 	 
 */
public class WechatUtil {
	private static final Logger LOG = LoggerFactory.getLogger(WechatUtil.class) ;
	
	public static final String ACCESS_TOKEN = "access_token" ;
	
	public static final String getAccessToken() {
		int RE_TRY_TIMES = 3 ; //接口调用重试次数
		int count = 0 ;
		String newAccessToken = null ;
		String accessTokenURL = Wechat_Constants.WECHAT_TOKEN_URL ;
		
		while(count<RE_TRY_TIMES) {
			newAccessToken = takeAccessToken(accessTokenURL);
			if(!MatchUtil.isEmpty(newAccessToken)) {
				break ;
			} else {
				count++ ;
			}
		}
		return newAccessToken ;
	}

	/**
	 * 
	 * takeAccessToken:获取AccessToken. <br/>
	 *
	 * @author Leon Xi
	 * @param accessTokenURL
	 * @return
	 */
	private static String takeAccessToken(String accessTokenURL) {
		String newAccessToken = null ;
		try {
			String data = HttpClientUtil.doPost(accessTokenURL) ;
			LOG.info(data);
			AccessTokenInfo accessToken = JsonUtils.jsonToPojo(data, AccessTokenInfo.class) ;
			newAccessToken = accessToken.getAccess_token() ;
			LOG.info("AccessTokenData->accessToken:{}; expiresTime:{}", newAccessToken, accessToken.getExpires_in());
		} catch (Exception e) {
			newAccessToken = null ;
			e.printStackTrace();
			
		}
		return newAccessToken;
	}
	
	
	
}

