package com.kuangkee.search.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
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
import com.kuangkee.common.pojo.req.UserSearchLogReq;
import com.kuangkee.common.utils.SearchResult;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.constant.Constants.KuangKeeResultConst;
import com.kuangkee.common.utils.exception.ExceptionUtil;
import com.kuangkee.search.pojo.Article;
import com.kuangkee.search.pojo.UserSearchLog;
import com.kuangkee.search.pojo.vo.UserInfo;
import com.kuangkee.search.service.intefacepackage.IUserService;
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
	
//	@Autowired
//	private IUserService userServiceImp;
	
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
		
		String qryStr = searchReq.getOriginalContent() ;
		//查询条件不能为空
		if (StringUtils.isBlank(qryStr)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		//bean copy from userInfo Inteface
		if("true".equals(SEARCH_LOGIN_ENABLE)) {
			
		}
		
//		String uId = searchReq.getTokenId() ;
//		if(!MatchUtil.isEmpty(uId)) {
//			try {
//				UserInfo userInfo = userServiceImp.getUserInfoFromInteface(uId) ;
//			} catch (BeansException e) {
//				log.error("搜索用户数据，读取接口或者拷贝失败");
//				e.printStackTrace();
//			}
//		}
		
		SearchResult<Article> searchResult = null;
		try {
//			qryStr = new String(qryStr.getBytes("iso8859-1"), "utf-8");
			searchResult = articleSearchService.search(qryStr, page, rows);
			searchReq.setSearchContent(qryStr); //设置实际查询值--需过滤掉特殊字符
			searchReq.setIp(request.getRemoteHost());

			String searchStatus = SearchResult.SearchStatus.NOT_MATCHED_SEARCH ;
			
			if(!MatchUtil.isEmpty(searchStatus)) {
				searchStatus = searchResult.getSearchStatus() ;
			} 
			searchReq.setIsMatch(searchStatus);
			
			saveUserSearchLog(searchReq) ; //save search log
			
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(searchResult);
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
			
			log.error("record:->"+record);
			userSearchLogService.insertUserSearchLog(record) ;
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}
	
}
