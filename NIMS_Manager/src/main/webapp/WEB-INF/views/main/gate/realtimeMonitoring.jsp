<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="hidden" name="ws_url" value="<spring:eval expression="@config['ws.url']" />">
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card" role="realtime">
				<div class="card-header">
					<div class="card-title">
						<h3>실시간 모니터링</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-round" role="export" target="excel">
							<i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
						</button>
					</div>
				</div>

				<div>
					<div class="nav nav-tabs" id="nav-tab" role="tablist"></div>
					<div class="tab-content" role="content">
						<div class="tab-action">
							<button class="btn btn-round btn-light mr-2" role="OPENGATE" target="gate">
								<i class="fas fa-door-open"></i><span class="icon-title">1회 개방</span>
							</button>
							<button class="btn btn-round btn-light mr-2" role="OPEN" target="gate">
								<i class="fas fa-door-open"></i><span class="icon-title">개방 모드</span>
							</button>
							<button class="btn btn-round btn-light" role="CLOSE" target="gate">
								<i class="fas fa-door-closed"></i><span class="icon-title">복귀 모드</span>
							</button>
						</div>
					</div>
				</div>
				<div class="card-body">
					<div id="listTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/gate/realtimeMonitoring.js"></script>
<script src="/resources/js/lib/sockjs/1.1.2/sockjs.min.js"></script>
<script src="/resources/js/lib/stomp/2.3.3-1/stomp.min.js"></script>