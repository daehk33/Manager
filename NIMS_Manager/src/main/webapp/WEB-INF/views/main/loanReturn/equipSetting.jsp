<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>장비 설정</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
					<div class="card-title-count"><span role="msg" target="count">0</span> 개</div>
				</div>
				<div class="card-header search-header">
					<select id="filter-field" class="form-control"></select>
				</div>
				<div class="card-body">
					<div id="settingTable" data-search="y"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/main/loanReturn/modal/equipSettingModal.jsp" %>

<script src="/resources/js/main/loanReturn/equipSetting.js"></script>
