<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 기간별 이력 검색 --%>
<div class="collapse show" id="searchCollapse">
	<div class="card-header search-header">
		<div class="selectgroup selectgroup-secondary selectgroup-pills">
			<label class="selectgroup-item">
				<input type="radio" name="type" value="day" class="selectgroup-input" checked>
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fas fa-star"></i><span class="icon-title">월별</span>
				</span>
			</label>
			<label class="selectgroup-item">
				<input type="radio" name="type" value="month" class="selectgroup-input">
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fas fa-moon"></i><span class="icon-title">연별</span>
				</span>
			</label>
			<label class="selectgroup-item">
				<input type="radio" name="type" value="all" class="selectgroup-input">
				<span class="selectgroup-button selectgroup-button-icon">
					<i class="fas fa-clock"></i><span class="icon-title">기간</span>
				</span>
			</label>
		</div>
		
		<span role="type">
		</span>
		
		<select id="filter-field" class="form-control ml-2">
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
<script src="/resources/js/common/search/searchStatsWithField.js"></script>