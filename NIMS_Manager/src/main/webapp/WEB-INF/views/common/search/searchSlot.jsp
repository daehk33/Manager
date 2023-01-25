<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 기간별 이력 검색 --%>
<div class="collapse show" id="searchCollapse">
	<div class="card-header search-header">
		<div id="checkbox-container">
			<label for="empty-slot">적재 상태
				<select id="empty-slot" class="form-control ml-1 mr-2">
					<option value="">전체</option>
					<option value="1">적재</option>
					<option value="2">미적재</option>
				</select>
			</label>
		</div>
		<select id="filter-drumTray" class="form-control mr-2">
			<option value="" selected disabled hidden="">서가 위치</option>
			<option value="">전체</option>
		</select>
		<input id="filter-slot" class="form-control mr-2" type="number" min="1" placeholder="선반 번호"/>
		<button class="btn btn-round" role="search">
			<i class="fa fa-search"></i><span class="icon-title">검색</span>
		</button>
	</div>
</div>

<script src="/resources/js/common/search/searchSlot.js"></script>