<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="page-inner">
	<div class="row">
		<div class="col-md-12">
			<div class="card" role="gateComposition">
				<div class="card-header">
					<div class="card-title">
						<h3>출입게이트 구성</h3>
					</div>
					<div class="card-title-action">
						<button class="btn btn-primary btn-round mr-2" role="action" data-target="input" data-action="add">
							<i class="fas fa-plus"></i><span class="icon-title">추가</span>
						</button>
						<button class="btn btn-round btn-danger mr-2" role="action" data-target="input" data-action="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">삭제</span>
						</button>
						<button class="btn btn-round btn-primary mr-2" role="action" data-target="gate" data-action="add">
							<i class="fas fa-plus"></i><span class="icon-title">추가</span>
						</button>
						<button class="btn btn-round btn-danger mr-2" role="action" data-target="gate" data-action="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">삭제</span>
						</button>
						<button class="btn btn-round btn-warning mr-2" role="action" data-target="group" data-action="setting">
							<i class="fas fa-cog"></i><span class="icon-title">명령어 설정</span>
						</button>
						<button class="btn btn-round btn-primary mr-2" role="action" data-target="group" data-action="add">
							<i class="fas fa-plus"></i><span class="icon-title">그룹생성</span>
						</button>
						<button class="btn btn-round btn-danger mr-2" role="action" data-target="group" data-action="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">그룹삭제</span>
						</button>
						<button class="btn btn-round btn-primary mr-2" role="action" data-target="server" data-action="submit">
							<span class="icon-title">적용</span>
						</button>
						<button class="btn btn-round btn-primary mr-2" role="action" data-target="gateButton" data-action="add">
							<i class="fas fa-plus"></i><span class="icon-title">버튼추가</span>
						</button>
						<button class="btn btn-round btn-danger mr-2" role="action" data-target="gateButton" data-action="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">버튼삭제</span>
						</button>
						<button class="btn btn-round btn-primary mr-2" role="action" data-target="fireTab" data-action="submit">
							<span class="icon-title">적용</span>
						</button>
						<button class="btn btn-round btn-warning mr-2" role="action" data-target="bio" data-action="setting">
							<i class="fas fa-cog"></i><span class="icon-title">명령어 설정</span>
						</button>
						<button class="btn btn-round btn-primary mr-2" role="action" data-target="bio" data-action="add">
							<i class="fas fa-plus"></i><span class="icon-title">추가</span>
						</button>
						<button class="btn btn-round btn-danger mr-2" role="action" data-target="bio" data-action="delete">
							<span class="trash">
								<span></span>
								<i></i>
							</span>
							<span class="icon-title">삭제</span>
						</button>
					</div>
				</div>
				<div class="nav nav-tabs" id="nav-tab" role="tablist"></div>
				<%@include file="/WEB-INF/views/common/submenu/menutabGate.jsp" %>
			</div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/main/gate/modal/inputModal.jsp" %>
<%@include file="/WEB-INF/views/main/gate/modal/gateModal.jsp" %>
<%@include file="/WEB-INF/views/main/gate/modal/bioModal.jsp" %>
<%@include file="/WEB-INF/views/main/gate/modal/bioCommandModal.jsp" %>
<%@include file="/WEB-INF/views/main/gate/modal/buttonModal.jsp" %>
<%@include file="/WEB-INF/views/main/gate/modal/commandModal.jsp" %>
<%@include file="/WEB-INF/views/main/gate/modal/groupModal.jsp" %>
<script src="/resources/js/main/gate/gatecomposition.js"></script>