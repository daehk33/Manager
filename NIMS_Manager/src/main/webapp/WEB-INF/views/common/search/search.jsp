<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 기간별 이력 검색 --%>
<div class="collapse show" id="searchCollapse">
	<div class="card-header search-header">
		<select id="filter-field" class="form-control">
			<option value="" selected disabled hidden="">검색항목</option>
		</select>
		<div id="filter-container">
			<input id="filter-value" class="form-control" type="text" placeholder="검색 내용">
			<span id="filter-clear" role="clear"><i class="fas fa-times"></i></span>
		</div>
		<!-- <button class="btn btn-secondary btn-round" role="search"> -->
		<button class="btn btn-round" role="search">
			<i class="fa fa-search"></i><span class="icon-title">검색</span>
		</button>
	</div>
</div>

<script src="/resources/js/common/search/search.js"></script>