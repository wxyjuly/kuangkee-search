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

import com.kuangkee.common.utils.constant.Constants;
import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.pojo.vo.UserInfo;

/**
 * ClassName:AccountSession:获取session值 <br/>
 * Date: 2018年2月25日 下午3:04:58 <br/>
 * @author Leon Xi
 * @version v1.0
 * @see
 */
public class AccountSession {
	
	/**
	 * 从session中获取用户
	 * @param session
	 * @return account
	 */
	public Object getAccount(HttpServletRequest request,Long uId) {
		Object sesAccount = SessionUtils.getSessionValue(request, Constants.SysConstant.ACOUNT) ;
		return sesAccount ;
	}

	public static void setAccount(HttpServletRequest request, UserInfo user) {
		request.getSession().setAttribute(Constants.SysConstant.ACOUNT, user);
	}

	public static void removeAccount(HttpServletRequest request) {
		HttpSession session = request.getSession() ;
		session.removeAttribute(Constants.SysConstant.ACOUNT);
		session.invalidate();
	}
}
