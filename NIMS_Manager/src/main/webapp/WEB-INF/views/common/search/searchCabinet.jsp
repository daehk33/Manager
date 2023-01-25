<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 기간별 이력 검색 --%>
<div class="collapse show" id="searchCollapse">
	<div class="card-header search-header">
		<div id="checkbox-container">
			<label for="cabinet-status">캐비닛 상태
				<select id="cabinet-status" class="form-control ml-1 mr-2">
					<option value="">전체</option>
					<option value="0">도서 없음</option>
					<option value="1">도서 있음</option>
					<option value="2">사용 안함</option>
				</select>
			</label>
		</div>
		<select id="filter-direction" class="form-control mr-2">
			<option value="" selected disabled hidden="">서가 위치</option>
			<option value="">전체</option>
			<option value="0">중앙</option>
			<option value="1">좌측</option>
			<option value="2">우측</option>
		</select>
		<input id="filter-cabinet" class="form-control mr-2" type="number" min="1" placeholder="캐비닛 번호"/>
		<input id="filter-insert-no" class="form-control mr-2" type="number" min="1" max="3" placeholder="투입 번호"/>
		<button class="btn btn-round" role="search">
			<i class="fa fa-search"></i><span class="icon-title">검색</span>
		</button>
	</div>
</div>

<script src="/resources/js/common/search/searchCabinet.js"></script>