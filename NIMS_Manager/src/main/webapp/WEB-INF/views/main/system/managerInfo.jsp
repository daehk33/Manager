<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header pb-0">
					<div class="card-title">
						<h3>사용자 목록</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-primary mr-2" role="add">
							<i class="fas fa-plus"></i><span class="icon-title">등록</span>
						</button>
						<button class="btn btn-round btn-danger mr-2" role="reset">
							<svg class="svg-icon" viewBox="0 0 12.7 12.7">
								<g stroke="#fff" stroke-linecap="round">
									<path class="lockBody" fill="#fff" fill-rule="evenodd" stroke-width="1.84823437" stroke-linejoin="round" d="M2.926 6.366h7.129v4.218H2.926z"/>
									<path class="lockHead" d="M3.622 5.339c0-3.424 1.69-3.421 2.801-3.424 1.868 0 3.113 0 3.113 5.292" fill="none" stroke-width="1.4117639999999998"/>
								</g>
							</svg>
							<span class="icon-title">초기화</span>
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

<%@include file="/WEB-INF/views/main/system/modal/manager.jsp" %>

<script src="/resources/js/main/system/managerInfo.js"></script>