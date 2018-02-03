	
	$(function() {	//page init
//		brandInitAndChange() ; 		
		addKeyEnterPressBtn() ;
//		var userToken = 'userToken' ;
//		alert(getAttr(ID_TYPE,userToken));
		initURLParams() ;
		
		redirectSearchIndex() ;
	});
	
	function sub() {
		var searchContent = $(".search-context-input").val();
		if (isEmpty(searchContent)) {
			alert('请先输入搜索内容');
			return false;
		}
		var userToken = $('#userToken').val();
		var brandId = $('#brandId').val();
		var brandName = $('#brandName').val();
		var lng = $('#lng').val();
		var lat = $('#lat').val();
		
		//ajax 查询数据
		var url = baseProjectPath+"/query" ;
		var data = {} ;
		data["searchContent"] = searchContent ;
		data["userToken"] = userToken ;
		data["brandId"] = brandId ;
		data["brandName"] = brandName ;
		data["longitude"] = lng ;
		data["latitude"] = lat ;
		data = JSON.stringify(data) ;
		
		var result = ajaxJsonTemplate(url,data) ;
		if(!isEmpty(result)) {
			alert("---------");
		}
	}
	
	/**
	 * :TODO
	 * 实现Ajax滑动分页效果：
	 * 滑动分页，效果：http://zixuephp.net/article-181.html
	 * @returns
	 */
	function scrollLoading() {
		//记录状态
		var state=true;
		//滚动条滚动的时候
		$(window).scroll(function(){
		        //获取当前加载更多按钮距离顶部的距离
		    var bottomsubmit = $('.caseud').offset().top;
		    //获取当前页面底部距离顶部的高度距离
		    var nowtop = $(document).scrollTop()+$(window).height();
		    //获取当前页数，默认第一页
		    var now = $('.caseud').attr('now');
		    //获取总页数，PHP分页的总页数
		    var num = $('.caseud').attr('num');
		        //当当前页面的高度大于按钮的高度的时候开始触发加载更多数据
		    if(nowtop>bottomsubmit){
		            //如果为真继续执行，这个是用于防止滚动获取过多的数据情况
		        if(state==true){
		                //执行一次获取数据并停止再进来获取数据
		            state=false;
		            //定时器
		            setTimeout(function(){
		                  //当前页数++
		                now++;
		                //记录当前为第二页
		                $('.caseud').attr('now',now);
		                loadingBackDataByAjax() ;
		            },500);
		        }
		    }
		});
	}
	
	/**
	 * 通过Ajax加载数据
	 * :TODO ----
	 * @returns
	 */
	function loadingBackDataByAjax() {
		$.ajax({
            //通过ajax传页数参数获取当前页数的数据
         url:'ajax_more_case.php',
         type:'GET',
         cache:false,
         dataType:"html",
         success:function(data){
                 //把通过php处理的html和数据，写入容器底部
             $('.case').append(data);
             //如果当前页大于等于总页数就提示没有更多数据
             if(now>=num){
                 $('.caseud a').text('没有更多数据');
                 //并把状态设置为假，下次下滑滚动时不再通过ajax获取数据
                 state=false;
             }else{
                    // 否则继续
                 state=true;
             }
         },
         error:function(){
             $('.caseud a').text('加载错误,请刷新页面！');
         }
     });
	}
	
	/**
	 * 重定向到首页
	 * @returns
	 */
	function redirectSearchIndex() {
		$("#redirect-search-index").click(function(){
			alert("-->"+getAttr(ID_TYPE,'userToken')) ;
			window.location.href="./search-index.html?userToken="+getAttr(ID_TYPE,'userToken') ;
		}) ;
	}
	
	/**
	 * 按照数组中的值，批量初始化隐藏域
	 * @returns
	 */
	function initURLParams() {
		var arrSelectorKeys = new Array() ;
		arrSelectorKeys[0] = "userToken" ;
		arrSelectorKeys[1] = "brandId" ;
		arrSelectorKeys[2] = "brandName" ;
		arrSelectorKeys[3] = "lng" ;
		arrSelectorKeys[4] = "lat" ;
		renderHiddenParamsByArray(arrSelectorKeys, ID_TYPE) ;
	}
	
	function brandInitAndChange(){
		//init first
		//change brand cells
		$(".container-brand-outter-div").click(function(){
			var curBrandDiv = $(this);
			curBrandDiv.addClass('active').siblings().removeClass('active');
			curBrandDiv.children('span').show().siblings().hide();
			
			var brandId = curBrandDiv.attr("brand_id");
			var brandName = curBrandDiv.attr("brand_name");
			
			//setter cur brandId,brandName
			$("#brandId").val(brandId) ;
			$("#brandName").val(brandName) ;
		});
		
	}