package com.kuangkee.search.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangkee.common.pojo.KuangkeeResult;
import com.kuangkee.common.pojo.req.OrderReq;
import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.constant.Constants;
import com.kuangkee.common.utils.constant.Constants.KuangKeeResultConst;
import com.kuangkee.common.utils.constant.OrderConst;
import com.kuangkee.common.utils.exception.ExceptionUtil;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.pojo.Expert;
import com.kuangkee.search.pojo.Order;
import com.kuangkee.search.pojo.vo.UserInfo;
import com.kuangkee.service.IExpertService;
import com.kuangkee.service.IOrderService;

/**
 * 专家Controller
 * ClassName: SearchController <br/>
 * date: 2018年1月20日 下午12:23:51 <br/>
 * @author Leon Xi
 * @version v1.0
 */
@RestController
public class OrderController {
	
	private static final Logger log = LoggerFactory.getLogger(OrderController.class) ;
	
	@Autowired
	private IExpertService expertService;
	
	@Autowired
	private IOrderService orderService ;
	
	/**
	 * saveOrder:保存订单. <br/>
	 * @author Leon Xi
	 * @param orderReq
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/createOrder")
	public KuangkeeResult saveOrder(
			OrderReq orderReq,
			HttpServletRequest request) {
		
		Long uId = orderReq.getuId() ; 
		Long profId = orderReq.getpId() ;
		UserInfo userInfo = null ;
		
		if(!MatchUtil.isEmpty(uId)) {  //session中获取用户信息
			try {
				Object sesAccount = SessionUtils.getSessionValue(request, 
						Constants.SysConstant.ACOUNT + "_"+  String.valueOf(uId)) ;
				if(!MatchUtil.isEmpty(sesAccount)) { //获取session值
					userInfo = (UserInfo) sesAccount ;  
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Order order = new Order() ;
		Order result = null ;
		//--未登录,或session失效，待处理
		if (MatchUtil.isEmpty(userInfo)) {
			BeanUtils.copyProperties(orderReq, order); //拷贝uId,PId
			
		} else { // 设置对应的值
			BeanUtils.copyProperties(userInfo, order);
			
			order.setuName(userInfo.getNickname());
			order.setPhone(userInfo.getPhone());
			
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
		}
		
		try {
			//查询专家数据
			log.info("getExpertById(profId)->{}",profId);
			Expert expert = expertService.getExpertById(profId) ;
			
			Integer price = expert.getPrice() ;
			if(MatchUtil.isEmpty(price) 
					&& price.equals(Integer.parseInt("0"))) { //0元，直接下单
				order.setOrderStatus(OrderConst.ORDER_PAYED);
				
			} else { //TODO : 大于0元，需要微信支付
				//调用微信支付页面
				order.setOrderStatus(OrderConst.ORDER_UNPAYED);
			}
			//save order
			log.info("saveOrderById(order)->{}",order);
			result = orderService.saveOrderById(order) ;
		} catch (Exception e) {
			e.printStackTrace();
			return KuangkeeResult.build(KuangKeeResultConst.ERROR_CODE, ExceptionUtil.getStackTrace(e)) ;
		}
		return KuangkeeResult.ok(result) ;
	}
	
}
