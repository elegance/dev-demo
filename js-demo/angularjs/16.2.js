// 指令复用
var m1 = angular.module('myApp', []);

m1.controller('MyCtrl1', ['$scope', function($scope) {
	$scope.loadData = function() {
		console.log('加载数据中1....');
	};
}])
.controller('MyCtrl2', ['$scope', function($scope) {
	$scope.loadData2 = function() {
		console.log('加载数据中2....');
	};
}]);

m1.directive('loader', function() {
	return {
		restrict: 'E',
		link: function(scope, element, attr) {
			element.bind('mouseenter', function() {
				//scope.loadData();
				//scope.$apply('loadData()');
				// 注意这个坑，howToLoad 会被转换成小写，这里写大写的将无效
				//scope.$apply(attr.howToLoad);
				scope.$apply(attr.howtoload);
			});
		}
	};
});
