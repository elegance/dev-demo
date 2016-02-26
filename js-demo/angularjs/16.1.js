// 1. link 方法中绑定元素的 事件
// 2. 可以使用scope调用方法，也可以使用scope.$apply 来调用 scope中的方法
var m1 = angular.module('myApp', []);

m1.controller('firstCtrl', ['$scope', function($scope) {
	$scope.loadData = function() {
		console.log('加载数据中....');
	};
}]);

m1.directive('loader', function() {
	return {
		restrict: 'E',
		link: function(scope, element, attr) {
			element.bind('mouseenter', function() {
				//scope.loadData();
				scope.$apply('loadData()');
			});
		}
	};
});
