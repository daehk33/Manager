<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- slsUserLoan Modal -->
<div class="modal fade" id="slsBookOutSelectModal" role="modal" target="slsBookOutSelect" tabindex="-1"
	aria-labelledby="slsBookOutSelectModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h3 class="modal-title" id="slsBookOutSelectModalLabel">배출 작업 제어</h3>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" id="device_key" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="device_key" class="placeholder">장비명</label>
								<br/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-round btn-success mr-2" role="action" action="start">
					<span class="btn-label">
						<i class="fas fa-play"></i><span class="icon-title">시작</span>
					</span>
				</button>
				<button class="btn btn-round btn-danger mr-2" role="action" action="stop">
					<span class="btn-label">
						<i class="fas fa-stop"></i><span class="icon-title">중지</span>
					</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/sls/modal/slsBookOutSelect.js"></script>