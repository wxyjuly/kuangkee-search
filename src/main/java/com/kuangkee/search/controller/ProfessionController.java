package com.kuangkee.search.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * 专家Controller
 * ClassName: SearchController <br/>
 * date: 2018年1月20日 下午12:23:51 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@RestController
public class ProfessionController {
	
	@Value("true")
	private String SEARCH_LOGIN_ENABLE ; //是否需要登陆

	private static final Logger log = LoggerFactory.getLogger(ProfessionController.class) ;
	
	@Autowired
	private IArticleSearchService articleSearchService;
	
	@Autowired
	private IUserSearchLogService userSearchLogService ;
	
//	@Autowired
//	private IUserService userServiceImp;
	
	/**
	 * 
	 * qryProfList:分页查询专家. <br/>
	 * @author Leon Xi
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryProfList")
	public KuangkeeResult qryProfList(
			UserSearchLogReq searchReq,
			@RequestParam(defaultValue="1")Integer page, 
			@RequestParam(defaultValue="10")Integer rows,
			HttpServletRequest request) {
		
		String uId = searchReq.getTokenId() ; //校验用户是否登陆
		
		List<Article> professions ;
		try {
//			searchResult = articleSearchService.search(qryStr, page, rows);
			professions = new ArrayList<>() ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(professions);
	}
	
}
