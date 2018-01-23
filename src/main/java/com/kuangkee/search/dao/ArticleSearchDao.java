package com.kuangkee.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.kuangkee.search.pojo.util.SearchResult;
import com.kuangkee.search.pojo.vo.ArticleVO;

public interface ArticleSearchDao {

	SearchResult<ArticleVO> search(SolrQuery query) throws Exception;
}
