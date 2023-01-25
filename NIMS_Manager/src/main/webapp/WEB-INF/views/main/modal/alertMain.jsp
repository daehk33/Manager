<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 긴급알림 Modal -->
<div class="modal fade" id="alertModal" role="modal" target="alert" tabindex="-1"
	aria-labelledby="alertModalLabel" aria-hidden="true" data-changed="false">
	<div class="modal-dialog modal-medium modal-dialog-centered" style="max-width: 600px!important;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="alertModalLabel">장애 알림 목록</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		        	<span aria-hidden="true">&times;</span>
		        </button>
			</div>
			<div class="modal-body" style="min-height: 50px; max-height: 400px; overflow: auto">
			</div>
			<div class="modal-footer" style="justify-content: center!important; color: #8e51ff;">
				<h6>장애 이력은 <b>'장비 관리 > 장애 이력'</b> 메뉴에서 다시 보실수 있습니다.</h6>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/modal/alertMain.js"></script>

