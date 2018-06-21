package com.kuangkee.search ;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kuangkee.common.pojo.req.ExpertBrands;
import com.kuangkee.common.pojo.req.ExpertReq;
import com.kuangkee.common.pojo.resp.ExpertResp;
import com.kuangkee.search.pojo.Expert;
import com.kuangkee.service.IExpertService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext-*.xml" })
public class ExpertServiceTest {
	private static final Logger log = LoggerFactory.getLogger(ExpertServiceTest.class) ;
	
	@Autowired
	IExpertService expertService ;
	
	/**
	 * testQryExpertByPage:查询专家列表数据. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testQryExpertByPage() throws Exception {
		int page = 1 ;
		int rows = 20 ;
		log.info("----------> info");
		ExpertReq record = new ExpertReq() ;
		List<Expert> experts = expertService.getExpertListByPageCommon(page, rows, record) ;
		System.err.println(experts) ;
		System.err.println(experts.get(0).toString()) ;
		
		record.setId(2L) ;
		
		List<ExpertBrands> expertBrandsExts = expertService.getExpertBrands(record) ;
		
		System.err.println(expertBrandsExts);
	}
	
	/**
	 * testQryExpertExt:查询专家列表扩展数据. <br/>
	 * @author Leon Xi
	 * @throws Exception 
	 */
	@Test
	public void testQryExpertExt() throws Exception {
		int page = 1 ;
		int rows = 20 ;
		ExpertReq record = new ExpertReq() ;
		List<ExpertResp> experts= expertService.getExpertReq(page, rows) ;
		System.err.println(experts) ;
		System.err.println(experts.get(0).toString()) ;
	}
	

}