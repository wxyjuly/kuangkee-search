var baseProjectPath = "/kuangkee-search" ;

//选择器类型
var CLASS_TYPE = "CLASS_TYPE" ;
var ID_TYPE = "ID_TYPE" ;

/**
 * 根据选择器设置元素
 * @param selectorType
 * @param key
 * @param val
 * @returns
 */
function setAttr(selectorType,key,val){
	if(CLASS_TYPE==selectorType){
		$('.'+key).attr(key,val) ;
	} else if(ID_TYPE==selectorType){
		$('#'+key).attr(key,val) ;
	}
}
/**
 * 根据选择器获取元素 
 * @param selectorType
 * @param key
 * @returns
 */
function getAttr(selectorType,key){
	if(CLASS_TYPE==selectorType){
		$('.'+key).attr(key) ;
	} else if(ID_TYPE==selectorType){
		$('#'+key).attr(key) ;
	}
}

/*------common part -------*/
/**
 * 为每个页面面包屑href添加时间戳
 * @private
 */
function setHtmlUrlTimestamps() {
	$('a[href]').each(function() {
		var href = $(this).attr('href');
		var time = getTime();
		//判断url不为# 或者 javascript:void(0)
		if (href.length > 1 && href.indexOf('javascript') == -1) {
			//若当前url已经加入了时间戳，去掉当前加入的时间戳
			if (href.indexOf('time=') != -1) {
				var idx = href.indexOf('time=');
				href = href.substring(0, idx - 1);
			}
			//若当前url已带有参数采用& 未带有参数采用?
			if (href.indexOf('?') != -1) {
				$(this).attr('href', href + '&time=' + time);
			} else {
				$(this).attr('href', href + '?time=' + time);
			}
		}
	})
}
/**
 * 获取时间戳
 * @returns {number}
 * @private
 */
function getTime() {
	var time = (new Date()).getTime();
	return time;
}
/**
 * 获取地址栏参数
 * @param name
 * @returns {null}
 * @private
 */
function getURLStr(name) {
	// //但是在使用的过程中，发现其在获取中文参数的时候，获取到的值是乱码的
	//解决办法:将解码方式unscape换为decodeURI
	//原因:浏览器会将url中的中文参数进行encodeURI编码，所以要通过js使用decodeURI进行解码
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return decodeURI(r[2]);
	return null;
}

/**
 * 校验JS元素是否为空
 * @param param
 * @returns
 */
function isEmpty(param){
	if(null==param||undefined==param
			||""==param||"null"==param) {
		return true ;
	} else {
		return false ;
	}
}

/**
 * 整个页面添加Enter键响应
 * @returns
 */
function addKeyEnterPressBtn() {
	$("body").keydown(function() {
		if (event.keyCode == "13") {
			sub();
		}
	});
}

/**
 * 获取并显示定位，参数存放在
 * lng ， lat 隐藏域
 * @returns
 */
function getAndSetGeo(){
	//get geolocation-获取地理位置
	var geolocation = new BMap.Geolocation();
	var gc = new BMap.Geocoder();
	geolocation.getCurrentPosition(function(r) { //定位结果对象会传递给r变量  
		// alert(this.getStatus());return false;
		if (this.getStatus() == BMAP_STATUS_SUCCESS) { //通过Geolocation类的getStatus()可以判断是否成功定位。  
			// var pt = r.point;    
			// gc.getLocation(pt, function(rs){    
			// var addComp = rs.addressComponents;    
			// console.log(r);
			$('#lng').val(r.longitude);
			$('#lat').val(r.latitude);
			// $("#thecity").html(addComp.city);
			// });  
		} else {
			//关于状态码    
			//BMAP_STATUS_SUCCESS   检索成功。对应数值“0”。    
			//BMAP_STATUS_CITY_LIST 城市列表。对应数值“1”。    
			//BMAP_STATUS_UNKNOWN_LOCATION  位置结果未知。对应数值“2”。    
			//BMAP_STATUS_UNKNOWN_ROUTE 导航结果未知。对应数值“3”。    
			//BMAP_STATUS_INVALID_KEY   非法密钥。对应数值“4”。    
			//BMAP_STATUS_INVALID_REQUEST   非法请求。对应数值“5”。    
			//BMAP_STATUS_PERMISSION_DENIED 没有权限。对应数值“6”。(自 1.1 新增)    
			//BMAP_STATUS_SERVICE_UNAVAILABLE   服务不可用。对应数值“7”。(自 1.1 新增)    
			//BMAP_STATUS_TIMEOUT   超时。对应数值“8”。(自 1.1 新增)    
			switch (this.getStatus()) {
			case 2:
				alert('位置结果未知 获取位置失败.');
				break;
			case 3:
				alert('导航结果未知 获取位置失败..');
				break;
			case 4:
				alert('非法密钥 获取位置失败.');
				break;
			case 5:
				alert('对不起,非法请求位置  获取位置失败.');
				break;
			case 6:
				alert('对不起,当前 没有权限 获取位置失败.');
				break;
			case 7:
				alert('对不起,服务不可用 获取位置失败.');
				break;
			case 8:
				alert('对不起,请求超时 获取位置失败.');
				break;
			}
		}
	}, {
		enableHighAccuracy : true
	});
}

/**
 * 校验是否登陆
 * @param userToken
 * @returns
 */
function checkIsLogin(userToken) { 
	var flag = false ;
	if(!isEmpty(userToken)){
		//ajax
	}
	return flag ;
}

/*------common part end-------*/

//init common part
$(function() {
	var userToken ;
	checkIsLogin(userToken) ;
	getAndSetGeo() ;	
	setHtmlUrlTimestamps() ; //add timestamp for all url
}) ;


	
