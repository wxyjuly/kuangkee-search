package com.kuangkee.search.service;

import com.kuangkee.search.pojo.util.SearchResult;
import com.kuangkee.search.pojo.vo.ArticleVO;

public interface IArticleSearchService {

	SearchResult<ArticleVO> search(String queryString, int page, int rows) throws Exception;
}
