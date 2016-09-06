var Benchmark = require('benchmark');

var int1 = function (str) {
	return parseInt(str, 10);
};

var int2 = function (str) {
	return Number(str);
};

var int3 = function (str) {
	return +str;
};
var int4 = function (str) {
	return 1 * str;
};

var suite = new Benchmark.Suite;
var number = '100';

suite
	.add('      1* method', function() {
		int4(number);
	})
	.add('parseInt method', function() {
		int1(number);
	})
	.add('  Number method', function() {
		int2(number);
	})
	.add('       + method', function() {
		int3(number);
	})
	.on('cycle', function(event) {
		console.log(String(event.target));
	})
	.on('complete', function() {
		console.log('Faster is ' + this.filter('fastest').pluck('name'));
		console.log('Slowest is ' + this.filter('slowest').pluck('name'));
	})
	.run({'async': true});
