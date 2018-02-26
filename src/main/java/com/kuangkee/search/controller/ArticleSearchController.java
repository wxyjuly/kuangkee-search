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
import com.kuangkee.service.IAccountService;
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
			@RequestParam(defaultValue="10")Integer rows,
			HttpServletRequest request) {
		
		String originalContent = searchReq.getOriginalContent() ;
		//查询条件不能为空
		if (StringUtils.isBlank(originalContent)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		
		//是否登陆才能搜索
		long uId =-1 ;
		try {
			uId = searchReq.getUserId() ;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Account account = null ;
		if(-1 != uId) {
			account = getAccount(request, uId) ;
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
			
			if(qryStrFlag) { // 只包含字符串或者数字，直接查询数据库error_code
				log.info("qry info[param:{},page:{},rows{}]", qryStr, page, rows);
				searchResult = articleSearchService.searchArticleListFromDBByPage(qryStr, page, rows) ;
			} 
			boolean firstSearchResutIsNullFlag = true ; //第一步搜索结果是否有值，有值返回false,否则返回true
			if(!MatchUtil.isEmpty(searchResult) 
					&& !MatchUtil.isEmpty(searchResult.getRecordCount())
					&& searchResult.getRecordCount()>0) {
				firstSearchResutIsNullFlag = false ; //有值
				searchStatus = SearchResult.SearchStatus.ERROR_CODE_MATCHED_SEARCH ;
			}
			//1. 查询关键字包含字母、数字之外内容；  或
			//2. 第一步未搜索通过errorCode没有值 
			if(!qryStrFlag && firstSearchResutIsNullFlag) { // 查询结果为空 或 前一步没有查询到值
				searchResult = articleSearchService.searchArticleListFromSolrByPage(qryStr, page, rows) ;
				if(!MatchUtil.isEmpty(searchResult)
						&& !MatchUtil.isEmpty(searchResult.getRecordCount())
						&& searchResult.getRecordCount()>0) {
					searchStatus = SearchResult.SearchStatus.ERROR_CODE_MATCHED_SEARCH ;
				} 
			}
			
			searchReq.setIsMatch(searchStatus) ;
			searchReq.setOriginalContent(originalContent);
			searchReq.setSearchContent(qryStr) ; //设置实际查询值--需过滤掉特殊字符
			searchReq.setIp(request.getRemoteHost()) ;
			
			searchReq.setTokenId(account.getOpenid()); //openId
			searchReq.setUserId(account.getId());
			searchReq.setUserName(account.getNickname());
			searchReq.setPhone(account.getPhone());
			
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
	public KuangkeeResult articleDetail(
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
			article.setArticleId(Integer.parseInt(articleId));
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
	@RequestMapping(value="/user")
	public Account getAccount(HttpServletRequest request,
			long uId) {
		
		Object sesAccount = SessionUtils.getSessionValue(request, Constants.SysConstant.ACOUNT) ;
		Account account ;
		if(MatchUtil.isEmpty(sesAccount)) { //session无值，从数据库获取，并放入session
			account = accountService.getAccountByUId(uId) ;
			
			SessionUtils.setSessionValue(request, Constants.SysConstant.ACOUNT, account);
			
		} else { //session有值，直接转换
			account = (Account) sesAccount ;
		}
		return account ;
	}
	
}
