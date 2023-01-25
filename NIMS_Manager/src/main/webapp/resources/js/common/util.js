/**
 * @Desc 공통 객체 라이브러리
 * 1. ajaxConfig
 * 2. paginationConfig
 * 3. Alert
 * 4. Timer
 * 5. ParamManager
 */

/**
 * @Group AJAX
 * @Desc Ajax 공통 유틸
 * @params url, data, done, [async, fail, always]
 */
const AjaxUtil = {
	request: function(params, loading, obj){
		const that = this;
		const url = params.url;
		const async = params.async && true;
		
		const type = params.type || 'POST';
		const successMessage = params.successMessage || '';
		const failMessage = params.failMessage || '';
		let data = params.data || {};
		
		const library = params.library || '';
		if(library != 'false' && data.library_key == undefined){
			const library_key = Number(sessionStorage.getItem('library_key'));
			data.library_key = library_key;
		}
		
		params.table = params.table || '';
		let table = '';
		if(params.table != ''){
			table = Tabulator.prototype.findTable(`#${params.table}`);
		}
		const modal = params.modal;
		const time = params.time || 0;
		const file = params.file || false;
		const reload = params.reload;
		
		// Callback function
		const done = params.done || function(data, that){
			data = data.result;
			let options = {title: '알림', text: data.DESC || '', textTime: true};
			if(time != 0){
				options.time = time;
			}
			
			if(data.CODE.startsWith('2')){
				if(successMessage != ''){
					options.text = successMessage;
				}
				Alert.success(options, function(){
					if(table){
						table[0].replaceData();
						if(modal != undefined) modal.modal('hide');
					} else {
						if(reload == undefined || reload != false){
							location.reload();
						}
					}
				});
			}
			else {
				if(failMessage != ''){
					options.text = failMessage;
				}
				Alert.warning(options, function(){
					if(table){
						table[0].replaceData();
					} else {
						location.reload();
					}
				});
			}
		};
		
		const fail = params.fail || function(xhr, status, errorThrown) {
			// _csrf Error
			if(xhr.status == '403'){
				// 새로고침
				location.reload();
			}
			// Unhandled Error
			else if(xhr.status == '405'){
				location.reload();
			}
			else {
				Alert.danger({text: '오류가 발생하였습니다. 관리자에게 문의해주시기 바랍니다.'});
				console.error(`코드: ${xhr.status}, 오류명: ${errorThrown}, 상태: ${status}`);
			}
		}
		const always = params.complete || function(data){};
		const beforeSend = params.beforeSend || function(xhr){}
		
		let options = {
			type: type,
			url: url,
			data: data,
			async: async,
			beforeSend: function(xhr){
				beforeSend(xhr);
				
				if(loading == true){
					obj.html(`
						<div class="spinner-grow" role="status">
							<span class="sr-only">Loading...</span>
						</div>
					`);
				}
			}
		};
		
		if(file){
			options = Object.assign({}, {
				enctype: 'multipart/form-data',
				processData: false,    
		        contentType: false,
			}, options);
		}
		
		$.ajax(options)
		.done(function(data){
			that.errorHandling(data);
			
			done(data, this);
		})
		.fail(fail)
		.always(always);
	},
	errorHandling: function(data){
		if(data.result){
			let code = data.result.CODE || '';
			let desc = data.result.DESC || '';
			
			// can't connect with api
			if(['0000', '421'].indexOf(code) > -1){
				Alert.error({text: desc}, () => {
					location.href = '/logout';
				});
			}
			else if(code == '410'){
				Alert.error({text: desc});
			}
		};
	}
}

/**
 * @Desc ajax Error 처리 객체
 */
const ajaxErrorHandling = {
	load: function(data){
		if(data.result){
			let code = data.result.CODE || '';
			let desc = data.result.DESC || '';
			
			// can't connect with api
			if(code == '0000'){
				Alert.error({text: desc}, () => {
					SessionStorage.clear();
					location.href = '/logout';
				}, 10000);
			}
			// unauthorized user
			else if(code == '421'){
				Alert.error({text: desc}, () => {
					SessionStorage.clear();
					location.href = '/logout';
				});
			}
			else if(code == '410'){
				Alert.error({text: desc});
			}
		};
	},
};

const SessionStorage = {
	clear: () => {
		for(let i=0; i<sessionStorage.length; i++){
			let key = sessionStorage.key(i)
			sessionStorage.removeItem(key);
		}
	}
};

const TableUtil = {
	cellMouseEnter: (e, cell, row, except) => {
		except = new Set(except);
		except.add('NO');
		
		// except에 정의된 값이면 종료
		if(except.has(cell.getColumn().getDefinition().title)) return;
		
		const cellElement = cell.getElement();
		const cellData = cellElement.innerText;
		
		// cell의 값이 공백이면 종료
		if(cellData.trim() == '') return;
		
		// cell의 값이 overflow가 아닌경우 종료
		if(cellElement.offsetWidth >= cellElement.scrollWidth) return;
		
		if (!cellElement._tippy){
			tippy(cellElement, { content: cellData, maxWidth: '450px', arrow: true, animation: 'fade' });
		}
		cellElement._tippy.show();
	},
	groupBy: (data, key) => {
	    return data.reduce(function (carry, el) {
	        var group = el[key];

	        if (carry[group] === undefined) {
	            carry[group] = []
	        }

	        carry[group].push(el)
	        return carry
	    }, {})
	},
	setDefaults: () => {
    	const langs = {
    		'ko-kr': {
    			pagination: {
    				first: '처음',
    				first_title: '첫 페이지',
    				last: '맨끝',
    				last_title: '마지막 페이지',
    				prev: '이전',
    				prev_title: '이전 페이지',
    				next: '다음',
    				next_title: '다음 페이지'
    			}
    		},
    	};
	
    	return langs;
	},
	getLoaderLoading: function(msg){
    	msg = msg || '로딩중입니다<br>잠시만 기다려주십시오';
    	
    	return `<div class="text-center display-flex flex-direction-column">
			<div class="dot-spin mt-3" style="align-self: center;"></div>
			<div class="mt-3 pt-3 justify-content-center loading-msg">
				${msg}
			</div>
		</div>`;
    },
    getPlaceholder: function(msg){
    	if(msg == undefined){
    		msg = '조회된 결과가 없습니다';
    	}
    	return `<div>
			<img src="/resources/img/emotion/sad_indigo.png" height="120px"/>
			<div class="mt-3 h4">${msg}</div>
		</div>`;
    },
	showRowDetail: function(row, modalID, params, callback) {
		params = params || {};
		if(callback == undefined){
			callback = () => {};
		}
		
		let rowActive = params.rowActive == undefined ? true: params.rowActive;
		let type = params.type == undefined ? 'modify': params.type;
		
		const table = this.table;
		let data = row.getData();
		
		ParamManager.show(modalID, type, data);
		
		if(rowActive){
			$(row.getElement()).addClass('active');
		}
		$('#'+modalID).on('hidden.bs.modal', function(){
			if(rowActive){
				$('.tabulator-row.tabulator-selectable').removeClass('active');
			}
			callback();
		});
	}
};

/**
 * @Group Tabulator
 * @Desc ajax config 관리 객체
 */
const ajaxConfig = {
    method: "POST", //set request type to Position
    headers: {
        "Content-type": 'application/json; charset=utf-8', //set specific content type
    },
};

/**
 * @Group Tabulator
 * @Desc pagination 설정 관리 객체
 */
const paginationConfig = {
	size: 25,
	selector: [10, 15, 20, 25, 50, 100]
}

/**
 * @Desc 알림창 관리 객체
 * 
 * 1. Alert.load(params, onHide);
 * @param params: object {type, icon, title, text}
 * 	- type: string [ info | success | warning | error | danger ]
 *  - icon: string [ info | success | warning | error ]
 *  - title: string, text: string
 * @param onHide: callback function (to perform after modal is closed)
 *   syntax => (result) => {}
 *   result: object {isConfirmed: boolean, isDenied: boolean, isDismissed: boolean, value: boolean}
 *   
 * 2. Alert.find(type, params, onHide)
 * - find function using type
 * - type: string [ info | success | warning | error | danger ]
 * 
 * 3. Alert.info(params, onHide)
 * 4. Alert.warning(params, onHide)
 * 5. Alert.success(params, onHide)
 * 6. Alert.danger(params, onHide)
 * 7. Alert.error(params, onHide)
 *
 * 8. Alert.confirm(params, onHide, time);
 * - params: object {title, text, confirmText, cancelText}
 *   title: string, text: string, confirmText: string, cancelText: string
 * - time: int - timeout seconds
 */
const Alert = {
	params : {},
	load : function(params, onHide) {
		this.params = params;
		
		Alert.draw(params, onHide);
	},
	find: function(type, params, onHide){
		const alert_type = {
			info: this.info,
			warning: this.warning,
			success: this.success,
			danger: this.danger,
			error: this.danger
		}
		
		alert_type[type](params, onHide);
	},
	info: function(params, onHide){
		params.type = 'info';
		params.icon = 'info';
		
		Alert.draw(params, onHide);
	},
	warning: function(params, onHide){
		params.type = 'warning';
		params.icon = 'warning';
		
		Alert.draw(params, onHide);
	},
	success: function(params, onHide){
		params.type = 'success';
		params.icon = 'success';
		
		Alert.draw(params, onHide);
	},
	danger: function(params, onHide){
		params.title = '에러 발생!';
		params.type = 'danger';
		params.icon = 'error';
		
		Alert.draw(params, onHide);
	},
	error: function(params, onHide){
		params.type = 'danger';
		params.icon = 'error';
		
		Alert.draw(params, onHide);
	},
	draw: function(params, onHide) {
		let icon = '';
		let title = '';
		let text = '';
		let btnClass = '';
		
		if(params.type != undefined) {
			icon = params.icon;
			title = params.title;
			text = params.text;
			btnClass = params.type;
		}
		else {
			icon = 'error';
			title = '알수 없음!';
			btnClass = 'danger';
		}
		if(onHide == undefined) {
			onHide = () => {};
		}
		
		let timerInterval;
		let time = 5;
		Swal.fire({
			icon: icon,
			title: title,
			html: text,
			timer: time*1000,
			customClass: {
				confirmButton: `btn btn-${btnClass}`
			}
		}).then(onHide);
		
		return;
	},
	confirm: function(params, onHide, time, preConfirm){
		let title = '정말로 삭제하시겠습니까?';
		let text = '삭제 시 복구가 불가능합니다.';
		let showCloseButton = true;
		
		let confirmButtonText = '예';
		let cancelButtonText = '아니오';
		let confirmButtonClass = 'btn btn-danger';
		
		// param setting
		if(params != undefined) {
			title = params.title;
			input = params.input;
			text = params.text;
			confirmButtonText = params.confirmText || '예';
			cancelButtonText = params.cancelText || '아니오';
			confirmButtonClass = params.confirmClass || 'btn btn-danger';
		}
		
		// onHide setting
		if(onHide === undefined) onHide = () => {};
		
		// timeout setting
		if(time === undefined) time = false;
		else time *= 1000;
		
		let timerInterval;
		
		// preConfirm setting
		if(preConfirm === undefined) preConfirm = () => {};
		
		Swal.fire({
			title: title,
			html: text,
			input: input,
			icon: 'warning',
			showCloseButton: showCloseButton,
			showCancelButton: true,
			confirmButtonText: confirmButtonText,
			cancelButtonText: cancelButtonText,
			customClass: {
				confirmButton: confirmButtonClass
			},
			preConfirm: preConfirm,
			timer: time
		}).then(onHide);
		
		return;
	},
	select: function(params, onHide, type, val){
		let title = '알림';
		let text = '정말로 삭제하시겠습니까? 삭제 시 복구가 불가능합니다.';
		
		let confirmButtonText = '예';
		let cancelButtonText = '아니오';
		let icon = 'warning';
		let preConfirm = function(value){
			if(value == val){
				Swal.showValidationMessage('변경된 사항이 없습니다!');
			}
		}
		// onHide setting
		if(onHide === undefined) onHide = () => {};
		
		// param setting
		if(params != undefined) {
			title = params.title || '알림';
			text = params.text;
			confirmButtonText = params.confirmText || '예';
			cancelButtonText = params.cancelText || '아니오';
			icon = params.icon || 'warning';
			preConfirm = params.preConfirm || preConfirm;
			onHide = params.onHide || onHide;
		}
		
		Swal.fire({
			title: title,
			html: text,
			icon: icon,
			input: 'select',
			inputOptions: type,
			inputValue: val,
			inputAttributes: {
				autocapitalize: 'off'
			},
			confirmButtonText: confirmButtonText,
			cancelButtonText: cancelButtonText,
			showCancelButton: true,
			customClass: {
				confirmButton: `btn btn-success`,
				cancelButton: `btn`
			},
			preConfirm: preConfirm,
			showLoaderOnConfirm: true,
			allowOutsideClick: () => !Swal.isLoading()
		}).then(onHide);
	},
	printLoading: function(params){
		const msg = params && params.text || '데이터를 출력중입니다.<br>잠시만 기다려주십시오!';
		Swal.fire({
			html: TableUtil.getLoaderLoading(msg),
			width: '20em',
			showCancelButton: false,
			showConfirmButton: false,
			backdrop: 'rgba(0, 0, 0, 0.6)'
		});
	}
};

/**
 * @Group Util
 * @Desc Date Format
 */
const DateUtil = {
	/**
	 * 날짜 yyyy-mm-dd 포맷으로 출력
	 * @param {type: string [default | month | year] [, day: number] [,month: number]}
	 */
	getDateToStr: function(type, day, month){
		var date = new Date();
		if(day === undefined) day = 0;
		if(month === undefined) month = 0;
		
		date.setDate(date.getDate() + day);
		date.setMonth(date.getMonth() + month);
		
		var year = date.getFullYear();
		var month = DateUtil.leftPad(date.getMonth() + 1);
		var day = DateUtil.leftPad(date.getDate());
		
		if (type == 'month'){
			return [year, month].join('-');
		}
		else if (type == 'year'){
			return year;
		}
		else return [year, month, day].join('-');
	},
	leftPad: function(val){
		if(val >= 10) return val;
		return `0${val}`;
	},
	getSlot: function(num, nm_flag){
		if(nm_flag == false){
			return `${num}-${num + 1}`;
		}
		return `${num}-${num + 1}시`;
	},
	getWeekToStr(strDay){
		var week = ['일', '월', '화', '수', '목', '금', '토'];
		var date = new Date(strDay);
		return week[date.getDay()];
	},
	getDays: function(start, end){
		var result = [];
		
		start = new Date(start);
		end = new Date(end);
		
		var dff = end.getTime() - start.getTime();
		
		while(end.getTime() > start.getTime()){
			result.push(start.toISOString().split('T')[0]);
			start.setDate(start.getDate() + 1);
		}
		
		return result;
	},
	getDaysToHash: function(datesArr){
		const result = [];
		
		const hash = {};
		datesArr.forEach(date => {
			const dayInfo = date.split('-');
			const key = dayInfo[0] + '-' + dayInfo[1];
			const day = dayInfo[2];
			
			if(hash[key] == undefined){
				hash[key] = [day];
			}
			else {
				hash[key].push(day);
			}
		});
		
		Object.keys(hash).forEach(key => {
			const temp = key.split('-');
			const days = hash[key].map(val => Number(val)).filter(val => val != '').join(',');
			result.push({
				year: temp[0],
				month: Number(temp[1]),
				days: days
			});
		});
		
		return result;
	}
};

/**
 * @Desc 세션 시간 관리 객체
 * 
 * 1. Timer.setTimer(id, time, onInvalidate)
 * @param id: string - id of element
 * @param time: long
 * @param onInvalidate: string [ login, ... ]
 */
const Timer = {
	x: null,
	time: null,
	remained: 1000,
	setTimer: function(id, time, onInvalidate){
		const that = this;
		const showTime = 20;
		
		if(time == '' || time == 0) {
			const container = $('div[role="expired"]');
			container.hide();
			
			return;
		}
		
		if(onInvalidate === undefined){
			onInvalidate = () => {};
		}
		else if(onInvalidate == "login") {
			onInvalidate = () => {
				Alert.confirm({title: '로그인 유지', text: '로그인을 유지하시겠습니까? <strong>'+showTime+'</strong>초 이내로 응답하지 않으면 자동으로 로그아웃됩니다.', confirmText: '확인'}, 
					(result) => {
						if(result.isConfirmed){
							location.reload();
						}
						else {
							Alert.info({text: '로그인 화면으로 이동합니다.'}, () => {
								SessionStorage.clear();
								location.href = '/logout';
							});
						}
					}, showTime);
			}
		}
		
		that.remained = time;
		that.time = time;
		
		const x = setInterval(function() {
			const timerObj = $("#"+id);
			
			timerObj.text(that.getLongToStr(that.remained));
			that.remained -= 1000;
			if(that.remained < 0){
				clearInterval(x);
				onInvalidate();
			}
		}, 1000);
		
		that.x = x;
	},
	getLongToStr: function(remainedTime){
		let reamined = remainedTime;
		if(reamined < 0){
			return '유효 시간 만료';
		}
		
		const time = [];
		let temp = reamined;
		for (let i = 2; i >= 0; i--){
			let val = Math.floor(temp / 1000 / Math.pow(60, i));
			let result = '' + val;
			if(result.length == 1) {
				result = '0' + result;
			}
			time.push(result);
			temp -= val * 1000 * Math.pow(60, i);
		}
		return time.join(':');
	},
	resetTimer: function() {
		const that = this;
		
		clearInterval(that.x);
		that.setTimer('timer', that.time, 'login');
	}
};

/**
 * @Desc Param 관리 객체 
 * - Modal 생성시 Param으로 넘겨주는 input 태그 생성 
 */
const ParamManager = {
	load: function(action, value){
		const input = $(document.createElement('input'));
		
		let params = {};
		params.type = 'hidden';
		params.name = 'item';
		params['class'] = 'param';
		params.action = action;
		params.value = value === undefined ? null : value;
		
		input.attr(params);
		return input;
	},
	show: function(modalID, action, params){
		const modal = $('#'+modalID);
		modal.append(ParamManager.load(action, JSON.stringify(params)));
		modal.modal('show');
	},
	event: function(modalID, event, callback) {
		$('#'+modalID).on(event+'.bs.modal', function(){
			callback();
		});
	}
}


/**
 * @Group Util
 * @Desc Validation Check
 */
const ValidationUtil = {
	/**
	 * 비밀번호 패턴 체크 (8자 이상, 문자, 숫자, 특수문자 포함여부 체크)
	 * @param {str}
	 */
	checkPasswordPattern: function(str) {
		var pattern1 = /[0-9]/; // 숫자 
		var pattern2 = /[a-zA-Z]/; // 문자 
		var pattern3 = /[.~!@#$%^&*()_+|<>?:{}]/; // 특수문자 
		if(!pattern1.test(str) || !pattern2.test(str) || !pattern3.test(str) || str.length < 8 || str.length > 20) { 
			return false; 
		}
		
		return true; 
	},
    /**
     * 아이디 패턴 체크 (5~20자, 알파벳 소문자, 숫자, '_', '-' 체크)
     * @param {str}
     */
    checkIdPattern: function(str) {
    	var rtnVal = /^[a-z0-9\_\-]{5,20}$/.test(str) // 5~20자 알파벳 소문자, 숫자, 특수문자(_),(-) 허용
    	return rtnVal;
    },
	/**
	 * 이름 패턴 체크 (특수문자 체크)
	 * @param {str}
	 */
    checkSpecialPattern: function(str) {
		var pattern = /([`~!@#$%^&*(),.\_\-\+\=\|\\\[\]\{\}\<\>\/\?])/;
		if(!pattern.test(str) || str.length < 2 || str.length > 20){
			return false;
		}
		return true;
	},
	checkSpecial: function(str) {
		var pattern = /([`~!@#$%^&*(),\_\-\+\=\|\\\[\]\{\}\<\>\/\?])/;
		if(!pattern.test(str) || str.length < 2 || str.length > 20){
			return false;
		}
		return true;
	},
	checkSpecialFileName: function(str){
		var pattern = /([`~!@#$%^&*(),\+\=\|\\\[\]\{\}\<\>\/\?])/;
		if(!pattern.test(str) || str.length < 2 || str.length > 20){
			return false;
		}
		return true;
	},
	
	/**
	 * 한글 체크
	 * @param {str}
	 */
	checkKoerean: function(str) {
		var pattern = /^[가-힣]+$/; // 문자 
		if(!pattern.test(str) || str.length > 10) { 
			return false; 
		}
		
		return true; 
	},
	/**
	 * 숫자 (자릿수) 체크
	 * @param {str}
	 */
	checkBookNo: function(str) {
		var pattern = /[0-9]/;
		if(!pattern.test(str) || str.length != 7){
			return false;
		}
		return true;
	},
    checkYear: function(str) {
		var pattern = /[0-9]/;
		if(!pattern.test(str) ||  str.length != 4){
			return false;
		}
		return true;
	},
	checkNo: function(str) {
		var pattern = /[^0-9]/g;
		if(pattern.test(str)){
			return false;
		}
		return true;
	},
	checkFile: function(file){
		const maxSize = 10 * 1024 * 1024 // 10 MB
       	const acceptedImageTypes = ['image/jpeg', 'image/png'];
		
    	// 특수 문자 처리
    	if(ValidationUtil.checkSpecial(file.name)){
    		Alert.warning({text: '특수문자가 포함된 파일은 업로드할 수 없습니다.'});
    		return false;
    	}
    	// 파일 형식 처리
    	else if($.inArray(file.type, acceptedImageTypes) == -1){
    		Alert.warning({text: 'JPG 형식의 파일만 업로드할 수 있습니다!'});
    		return false;
    	}
    	// 파일 크기 처리
    	else if(file.size > maxSize){
    		Alert.warning({text: '10MB 이상의 파일은 업로드할 수 없습니다!'});
    		return false;
    	}
    	
    	return true
	},
}

/**
 * @Desc 대시보드 차트 관리
 */
const ChartManager = {
	randomizeColor : function(except) {
		const r = Math.random() * 255;
		const g = Math.random() * 255;
		const b = Math.random() * 255;
	    return `rgba(${r}, ${g}, ${b})`;
	},
	getEventTypeColor: function(){
		return {0: '#ff3f51', 1: '#efaf34', 2: '#23cf93'};
	},
	getCriticalColor : function(except) {
		colors = ['#ff3f51', '#ef7f34', '#efaf34', '#e85fb6', '#d03fff', '#7e44ea', '#a478f7', '#5c417f', '#7f4177', '#7f4141']
		
		except.forEach(color => {
			const idx = colors.indexOf(color);
			if(idx > -1) colors.splice(idx, 1);
		});
	    return colors[0];
	},
	getBarChartColor : function() {
		return [
			['#7e44ea', '#7e44ea', 'rgba(255, 255, 255, 0.4)'],
			['#23cf93', '#23cf93', 'rgba(255, 255, 255, 0.4)'],
			['#efaf34', '#efaf34', 'rgba(255, 255, 255, 0.4)'],
			['#e85fb6', '#e85fb6', 'rgba(23, 125, 255, 0.1)'],
			['rgb(154, 59, 207)', 'rgba(154, 59, 207, 0.8)', 'rgba(154, 59, 207, 0.1)'],
		]
	},
	getTotal: function(arr){
		return arr.length != 0 ? arr.map(item => item.count).reduce((x, y) => (x + y)) : 0;
	},
	getPercent: function(count, total){
		if(!total) {
			return '0.0%';
		}
		
		// 소수점 1자리까지 나타냄
		return `${((count/total) * 100).toFixed(1)}%`; 
	}
}

const Animation = {
	load: function(container, classList){
		container.addClass(classList);
		container[0].addEventListener('animationend', () => {
			container.removeClass(classList);
		});
	},
	number: function(target, value) {
		$({val : 0}).animate({val : value}, {
			duration: 1000,
			step: function() {
				let num = (Math.floor(this.val)).toLocaleString();
				target.text(num);
			},
			complete: function() {
				let num = (Math.floor(this.val)).toLocaleString();
				target.text(num);
			}
		});
	},	
}

const Utils = {
	sumMap: function(map){
		return Object.values(map).reduce((x,y) => x+y);
	},
	calcByteSize: function(bytes) {
		var bytes = parseInt(bytes);
		var s = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
		
		var e = Math.floor(Math.log(bytes)/Math.log(1024));
		
		if(e == "-Infinity") return "0 "+s[0];
		else {
			return (bytes/Math.pow(1024, Math.floor(e))).toFixed(2)+" "+s[e];
		}
	},
	compareMap: function(map1, map2){
		for(key of Object.keys(map1)){
			if(map1[key] == map2[key]) continue;
			else return false;
		}
		return true;
	}
}

const Masking = {
	checkNull : function (str){ 
		if(typeof str == "undefined" || str == null || str == ""){
			return true; 
		} else{ 
			return false; 
		} 
	},
	name : function(str){ 
		let originStr = str;
		let maskingStr; 
		let strLength; 
		if(this.checkNull(originStr) == true){ 
			return originStr; 
		} 
		strLength = originStr.length; 
		if(strLength < 4){ 
			maskingStr = originStr.replace(/(?<=.{1})./, "*"); 
		}else { 
			maskingStr = originStr.replace(/(?<=.{2})./, "*"); 
		} 
		return maskingStr;
	}
}

const Excel = {
	download : function(target, table, file_name){
		if(table.getData().length < 1 ){  
			Alert.warning({text: '출력할 데이터가 없습니다!'});
			return;
		}
		if(target == 'excel'){
			setTimeout(function(){
				table.download("xlsx", file_name, {});
			}, 100);
			
			Alert.printLoading();
		}
	}
}

/**
 * @Desc 정책 항목 설정
 */
const ConfigManager = {
	setFieldType: function(type, target) {
		let input = $(`<input type="text" class="form-control input-border-bottom item-field" id="${target}" required="">`);
		
		$(`#${target}`).remove();
		
		if(type) {
			if(type.toLowerCase().includes("select")) {
				input = $(`<select class="form-control input-border-bottom item-field" id="${target}" required=""></select>`);
				
				let options = type.split(/[{}]/)[1];
				for(option of options.split(",")) {
					input.append(`<option value="${option.trim()}">${option.trim()}</option>`);
				}
			} else if(type.toLowerCase().includes("time")) {
				input.inputmask({
					alias: "datetime",
					placeholder: "0600",
					inputFormat: "HHMM",
					insertMode: false,
					showMaskOnHover: false,
					showMaskOnFocus: false,
					hourFormat: 24
				});
			} else if(type.toLowerCase().includes("number")) {
				input = $(`<input type="number" class="form-control input-border-bottom item-field" id="${target}" required="">`);
			}
		}
		else {
			input = $(`<input type="text" class="form-control input-border-bottom item-field" id="${target}" required="">`);
		}
		
		$(`label[for="${target}"]`).parent().prepend(input);
	},
	setFieldName: function(innerText, visible, target) {
		const label = $(`label[for="${target}"]`)[0];
		const container = $(`label[for="${target}"]`).parent().parent();
		
		if(innerText.includes("항목")) {
			label.innerText = innerText;
		}
		else {
			label.innerText += ` - ${innerText}`;
		}
		
		if(visible) {
			container.show();
		}
		else {
			container.hide();
		}
	}
}

const BannerManager = {
	sendChanged: function(device_id, value) {
		let result = null;
		
		AjaxUtil.request({
			url: '/api/loanReturn/updateLoanReturnBannerChanged',
			async: false,
			data: {
				'device_id': device_id,
				'banner_changed': value
			},
			done: function(data) {
				data = data.result;
				
				result = data;
			}
		});
		
		return result;
	}
};

/**
 * @Desc 로딩중 화면 넣기
 */
const LoadingUtil = {
	init: function(container){
		container.append(`
			<div class="loading-container">
				<div class="dot-spin mt-2" style="align-self: center;"></div>
			</div>`);
	},
};

/**
 * @Desc 프린트
 */
const PrintUtil = {
	print: function(params){
		const id = params.id || '';
		const style = params.style || '';
		const title = params.title || '출력 페이지';
		
		const height = params.height || 0;
		const width = params.width || 0; 
		
		const div = document.getElementById(id);
		
		const features = 'width=800,height=700,popup=true'
		const win = window.open('_blank', '', features);
		self.focus(); 
		win.document.open();
		
		win.document.write('<html><head><title>'+title+'</title><style>');
		win.document.write(style);
		win.document.write('</style></head><body>');
		win.document.write(div.innerHTML);
		win.document.write('</body></html>');
		win.document.close();
		
		win.print();
		if(height != 0 && width != 0){
			win.resizeTo(width,height);
		}
	}
}