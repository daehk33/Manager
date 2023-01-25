<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Event Modal -->
<div class="modal fade" id="eventModal" role="modal" target="event" tabindex="-1"
	aria-labelledby="eventModalLabel" aria-hidden="true" data-changed="false">
	<div class="modal-dialog modal-medium modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="eventModalLabel">이벤트 상세 내용</h4>
				<span class="status ml-2" ><span class="badge">critical</span></span>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		        	<span aria-hidden="true">&times;</span>
		        </button>
			</div>
			<div class="modal-body">
				<div class="section mb-3">
					<div class="title">
						<i class="fas fa-clock"></i><span class="icon-title">이벤트 일시</span>
					</div>
					<div class="date"></div>
				</div>
				<div class="section">
					<div class="title">
						<i class="fas fa-comment"></i><span class="icon-title">이벤트 내용</span>
					</div>
					<div class="text"></div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round mr-2" role="action" action="confirm">
					<i class="fas fa-check"></i><span class="icon-title">확인 완료</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/modal/eventMain.js"></script>

