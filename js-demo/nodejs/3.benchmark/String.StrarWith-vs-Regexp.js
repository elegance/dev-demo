var Benchmark = require('benchmark');

var proStartWith = function (str, part) {
	return str.startsWith(part);// ES6
};

var regStartWith = function (str, part) {
	return new RegExp('^' + part).test(str);
};

var suite = new Benchmark.Suite;

var match = ['600361', '6'];
var unMatch = ['000361', '6'];

suite
	.add('proStartWith match method', function() {
		proStartWith(match[0], match[1]);
	})
//	.add('proStartWith unMatch method', function() {
//		proStartWith(unMatch[0], unMatch[1]);
//	})
	.add('regStartWith match method', function() {
		regStartWith(match[0], match[1]);
	})
//	.add('regStartWith unMatch method', function() {
//		regStartWith(unMatch[0], unMatch[1]);
//	})
	.on('cycle', function(event) {
		console.log(String(event.target));
	})
	.on('complete', function() {
		console.log('Faster is ' + this.filter('fastest').pluck('name'));
		console.log('Slowest is ' + this.filter('slowest').pluck('name'));
	})
	.run({'async': true});
