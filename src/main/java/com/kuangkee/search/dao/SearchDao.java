package com.kuangkee.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.kuangkee.search.pojo.SearchResult;

public interface SearchDao {

	SearchResult search(SolrQuery query) throws Exception;
}
