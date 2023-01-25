<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="hidden" name="ws_url" value="<spring:eval expression="@config['ws.url']" />">
<div class="page-inner dashboard">
	<div class="row mt-2">
		<div class="slide col-xl-9">
			<div role="sls">
				<%@include file="/WEB-INF/views/main/slide/sls.jsp" %>
			</div>
			<div role="loanReturn">
				<%@include file="/WEB-INF/views/main/slide/loanReturn.jsp" %>
			</div>
			<div role="return">
				<%@include file="/WEB-INF/views/main/slide/return.jsp" %>
			</div>
			<div role="resvLoan">
				<%@include file="/WEB-INF/views/main/slide/resvLoan.jsp" %>
			</div>
			<div role="gate">
				<%@include file="/WEB-INF/views/main/slide/gate.jsp" %>
			</div>
			<div role="antiLost">
				<%@include file="/WEB-INF/views/main/slide/antiLost.jsp" %>
			</div>
		</div>
		<%-- 세번째 column --%>
		<div class="col-xl-3">
			<%-- 실시간 이벤트 모니터링 --%>
			<div class="row monitoring">
				<div class="col-xl-12">
					<div class="card">
						<div class="card-header">
							<div class="card-title"><h4>실시간 이벤트 모니터링</h4></div>
							<div class="card-title-action">
								<span role="refresh" data-target="monitoring"><i class="fas fa-sync-alt"></i></span>
							</div>
						</div>
						<div class="card-body">
							<ol class="activity-feed"></ol>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Custom -->
<link rel="stylesheet" href="/resources/css/main/main.css">
<link rel="stylesheet" href="/resources/css/lib/slick/1.8.1/slick.css">
<link rel="stylesheet" href="/resources/css/lib/slick/1.8.1/slick-theme.css">

<script src="/resources/js/main/main.js"></script>
<script src="/resources/js/lib/sockjs/1.1.2/sockjs.min.js"></script>
<script src="/resources/js/lib/stomp/2.3.3-1/stomp.min.js"></script>
<script src="/resources/js/lib/slick/1.8.1/slick.min.js"></script>

<%@include file="/WEB-INF/views/main/modal/eventMain.jsp" %>
<%@include file="/WEB-INF/views/main/modal/deviceMain.jsp" %>
<%@include file="/WEB-INF/views/main/modal/alertMain.jsp" %>