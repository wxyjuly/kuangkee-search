/**
 * Project Name:kuangkee-search
 * File Name:AccountSession.java
 * Package Name:com.kuangkee.search.util
 * Date:2018年2月25日下午3:04:58
 * Copyright (c) 2018, 【Leon Xi】 All Rights Reserved.
 *
*/

package com.kuangkee.search.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.kuangkee.common.utils.check.MatchUtil;
import com.kuangkee.common.utils.constant.Constants;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.pojo.Account;
import com.kuangkee.service.IAccountService;
import com.kuangkee.service.impl.AccountServiceImpl;

/**
 * ClassName:AccountSession:获取session值 <br/>
 * Date: 2018年2月25日 下午3:04:58 <br/>
 * @author Leon Xi
 * @version v1.0
 * @see
 */
public class AccountSession {
	
	@Autowired
	private IAccountService accountService ;
	
	/**
	 * 从session中获取用户
	 * @param session
	 * @return account
	 */
	public Account getAccount(HttpServletRequest request,Long uId) {
		Object sesAccount = SessionUtils.getSessionValue(request, Constants.SysConstant.ACOUNT) ;
		Account account ;
		if(MatchUtil.isEmpty(sesAccount)) { //session无值，从数据库获取，并放入session
			account = accountService.getAccountByUId(uId) ;
			
			SessionUtils.setSessionValue(request, Constants.SysConstant.ACOUNT, account);
			
		} else { //session有值，直接转换
			account = (Account) sesAccount ;
		}
		return account ;
	}

	public static void setAccount(HttpServletRequest request, Account user) {
		request.getSession().setAttribute(Constants.SysConstant.ACOUNT, user);
	}

	public static void removeAccount(HttpServletRequest request) {
		HttpSession session = request.getSession() ;
		session.removeAttribute(Constants.SysConstant.ACOUNT);
		session.invalidate();
	}
}
