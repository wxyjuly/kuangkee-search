package com.kuangkee.search.pojo.util ;

import java.util.List;

@SuppressWarnings("hiding")
public class SearchResult<T> {

	//商品列表
	private List<T> result;
	//总记录数
	private long recordCount;
	//总页数
	private long pageCount;
	//当前页
	private long curPage;
	
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> Result) {
		this.result = Result;
	}
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public long getCurPage() {
		return curPage;
	}
	public void setCurPage(long curPage) {
		this.curPage = curPage;
	}
}
