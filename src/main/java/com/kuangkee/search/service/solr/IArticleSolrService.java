package com.kuangkee.search.service.solr;

import com.kuangkee.common.pojo.KuangkeeResult;
/**
 * 处理数据库与solr之间的数据
 * ClassName: IArticleSolrService <br/>
 * date: 2018年1月23日 上午11:01:21 <br/>
 * @author Leon Xi
 * @version v1.0
 */
public interface IArticleSolrService {

	KuangkeeResult importAllItems();
}
