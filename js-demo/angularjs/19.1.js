var m1 = angular.module('myApp', []);

m1.controller('TestFormCtrl', ['$scope', function($scope) {
	$scope.user = {
		userName: 'elegance',
		password: ''
	};
	$scope.canSub = true;

	$scope.save = function() {
		alert('保存数据');
	};
}]);
