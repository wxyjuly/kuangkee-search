package com.kuangkee.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.common.utils.constant.Constants.KuangKeeResultConst;
import com.kuangkee.common.utils.exception.ExceptionUtil;
import com.kuangkee.search.TestArticleSearchService;
import com.kuangkee.search.pojo.util.SearchResult;
import com.kuangkee.search.pojo.vo.Article;
import com.kuangkee.search.service.solr.IArticleSearchService;

/**
 * 文章查询Controller
 * ClassName: SearchController <br/>
 * date: 2018年1月20日 下午12:23:51 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@Controller
public class ArticleSearchController {

	private static final Logger log = LoggerFactory.getLogger(ArticleSearchController.class) ;
	
	@Autowired
	private IArticleSearchService articleSearchService;
	
	/**
	 * 
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
	@RequestMapping(value="/query", method=RequestMethod.GET)
	@ResponseBody
	public KuangkeeResult search(@RequestParam("q")String qryStr, 
			@RequestParam(defaultValue="1")Integer page, 
			@RequestParam(defaultValue="10")Integer rows) {
		
		
		log.error("--------> info") ;
		//查询条件不能为空
		if (StringUtils.isBlank(qryStr)) {
			return KuangkeeResult.build(KuangKeeResultConst.PARAM_ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR);
		}
		
		SearchResult<Article> searchResult = null;
		try {
			qryStr = new String(qryStr.getBytes("iso8859-1"), "utf-8");
			searchResult = articleSearchService.search(qryStr, page, rows);
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(searchResult);
	}
	
}
