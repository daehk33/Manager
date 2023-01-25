<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>배너 설정</h3>
						<div class="card-title-action">
							<button class="btn btn-round btn-primary mr-2" role="action" data-action="typeSetting">
								<i class="fas fa-cog"></i><span class="icon-title">유형 설정</span>
							</button>
							<button class="btn btn-round btn-warning mr-2" role="action" data-action="syncSetting">
								<i class="fas fa-sync-alt"></i><span class="icon-title">설정 동기화</span>
							</button>
							<button class="btn btn-round btn-success mr-2" role="action" data-action="loadSetting">
								<span class="btn-label">
									<svg class="svg-icon" viewBox="0 0 12.7 12.7">
										<path class="arrow" d="M6.35 8.202l2.91-2.381M6.35 8.202L3.44 5.821m2.91-4.498v6.614" fill="none" stroke="#fff" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"></path>
										<path class="holder" d="M1.058 10.224v1.852h10.584v-1.852" fill="none" stroke="#fff" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"></path>
									</svg>
								</span>설정 불러오기
							</button>
						</div>
					</div>
				</div>
				<div class="card-header search-header mb-3">
					<select id="filter-field" class="form-control"></select>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-5">
			<div class="card" data-card="info">
				<div class="card-header">
					<div class="card-title">
						<h3>이용안내 관리</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round btn-primary" role="action" action="update">
							<div class="pen-container">
								<span class="pen">
									<span></span>
								</span>
								<i></i>
							</div>
							<span class="icon-title">수정</span>
						</button>
					</div>
				</div>
				<div class="card-body">
					<div id="infoSettingTable"></div>
				</div>
			</div>
		</div>
		<div class="col-md-7">
			<div class="card" data-card="image">
				<div class="card-header">
					<div class="card-title">
						<h3>배너 관리</h3>
					</div>
					<div class="card-title-action">
						<button type="button" class="btn btn-primary btn-round mr-2" role="action" action="add">
							<i class="fas fa-plus"></i><span class="icon-title">등록</span>
						</button>
						<button type="button" class="btn btn-danger btn-round" role="action" action="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">삭제</span>
						</button>
					</div>
				</div>
				<div class="card-body">
					<div id="imageSettingTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<style type="text/css">
	.page-inner > .row:last-child {
	    height: calc(100% - 170px);
	}
	.card .card-header {
		border: none !important;
		padding-bottom: 0 !important;
	}
</style>
<%@include file="/WEB-INF/views/main/loanReturn/modal/infoSettingModal.jsp" %>
<%@include file="/WEB-INF/views/main/loanReturn/modal/imageSettingModal.jsp" %>

<script src="/resources/js/main/loanReturn/bannerSetting.js"></script>
