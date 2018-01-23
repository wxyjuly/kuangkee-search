package com.kuangkee.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuangkee.common.pojo.KuangkeeResult;

/**
 * 文章查询服务
 * ClassName: ArticleController <br/>
 * date: 2018年1月23日 上午10:54:25 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@Controller
@RequestMapping("/article")
public class ArticleSolrController {
	
	@Autowired
//	private IArticleSolrService articleSolrService;

	/**
	 * 导入商品数据到索引库
	 */
	@RequestMapping("/manager/importall")
	@ResponseBody
	public KuangkeeResult importAllItems() {
//		KuangkeeResult result = articleSolrService.importAllItems();
//		return result;
		return null ;
	}
}
