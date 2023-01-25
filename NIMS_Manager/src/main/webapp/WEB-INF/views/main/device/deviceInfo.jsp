<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header pb-0">
					<div class="card-title">
						<h3>장비 정보</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-primary mr-2" role="add">
							<i class="fas fa-plus"></i><span class="icon-title">등록</span>
						</button>
						<button class="btn btn-round btn-danger mr-2" role="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">삭제</span>
						</button>
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
					<div class="card-title-count"><span role="msg" target="count">0</span> 개</div>
				</div>
				<div class="card-body">
					<div id="deviceTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/main/device/modal/infoModal.jsp" %>

<script src="/resources/js/main/device/deviceInfo.js"></script>
