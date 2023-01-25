<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 기간별 이력 검색 --%>
<div class="collapse show" id="searchCollapse">
	<div class="card-header search-header">
		<!-- 빠른 검색 -->
		<div class="selectgroup selectgroup-secondary selectgroup-pills">
			<label class="selectgroup-item">
				<input type="radio" name="type" value="week" class="selectgroup-input">
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fa fa-star"></i><span class="icon-title">1주일</span>
				</span>
			</label>
			<label class="selectgroup-item">
				<input type="radio" name="type" value="month" class="selectgroup-input">
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fa fa-moon"></i><span class="icon-title">1개월</span>
				</span>
			</label>
		</div>
		<!-- 기간 검색 -->
		<input type="date" name="startDate" class="form-control date-field">
		<span class="search-label">~</span>
		<input type="date" name="endDate" class="form-control date-field">
		
		<!-- <button class="btn btn-secondary btn-round" role="search"> -->
		<button class="btn btn-round" role="search">
			<i class="fa fa-search" role="search"></i><span class="icon-title">검색</span>
		</button>
	</div>
</div>

<script src="/resources/js/common/search/searchHistory.js"></script>