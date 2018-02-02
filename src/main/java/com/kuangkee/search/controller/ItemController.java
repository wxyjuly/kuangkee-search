package com.kuangkee.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.service.solr.IArticleSolrService;

/**
 * 索引库维护
 * ClassName: ItemController <br/>
 * date: 2018年1月6日 下午10:05:38 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@Controller
@RequestMapping("/manager")
public class ItemController {
	
	@Autowired
	private IArticleSolrService itemService;

	/**
	 * 导入商品数据到索引库
	 */
	@RequestMapping("/importall")
	@ResponseBody
	public KuangkeeResult importAllItems() {
//		KuangkeeResult result = itemService.importAllItems();
//		return result;
		return null ;
	}
}
