<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title collapsed">
<!-- 					<div class="card-title" data-toggle="collapse" aria-expanded="true" class="collapsed" -->
<!-- 						data-target="#searchCollapse" aria-controls="searchCollapse"> -->
						<h3>예약 도서 목록</h3>
<!-- 						<span class="caret"></span> -->
					</div>
					<div class="card-title-action">
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
					<div class="card-title-count"><span role="msg" target="count">0</span> 권</div>
				</div>
				<div class="card-body">
					<div id="listTable" class="expand"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/resvLoan/reservedBookList.js"></script>