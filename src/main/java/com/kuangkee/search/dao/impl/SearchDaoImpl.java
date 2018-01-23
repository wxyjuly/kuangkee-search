package com.kuangkee.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kuangkee.search.dao.ArticleSearchDao;
import com.kuangkee.search.pojo.util.SearchResult;
import com.kuangkee.search.pojo.vo.ArticleVO;

/**
 * 文章搜索Dao
 * ClassName: SearchDaoImpl <br/>
 * date: 2018年1月20日 下午12:18:08 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@Repository
public class SearchDaoImpl implements ArticleSearchDao {
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public SearchResult<ArticleVO> search(SolrQuery query) throws Exception {
		//返回值对象
		SearchResult<ArticleVO> result = new SearchResult<>();
		//根据查询条件查询索引库
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//取查询结果总数量
		result.setRecordCount(solrDocumentList.getNumFound());
		//文章列表
		List<ArticleVO> articles = new ArrayList<>();
		//取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		
		//取文章列表
		ArticleVO article ;
		for (SolrDocument solrDocument : solrDocumentList) {
			//创建一文章对象
			article = new ArticleVO() ;
			article.setArticleId((Integer)solrDocument.get("id")) ;
			//取高亮显示的结果
			List<String> list = highlighting.get(solrDocument.get("id")).get("title");
			String title = "";
			if (list != null && list.size()>0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("title");
			}
			article.setTitle(title);
			article.setBrandId((String)solrDocument.get("brand_id"));
			article.setBrandName((String)solrDocument.get("brand_name"));
			article.setErrorCode((String)solrDocument.get("error_code"));
			article.setImgSearchSmall((String)solrDocument.get("img_search_small"));
			article.setImgContentSmall((String)solrDocument.get("img_content_small"));
			article.setImgContentBig((String)solrDocument.get("img_content_big"));
			article.setContent((String)solrDocument.get("content"));
			
			//添加的文章列表
			articles.add(article);
		}
		result.setResult(articles);
		return result;
	}

}
