<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="modal fade" id="bookModal" role="modal" tabindex="-1"
	aria-labelledby="bookModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="bookModalLabel"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-title-desc">
				<div><i class="fas fa-exclamation-circle"></i><span class="icon-title modal-ext-desc"></span></div> 
			</div>
			<div class="modal-body">
				<div class="row ta-l">
					<div class="col-lg-12">
						<form id="bookForm">
							<input type="hidden" class="form-control param" id="rec_key" name="rec_key" value="${rec_key}" autocomplete="off" aria-describedby="invalidFB_rec_key">
							<div class="form-group form-floating-label add-group">
								<input type="text" class="form-control param input-border-bottom item-field" id="book_no" name="book_no" autocomplete="off" aria-describedby="invalidFB_book_no" required >
								<label for="book_no" class="placeholder"><span class="necessary-field">*</span> 등록번호</label>
								<div id="invalidFB_book_no" class="invalid-feedback">
									이미 사용중이거나, 사용할 수 없는 형식입니다. 다시 입력해주세요.(숫자 7자리)
								</div>
							</div>
							<div class="form-group form-floating-label add-group">
								<input type="text" class="form-control param input-border-bottom item-field" id="title" name="title" autocomplete="off" aria-describedby="invalidFB_title" required>
								<label for="title" class="placeholder"><span class="necessary-field">*</span>서명</label>
								<div id="invalidFB_title" class="invalid-feedback">
								</div>
							</div>
							<div class="form-group form-floating-label add-group">
								<input type="text" class="form-control param input-border-bottom item-field" id="author" name="author" autocomplete="off" aria-describedby="invalidFB_author" required>
								<label for="author" class="placeholder">저자</label>
								<div id="invalidFB_author" class="invalid-feedback">
								</div>
							</div>
							<div class="form-group form-floating-label add-group">
								<input type="text" class="form-control param input-border-bottom item-field" id="publisher" name="publisher" autocomplete="off" aria-describedby="invalidFB_publisher" required>
								<label for="publisher" class="placeholder">출판사</label>
								<div id="invalidFB_publisher" class="invalid-feedback">
								</div>
							</div>
							<div class="form-group form-floating-label add-group">
								<input type="text" class="form-control param input-border-bottom item-field" id="publish_year" name="publish_year" autocomplete="off" aria-describedby="invalidFB_publish_year" required>
								<label for="publish_year" class="placeholder">출판 연도</label>
								<div id="invalidFB_publish_year" class="invalid-feedback">
									올바른 형식이 아닙니다. 다시 입력해주세요. (숫자 4자리)
								</div>
							</div>
							<div class="form-group form-floating-label add-group">
								<input type="text" class="form-control param input-border-bottom item-field" id="call_no" name="call_no" autocomplete="off" aria-describedby="invalidFB_call_no" required>
								<label for="call_no" class="placeholder">청구기호</label>
								<div id="invalidFB_call_no" class="invalid-feedback">
								</div>
							</div>
							<div class="form-group form-floating-label add-group">
								<input type="text" class="form-control param input-border-bottom item-field" id="location" name="location" autocomplete="off" aria-describedby="invalidFB_location" required>
								<label for="location" class="placeholder">위치</label>
								<div id="invalidFB_location" class="invalid-feedback">
									올바른 형식이 아닙니다. 다시 입력해주세요. (최대 100자)
								</div>
							</div>
						</form>
						<form action="/api/book/excel" id="fileupload" method="post" enctype="multipart/form-data">
							<div class="row">
								<div class="col-md-3">
									<div class="form-group bmd-form-group add-group">
										<label for="start_row" class="bmd-label-floating">시작 위치</label>
										<input type="number" class="form-control param" id="start_row" name="start_row" min="1">
									</div>
								</div>
								<div class="col-md-9">
									<div class="form-group bmd-form-group excel-group">
										<label for="background_image">엑셀 파일 업로드</label>
										<input type="file" class="form-control" name="file" id="file" accept=".xls, .xlsx" />
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-round btn-indigo btn-pulse" role="action" data-action="add">
					<i class="fas fa-plus"></i><span class="icon-title">등록</span>
				</button>
				<button class="btn btn-round btn-indigo btn-pulse" role="action" data-action="add_excel">
					<i class="fas fa-plus"></i><span class="icon-title">등록</span>
				</button>
				<button type="button" class="btn btn-indigo btn-round btn-update" role="action" data-action="update">
					<div class="pen-container">
						<span class="pen"><span></span></span>
						<i></i>
					</div>
					<span class="icon-title">수정</span>
				</button>
				<button class="btn btn-round btn-danger btn-trash" role="action" data-action="delete">
					<span class="trash">
						<span></span><i></i>
					</span>
					<span class="icon-title">삭제</span>
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/system/modal/bookModal.js"></script>

