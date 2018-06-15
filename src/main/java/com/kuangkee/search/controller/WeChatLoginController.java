package com.kuangkee.search.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.common.utils.SearchResult;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.constant.Constants.KuangKeeResultConst;
import com.kuangkee.common.utils.exception.ExceptionUtil;
import com.kuangkee.common.utils.httpclient.HttpClientUtil;
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
	 * login:微信登陆. <br/>
	 * @author Leon Xi
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/index")
	public KuangkeeResult index(HttpServletRequest request) {
		
		String code = request.getParameter("") ;
		//查询条件不能为空
		if (StringUtils.isBlank(code)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		log.info("登陆成功->"+"");
		SearchResult<?> searchResult = null;
		
		try {
			String url =  Wechat_Constants.WECHAT_CODE_URL ;
			String ret = HttpClientUtil.doPost(url) ;
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		
		
		
		return KuangkeeResult.ok(searchResult);
	}
	
	/**
	 * login:微信登陆. <br/>
	 * @author Leon Xi
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/login")
	public KuangkeeResult login(HttpServletRequest request) {
		
		String code = request.getParameter("code") ;
		//查询条件不能为空
		if (StringUtils.isBlank(code)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		log.info("登陆成功->"+"");
		SearchResult<?> searchResult = null;
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(searchResult);
	}
	
	
	/**
	 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1472017492_58YV5
	 * wx:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author Leon Xi
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wx")
	public String wx(HttpServletRequest request) {
		
		String signature = request.getParameter("signature") ;
		String timestamp = request.getParameter("timestatmp") ;
		String nonce = request.getParameter("nounce") ;
		String echostr = request.getParameter("echostr") ;
		String token = "wechat" ;
		log.info("echostr"+echostr);
		if (MatchUtil.isEmpty(echostr)) {
			echostr = "" ;
		}
		return echostr ;
	}
	
	public static void main(String[] args) {
		String url =  Wechat_Constants.WECHAT_CODE_URL ;
		String ret = HttpClientUtil.doPost(url) ;
		log.debug(ret);
	}
	
}
