package com.kuangkee.search.quatz ;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kuangkee.common.utils.session.SessionUtils;
import com.kuangkee.search.util.WechatUtil;
import com.kuangkee.service.wechat.IWechatService;

/**
 * @see 
 * http://www.cnblogs.com/lic309/p/4089633.html
 */
@Component
@Configurable
@EnableScheduling
public class SpringQuatz{

	@Autowired
	IWechatService wechatService ;
	
	private static final Logger LOG = LoggerFactory.getLogger(SpringQuatz.class) ;
	
//    @Scheduled(fixedRate = 1000 * 30)
    public void reportCurrentTime(){
        System.out.println ("Scheduling Tasks Examples: The time is now " + dateFormat ().format (new Date ()));
    }

    //每10分钟执行一次
//    @Scheduled(cron = "0 */10 *  * * * ")
    public void reportCurrentByCron(){
        System.out.println ("Scheduling Tasks Examples By Cron each 10 mins: The time is now " + dateFormat ().format (new Date ()));
    }

    /**
     * 每小时刷新access_token
     */
    @Scheduled(cron = "0 * */1  * * * ")
    public void refreshByCronByHours() {
    	wechatService.genetateAccessToken() ;
    }
    
    private SimpleDateFormat dateFormat(){
        return new SimpleDateFormat ("HH:mm:ss");
    }
    
}