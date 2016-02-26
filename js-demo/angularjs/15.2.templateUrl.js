// 1. 使用templateUrl
var m1 = angular.module('myApp', []);

m1.directive('hello', function() {
	return {
		restrict: 'AEMC',
		templateUrl: '15.2.template.html',
		replace: true
	};
});
