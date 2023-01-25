<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>운영 정책</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
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

<%@include file="/WEB-INF/views/main/return/modal/ruleSettingModal.jsp" %>

<script src="/resources/js/main/return/ruleSetting.js"></script>