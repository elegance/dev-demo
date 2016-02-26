var m1 = angular.module('myApp', []);

m1.directive('hello', function() {
	return {
		restrict: 'E',
		template: '<div>Hello everyone(标签嵌套的内容已被我覆盖e！) !!<div>',
		replace: true
	};
});
