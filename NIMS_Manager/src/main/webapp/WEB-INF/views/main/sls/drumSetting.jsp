<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title" data-toggle="collapse" aria-expanded="true" class="collapsed"
						data-target="#searchCollapse" aria-controls="searchCollapse">
						<h3>서가 설정</h3>
						<span class="caret"></span>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
				</div>
				
				<%@include file="/WEB-INF/views/common/search/searchDrum.jsp" %>
				
				<div class="card-body">
					<div id="settingTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/main/sls/modal/drumSettingModal.jsp" %>

<script src="/resources/js/main/sls/drumSetting.js"></script>
