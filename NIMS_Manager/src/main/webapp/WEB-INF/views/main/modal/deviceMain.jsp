<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Main Device Modal -->
<div class="modal fade" id="deviceMainModal" role="modal" target="deviceMain" tabindex="-1"
	aria-labelledby="deviceMainModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-medium modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="deviceMainModalLabel">장비 상세 정보</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		        	<span aria-hidden="true">&times;</span>
		        </button>
			</div>
			<div class="modal-body row">
				<div class="col-xl-4 device-img">
					<img class="item-field" id="model_id">
				</div>
				<div class="col-xl-8">
					<div class="section mb-3">
						<div class="title">
							<i class="fas fa-tag"></i><span class="icon-title">장비명</span>
						</div>
						<div class="text">
							<span class="item-field" id="device_nm"></span>
						</div>
					</div>
					<div class="section mb-3">
						<div class="title">
							<i class="fas fa-map-marker-alt"></i><span class="icon-title">장비 위치</span>
						</div>
						<div class="text">
							[<span class="item-field" id="library_nm"></span>]&nbsp;
							<span class="item-field" id="device_location"></span> <br/>
							IP 주소: <span class="item-field" id="device_ip"></span>
						</div>
					</div>
					<div class="section mb-3">
						<div class="title">
							<i class="fas fa-hdd"></i><span class="icon-title">장비 상태</span>
						</div>
						<div class="text">
							전원 상태 : <span class="item-field" id="device_status"></span><br/>
							네트워크 상태 : <span class="item-field" id="connect_yn"></span>
						</div>
					</div>
					<div class="section">
						<div class="title">
							<i class="fas fa-mobile-alt"></i><span class="icon-title">모델명</span>
						</div>
						<div class="text">
							<span class="item-field" id="model_nm"></span>&nbsp;
							(<span class="item-field" id="model_type_nm"></span>)
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/modal/deviceMain.js"></script>

