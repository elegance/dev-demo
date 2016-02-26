// 1. 设置transclude 为true
var m1 = angular.module('myApp', []);

m1.directive('hello', function() {
	return {
		restrict: 'E',
		transclude: true,
		template: '<div>Hello everyone !!<div ng-transclude></div><div>'
	};
});
