package com.kuangkee.search ;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kuangkee.service.solr.IArticleSearchService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext-*.xml" })
public class TestArticleSearchService {
	Logger log = LoggerFactory.getLogger(TestArticleSearchService.class) ;
	
	@Autowired
	IArticleSearchService searchService ;
	
	/**
	 * testSearch:查询服务. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testSearchSolr() throws Exception {
		String qryStr = "手机" ;
		int page = 1 ;
		int rows = 20 ;
		
//		SearchResult<Article> result = searchService.search(qryStr, page, rows) ;
//		log.info("[查询结果]-->"+result.getResult());
//		System.out.println(result.getResult());
	}

}