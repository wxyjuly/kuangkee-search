package com.kuangkee.search.service;

import com.kuangkee.search.pojo.SearchResult;

public interface SearchService {

	SearchResult search(String queryString, int page, int rows) throws Exception;
}
