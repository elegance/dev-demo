/**
 * 对比 取 最大值方法速度
 * 原生：Math.max
 * 移位：>>
 * 对比符号： a > b ? a : b
 */
var Benchmark = require('benchmark');

// 使方法名长度相同方, 方便直观的对比数值
function lpad(str, len, cha) {
	cha = cha || ' ';
	return (Array(len).join(cha) + str).slice(-len);
}

var nativeMax = function (a, b) {
    return Math.max(a, b);
};

var bitShiftMax = function (a, b) {
    return b & ((a-b) >> 31) | a & (~(a-b) >> 31); 
};

var  operComMax = function (a, b) {
    return a > b ? a : b;
};

var suite = new Benchmark.Suite;

suite
	.add(lpad('nativeMax positive Num', 30), function() {
		nativeMax(25, 37);
	})
	.add(lpad('nativeMax negative Num', 30), function() {
		nativeMax(-25, -37);
	})
	.add(lpad('bitShiftMax positive Num', 30), function() {
		bitShiftMax(25, 37);
	})
	.add(lpad('bitShiftMax negative Num', 30), function() {
		bitShiftMax(-25, -37);
	})
	.add(lpad('operComMax positive Num', 30), function() {
		operComMax(25, 37);
	})
	.add(lpad('operComMax negative Num', 30), function() {
		operComMax(-25, -37);
	})
	.on('cycle', function(event) {
		console.log(String(event.target));
	})
	.on('complete', function() {
		console.log('Faster is ' + this.filter('fastest').map('name'));
		console.log('Slowest is ' + this.filter('slowest').map('name'));
	})
	.run({'async': true});