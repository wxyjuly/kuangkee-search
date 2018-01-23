package com.kuangkee.search.service.solr;

import com.kuangkee.search.pojo.util.SearchResult;
import com.kuangkee.search.pojo.vo.Article;

public interface IArticleSearchService {

	SearchResult<Article> search(String queryString, int page, int rows) throws Exception;
}
