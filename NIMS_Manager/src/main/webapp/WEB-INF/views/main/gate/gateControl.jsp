<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="hidden" name="ws_url" value="<spring:eval expression="@config['ws.url']" />">
<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card">
				<div class="card-header">
					<div class="card-title">
						<h3>출입게이트 제어</h3>
					</div>
				</div>
				<div class="card-body">
					<div class="ctrl-container">
						<div class="ctrl" role="device">
						</div>
						<div class="ctrl" role="control">
							<fieldset class="ctrl-fieldset">
								<legend>개폐 모드</legend>
								<div class="btn btn-light ctrl-field" role="button" data-action="OPEN">
									<i class="fas fa-door-open big-icon mb-1"></i>
									<div role="comment">
										개방 모드
									</div>
								</div>
								<div class="btn btn-light ctrl-field" role="button" data-action="CLOSE">
									<i class="fas fa-door-closed big-icon mb-1"></i>
									<div role="comment">
										복귀 모드
									</div>
								</div>
							</fieldset>
							<fieldset class="ctrl-fieldset">
								<legend>전원</legend>
								<div class="btn btn-light ctrl-field" role="button" data-action="ON">
									<i class="fas fa-power-off big-icon mb-1"></i>
									<div role="comment">
										ON
									</div>
								</div>
								<div class="btn btn-light ctrl-field" role="button" data-action="OFF">
									<i class="fas fa-power-off big-icon mb-1"></i>
									<div role="comment">
										OFF
									</div>
								</div>
							</fieldset>
							<fieldset class="ctrl-fieldset">
								<legend>시스템</legend>
								<div class="btn btn-light ctrl-field" role="button" data-action="RESET">
									<i class="fas fa-sync-alt big-icon mb-1"></i>
									<div role="comment">
										초기화
									</div>
								</div>
							</fieldset>
							<fieldset class="ctrl-fieldset">
								<legend>운용 모드</legend>
								<select class="ctrl-field" role="select" data-action="mode">
									<option value="0" selected disabled>운영 모드 선택</option>
								</select>
								<div class="btn btn-light" role="button" data-action="OK">
									<div role="comment">
										변경
									</div>
								</div>
							</fieldset>
							<fieldset class="ctrl-fieldset">
								<legend>강제 폐쇄</legend>
								<div class="btn btn-light ctrl-field" role="button" data-action="FORCED_CLOSE">
									<i class="fas fa-door-closed big-icon mb-1"></i>
									<div role="comment">
										닫기
									</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/resources/js/main/gate/gateControl.js"></script>
<script src="/resources/js/lib/sockjs/1.1.2/sockjs.min.js"></script>
<script src="/resources/js/lib/stomp/2.3.3-1/stomp.min.js"></script>