var Benchmark = require('benchmark');

var a = '101';

var int1 = function (str) {
	if (str == '100') {
		return '100';
	}
	return '101';
};

var int2 = function (str) {
	return str == '100' ? '100' : '101';
};

var suite = new Benchmark.Suite;

suite
	.add('  if method', function() {
		int1(a);
	})
	.add('  ?  method', function() {
		int2(a);
	})
	.on('cycle', function(event) {
		console.log(String(event.target));
	})
	.on('complete', function() {
		console.log('Faster is ' + this.filter('fastest').pluck('name'));
		console.log('Slowest is ' + this.filter('slowest').pluck('name'));
	})
	.run({'async': true});
