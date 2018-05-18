package com.kuangkee.search.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.common.pojo.req.ArticleReq;
import com.kuangkee.common.pojo.req.UserSearchLogReq;
import com.kuangkee.common.utils.SearchResult;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.check.QueryStrParser;
import com.kuangkee.common.utils.constant.Constants;
import com.kuangkee.common.utils.constant.Constants.KuangKeeResultConst;
import com.kuangkee.common.utils.exception.ExceptionUtil;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.pojo.Account;
import com.kuangkee.search.pojo.Article;
import com.kuangkee.search.pojo.UserSearchLog;
import com.kuangkee.search.pojo.vo.UserInfo;
import com.kuangkee.service.IAccountService;
import com.kuangkee.service.IBrandVolvoService;
import com.kuangkee.service.IUserSearchLogService;
import com.kuangkee.service.solr.IArticleSearchService;

/**
 * 文章查询Controller
 * ClassName: SearchController <br/>
 * date: 2018年1月20日 下午12:23:51 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@RestController
public class ArticleSearchController {
	
	@Value("true")
	private String SEARCH_LOGIN_ENABLE ; //是否需要登陆

	private static final Logger log = LoggerFactory.getLogger(ArticleSearchController.class) ;
	
	@Autowired
	private IArticleSearchService articleSearchService;
	
	@Autowired
	private IUserSearchLogService userSearchLogService ;
	
	@Autowired
	private IAccountService accountService ;
	
	@Autowired
	IBrandVolvoService brandVolvoService ;
	
	/**
	 * http://127.0.0.1:8080/kuangkee-search/query?q=111
	 * search:查询核心业务. <br/>
	 * 查询规则：
	 * 1. 优先按照错误代码(只包含字母和数字)进行匹配
	 * 2. 错误代码无法匹配，再通过文章正文内容进行匹配
	 *    若两者均无法匹配，给出一些提示
	 * @author Leon Xi
	 * @param qryStr
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/query")
	public KuangkeeResult search(
//			:TODO Bean无法绑定问题,通过$(post)
			UserSearchLogReq searchReq, 
			@RequestParam(defaultValue="1")Integer page, 
			@RequestParam(defaultValue="50")Integer rows,
			HttpServletRequest request) {
		
		String originalContent = searchReq.getOriginalContent() ;
		//查询条件不能为空
		if (StringUtils.isBlank(originalContent)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		
		//是否登陆才能搜索
		long uId =-1 ;
		int uIdTmp = 0 ; //保存值
		try {
			uId = searchReq.getUserId() ;
			uIdTmp = (int) uId ;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		UserInfo account = null ;
		if(-1 != uId) {
			account = getAccount(request, searchReq, uId) ; //注册功能
		} else {
			//用户为空，抛异常
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, KuangKeeResultConst.USER_LOGGING_ERROR);
		}
		
		if (MatchUtil.isEmpty(account)) { //找不到用户，非法登陆
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, KuangKeeResultConst.USER_LOGGING_ERROR);
		}
		
		SearchResult<?> searchResult = null;
		try {
			String qryStr = originalContent ;
//					QueryStrParser.trimSpecialSymbol(originalContent) ; //替换特殊字符
			String searchStatus = SearchResult.SearchStatus.NOT_MATCHED_SEARCH ; //匹配结果 
			
//			qryStr = new String(qryStr.getBytes("iso8859-1"), "utf-8");  //转码
			boolean qryStrFlag = QueryStrParser.checkStrIsNumOrAlphabet(qryStr) ; //搜索内容，只包含字母和数字
			Integer brandId = searchReq.getBrandId() ;
			String brandName = searchReq.getBrandName() ;
			String brandCater = "卡特" ;
			
			if(brandId.equals(2) 
					|| brandName.equals(brandCater)) { //卡特单独处理
				qryStrFlag = true ;
			}
			
			if(qryStrFlag) { // 只包含字符串或者数字，直接查询数据库error_code
				log.info("qry info[param:{},page:{},rows{}]", qryStr, page, rows);
				if(brandId.equals(2) 
						|| brandName.equals(brandCater)) { //卡特的搜索，三个部分拼装
					String mid = "" ;
					String cid = "" ;
					String fmi = "" ;
					String caterSpliter = "\\$\\$" ;
					
					try {
						String[] caterSearch = qryStr.split(caterSpliter) ;
						mid = caterSearch[0] ;
						cid = caterSearch[1] ;
						fmi = caterSearch[2] ;
								 
					} catch(Exception e) {
						return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
					}
					if(MatchUtil.isEmpty(mid) 
							|| MatchUtil.isEmpty(cid) 
							|| MatchUtil.isEmpty(fmi)) { //三个搜索条件必须有值
						return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, "卡特搜索条件录入不全，请重新录入...");
					}
					searchResult = brandVolvoService.qryBrandCartByPart(mid, cid, fmi) ;
					
				} else {
					searchResult = articleSearchService.searchArticleListFromDBByPage(qryStr, page, rows) ;
				}
			} 
			
			boolean firstSearchResutIsNotNullFlag = false ; //第一步搜索结果是否有值，有值返回false,否则返回true
			if(!MatchUtil.isEmpty(searchResult) 
					&& !MatchUtil.isEmpty(searchResult.getRecordCount())
					&& searchResult.getRecordCount()>0) {
				firstSearchResutIsNotNullFlag = true ; //有值
				searchStatus = SearchResult.SearchStatus.ERROR_CODE_MATCHED_SEARCH ;
			}
			//1. 查询关键字包含字母、数字之外内容；  或
			//2. 第一步未搜索errorCode没有值 
			if(!qryStrFlag || !firstSearchResutIsNotNullFlag) { // 查询结果为空 或 前一步没有查询到值
				searchResult = articleSearchService.searchArticleListFromSolrByPage(qryStr, page, rows) ;
				if(!MatchUtil.isEmpty(searchResult)
						&& !MatchUtil.isEmpty(searchResult.getRecordCount())
						&& searchResult.getRecordCount()>0) {
					searchStatus = SearchResult.SearchStatus.CONTENT_MATCHED_SEARCH ;
				} 
			}
			
			searchReq.setIsMatch(searchStatus) ;
			searchReq.setOriginalContent(originalContent);
			searchReq.setSearchContent(qryStr) ; //设置实际查询值--需过滤掉特殊字符
			searchReq.setIp(request.getRemoteHost()) ;
			
			searchReq.setTokenId(account.getOpenid()); //openId
			
			searchReq.setUserId(uIdTmp);
			searchReq.setUserName(account.getNickname());
			searchReq.setPhone(account.getPhone());
			
			searchReq.setLatitude(account.getLatitude());
			searchReq.setLongitude(account.getLongitude());
			
			saveUserSearchLog(searchReq) ; //save search log
			
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(searchResult);
	}
	
	/**
	 * 
	 * getArticleDetail: 获取文章明细数据，通过Article_Id. <br/>
	 * @author Leon Xi
	 * @param searchReq
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/articleDetail",method= {})
	public KuangkeeResult getArticleDetail(ArticleReq articleReq,
			HttpServletRequest request) {
		
		//校验是否登陆，并且提示重定向到登陆页面
		
		//文章id不能为空
		if (!MatchUtil.isEmpty(articleReq) 
				&& MatchUtil.isEmpty(articleReq.getArticleId())) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		Article articleResult = null;
		try {
			Article article = new Article() ;
			BeanUtils.copyProperties(articleReq, article);
			articleResult = articleSearchService.qryArticleDetail(article);
			
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(articleResult);
	}
	
	/**
	 * saveUserSearchLog:保存用户搜索记录;保存不允许抛出异常. <br/>
	 * @author Leon Xi
	 * @param searchReq
	 * @param request
	 */
	private void saveUserSearchLog(UserSearchLogReq searchReq) {
		try {
			UserSearchLog record = new UserSearchLog() ;
			//bean copy from req
			BeanUtils.copyProperties(searchReq, record);
			//add different info
			
			record.setMerchantId(searchReq.getFrom());
			record.setCreateTime(new Date());
			record.setUpdateTime(new Date());
			
			log.info("record:->{}",record);
			userSearchLogService.insertUserSearchLog(record) ;
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * http://127.0.0.1:8080/kuangkee-search/query?q=111
	 * search:查询核心业务. <br/>
	 * 查询规则：
	 * 1. 优先按照错误代码(只包含字母和数字)进行匹配
	 * 2. 错误代码无法匹配，再通过文章正文内容进行匹配
	 * 若两者均无法匹配，给出一些提示
	 * @author Leon Xi
	 * @param qryStr
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/qryArticleDetail")
	public KuangkeeResult articleArticleDetail(
//			:TODO Bean无法绑定问题,通过$(post)
			UserSearchLogReq searchReq,
			HttpServletRequest request) {
		String articleId = searchReq.getArticleId() ;
		String uId = searchReq.getTokenId() ;
		//查询条件不能为空
		if (MatchUtil.isEmpty(articleId)
				||MatchUtil.isEmpty(uId)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		Article article = new Article() ;
		
		try {
			article.setArticleId(Long.parseLong(articleId));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e1));
		}
		boolean flag = true ; //查询结果
		Article art = null ;
		try {
			art = articleSearchService.qryArticleDetail(article);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false ;
		}
		if(MatchUtil.isEmpty(art)) {
			flag = false ;
		}
		if(flag) {
			return KuangkeeResult.ok(art);
		} else {
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, KuangKeeResultConst.DB_QUERY_EMPTY_MSG);
		}
	}
	
	/**
	 * 从session中获取用户
	 * @param session
	 * @return account
	 */
	@RequestMapping(value="/userSession")
	public UserInfo getAccount(HttpServletRequest request, UserSearchLogReq searchReq,
			long uId) {
		
		UserInfo userInfo = new UserInfo();
		
		Object userInfoSession = SessionUtils.getSessionValue(request, 
				Constants.SysConstant.ACOUNT + "_"+  String.valueOf(uId)) ;
		if(MatchUtil.isEmpty(userInfoSession)) { //session无值，从数据库获取，并放入session
			Account account = accountService.getAccountByUId(uId) ;
			
			if(!MatchUtil.isEmpty(account)) {
				BeanUtils.copyProperties(account, userInfo);
			}
			if(!MatchUtil.isEmpty(searchReq)) {
				userInfo.setLatitude(searchReq.getLatitude()) ;
				userInfo.setLongitude(searchReq.getLongitude()) ;
			}
			SessionUtils.setSessionValue(request, 
					Constants.SysConstant.ACOUNT + "_"+  String.valueOf(uId), userInfo);
			
		} else { //session有值，直接转换
			userInfo = (UserInfo) userInfoSession ;
		}
		return userInfo ;
	}
	
}
