package com.kuangkee.search.service.intefacepackage.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kuangkee.common.utils.httpclient.HttpClientUtil;
import com.kuangkee.search.pojo.vo.UserInfo;
import com.kuangkee.search.service.intefacepackage.IUserService;

/**
 * 搜索Service
 * ClassName: SearchServiceImpl <br/>
 * date: 2018年1月20日 下午3:49:53 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@Service
public class UserServiceImp implements IUserService {

	@Override
	public UserInfo getUserInfoFromInteface(String tokenId) {
		
		String url = "127.0.0.1" ;
		Map<String,String> param = new HashMap<>() ;
		param.put("tokenId", tokenId) ;
		String intfaceResult = HttpClientUtil.doGet(url, param) ;
		
		//:TODO transfer user Info to Bean
		UserInfo userInfo = new UserInfo() ;
		
		userInfo.setPhone("13608215714");
		userInfo.setWechantID("wechat-Test01");
		userInfo.setUserId("0001");
		userInfo.setUserName("测试用户1");
		return userInfo ;
	}


}
