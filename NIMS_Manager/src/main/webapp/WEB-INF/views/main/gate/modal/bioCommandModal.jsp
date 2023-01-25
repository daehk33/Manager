<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Info Modal -->
<div class="modal fade" id="infoModal" role="modal" target="bioCommand" tabindex="-1"
	aria-labelledby="bioModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="bioModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid ta-l">
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group form-floating-label" id="bio_lib">
								<select class="form-control input-border-bottom item-field"  id="library_key" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="library_key" class="placeholder">도서관명</label>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group form-floating-label">
								<select class="form-control input-border-bottom item-field" data-category="bio_security" id="USE" required="">
									<option value="Y" selected>사용</option>
									<option value="N">미사용</option>
								</select>
								<label for="USE" class="placeholder">사용 여부</label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="bioCommandSave">
					<span class="icon-title">적용</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/gate/modal/bioCommandModal.js"></script>
