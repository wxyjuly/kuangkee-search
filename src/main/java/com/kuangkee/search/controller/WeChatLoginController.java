package com.kuangkee.search.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.httpclient.HttpClientUtil;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.util.Wechat_Constants;
import com.kuangkee.service.IUserSearchLogService;

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
	private IUserSearchLogService userSearchLogService ;
	
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
		Object sessionObj = SessionUtils.getSessionValue(request, Wechat_Constants.PRE_USER+token) ;
		//step01: session获取token对应的用户信息
		if(!MatchUtil.isEmpty(sessionObj)) {  //已登录，从定向到搜索页面
			
		} else { //未登陆，从微信拉取用户信息
			try {
				String url =  Wechat_Constants.WECHAT_CODE_URL ;
				String ret = HttpClientUtil.doPost(url) ;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Wechat Login {} Error!", Wechat_Constants.WECHAT_CODE_URL);
				return "redirect:" + Wechat_Constants.LOGIN_PAGE ; 
			}
		}
		
		return "";
	}
	
	/**
	 * login:微信登陆获取用户信息，模拟登录. <br/>
	   1 第一步：用户同意授权，获取code。页面将跳转至redirect_uri/?code=CODE&state=STATE。
	   		https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
	   2 第二步：通过code换取网页授权access_token，
			https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
	   3 第三步：刷新access_token（如果需要）
	   4 第四步：拉取用户信息(需scope为 snsapi_userinfo)
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
		
		//查询条件不能为空
		if (StringUtils.isBlank(code)) {
			log.info("step02: wechat code get error!");
			return "redirect:" + Wechat_Constants.LOGIN_PAGE ; 
		}

log.info("2. 通过wechat code获取openId 开始--start--");
		String openId = "" ;
		try {
			String url =  Wechat_Constants.WECHAT_OPENID_URL+"&code="+code ;
			String retData = HttpClientUtil.doPost(url) ;
log.info("2. 通过wechat code获取openId 开始--end--,call:{},return:{}", url, retData);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:" + Wechat_Constants.LOGIN_PAGE ; 
		}
		
		return "redirect:" + Wechat_Constants.INDEX_PAGE ; 
	}
	
	public static void main(String[] args) {
		String url =  Wechat_Constants.WECHAT_CODE_URL ;
		String ret = HttpClientUtil.doPost(url) ;
		log.debug(ret);
		//批量获取用户http://www.phpos.net/dingyuehao/178.html
		String url1 =  "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" ;
		String accessToken = "10_o1lHmAwK2fb0e6g6MvrxOnCMt2HRzaWYXz4umDFkxXScSu4iwZL_HkiMBPdPHhi8z7C9yh4M4tNP24bwCC40DYxKua7rStEQzUDtm3v_W1dkJUjY0L5gfJbj3mLznkJh-EiClDk_oEWwYSiwTFEgAJADPO";
		Map<String,Object> data = new HashMap<>() ;
		
		String ret1 = HttpClientUtil.doPost(url1+accessToken) ;
		log.debug(ret);
		
		
		
		
		
	}
	
}
