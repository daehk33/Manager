<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title" data-toggle="collapse" aria-expanded="true" class="collapsed"
						data-target="#searchCollapse" aria-controls="searchCollapse">
						<h3>시간별 통계</h3>
						<span class="caret"></span>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
				</div>
				<%@include file="/WEB-INF/views/common/search/searchStats.jsp" %>
				<div class="card-body">
					<div class="tab-content" id="stats-tabContent">
						<div class="tab-pane fade active" id="stats-grid" role="tabpanel" aria-labelledby="stats-grid-tab">
							<div id="statsTable"></div>
						</div>
						<div class="tab-pane fade" id="stats-chart" role="tabpanel" aria-labelledby="stats-chart-tab">
							<div id="statsChart"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/sls/slsReturnTimeStatistics.js"></script>