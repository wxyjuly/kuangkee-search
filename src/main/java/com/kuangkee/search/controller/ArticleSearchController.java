package com.kuangkee.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.search.pojo.util.SearchResult;
import com.kuangkee.search.pojo.vo.ArticleVO;
import com.kuangkee.search.service.IArticleSearchService;
import com.taotao.common.utils.ExceptionUtil;

/**
 * 文章查询Controller
 * ClassName: SearchController <br/>
 * date: 2018年1月20日 下午12:23:51 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@Controller
public class ArticleSearchController {

	@Autowired
	private IArticleSearchService articleSearchService;
	
	@RequestMapping(value="/query", method=RequestMethod.GET)
	@ResponseBody
	public KuangkeeResult search(@RequestParam("q")String qryStr, 
			@RequestParam(defaultValue="1")Integer page, 
			@RequestParam(defaultValue="10")Integer rows) {
		//查询条件不能为空
		if (StringUtils.isBlank(qryStr)) {
			return KuangkeeResult.build(400, "查询条件不能为空");
		}
		SearchResult<ArticleVO> searchResult = null;
		try {
			qryStr = new String(qryStr.getBytes("iso8859-1"), "utf-8");
			searchResult = articleSearchService.search(qryStr, page, rows);
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return KuangkeeResult.ok(searchResult);
	}
	
}
