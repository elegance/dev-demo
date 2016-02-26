var m1 = angular.module('myApp', []);

m1
.controller('mainCtrl', function($scope) {
	$scope.sayHello = function(name) {
		alert('hello :' + name);
	};
})
.directive('greeting', function() {
	return {
		restrict: 'AE',
		scope: {
			greet: '&'
		},
		template: '<input type="text" ng-model="userName">' +
				'<button class="btn btn-default" ng-click="greet({name:userName})">Greeting</button>' +
				'<br>'
	};
});
