window.Mee = window.Mee || {};

/**
 *数组扩展操作
 */
Mee.ArrExt = {
	//查找数组中是否有某元素的值
	indexOf: function(arr, obj) {
		if (Array.prototype.indexOf) {
			return arr.indexOf(obj);
		}
		for (var i = 0; i < arr.length; i++) {
			if (obj == arr[i]) {
				return i;
			}
		}
	},

	//判断数组中是否包含某元素
	contains: function(arr, obj) {
		if (Array.prototype.indexOf) {
			return arr.indexOf(obj) >= 0;
		}
		return this.indexOf(arr, obj) >= 0;
	},
	//删除所有数组元素
	clear: function(arr) {
		arr.length = 0;
	},

	//根据开始索引移除指定数组中的元素  index为开始的索引
	removeAt: function(arr, index) {
		arr.splice(index, 1);
	},
	//移除数组某个元素
	remove: function(arr, obj) {
		var i = this.indexOf(arr, obj);
		if (i < 0) {
			return;
		}
		this.removeAt(arr, i);
	},
	//在数组指定索引处插入元素的值 
	insertAt: function(arr, index, obj) {
		arr.splice(index, 0, obj);
	}
};

/**
 * 根据给定的数字区间，产生随机数
 */
Mee.NumUtil = {
	radom: function(lowerValue, upperValue) {
		var choices = upperValue - lowerValue + 1;
		return Math.floor(Math.random() * choices + lowerValue);
	},

	/**
	 * 获取数字包含了几位小数
	 * @param {Number} num
	 */
	getPrecision: function(num) {
		return (!$.trim(num) || isNaN(num) || num.toString().indexOf('.') <= 0) ? 0 : num.toString().split('.')[1].length;
	}
};

Mee.StringUtil = {
	/**
	 * 字符串格式化输出,json对象
	 * var json = [{text: '亚马逊',url: 'z.cn'}]; document.write(jsonFormat('<a href="${url}">${text}</a>'));
	 */
	jsonFormat: function(template, json) {
		return template.replace(/\$\{(.+?)\}/g, function($, $1) {
			return json[$1];
		});
	},

	/**
	 * 字符串格式化输出
	 * document.write(strFormat('<b>{0}</b>,<i>{1}<i>',hi: {0},'poxi','到此一游'));
	 * */
	strFormat: function(template) {
		var args = arguments;
		return template.replace(/\{(\d+)\}/g, function($, $1) {
			return args[++$1];
		});
	},

	/**
	 * 返回字符串是否以特定字符串什么开始
	 * @param {String} str 原字符串
	 * @param {String} pattern 特定字符串
	 * @returns {Boolean}
	 */
	startWith: function(str, pattern) {
		var reg = new RegExp(pattern + '$');
		return reg.test(str);
	},

	/**
	 * 返回字符串是否以特定字符串什么开始
	 * @param {String} str 原字符串
	 * @param {String} pattern 特定字符串
	 * @returns {Boolean}
	 */
	endWith: function(str, pattern) {
		var reg = new RegExp('^' + pattern);
		return reg.test(str);
	}
};

/**
 * 日期扩展处理
 */
Mee.DateExt = {
	/**
	 * 格式化日期
	 * @param {Object} date 日期对象
	 * @param {String} fmt 格式 , 0、1、2、3、4、5分别代表年、月、日、时、分、秒默认为：{0}-{1}-{2} {3}:{4}:{5}
	 */
	format: function(date, fmt) {
		var y = date.getFullYear(),
			m = ('0' + (date.getMonth() + 1)).slice(-2),
			d = ('0'  + date.getDate()).slice(-2),
			h = ('0' + date.getHours()).slice(-2),
			mi = ('0'  + date.getMinutes()).slice(-2),
			s = ('0'  + date.getSeconds()).slice(-2) ;

		return Mee.StringExt.format((fmt || '{0}-{1}-{2} {3}:{4}:{5}'), y, m, d, h, mi, s)
	},

	/**
	 * 获取周的开始结束日期
	 * @param {Object} date
	 */
	getWeek: function(date) {
		var monday = new Date(date.getTime());
		var sunday = new Date(date.getTime());
		monday.setDate(monday.getDate() + 1 - monday.getDay());
		sunday.setDate(sunday.getDate() + 7 - sunday.getDay());
		return {
			monday: monday,
			sunday: sunday
		};
	},

	/**
	 * 判断字符串是否可转为日期
	 * @param {String} str	：要判断的日期字符串,支持含分隔符 '-'、'/' 2010-10-10 或 2010/10/10 或 20121010
	 */
	isDate: function(str) {
		if (str == '' || typeof(str) != 'string') {
			return false;
		}
		//str = str.replace(/[-/]/g,'');
		var reg = /(\d{4})[-/]?([0-9]{1,2})[-/]?(\d{1,2})/;
		if (reg.test(str) && RegExp.$2 <= 12 && RegExp.$3 <= 31) { //通过格式验证
			var isRun = false;
			isRun = RegExp.$1 % 4 == 0;
			switch (true) {
				case RegExp.$2 == 2 && isRun:
					return RegExp.$3 <= 29; //闰年的二月 最多29天
				case RegExp.$2 == 2:
					return RegExp.$3 <= 28; //非闰年的二月 最多28天
				case RegExp.$2 == 4 || RegExp.$2 == 6 || RegExp.$2 == 9 || RegExp.$2 == 11:
					return RegExp.$3 <= 30; //4,6,9,11月 最多30 天
				default:
					return true; //已通过小于31天的验证
			}
		}
		return false;
	}
};

Mee.util = {
	/**
	 * 借鉴easyui的parse，将元素的data-options转换为对象
	 */
	parseOptions: function($ele) {
		var opts = {},
			s = $.trim($ele.attr('data-options'));

		if (s) {
			if (s.substring(0, 1) != '{') {
				s = '{' + s + '}';
			}
		}
		options = (new Function('return ' + s))();
		return options;
	},
	/**
	 * 左补位
	 * @param {Object} a 需要左补位的数字
	 * @param {Number} b 需要的长度
	 * @param {Object} c 补位字符，不传默认为'0'
	 */
	lpad: function(a, b, c) {
		c = c || '0';
		return (Array(b).join(c) + a).slice(-b);
	},

	/**
	 * 右补位
	 * @param {Object} a 需要左补位的数字
	 * @param {Number} b 需要的长度
	 * @param {Object} c 补位字符，不传默认为'0'
	 */
	rpad: function(a, b, c) {
		c = c || '0';
		return (a + Array(b).join(c)).slice(0, b);
	},
	/**
	 * 巧用a标签得到url的详细信息
	 * @param {String} url
	 */
	parseURL: function(url) {
		var a = document.createElement('a');
		a.href = url;
		return {
			source: url,
			protocol: a.protocol.replace(':', ''),
			host: a.hostname,
			port: a.port,
			query: a.search,
			params: (function() {
				var ret = {},
					seg = a.search.replace(/^\?/, '').split('&'),
					len = seg.length,
					i = 0,
					s;
				for (; i < len; i++) {
					if (!seg[i]) {
						continue;
					}
					s = seg[i].split('=');
					ret[s[0]] = s[1];
				}
				return ret;
			})(),
			file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
			hash: a.hash.replace('#', ''),
			path: a.pathname.replace(/^([^\/])/, '/$1'),
			relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
			segments: a.pathname.replace(/^\//, '').split('/')
		};
	},

	/**
	 * window.setInterval、window.setTimeout 的扩展，可传递动态参数
	 */
	setInterval: function(callFun, intervalTime, param) {
		var args = Array.prototype.slice.call(arguments, 2); // arguments 从第二位开始认为是参数
		var _p_callFun = function() {
			callFun.apply(null, args);
		}
		return window.setInterval(_p_callFun, intervalTime); //返回 供clearInterval 使用
	},

	setTimeout: function(callFun, timeout, param) {
		var args = Array.prototype.slice.call(arguments, 2);
		var _p_callFun = function() {
			callFun.apply(null, args);
		}
		return window.setTimeout(_p_callFun, timeout);
	}

};

//cookie操作
Mee.CookieUtil = {
	/**
	 *  新增cookie
	 *  @param {String} name: cookie的名称，key
	 *  @param {Object} value: cookie的值
	 *  @param {number} effTime: 有效时长,单位秒
	 */
	set: function(name, value, effTime) {
		var nowTime = new Date();
		nowTime.setSeconds(nowTime.getSeconds() + effTime);
		document.cookie = name + "=" + escape(value) + ";expires=" + nowTime.toGTMString();
	},

	/**
	 *  取cookie
	 *  @param {String} name: cookie的名称，key
	 */
	get: function(name) {
		if (document.cookie.length > 0) { //有存cookie
			c_start = document.cookie.indexOf(name + "=")
			if (c_start != -1) { //cookie有存在该值
				c_start = c_start + name.length + 1
				c_end = document.cookie.indexOf(";", c_start)
				if (c_end == -1) {
					c_end = document.cookie.length
					return unescape(document.cookie.substring(c_start, c_end))
				}
			}
		}
		return undefined;
	},
	/**
	 * 移除cookie
	 * @param {String} name:cookie名称key
	 * */
	unset: function(name) {
		this.set(name, "", 0);
	}
};
