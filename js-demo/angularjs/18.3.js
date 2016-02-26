var m1 = angular.module('myApp', []);

m1
.controller('mainCtrl', function($scope) {
	$scope.ctrlFlavor = '百威';
})
.directive('drink', function() {
	return {
		restrict: 'AE',
		scope: {
			flavor: '='
		},
		template: '<input type="text" ng-model="flavor" />'
	};
});
