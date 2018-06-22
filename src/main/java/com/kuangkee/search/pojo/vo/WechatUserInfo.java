/**
 * Project Name:kuangkee-search
 * File Name:WechatUserInfo.java
 * Package Name:com.kuangkee.search.pojo.vo
 * Date:2018年6月22日上午9:32:04
 * Copyright (c) 2018, 【Leon Xi】 All Rights Reserved.
 *
*/

package com.kuangkee.search.pojo.vo;
/**
 * ClassName:WechatUserInfo <br/>
 * Date:     2018年6月22日 上午9:32:04 <br/>
 * @author   Leon Xi
 * @version  v1.0
 * @see 	 
 */
public class WechatUserInfo {
	
	private String subscribe ;
	
	private String openId ;
	
	private String nickName ;
	
	private String sex ;
	
	private String language ;
	
	private String city ;
	
	private String province ;
	
	private String country ;
	
	private String headImgUrl ;
	
	private String subscribeTime  ;
	
	private String remark ;
	
	private String groupId ;
	
	private String tagIdList ;
	
	private String subscribeScene ;
	
	private String qrScene ;
	
	private String qrSceneStr ;
	
	
	
	public static void main(String[] args) {
		String jsonUserInfo = "{" + 
				"  \"user_info_list\": [" + 
				"    {" + 
				"      \"subscribe\": 1," + 
				"      \"openid\": \"oVF7E1LZRGZpsJpAQHzsEKzDZXYc\"," + 
				"      \"nickname\": \"LeonXi\"," + 
				"      \"sex\": 1," + 
				"      \"language\": \"zh_CN\"," + 
				"      \"city\": \"\"," + 
				"      \"province\": \"\"," + 
				"      \"country\": \"Finland\"," + 
				"      \"headimgurl\": \"http://thirdwx.qlogo.cn/mmopen/cfOkG9HSSBP2tGibXuoXIHBUGX7WzEECxpHfpx4LPn1QHwgLzPFgBNghQ4sHKicG7OAVTSvs8z3RDWiaNZgiaqE8BCl2seqv9iaAc/132\"," + 
				"      \"subscribe_time\": 1529394719," + 
				"      \"remark\": \"\"," + 
				"      \"groupid\": 0," + 
				"      \"tagid_list\": []," + 
				"      \"subscribe_scene\": \"ADD_SCENE_QR_CODE\"," + 
				"      \"qr_scene\": 0," + 
				"      \"qr_scene_str\": \"\"" + 
				"    }" + 
				"  ]" + 
				"}";
	}
}

