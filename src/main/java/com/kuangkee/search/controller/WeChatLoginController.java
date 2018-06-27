package com.kuangkee.search.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kuangkee.common.pojo.common.wechat.WechatOpenId;
import com.kuangkee.common.pojo.common.wechat.WechatUserInfo;
import com.kuangkee.common.pojo.common.wechat.Wechat_Constants;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.httpclient.HttpClientUtil;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.pojo.Account;
import com.kuangkee.search.util.AccountSession;
import com.kuangkee.service.IAccountService;
import com.kuangkee.service.wechat.IWechatService;

/**
 * 微信登陆Controller
 * ClassName: SearchController <br/>
 * date: 2018年1月20日 下午12:23:51 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@RestController
public class WeChatLoginController {
	
	private static final Logger log = LoggerFactory.getLogger(WeChatLoginController.class) ;
	
	@Autowired
	private IAccountService accountService ;
	
	@Autowired
	private IWechatService wechatService ;
	
	/**
	 * index:搜索页面-微信登陆. <br/>
	 * @author Leon Xi
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request,RedirectAttributes attr) {
		
		String token = request.getParameter("token") ;  //user token
		
		if (StringUtils.isBlank(token)) {
			log.info("user token is null!");
			return "redirect:"+Wechat_Constants.LOGIN_PAGE ; 
		}
		Object sessionObj = SessionUtils.getSessionValue(request, token) ;
		//step01: session获取token对应的用户信息
		if(!MatchUtil.isEmpty(sessionObj)) {  //已登录，从定向到搜索页面
			return "redirect:" + Wechat_Constants.INDEX_PAGE + "?token=" + token ;
			
		} else { //未登陆，页面跳转，从微信拉取用户信息从定向到页面
			try {
				String url =  Wechat_Constants.WECHAT_CODE_URL ;
				String ret = HttpClientUtil.doPost(url) ;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Wechat Login {} Error!", Wechat_Constants.WECHAT_CODE_URL);
				return "redirect:" + Wechat_Constants.LOGIN_PAGE ; 
			}
		}
		
		return "" ;
	}
	
	/**
	 * login:微信登陆获取用户信息，模拟登录. <br/>
	   1 第一步：用户同意授权，获取code。页面将跳转至redirect_uri/?code=CODE&state=STATE。
	   		https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
	   2 第二步：通过code换取网页授权access_token，
			https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
	   3 第三步：刷新access_token（如果需要）
	   4 第四步：拉取用户信息(需scope为 snsapi_userinfo)
	   
------------------------------------------------------------------------------	   
	1. 获取code
	2. 通过code获取openId.
	3. 通过openId查询数据库，是否存在用户数据?
		3.1  存在，看是否包含手机号码？
			3.1.1  不包含,跳转到登陆页面5
			3.1.2 包含，跳转到6
		3.2 不存在，跳转到4
	4. 通过access_token,openId获取用户基本数据
		4.1	获取成功，保存用户基本数据，跳转到手机号录入页面5。
		4.2	获取失败，刷新access_token，若出错提示.
	5. 手机登陆页面录入到手机号码。
	6. 搜索页面首页			
-----------------------------------------------------------------------------
	
	 * @see https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
	 * @author Leon Xi
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/login")
	public String login(HttpServletRequest request) {
		
        String code = request.getParameter("code") ;
		String state = request.getParameter("state") ;
		log.error("===> 登陆获取的参数code:{}, state:{}", code, state);
		
		String token = null ;
		//查询条件不能为空
		if (StringUtils.isBlank(code)) {
			log.info("step02: wechat code get error!");
			return "redirect:" + Wechat_Constants.LOGIN_PAGE ; 
		}

		WechatOpenId wechatOpenId = wechatService.getUserOpenId(code) ;
		String openId = wechatOpenId.getOpenId() ;
		
		if(MatchUtil.isEmpty(openId)) {
			return "获取openId出错..." ; 
		}
		
		token = openId ; //获取用户token
		
		//qryUserInfoByOpenId
		Account accountReq = new Account() ;
		accountReq.setOpenid(openId) ;
		Account account = accountService.getAccountByAccountInfo(accountReq) ; //qry from DB  :TODO
		log.info("account:{}", account) ;
		
		if(MatchUtil.isEmpty(account)) { //DB中没有，从接口中获取
			String accessToken = (String) AccountSession.getSessionData(request, Wechat_Constants.ACCESS_TOKEN) ; //session中获取
			
			WechatUserInfo userInfo = wechatService.getUserInfo(openId, accessToken) ;
			
			if(MatchUtil.isEmpty(userInfo)) {
				log.info("通过接口获取用户信息失败...");
				//throw Error to inteface 
				return "获取用户数据出错，请稍后再试..." ;
			} else {
				//save to DB 
				accountReq = new Account() ;
				BeanUtils.copyProperties(userInfo, accountReq) ;
				log.error("from Bean[userInfo]{}, to Bean[userInfo]{}", userInfo.toString(), accountReq.toString());
				boolean flag = accountService.saveAccountInfo(account) ;
				log.info("用户信息保存:{}",flag) ; 
				
				//save to session
				AccountSession.setSessionData(request, openId, accountReq);
				
			}
			return "redirect:"+ "savePhonePage" + "?token=" + token ;   // :TODO 跳转到手机号码录入页面
			
		} else {  
			String userPhoneNo = account.getPhone() ;
			if(MatchUtil.isEmpty(userPhoneNo)) {  //DB中有数据，但手机号码为空
				return "redirect:"+ "savePhonePage";  // :TODO 跳转到手机号码录入页面
			}
		}
		return "redirect:" + Wechat_Constants.INDEX_PAGE + "?token=" + token ;  //:TODO 跳转到搜索首页，带参数Token
	}
	
	/**
	 * savePhoneNo:保存手机号码，并重定向到搜索页面. <br/>
	 * @author Leon Xi
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/phone")
	public String savePhoneNo(HttpServletRequest request, 
			@Param("phoneNo") String phoneNo,
			@Param("token") String token,
			@Param("lat") String lat,
			@Param("lng") String lng) {
		
		if(MatchUtil.isEmpty(phoneNo) || MatchUtil.isEmpty(token)) {
			return null ;
		}
		
		//get userInfo from session
		Account accountReq = (Account) SessionUtils.getSessionValue(request, token) ;
		if(MatchUtil.isEmpty(accountReq)) {
			return null ;
		}
		
		if(MatchUtil.isEmpty(lat)) {
			accountReq.setLat(lat) ;
		}
		if(MatchUtil.isEmpty(lng)) {
			accountReq.setLng(lng) ;
		}
		
		accountReq.setPhone(phoneNo) ;
		
		//update session 
		SessionUtils.setSessionValue(request, token, accountReq) ;
		//save DB
		boolean flag = accountService.saveAccountInfo(accountReq) ;
		log.info("用户手机号保存:{}",flag) ;
		
		return "redirect:" + Wechat_Constants.INDEX_PAGE + "?token=" + token ;  // and Token :TODO
	}
	
	@RequestMapping(value="/testRedirect")
	public String testRedirect(HttpServletRequest request, 
			HttpServletResponse response,
			@Param("phoneNo") String phoneNo,
			@Param("token") String token) {
		
		return "redirect:" + Wechat_Constants.INDEX_PAGE + "?token=" + token ;  // and Token 
	}
	
	
	public static void main(String[] args) {
		String url =  Wechat_Constants.WECHAT_CODE_URL ;
		String ret = HttpClientUtil.doPost(url) ;
		log.debug(ret);
		//批量获取用户-> http://www.phpos.net/dingyuehao/178.html
		String url1 =  "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" ;
		String accessToken = "10_o1lHmAwK2fb0e6g6MvrxOnCMt2HRzaWYXz4umDFkxXScSu4iwZL_HkiMBPdPHhi8z7C9yh4M4tNP24bwCC40DYxKua7rStEQzUDtm3v_W1dkJUjY0L5gfJbj3mLznkJh-EiClDk_oEWwYSiwTFEgAJADPO";
		Map<String,Object> user_list = new HashMap<>() ;
		Map<String,Object> openIds = new HashMap<>() ;
		openIds.put("openid", "oVF7E1LZRGZpsJpAQHzsEKzDZXYc") ;
		String ret1 = HttpClientUtil.doPost(url1+accessToken) ;
		log.debug(ret);
		
	}
	
}
