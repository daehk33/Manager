<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="page-inner">
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    <div class="card-title" data-toggle="collapse" aria-expanded="true" class="collapsed"
                        data-target="#searchCollapse" aria-controls="searchCollapse">
                        <h3 class="card-title">서지 정보 관리</h3>
                    </div>
                    <div class="card-title-action">
                        <button id="add-row" class="btn btn-round btn-primary btn-pulse mr-1" role="action" data-action="add" >
                            <i class="fas fa-plus"></i><span class="icon-title">등록</span>
                        </button>
                        <div class="dropdown display-inline mr-1">
                            <button class="btn btn-round btn-excel" role="action" data-toggle="dropdown" aria-expanded="false">
                                <i class="fas fa-file-excel"></i><span class="icon-title">엑셀</span>
                            </button>
                            
                            <div class="dropdown-menu bg-excel">
                                <a class="dropdown-item btn bg-excel text-white" role="action" data-action="add_excel">업로드</a>
                                <a class="dropdown-item btn bg-excel text-white" role="action" data-action="export" data-target="excel">다운로드</a>
                            </div>
                        </div>
                        <div class="dropdown display-inline mr-1">
                            <button class="btn btn-round btn-danger btn-trash mr-1" role="action" data-toggle="dropdown" aria-expanded="false">
                                <span class="trash">
                                    <span></span><i></i>
                                </span>
                                <span class="icon-title">삭제</span>
                            </button>
                            <div class="dropdown-menu bg-danger">
                                <a class="dropdown-item btn bg-danger text-white" role="action" data-action="delete">부분 삭제</a>
                                <a class="dropdown-item btn bg-danger text-white" role="action" data-action="deleteAll">일괄 삭제</a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="card-body">
                    <div id="bookTable"></div>
                </div>
            </div>
		</div>
	</div>
</div>

<script src="/resources/js/main/system/book.js"></script>

<%@include file="/WEB-INF/views/did/modal/receipt.jsp" %>
<%@include file="/WEB-INF/views/main/system/modal/bookModal.jsp" %>