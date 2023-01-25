<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="header">
	<div class="header-title">
		<img alt="" src="/resources/img/favicon/favicon.png" class="logo" height="22px">
		도서 검색
		<img alt="" src="/resources/img/favicon/favicon.png" class="logo" height="22px">
	</div>
</div>
<div class="header-search">
	<div class="search-container">
		<select class="form-control param" name="searchItem">
			<option value="keyword">전체</option>
			<option value="title">제목</option>
			<option value="author">저자</option>
			<option value="publisher">출판사</option>
			<option value="book_no">등록번호</option>
			<option value="call_no">청구기호</option>
		</select>
		<div class="input-group mr-1">
			<input type="text" class="form-control param" name="searchWord" placeholder="검색어를 입력해주세요">
			<button class="btn" id="action-clear" role="action" data-action="clear">
				<i class="fas fa-eraser" aria-hidden="true"></i>
				<span class="icon-title">지우기</span>
			</button>
			<button class="btn mr-1" role="action" data-action="search">
				<i class="fas fa-search" aria-hidden="true"></i>
				<span class="icon-title">검색</span>
			</button>
		</div>
	</div>
</div>
<div class="content">
	<div class="content-summary mb-2">
		<p class="result">
			<span class="result-keyword hide" id="result-keyword">키워드</span> 검색 결과
			<span class="result-count" id="result-count">0</span> 건
		</p>
	</div>
	<div class="tab-content content-item" id="stats-tab-content">
		<div class="tab-pane fade active show tabulator" id="stats-grid" role="grid" aria-labelledby="stats-grid-tab" tabulator-layout="fitColumns">
			<div id="table"></div>
		</div>
		<div class="tab-pane fade" id="stats-chart" role="tabpanel" aria-labelledby="stats-chart-tab">
			<div id="statsChart"></div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/did/modal/receipt.jsp" %>
<link rel="stylesheet" href="/resources/css/views/did/style.css" />
<script type="text/javascript" src="/resources/js/views/did/book.js"></script>