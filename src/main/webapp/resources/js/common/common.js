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
		$('.'+key).val(val) ;
	} else if(ID_TYPE==selectorType){
		$('#'+key).val(val) ;
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
		return $('.'+key).val() ;
	} else if(ID_TYPE==selectorType){
		return $('#'+key).val() ;
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
function getURLParamVal(name) {
	// //但是在使用的过程中，发现其在获取中文参数的时候，获取到的值是乱码的
	//解决办法:将解码方式unscape换为decodeURI
	//原因:浏览器会将url中的中文参数进行encodeURI编码，所以要通过js使用decodeURI进行解码
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return decodeURI(r[2]);
	return null;
}

/**
 * 按照数组标识的key类型和选择器类型，批量将地址栏数据初始化到对应的位置。
 * 批量支持Id选择器或者class选择的[数组]
 */
function renderHiddenParamsByArray(arrSelectorKeys, selectorType) {
	if(isEmpty(arrSelectorKeys)
			||isEmpty(selectorType)) {
		return ;
	}
	var tmpSelectorKey ;
	var addrVal ;
	for(var i=0; i<arrSelectorKeys.length; i++){
		tmpSelectorKey = arrSelectorKeys[i] ;
		if(isEmpty(tmpSelectorKey)){
			continue ;
		}
		addrVal = getURLParamVal(tmpSelectorKey);
		if(CLASS_TYPE==selectorType){
			$('.'+tmpSelectorKey).val(addrVal);
		} else if(ID_TYPE==selectorType){
			$('#'+tmpSelectorKey).val(addrVal);
		}
	}
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
	setHtmlUrlTimestamps() ; //add timestamp for all url
}) ;


	
