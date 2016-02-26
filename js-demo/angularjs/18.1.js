var m1 = angular.module('myApp', []);

m1.directive('hello', function() {
	return {
		restrict: 'E',
		template: '<div><input type="text" ng-model="userName">{{userName}}</div>',
		replace: true
	};
});

m1.directive('hello2', function() {
	return {
		restrict: 'E',
		scope: {},
		template: '<div><input type="text" ng-model="userName">{{userName}}</div>',
		replace: true
	};
});

