<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Info Modal -->
<div class="modal fade" id="infoModal" role="modal" target="command" tabindex="-1"
	aria-labelledby="commandModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="commandModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group form-floating-label" id="commandLib">
								<select class="form-control input-border-bottom item-field"  id="library_key" required="">
									<option value="">&nbsp;</option>
								</select>
								<label for="library_key" class="placeholder">도서관명</label>
							</div>
							<div class="form-group form-floating-label">
								
								<div class="row">
									<div class="col-lg-4">
										<select class="form-control input-border-bottom item-field" data-category="command" id="USE" required="">
											<option value="Y" selected>사용</option>
											<option value="N">미사용</option>
										</select>
										<label for="USE" class="placeholder">사용 여부</label>
									</div>
									<div class="col-lg-8">
										<div class="form-control" role="command">
											<div>
												<input type="checkbox" class="item-field" id="STX" data-category="protocol" data-name="STX" data-value="N">
												<label for="STX">STX</label>
											</div>
											<div>
												<input type="checkbox" class="item-field" id="ETX" data-category="protocol" data-name="ETX" data-value="N">
												<label for="ETX">ETX</label>
											</div>
											<div>
												<input type="checkbox" class="item-field" id="ENTER" data-category="protocol" data-name="ENTER" data-value="N">
												<label for="ENTER">ENTER</label>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn-round" role="action" action="save">
					<span class="icon-title">확인</span>
				</button>
				<button type="button" class="btn btn-danger btn-round" role="action" action="delete">
					<span class="trash">
						<span></span>
						<i></i>
					</span>
					<span class="icon-title">삭제</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/gate/modal/commandModal.js"></script>
