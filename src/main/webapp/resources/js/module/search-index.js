	
	$(function() {	//page init
		brandInitAndChange() ; 		
		addKeyEnterPressBtn() ;
	});
	
	function sub() {
		var searchContent = $(".search-context-input").val();
		if (isEmpty(searchContent)) {
			alert('请先输入搜索内容');
			return false;
		}
		var finallyclass = $('#thisclass').val();
		var brandId = $('#brandId').val();
		var brandName = $('#brandName').val();
		var lng = $('#lng').val();
		var lat = $('#lat').val();
		
		location.href = "./search-result.html?"
				+"searchContent="
				+ searchContent
				+ "&brandId="
				+ brandId
				+ "&brandName="
				+ brandName
				+ "&lng="
				+ lng 
				+ "&lat=" 
				+ lat;
	}
	
	function brandInitAndChange(){
//		var come = $('#thisclass').val();
//		var dorel = $('.d0').attr('rel');
//		var aClass = $('.row .col-xs-3');
//		if (come == dorel) {
//			var first = $('.d0').addClass('active');
//		} else {
//			aClass.each(function() {
//						if ($(this).attr('rel') == come) {
//							$(this).addClass('active').siblings().removeClass('active');
//							$(this).children('span').show().siblings().hide();
//						}
//			});
//		}
//		aClass.each(function() {
//			$(this).click(function() {
//				$(this).addClass('active').siblings().removeClass('active');
//				$(this).children('span').show().siblings().hide();
//				var thisclass = $(this).attr('rel');
//				$('#thisclass').val(thisclass);
//			});
//		});
		
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