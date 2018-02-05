package com.kuangkee.search.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.common.pojo.req.UserSearchLogReq;
import com.kuangkee.common.utils.SearchResult;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.constant.Constants.KuangKeeResultConst;
import com.kuangkee.common.utils.exception.ExceptionUtil;
import com.kuangkee.search.pojo.Article;
import com.kuangkee.search.pojo.UserSearchLog;
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

	private static final Logger log = LoggerFactory.getLogger(ArticleSearchController.class) ;
	
	@Autowired
	private IArticleSearchService articleSearchService;
	
	private IUserSearchLogService userSearchLogService ;
	
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
	@RequestMapping(value="/query")
	public KuangkeeResult search(
//			:TODO Bean无法绑定问题,通过$(post)
			UserSearchLogReq searchReq, 
			@RequestParam(defaultValue="1")Integer page, 
			@RequestParam(defaultValue="10")Integer rows,
			HttpServletRequest request) {
		
		String qryStr = searchReq.getSearchContent() ;
		//查询条件不能为空
		if (StringUtils.isBlank(qryStr)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		
		SearchResult<Article> searchResult = null;
		searchReq.setIp(request.getRemoteHost());
		try {
			qryStr = new String(qryStr.getBytes("iso8859-1"), "utf-8");
			searchResult = articleSearchService.search(qryStr, page, rows);
			
			saveUserSearchLog(searchReq, searchResult.getSearchStatus()) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(searchResult);
	}
	
	/**
	 * 
	 * saveUserSearchLog:保存用户搜索记录). <br/>
	 *
	 * @author Leon Xi
	 * @param searchReq
	 * @param request
	 */
	private void saveUserSearchLog(UserSearchLogReq searchReq, String searchStatus) {
		
		UserSearchLog record = new UserSearchLog() ;
		Integer brandId = record.getBrandId() ;
		String brandName = record.getBrandName() ;
		String searchContent = record.getSearchContent() ;
		String userName = record.getUserName() ;
		String phone = record.getPhone() ;
		String longitude = record.getSearchContent() ;
		String latitude = record.getSearchContent() ;
		
		String isMatch = record.getIsMatch() ;
		
		if(MatchUtil.isEmpty(brandId)) {
			brandId = 0 ;
		}
		if(MatchUtil.isEmpty(brandName)) {
			brandName = "" ;
		}
		if(MatchUtil.isEmpty(searchContent)) {
			searchContent = "" ;
		}
		if(MatchUtil.isEmpty(userName)) {
			userName = "" ;
		}
		if(MatchUtil.isEmpty(phone)) {
			phone = "" ;
		}
		if(MatchUtil.isEmpty(longitude)) {
			longitude = "" ;
		}
		if(MatchUtil.isEmpty(latitude)) {
			latitude = "" ;
		}
		if(MatchUtil.isEmpty(isMatch)) {
			isMatch = SearchResult.SearchStatus.NOT_MATCHED_SEARCH ;
		}
		
		record.setBrandId(brandId);
		record.setBrandName(brandName);
		record.setSearchContent(searchContent);
		record.setUserName(userName);
		record.setPhone(phone);
		record.setLongitude(longitude);
		record.setLatitude(latitude);
		record.setIsMatch(isMatch);
		
		userSearchLogService.insertUserSearchLog(record) ;
	}
	
}
