<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title" data-toggle="collapse" aria-expanded="true" class="collapsed"
						data-target="#searchCollapse" aria-controls="searchCollapse">
						<h3>반납 이력</h3>
						<span class="caret"></span>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
					<div class="card-title-count"><span role="msg" target="count">0</span> 건</div>
				</div>
				
				<%@include file="/WEB-INF/views/common/search/searchStats.jsp" %>
				
				<div class="card-body">
					<div id="listTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/return/returnHistoryList.js"></script>