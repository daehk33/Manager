<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Info Modal -->
<div class="modal fade" id="infoModal" role="modal" target="input" tabindex="-1"
	aria-labelledby="inputModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="inputModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label">
								<label for="baudrate">Baudrate</label>
								<select class="form-control" id="baudrate" required>
									<option value="9600" selected>9600</option>
									<option value="19200">19200</option>
									<option value="38400">38400</option>
									<option value="57600">57600</option>
									<option value="115200">115200</option>
									<option value="230400">230400</option>
									<option value="460800">460800</option>
									<option value="921600">921600</option>
								</select>
								<label for="databit">Databit</label>
								<select class="form-control" id="databit" required>
									<option value="5">5</option>
									<option value="6">6</option>
									<option value="7">7</option>
									<option value="8" selected>8</option>
								</select>
								<label for="paritybit">Paritybit</label>
								<select class="form-control" id="paritybit" required>
									<option value="None" selected>None</option>
									<option value="Odd">Odd</option>
									<option value="Even">Even</option>
									<option value="Mark">Mark</option>
									<option value="Space">Space</option>
								</select>
								<label for="stopbit">Stopbit</label>
								<select class="form-control" id="stopbit" required>
									<option value="None">None</option>
									<option value="One" selected>One</option>
									<option value="Two">Two</option>
									<option value="OnePointFive">OnePointFive</option>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="save">
					<span class="icon-title">설정</span>
				</button>
				<button type="button" class="btn btn-danger btn-round" data-dismiss="modal" aria-label="Close">
					<span class="icon-title">취소</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/gate/modal/inputModal.js"></script>