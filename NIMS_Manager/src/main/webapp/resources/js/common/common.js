// Navigator variables
var userAgent = navigator.userAgent.toLowerCase();
var appName   = navigator.appName.toLowerCase();

// Detect browser
var IE      = !!(appName == 'netscape' && userAgent.search('trident') != -1 || userAgent.indexOf('msie') != -1 || userAgent.indexOf('edge') != -1);
var CHROME  = !!(userAgent.indexOf('chrome') != -1 && userAgent.indexOf('edge') == -1);
var FIREFOX = !!(userAgent.indexOf('firefox') != -1);
var SAFARI  = !!(userAgent.indexOf('chrome') == -1 && userAgent.indexOf('safari') != -1);

// Detect device
var WINDOWS = !!(navigator.platform && 'win16|win32|win64'.indexOf(navigator.platform.toLowerCase()) != -1);
var MAC     = !!(navigator.platform && 'mac|macintel'.indexOf(navigator.platform.toLowerCase()) != -1);
var MOBILE  = !!(navigator.platform && 'win16|win32|win64|mac|macintel'.indexOf(navigator.platform.toLowerCase()) == -1);

(function() {

    var init = function() {
        var pass = true;
        var data;

        var regExp = [];

        regExp.case1 = /(\<[a-zA-Z]+[^>]+[\>])([^<]*)(\<\/[a-zA-Z]+\>)/g; // Select a pair of tags.
        regExp.case2 = /[!@#$%^*()\-_+\\\|\[\]{};:\'",.<>\/].*/g; // Select special characters.

        data = decodeURIComponent(location.search);

        if (new RegExp(regExp.case1).test(data)) {
            pass = false;
            data = data.replace(regExp.case1, '');
        }

        if (new RegExp(regExp.case2).test(data)) {
            pass = false;
            data = data.replace(regExp.case2, '');
        }

        if (!pass) location.search = data;
    };

    init();

    var common = function() {
        if (!Object.assign) {
            Object.defineProperty(Object, 'assign', {
                value: function(target) {
                    if (target == null) {
                        throw new TypeError('Can\'t convert undefined or null to object.');
                    }

                    target = Object(target);

                    for (var i = 1; i < arguments.length; i++) {
                        if (arguments[i]) {
                            for (var j in arguments[i]) {
                                if (Object.prototype.hasOwnProperty.call(arguments[i], j)) {
                                    target[j] = arguments[i][j];
                                }
                            }
                        }
                    }

                    return target;
                },
                writable: true,
                configurable: true
            });
        }

        if (!Array.prototype.find) {
            Object.defineProperty(Array.prototype, 'find', {
                value: function(predicate) {
                    if (this == null) throw new TypeError('"this" is null or undefined.');
                    if (typeof predicate !== 'function') throw new TypeError('predicate must be a function');

                    var i = 0;

                    while (i < Object(this).length >>> 0) {
                        var value = Object(this)[i];

                        if (predicate.call(arguments[1], value, i, Object(this))) {
                            return value;
                        }

                        i++;
                    }

                    return undefined;
                }
            });
        }

        if (!('remove' in Element.prototype)) {
            Element.prototype.remove = function() {
                if (this.parentNode) {
                    this.parentNode.removeChild(this);
                }
            }
        }

        // Capitalize the first letter of a string
        String.prototype.capitalize = function() {
            return this.charAt(0).toUpperCase() + this.slice(1);
        };

        $.ajaxSetup({
            dataType: 'json',
            cache: false,
            timeout: 100000,
            traditional: true
        });

        String.prototype.includes = String.prototype.includes || function(value) {
            return this.indexOf(value) != -1 ? true : false;
        };

        String.prototype.startsWith = String.prototype.startsWith || function(value, position) {
            position = position || 0;
            return this.substr(position, value.length) === value;
        };

        // Less than equal IE 10
        if (!window.location.origin) {
            window.location.origin = window.location.protocol + "//" +
                                     window.location.hostname + (window.location.port ? ':' + window.location.port: '');
        }
    };

    common.prototype = {

        global: {},
        dialog: {},
        params: {},
        client: {},
        config: {},

        addZero: function(i) {
            if (i < 10) i = '0' + Number(i);
            return i;
        },

        checkDate: function(value) {
            if (!(/^\d{4}\-\d{2}\-\d{2}$/.test(value))) return false;

            var year  = Number(value.substring(0, 4));
            var month = Number(value.substring(5, 7));
            var day   = Number(value.substring(8, 10));

            if (year < 1000 || year > 3000) return false;

            var date = new Date(year, month - 1, day);

            if (
                date.getFullYear() !== year ||
                date.getMonth() !== month - 1 ||
                date.getDate() !== day
            ) return false;

            return true;
        },

        checkAreaInput: function(value) {
            value = value.replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, '');
            value = value.replace(/[\{\}\[\]\/;:|*`!^\+<>@\#$%?~_&\\\=\'\"]/gi, '');

            return value;
        },

        checkEmail: function(value) {
            return /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(value);
        },

        checkInput: function(value) {
            value = value.replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, '');
            value = value.replace(/[\{\}\[\]\/.,;:|\)*`!^\+<>@\#$%?~_&\\\=\(\'\"]/gi, '');

            return value;
        },

        checkPassword: function(value, words) {
            if (Array.isArray(words)) {
                for (var i in words) if (value.match(words[i])) return false;
            }

            return (
                /^.*(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*()\-_=+\\\|\[\]{};:\'",.<>\/?]).*$/.test(value) && // 영문, 숫자, 특수 문자 조합
                /^.*(?=^.{9,20}$).*$/.test(value) && // 9자리 이상 20자리 이하
                !(/(\w)\1\1/.test(value)) // 동일 문자 3회 연속이 아닌 조합
            );
        },

        /**
        * Converts Handlebars syntax to HTML.
        *
        * @param {id} - element
        * @param {data} - object
        */
        compileHTML: function(id, data) {
            if (typeof window['Handlebars'] == 'object') {
                var script = document.getElementById(id).innerHTML;
                var html   = Handlebars.compile(script);

                return html(data);
            } else {
                throw new Error('Library could not be found. Additional library load is required. [ Handlebars ]');
            }
        },

        getUrlParam: function(name) {
            var value = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
            return value == null ? null : (value[1] || 0);
        },
        
        /**
         * @param {json} - object
         * @param {title} - String
         * @param {showLabel} - Boolean
         */
        jsonToCsv: function (json, title, showLabel) {
            var arrData = typeof json != 'object' ? JSON.parse(json) : json;
            
            var CSV = '';    
            CSV += title + '\r\n\n';

            if (showLabel) {
                let row = "";
                for (let index in arrData[0]) {
                    row += index + ',';
                }
                row = row.slice(0, -1);
                CSV += row + '\r\n';
            }
            
            for (let i = 0; i < arrData.length; i++) {
                let row = "";

                for (let index in arrData[i]) {
                    row += '"' + arrData[i][index] + '",';
                }
                
                row.slice(0, row.length - 1);
                CSV += row + '\r\n';
            }

            if (CSV == '') {        
                alert("Invalid data");
                return;
            }   
            
            let fileName = '';
            fileName += title.replace(/ /g,"_");   

            const uri = 'data:text/csv;charset=ANSI,' + escape(CSV);
            
            const link = document.createElement("a");    
            link.href = URL.createObjectURL(new Blob(["\ufeff"+CSV], { type: "application/octet-stream" }));
            link.style = "visibility:hidden";
            link.download = fileName + ".csv"
            ;
            //this part will append the anchor tag and remove it after automatic click
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        },
        
        /**
         * @param table - JSGrid table
         * @param data - object
         * @param except - number array
         * @param title - string
         */
        // $('[role="result"] [role="table"]').data('JSGrid').fields를 하면 except 할 컬럼 확인 가능.
        gridToExcel: function(table, data, except, title) {
            if (typeof window.jsGrid == 'object') {
                try {
                    if (typeof data == 'undefined') data = table.data('JSGrid').data;
                    if (data.length == 0) throw new Error('Data to convert to Excel could not be found.');

                    let fields = table.data('JSGrid').fields;
                    except.sort();
                    if(except != null){
                    	while(except.length != 0){
                    		element = except.pop();
                    		fields.splice(element, 1);
                    	}
                    }

                    let html = '<table style="font-size: 1em; border-collapse: collapse">';
                    html += '<tr>';

                    var temp = [];
                    for (var i in fields) {
                        if (fields[i].title) temp.push(fields[i]);
                    }
                    
                    fields = temp;
                    for (var i in fields) {
                        html += '<th style="height: 16.5pt; border: thin solid black">' + fields[i].title + '</th>';
                    }
                    
                    html += '</tr>';

                    for (let i in data) {
                        html += '<tr>';
                        
                        for (let k in fields) {
                            var keys = Object.keys(data[i]);

                            var isExecuting = true;

                            for (let m in keys) {
                                if (fields[k].name == keys[m]) {
                                    html += '<td style="height: 16.5pt; text-align: center; border: thin solid black; mso-number-format: \'\\@\' ">' +
                                            (typeof data[i][keys[m]] == 'undefined' ? '' : data[i][keys[m]]) + '</td>';
                                    isExecuting = false;
                                }
                            }
                            if (isExecuting) {
                                html += '<td style="height: 16.5pt; text-align: center; border: thin solid black; mso-number-format: \'\\@\' "></td>';
                            }
                        }
                        html += '</tr>';
                    }
                    html += '</table>';

                    // BOM(Byte Order Mark, 바이트 순서 표식) 적용: '\uFEFF'

                    var date = new Date(Date.now() - (new Date().getTimezoneOffset() * 1000 * 60)).toISOString();
                    
                    // 2020.08.26 khj 파일 이름 변경
                    // var fileName = date.replace(/-/g, '').replace('T', '').replace(/:/g, '').substring(0, 14);
                    
                    let fileName = '';
                    if(title != '' && title != null) fileName += title.replace(/ /g,"_");
                    else fileName = 'report';
                    
                    if (IE) {
                        if (typeof Blob != 'undefined') {
                            var blob = new Blob(['\uFEFF' + html], { type: 'text/html' });
                            navigator.msSaveOrOpenBlob(blob, fileName + '.xlsx');
                        }
                    } else {
                        var link = document.createElement('a');

                        document.body.appendChild(link);
                        link.setAttribute('type', 'hidden');

                        link.download = fileName;
                        link.href     = 'data:application/vnd.ms-excel,' + '\uFEFF' + encodeURIComponent(html);
                        link.click();
                        link.remove();
                    }
                } catch (error) {
                    throw new Error(error.message);
                }
            } else {
                throw new Error('Library could not be found. Additional library load is required. [ jsGrid ]');
            }
        },

        /**
         * Load the JS script file.
         *
         * @param {object} script
         * @param {array}  script
         * @param {string} script
         */
        import: function(script) {
            var load = function(elem, i) {
                var deferred_ = $.Deferred();

                try {
                    var url;

                    if (typeof elem == 'object') {
                        var defaults = [];

                        defaults.path    = '';
                        defaults.name    = null;
                        defaults.version = null;
                        defaults.minify  = false;

                        var item = $.extend(true, defaults, elem);

                        if (item.name && item.version) {
                            url = item.path + '/js/lib/' +
                                  item.name + '/' +
                                  item.version + '/' +
                                  item.name +
                                  (item.minify ? '.min' : '') + '.js';
                        } else throw new Error('Library name or version could not be found.' + (i ? ' [ ' + (Number(i) + 1) + ' ]' : ''));
                    } else url = elem;

                    $.ajax({
                        type: 'GET',
                        url: url,
                        dataType: 'script',
                        async: false,
                        complete: function(data) {
                            n++;
                            if (!i || (n == script.length)) deferred_.resolve(data);
                        },
                        error: function(event) {
                            throw new Error('Library load is failed. [ ' + item.name + (item.minify ? '.min' : '') + '.js ]');
                        }
                    });
                } catch (error) {
                    deferred_.reject(error);
                }

                return deferred_.promise();
            };

            var deferred = $.Deferred();

            try {
                var n = 0;

                if (typeof script == 'object') {
                    if ($.isEmptyObject(script)) deferred.resolve();
                    else {
                        if ($.isArray(script)) {
                            for (var i in script) {
                                load(script[i], i).then(function() { deferred.resolve(); });
                            }
                        } else load(script).then(function() { deferred.resolve(); });
                    }
                } else load(script).then(function() { deferred.resolve(); });
            } catch (error) {
                deferred.reject(error);
            }

            return deferred.promise();
        },

        isDateFormat: function(value) {
            return (
                /^\d{4}\-\d{2}\-\d{2}$/.test(value) &&
                Date.parse(value)
            );
        },

        /**
         * Set the format of the date.
         *
         * @param {string} value
         *
         * @description
         * Change the format if the date format is different.
         */
        setDateFormat: function(value) {
            if (!value) return '';

            var number = value.replace(/[^0-9]/g, '');
            var result = '';

            if (number.length < 5) {
                return number;
            } else if (number.length < 7) {
                result += number.substr(0, 4);
                result += '-';
                result += number.substr(4, 2);
            } else if (number.length < 9) {
                result += number.substr(0, 4);
                result += '-';
                result += number.substr(4, 2);
                result += '-';
                result += number.substr(6);
            } else if (number.length > 7) {
                result += number.substr(0, 4);
                result += '-';
                result += number.substr(4, 2);
                result += '-';
                result += number.substr(6, 2);
            }

            return result;
        },

        /**
         * Set the begin date and the end date when selecting a date with both calendars loaded.
         *
         * @param {object} begin
         * @param {object} end
         *
         * @description
         * This function applies when using the 'Datepicker' widget.
         */
        setDateLimit: function(begin, end) {
            begin.on('change', function() {
                end.datepicker('option', { minDate: $(this).datepicker('getDate') });
            });

            end.on('change', function() {
                begin.datepicker('option', { maxDate: $(this).datepicker('getDate') });
            });
        },

        /**
         * Reset the default value when using the JS library.
         *
         * @param {string} library
         */
        setDefaults: function(library) {
            if (typeof library == 'string') {
                switch (library.toLowerCase()) {
                    case 'handlebars':
                        if (typeof window['Handlebars'] == 'object' ) {
                            Handlebars.registerHelper('is', function(a, b, options) {
                                return a == b ? options.fn(this) : options.inverse(this);
                            });

                            Handlebars.registerHelper('not', function(a, b, options) {
                                return a == b ? options.inverse(this) : options.fn(this);
                            });
                        }
                        break;

                    case 'dialog':
                        $.extend($.ui.dialog.prototype.options, {
                            modal: true,
                            autoOpen: true,
                            dialogClass: 'ui-dialog-light',
                            closeText: '닫기',
                            show: {
                                effect: 'drop',
                                direction: 'up',
                                duration: 'slow'
                            },
                            hide: {
                                effect: 'drop',
                                direction: 'up',
                                duration: 'slow'
                            },
                            close: function() {
                                if ($('[role="loader"]').length > 0) {
                                    if (typeof window.Loader == 'object') Loader.stop();
                                }

                                $(this).dialog('destroy').empty();
                            },
                            buttons: [
                                {
                                    text: '확인',
                                    class: 'ui-dialog-button',
                                    icon: 'ui-icon-check',
                                    click: function() {
                                        $(this).dialog('close');
                                    }
                                }
                            ]
                        });
                        break;

                    case 'datepicker':
                        if (typeof $.datepicker == 'object') {
                            $.datepicker.regional.ko = {
                                buttonImageOnly: true,
                                changeMonth: true,
                                changeYear: true,
                                closeText: '닫기',
                                currentText: '오늘',
                                dateFormat: 'yy-mm-dd',
                                dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
                                dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
                                monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                                monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                                nextText: '다음 달',
                                prevText: '이전 달',
                                showButtonPanel: false,
                                showMonthAfterYear: true,
                                yearRange: 'c-5:c',
                                yearSuffix: '년',

                                onSelect: function(dateText, inst) {
                                    $(this).trigger('change');

                                    if (IE) { // datepicker in dialog closing issue
                                        if ($(this).parents('[role="dialog"]').length > 0) {
                                            inst.dpDiv.empty();
                                            return false;
                                        }
                                    }
                                },

                                onChangeMonthYear: function(year, month, inst) {
                                    var date = new Date();

                                    if (inst.selectedYear == date.getFullYear() &&
                                        inst.selectedMonth == date.getMonth() &&
                                        inst.selectedDay == date.getDate()) {
                                        $('#' + inst.id).datepicker('setDate', date).trigger('change');
                                        return;
                                    }

                                    if (inst.currentYear != 0 && inst.currentYear != inst.selectedYear) {
                                        var yearRange = inst.settings.yearRange.split(':');

                                        var min = Number(yearRange[0].replace('c', ''));
                                        var max = Number(yearRange[1].replace('c', ''));

                                        if (year > inst.currentYear + max) {
                                            $('#' + inst.id).datepicker(
                                                'setDate',
                                                inst.currentYear + '-' +
                                                (
                                                    inst.selectedMonth - 1 < 0
                                                    ? 12
                                                    : (inst.selectedMonth + 1 < 10 ? '0' : '') + (inst.selectedMonth + 1)
                                                ) + '-01'
                                            );
                                        }
                                    }
                                }
                            };

                            $.datepicker.setDefaults($.datepicker.regional.ko);
                        }
                        break;

                    case 'jsgrid':
                    	
                    	jsGrid.setDefaults({
                            noDataContent: '조회된 목록이 없습니다.',

                            confirmDeleting: false,
                            deleteConfirm: '선택한 항목을 삭제합니다.',

                            pageButtonCount: 10,
                            pagerFormat: '{first} {prev} {pages} {next} {last}',
                            pagePrevText: '<',
                            pageNextText: '>',
                            pageFirstText: '<<',
                            pageLastText: '>>',

                            loadMessage: '목록을 불러오는 중',

                            onError: function(args) {
                                console.error(args);
                            }
                        });
                        
                    	break;
                    	
                    case 'tabulator':
                    	const langs = {
                    		"ko":{
                    			"groups":{
                    				"item": "Device",
                    				"items": "Devices"
                    			},
                    			"pagination":{
                    				"first": "처음",
                    				"first_title":"첫 페이지",
                    				"last": "맨끝",
                    				"last_title": "마지막 페이지",
                    				"prev": "이전",
                    				"prev_title": "이전 페이지",
                    				"next": "다음",
                    				"next_title": "다음 페이지"
                    			}
                    		}
                    	}
                    	return langs;
                    	
                    	break;
                }
            }
        },

        /**
        * Set the format of the phone number.
        *
        * @param {string} value
        */
        setPhoneNumberFormat: function(value) {
            var number = value.replace(/[^0-9]/g, '');
            var result = '';

            if (number.length < 4) {
                return number;
            } else if (number.length < 8) {
                result += number.substr(0, 3);
                result += '-';
                result += number.substr(3);
            } else if (number.length < 11) {
                result += number.substr(0, 3);
                result += '-';
                result += number.substr(3, 3);
                result += '-';
                result += number.substr(6);
            } else {
                result += number.substr(0, 3);
                result += '-';
                result += number.substr(3, 4);
                result += '-';
                result += number.substr(7);
            }

            return result;
        },
        
        /**
         * Get jsTree Structure
         * 
         * @param {list} value - getXXXListByHierachy
         * @author khj
         * @date 2020.09.10
         * 
         */
        convertListToTree: function(list){
        	// rec_key:name 해싱
        	const rec_key = {};
        	list.forEach(e => {
				rec_key[e.rec_key] = e.name;
			})
			
			const result = {
				core: {
					data: []
				}
			};
			
			const stack = result.core.data;
			
			for(element of list){
				const node = {'text': element.name, 'children': [], 'id': element.rec_key };
				let path = element.path;
				
				// root node 일 때
				if(path.length == 1){
					stack.push(node);
				}
				else {
					let root = stack.filter(e => e.text == rec_key[path[0]])[0];
					
					// 마지막은 자기 자신이기 때문에 제외
					path = path.splice(1, path.length - 2);
					
					for (p of path){
						// 자신의 위치를 찾아감.
						root = root.children.filter(e => e.text == rec_key[p])[0];
					}
					if(root === undefined) continue;
					root.children.push(node);
				}
			}
			
			return result
        },
        
        /**
         * 문자열에 특수 문자가 있는지 체크
         * @param {str}
         * @author khj
         * @date 2020.09.21
         */
        checkSpecial: function(str) { 
        	const special_pattern = /[`~!@#$%^&*|\\\'\";:\/?]/gi; 
        	
        	if(special_pattern.test(str) == true) return true; 
        	else return false; 
        },
        
        /**
         * 비밀번호 패턴 체크 (8자 이상, 문자, 숫자, 특수문자 포함여부 체크)
         * @param {str}
         * @author khj
         * @date 2020.09.21
         */
        checkPasswordPattern: function(str) {
        	var pattern1 = /[0-9]/; // 숫자 
        	var pattern2 = /[a-zA-Z]/; // 문자 
        	var pattern3 = /[~!@#$%^&*()_+|<>?:{}]/; // 특수문자 
        	if(!pattern1.test(str) || !pattern2.test(str) || !pattern3.test(str) || str.length < 8) { 
        		return false; 
        	} 
        	else { 
        		return true; 
        	} 
        },
        
        /**
         * 아이디 패턴 체크 (5~20자, 알파벳 소문자, 숫자, '_', '-' 체크)
         * @param {str}
         * @author pky
         * @date 2020.11.02
         */
        checkIdPattern: function(str) {
        	var rtnVal = /^[a-z0-9\_\-]{5,20}$/.test(str) // 5~20자 알파벳 소문자, 숫자, 특수문자(_),(-) 허용
        	return rtnVal;
        },
        
        /**
         * 이름 패턴 체크 (특수문자 체크)
         * @param {str}
         * @author pky
         * @date 2020.11.02
         */
        checkNamePattern: function(str) {
        	var rtnVal = /([`~!@#$%^&*(),.\_\-\+\=\|\\\[\]\{\}\<\>\/\? ])/.test(str);
        	return rtnVal;
        },
        
        /**
         * 부서명 패턴 체크 (문자, 숫자, 일부 특수문자 허용)
         * @param {str}
         * @author pky
         * @date 2020.11.26
         */
        checkDeptPattern: function(str) {
        	var rtnVal = /^([a-zA-Z가-힣0-9\_\-\(\)\&\s]){1,}$/.test(str);
        	return rtnVal;
        },
        
        /**
         * 날짜 yyyy-mm-dd 포맷으로 출력
         * @param {type: string [default | month | year] [, day: number] [ ,month: number]}
         */
        getDateToStr: function(type, day, month){
        	var date = new Date();
        	if(day === undefined) day = 0;
        	if(month === undefined) month = 0;
        	
        	date.setDate(date.getDate() + day);
        	date.setMonth(date.getMonth() + month);
        	
        	var year = date.getFullYear();
        	var month = (date.getMonth() + 1).toString().padStart(2, '0');
        	var day = date.getDate().toString().padStart(2, '0');
        	
        	if (type == 'month'){
        		return [year, month].join('-');
        	}
        	else if (type == 'year'){
        		return year;
        	}
        	else return [year, month, day].join('-');
        },
        
        /**
         * URL parameter parsing; object -> string
         * @param obj - {key: value}
         */
        parseParam: function(obj){
        	return Object.entries(obj).map(x => x[0] + '=' + x[1]).join('&');
        },
        
        /**
         * float-label이 적용된 input과 label에 disable 효과 적용
         * @id id: string - 객체를 참조할 id
         * @apply apply: boolean - 적용여부(true or false)
         */
        disableFieldAndLabel: function(id, apply){
        	const obj = $('#'+id);
        	const objLabel = $(`label[for="${id}"]`);
        	
        	if(apply) {
        		objLabel.find('.necessary-field').remove();
        		obj.prop('disabled', true);
        		objLabel.removeClass('placeholder');
        		objLabel.addClass('disabled-placeholder');
        	}
        	else {
        		obj.prop('disabled', false);
        		objLabel.removeClass('disabled-placeholder');
        		objLabel.addClass('placeholder');
        	}
        },
        /**
         * float-label이 적용된 input과 label에 disable 효과 적용
         * @parent parent: Object - 참조할 객체의 부모 객체
         * @id id: string - 객체를 참조할 id
         * @apply apply: boolean - 적용여부(true or false)
         */
        disableFieldAndLabelInParent: function(parent, id, apply){
        	const obj = parent.find('#'+id);
        	const objLabel = parent.find(`label[for="${id}"]`);
        	
        	if(apply) {
        		objLabel.find('.necessary-field').remove();
        		obj.prop('disabled', true);
        		objLabel.removeClass('placeholder');
        		objLabel.addClass('disabled-placeholder');
        	}
        	else {
        		obj.prop('disabled', false);
        		objLabel.removeClass('disabled-placeholder');
        		objLabel.addClass('placeholder');
        	}
        },
        /**
         * label에 * 추가
         */
        markNecessaryField: function(label_id){
        	const label = $(`label[for="${label_id}"]`);
        	label.find('.necessary-field').remove();
        	
        	const container = '<span class="necessary-field">*</span>';
        	label.html(container + label.text());
        },
        getPlainText: function(txt){
        	txt.replace('\n', '');
        	txt.replace('\t', '');
        	txt.replace('\b', '');
        	txt.replace('\v', '');
        	txt.replace('\f', '');
        	txt.replace('\r', '');
        	return txt.trim(); 
        }
	};
    
    window.common = new common();

})();
