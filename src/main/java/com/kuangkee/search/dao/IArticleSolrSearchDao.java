package com.kuangkee.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.kuangkee.search.pojo.util.SearchResult;
import com.kuangkee.search.pojo.vo.Article;

public interface IArticleSolrSearchDao {
	/**
	 * 
	 * search: 文章搜索Dao,从Solr中搜索数据. <br/>
	 *
	 * @author Leon Xi
	 * @param query
	 * @return
	 * @throws Exception
	 */
	SearchResult<Article> search(SolrQuery query) throws Exception;
}
