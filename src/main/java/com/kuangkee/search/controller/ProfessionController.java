package com.kuangkee.search.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.common.pojo.req.ExpertReq;
import com.kuangkee.common.pojo.req.UserSearchLogReq;
import com.kuangkee.common.pojo.resp.ExpertResp;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.constant.Constants.KuangKeeResultConst;
import com.kuangkee.common.utils.exception.ExceptionUtil;
import com.kuangkee.search.pojo.Expert;
import com.kuangkee.service.IExpertService;

/**
 * 专家Controller
 * ClassName: SearchController <br/>
 * date: 2018年1月20日 下午12:23:51 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@RestController
public class ProfessionController {
	
	private static final Logger log = LoggerFactory.getLogger(ProfessionController.class) ;
	
	@Autowired
	private IExpertService expertService;
	
	/**
	 * qryProfList:分页查询专家. <br/>
	 * @author Leon Xi
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryProfList")
	public KuangkeeResult qryProfList(
			UserSearchLogReq searchReq,
			@RequestParam(defaultValue="1")Integer page, 
			@RequestParam(defaultValue="10")Integer rows,
			HttpServletRequest request) {
		
		String uId = searchReq.getTokenId() ; //校验用户是否登陆
		
		List<ExpertResp> professions ;
		try {
			professions = expertService.getExpertReq(page, rows) ;
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e)) ;
		}
		log.info(professions.toString());
		return KuangkeeResult.ok(professions) ;
	}
	
	/**
	 * updateProf:更新专家数据. <br/>
	 * @author Leon Xi
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateProf")
	public KuangkeeResult updateProfInfo(
			ExpertReq expertReq,
			HttpServletRequest request) {
		//tokenId
		String uId = expertReq.getTokenId() ; //校验用户是否登陆
		
		if(MatchUtil.isEmpty(expertReq.getId())) {
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, KuangKeeResultConst.INPUT_PARAM_ERROR) ;
		}
		Expert record = new Expert() ;
		record.setId(expertReq.getId());
		record.setCousultTimes(10); //非空标志
		
		boolean result = false ;
		try {
			result = expertService.updateExpert(record) ;
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e)) ;
		}
		log.info(Boolean.toString(result));
		
		return KuangkeeResult.ok(result) ;
	}
	
}
