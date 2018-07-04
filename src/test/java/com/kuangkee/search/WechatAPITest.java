package com.kuangkee.search ;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kuangkee.common.pojo.common.wechat.AccessTokenInfo;
import com.kuangkee.common.pojo.common.wechat.WechatUserInfo;
import com.kuangkee.common.pojo.common.wechat.Wechat_Constants;
import com.kuangkee.common.utils.httpclient.HttpClientUtil;
import com.kuangkee.common.utils.json.JsonUtils;
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
		String accessTokenURL = Wechat_Constants.WECHAT_TOKEN_URL ;
		String data = HttpClientUtil.doPost(accessTokenURL) ;
		log.info(data);
		AccessTokenInfo accessToken = JsonUtils.jsonToPojo(data, AccessTokenInfo.class) ;
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
	 * testGetUserDetailInfo:通过access_token,openId获取用户基本信息 . <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testGetUserDetailInfo() throws Exception {
		String accessToken = "" ;
		String openId = "" ;
		String openIdURL = Wechat_Constants.WECHAT_USERINFO_URL  
						 + "&access_token=" + accessToken 
				         + "&openid=" + openId ;
		String data = HttpClientUtil.doPost(openIdURL) ;
		log.info(data) ;
		
		WechatUserInfo userInfo = JsonUtils.jsonToPojo(data, WechatUserInfo.class) ;
		
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
	/**
	 * testRedirectFromHttpClient:测试用户数据. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 * oVF7E1LZRGZpsJpAQHzsEKzDZXYc
	 */
	@Test
	public void testRedirectFromHttpClient() throws Exception {
		
		String accessToken = "oVF7E1LZRGZpsJpAQHzsEKzDZXYc" ; 
		String batchUserInfoURL = "http://127.0.0.1:8080/kuangkee-search/testRedirect" ;
		
		String json = "" ;
		//set 1
		Map<String,Object> openIds = new HashMap<>() ;
		openIds.put("token", accessToken) ;
		json = JsonUtils.objectToJson(openIds) ;
		log.error("Json:{}", json);
		
		String data = HttpClientUtil.doPostJson(batchUserInfoURL, json) ;
		log.error("========result======"+data);
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