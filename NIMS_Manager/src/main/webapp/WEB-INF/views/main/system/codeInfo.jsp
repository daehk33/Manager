<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-4 full-height">
			<div class="card" role="card" target="codeGroup">
				<div class="card-header">
					<div class="card-title">
						<h3>코드그룹 관리</h3>
					</div>
				</div>
				<div class="card-body">
					<div id="groupTable"></div>
				</div>
			</div>
		</div>
		<div class="col-md-8 full-height">
			<div class="card" role="card" target="codeInfo">
				<div class="card-header">
					<div class="card-title">
						<h3>코드정보 관리</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-primary" role="add">
							<i class="fas fa-plus"></i><span class="icon-title">등록</span>
						</button>
					</div>
					<div class="card-title-count"><span role="msg" target="count">0</span> 건</div>
				</div>
				<div class="card-body">
					<div id="codeTable">관리할 코드 그룹을 선택해주세요.</div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/views/main/system/modal/code.jsp" %>

<script src="/resources/js/main/system/codeInfo.js"></script>