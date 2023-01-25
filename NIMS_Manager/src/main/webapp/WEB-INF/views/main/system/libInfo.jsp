<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header pb-0">
					<div class="card-title">
						<h3>도서관 관리</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-primary mr-2" role="add">
							<i class="fas fa-plus"></i><span class="icon-title">등록</span>
						</button>
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
				</div>
				<div class="card-body">
					<div id="listTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/views/main/system/modal/library.jsp" %>

<script src="/resources/js/main/system/libInfo.js"></script>