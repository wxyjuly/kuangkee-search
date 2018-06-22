package com.kuangkee.search ;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.druid.support.json.JSONUtils;
import com.kuangkee.common.utils.httpclient.HttpClientUtil;
import com.kuangkee.common.utils.json.JsonUtils;
import com.kuangkee.search.pojo.vo.AccessToken;
import com.kuangkee.search.pojo.vo.UserOpenIdReq;
import com.kuangkee.search.util.Wechat_Constants;
import com.kuangkee.service.IExpertService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext-*.xml" })
public class WechatAPITest {
	private static final Logger log = LoggerFactory.getLogger(WechatAPITest.class) ;
	
	@Autowired
	IExpertService expertService ;
	
	/**
	 * testGetAccessToken:获取AccessTocken. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testGetAccessToken() throws Exception {
		String accessTokenURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxa2700dcfca7c6d14&secret=9cbccfb6052a2a7c85051dc7c87b3717" ;
		String data = HttpClientUtil.doPost(accessTokenURL) ;
		log.info(data);
		AccessToken accessToken = JsonUtils.jsonToPojo(data, AccessToken.class) ;
		log.info("AccessToken-data->{};{}", accessToken.getAccess_token() , accessToken.getExpires_in());
	}
	
	/**
	 * testGetCode:获取Code. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testGetCode() throws Exception {
		String data = HttpClientUtil.doPost(Wechat_Constants.WECHAT_CODE_URL) ;
		log.info(data);
	}
	
	/**
	 * testGetOpenIdByCode:通过code获取openId. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testGetOpenIdByCode() throws Exception {
		String code = "" ;
		String openIdURL = Wechat_Constants.WECHAT_OPENID_URL + "&code=" + code ;
		String data = HttpClientUtil.doPost(openIdURL) ;
		log.info(data);
	}
	
	/**
	 * testGetUserInfoByByBatchOpenIds:通过openId批量获取用户信息. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 * oVF7E1LZRGZpsJpAQHzsEKzDZXYc
	 */
	@Test
	public void testGetUserInfoByByBatchOpenIds() throws Exception {
		
		String accessToken = "" ; 
		String batchUserInfoURL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=accessToken" ;
		
		String json = "" ;
		Map<String,Object> user_list = new HashMap<>() ;
		//set 1
		Map<String,Object> openIds = new HashMap<>() ;
		openIds.put("openid", "oVF7E1LZRGZpsJpAQHzsEKzDZXYc") ;
		openIds.put("lang", "zh-CN") ;
		user_list.put("user_list", openIds) ;
		json = JsonUtils.objectToJson(user_list) ;
		log.error("Json:{}", json);
		
		String data = HttpClientUtil.doPostJson(batchUserInfoURL, json) ;
		log.info(data);
	}
	
	public static void main(String[] args) {
		
//		String json = "" ;
//		Map<String,Object> user_list = new HashMap<>() ;
		//set 1
//		List<Object> openIds = new ArrayList<>() ;
//		UserOpenIdReq openIdReq = new UserOpenIdReq() ;
//		openIdReq.setOpenid("oVF7E1LZRGZpsJpAQHzsEKzDZXYc");
//		openIdReq.setLang("zh-CN");
//		openIds.add(openIdReq) ;
//		user_list.put("user_list", openIds) ;
		
//		json = JsonUtils.objectToJson(user_list) ;

		String json = "" ;
		Map<String,Object> user_list = new HashMap<>() ;
		//set 1
		Map<String,Object> openIds = new HashMap<>() ;
		openIds.put("openid", "oVF7E1LZRGZpsJpAQHzsEKzDZXYc") ;
		openIds.put("lang", "zh-CN") ;
		user_list.put("user_list", openIds) ;
		json = JsonUtils.objectToJson(user_list) ;
		
		System.err.println(json);

	}
	
	
	
	
	
	
	
	
	

}