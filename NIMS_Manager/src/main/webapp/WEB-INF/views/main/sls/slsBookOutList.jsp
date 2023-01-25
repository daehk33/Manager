<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>도서 배출</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-primary mr-2" role="action" data-action="control">
							<span class="btn-label">
								<i class="fas fa-cog"></i><span class="icon-title">배출 작업 제어</span>
							</span> 
						</button>
						<button class="btn btn-round btn-danger mr-2" role="action" data-action="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">배출 취소</span>
						</button>
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
					<div class="card-title-count"><span role="msg" target="count">0</span> 권</div>
				</div>
				<div class="card-header search-header">
					<select id="filter-field" class="form-control"></select>
				</div>
				<div class="card-body">
					<div id="listTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/sls/slsBookOutList.js"></script>

<%@include file="/WEB-INF/views/main/sls/modal/slsBookOutSelect.jsp" %>