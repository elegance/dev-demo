window.Tzb = window.Tzb || {};

Tzb.NumberExt = {
	/**
	 * 格式化数字，保留几位小数(不受Math.pow数值范围的影响)
	 * @param {Number} num 需要处理的数值
	 * @param {Number} precision 保留的小数位数
	 * @param {String} type : 'floor'、'ceil'、'round'、'cut'分别代表：保留位数向下、向上、四舍五入、截取四种方式取值,不填默认值为'cut'
	 */
	fixed: function(num, precision, type) {
		type = type || 'cut';
		num = +num;

		var nums = (num + '').split('.'),
			isNegative = num < 0, //是否为负数
			intNum = nums[0], //整数位数
			decNum = nums[1] || ''; //decimalNum小数位数;

		//如果是截取的方式
		if (type == 'cut') {
			return precision == 0 ? intNum : (intNum + '.' + Tzb.utils.rpad(decNum, precision));
		}

		// 不保留小数
		if (precision == 0) {
			return eval('Math.' + type + '(' + num + ')') + '';
		}

		// 传入的数字本身没有小数,则直接将右侧填充0后返回
		if (decNum == '') {
			return intNum + '.' + Tzb.utils.rpad('', precision, '0');
		}

		// 保留 n位小数，除了截取方式外，其他方式取值需要根据第n+1位来判断第n位的取值
		// 保留n位，但是不足n+1位数字，则直接按截取的方式来做
		if (precision + 1 > decNum.length) {
			return intNum + '.' + Tzb.utils.rpad(decNum, precision);
		}

		var tmpNumStr = (isNegative ? '-' : '') + (decNum.substr(0, precision + 1) / 10); //正负符号，截取n+1,并且小数点向左移动一位
		var tmpNum = eval('Math.' + type + '(' + tmpNumStr + ')');

		var isCarryBit = tmpNumStr.length - 2 < (tmpNum + '').length;

		if (isCarryBit) { //判断是否有进位，去除小数点和小数长度与计算后的数字比较
			return (intNum * 1 + (isNegative ? -1 : 1)) + '.' + Tzb.utils.rpad('', precision);
		} else {
			return intNum + '.' + Math.abs(tmpNum);
		}
	}
};

Tzb.utils = {
	random: function(lowerValue, upperValue) {
		var choices = upperValue - lowerValue + 1;
		return Math.floor(Math.random() * choices + lowerValue);
	},
	/**
	 * 左补位
	 * @param {Object} a 需要左补位的数字
	 * @param {Number} b 需要的长度
	 * @param {Object} c 补位字符，不传默认为'0'
	 */
	lpad: function(a, b, c) {
		c = c || '0';
		a = a == '' ? c : a;
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
		a = a == '' ? c : a;
		return (a + Array(b).join(c)).slice(0, b);
	},
	parseOptionStr: function(s) {
		if (s) {
			if (s.substring(0, 1) != '{') {
				s = '{' + s + '}';
			}
		}
		return (new Function('return ' + s))();
	}
};
/**
 * 日期扩展处理
 */
Tzb.DateExt = {
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

		return Tzb.StringExt.format((fmt || '{0}-{1}-{2} {3}:{4}:{5}'), y, m, d, h, mi, s)
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
	}
};

Tzb.StringExt = {
	/**
	 * 字符串格式化
	 * @param {String} str
	 * @example Tzb.StringExt.format('{0} , {1}!', 'hello', 'world'); ==> hello world!
	 */
	format: function(str) {
		var args = arguments;
		return str.replace(/\{(\d+)\}/g, function($, $1) {
			return args[++$1];
		});
	}
};
