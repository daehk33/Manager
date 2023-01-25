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
						<h3>스케줄 관리</h3>
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
				</div>
				<div class="card-body">
					<div id="listTable"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/main/gate/modal/scheduleModal.jsp" %>

<script src="/resources/js/main/gate/scheduleManager.js"></script>
<script src="/resources/js/lib/sockjs/1.1.2/sockjs.min.js"></script>
<script src="/resources/js/lib/stomp/2.3.3-1/stomp.min.js"></script>