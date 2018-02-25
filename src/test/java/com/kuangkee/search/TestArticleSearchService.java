package com.kuangkee.search ;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kuangkee.common.utils.SearchResult;
import com.kuangkee.search.pojo.Article;
import com.kuangkee.service.solr.IArticleSearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext-*.xml" })
public class TestArticleSearchService {
	private static final Logger log = LoggerFactory.getLogger(TestArticleSearchService.class) ;
	
	@Autowired
	IArticleSearchService searchService ;
	
	/**
	 * testSearchSolr:查询服务,从Solr中查询数据. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testSearchSolr() throws Exception {
		String qryStr = "测试" ;
		int page = 1 ;
		int rows = 20 ;
		log.info("----------> info");
		SearchResult<Article> result = searchService.searchArticleListFromSolrByPage(qryStr, page, rows) ;
		log.info("[查询结果]-->"+result.getResult());
		System.err.println(result.getResult());
	}
	
	@Test
	public void testSearchArticleListFromDBByPage() throws Exception {
		String errorCode = "E" ;
		int page = 1 ;
		int rows = 20 ;
		SearchResult<Article> result = searchService.searchArticleListFromDBByPage(errorCode, page, rows) ;
		List<Article> articles = result.getResult();
		for (Article article : articles) {
			System.err.println(article.toString());
		}
	}

}