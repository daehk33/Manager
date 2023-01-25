<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="card-body" >
	<div class="tab-content" id="stats-tabContent">
		<!-- 입력장치 분류 -->
		<div class="tab-pane fade" data-target="inputDevices" role="tabpanel">
			<div id="inputTab">
				<div role="select">
					<select class="form-control"></select>
				</div>
				<div class="gate-container">
					<div class="row">
						<div class="col-lg-6">
							<label for="serial">시리얼 포트</label>
							<div class="contain" role="all" data-target="list"></div>
						</div>
						<div class="col-lg-6">
							<div class="form-group-gate">
								<div class="input-container">
									<div class="input-content">
										<label for="controller">CONTROLLER</label>
										<div class="contain" role="one" data-target="controller"></div>
									</div>
									<div class="input-action">
										<button class="btn btn-round btn-primary" role="action" data-action="controller">
											<span class="icon-title">적용</span>
										</button>
									</div>
								</div>
								<div class="input-container">
									<div class="input-content">
										<label for="barcode">BARCODE</label>
										<div class="contain" role="one" data-target="barcode"></div>
									</div>
									<div class="input-action">
										<button class="btn btn-round btn-primary" role="action" data-action="barcode">
											<span class="icon-title">적용</span>
										</button>
									</div>
								</div>
								<div class="input-container">
									<div class="input-content">
										<label for="reader">RF READER</label>
										<div class="contain" role="one" data-target="reader"></div>
									</div>
									<div class="input-action">
										<button class="btn btn-round btn-primary" role="action" data-action="reader">
											<span class="icon-title">적용</span>
										</button>
									</div>
								</div>
								<div class="input-container">
									<div class="input-content">
										<label for="moblie">MOBLIE</label>
										<div class="contain" role="one" data-target="moblie"></div>
									</div>
									<div class="input-action">
										<button class="btn btn-round btn-primary" role="action" data-action="mobile">
											<span class="icon-title">적용</span>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 게이트 추가 -->
		<div class="tab-pane fade" data-target="gateAdd" role="tabpanel">
			<div id="gateTable"></div>
		</div>

		<!-- 그룹 추가 -->
		<div class="tab-pane fade" data-target="groupAdd" role="tabpanel">
			<div id="groupTable"></div>
		</div>

		<!-- 서버 설정 -->
		<div class="tab-pane fade" data-target="serverSetting" role="tabpanel">
			<div id="serverTab">
				<div role="select">
					<select class="form-control"></select>
				</div>
				<div class="gate-container">
					<div class="row">
						<div class="col-lg-6">
							<div class="contain" role="server">
								<div class="row">
									<div class="col-lg-12">
										<div class="contain-title">
											<h4>LAS 설정</h4>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<select class="form-control" id="servertype" required="">
											</select>
											<label for="servertype" class="placeholder">도서관 관리 서버</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="server_ip" required="">
											<label for="server_ip" class="placeholder">서버 아아피</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="port" required="">
											<label for="port" class="placeholder">포트 번호</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="data_source" required="">
											<label for="data_source" class="placeholder">Data Source</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="user_id" required="">
											<label for="user_id" class="placeholder">이용자 ID</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="password" class="form-control" id="pwd" required="">
											<label for="pwd" class="placeholder">비밀번호</label>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="contain" role="server">
								<div class="row">
									<div class="col-lg-12">
										<div class="contain-title">
											<h4>Smart 설정</h4>
										</div>
									</div>
									<div class="col-lg-12">
										<div class="form-group form-floating-label">
											<select class="form-control" id="use_smart">
												<option value="0">N</option>
												<option value="1">Y</option>
											</select>
											<label for="use_smart" class="placeholder">사용</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="smart_ip" value="">
											<label for="smart_ip" class="placeholder">서버 아아피</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="smart_port" value="">
											<label for="smart_port" class="placeholder">포트 번호</label>
										</div>
									</div>
									<div class="col-lg-12">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="smart_data_source" value="">
											<label for="smart_data_source" class="placeholder">Data Source</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="smart_user_id" value="">
											<label for="smart_user_id" class="placeholder">이용자 ID</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="password" class="form-control" id="smart_pwd" value="">
											<label for="smart_pwd" class="placeholder">비밀번호</label>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="contain" role="server">
								<div class="row">
									<div class="col-lg-12">
										<div class="contain-title">
											<h4>출입관리 서버</h4>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="gate_ip" value="">
											<label for="gate_ip" class="placeholder">서버 아아피</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="gate_port" value="">
											<label for="gate_port" class="placeholder">포트 번호</label>
										</div>
									</div>
									<div class="col-lg-12">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="gate_data_source" value="">
											<label for="gate_data_source" class="placeholder">Data Source</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="gate_user_id" value="">
											<label for="gate_user_id" class="placeholder">이용자 ID</label>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group form-floating-label">
											<input type="password" class="form-control" id="gate_pwd" value="">
											<label for="gate_pwd" class="placeholder">비밀번호</label>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="contain" role="server">
								<div class="row">
									<div class="col-lg-12">
										<div class="contain-title">
											<h4>설정</h4>
										</div>
									</div>
									<div class="col-lg-12">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="manage_code" value="">
											<label for="manage_code" class="placeholder">도서관명</label>
										</div>
									</div>
									<div class="col-lg-12">
										<div class="form-group form-floating-label">
											<input type="text" class="form-control" id="position" value="">
											<label for="position" class="placeholder">위치</label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 게이트 버튼 -->
		<div class="tab-pane fade" data-target="gateButton" role="tabpanel">
			<div id="buttonTable"></div>
		</div>

		<!-- 소방 설비 -->
		<div class="tab-pane fade" data-target="fireSetting" role="tabpanel">
			<div id="fireTab">
				<div role="select">
					<select class="form-control"></select>
				</div>
				<div class="gate-container">
					<div class="row">
						<div class="col-lg-4">
							<div class="contain" role="fire">
								<div id="fireChild">
									<div class="form-group form-floating-label">
										<select class="form-control" data-category="contact" id="USE">
											<option value="N">N</option>
											<option value="Y">Y</option>
										</select>
										<label for="USE" class="placeholder">사용</label>
									</div>
									<div class="form-group form-floating-label">
										<select class="form-control" data-category="contact" id="CONTROL">
											<option value="N">N</option>
											<option value="Y">Y</option>
										</select>
										<label for="CONTROL" class="placeholder">제어</label>
									</div>
									<div class="form-group form-floating-label">
										<input type="text" class="form-control" data-category="contact" id="PORT_NAME">
										<label for="PORT_NAME" class="placeholder">포트명</label>
									</div>
									<div class="form-group form-floating-label">
										<input type="text" class="form-control" data-category="contact" id="OPEN_MODE">
										<label for="OPEN_MODE" class="placeholder">계속열림</label>
									</div>
									<div class="form-group form-floating-label">
										<input type="text" class="form-control" data-category="contact" id="RETURN_MODE">
										<label for="RETURN_MODE" class="placeholder">정상복귀</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 생체 인증 -->
		<div class="tab-pane fade" data-target="bioSecurity" role="tabpanel">
			<div id="bioTable"></div>
		</div>
	</div>
</div>
