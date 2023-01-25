<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- slsUserLoan Modal -->
<div class="modal fade" id="slsUserLoanModal" role="modal" target="slsUserLoan" tabindex="-1"
	aria-labelledby="slsUserLoanModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h3 class="modal-title" id="slsUserLoanModalLabel">대출 목록</h3>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		        	<span aria-hidden="true">&times;</span>
		        </button>
			</div>
			<div class="modal-body">
				<div id="userLoanTable"></div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/main/sls/modal/slsUserLoan.js"></script>

