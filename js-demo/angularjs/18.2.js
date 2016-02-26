var m1 = angular.module('myApp', []);

m1.controller('mainCtrl', ['$scope', function($scope) {
	$scope.ctrlFlavor = '百威';
}]);

m1.directive('drink', function() {
	return {
		restrict: 'AE',
		replace: true,
		template: '<div>{{flavor}}</div>',
		link: function(scope, ele, attrs) {
			console.log(attrs.flavor);
			scope.flavor = attrs.flavor;
		}
	};
})
//使用 @符号也可以 实现上面的效果, 不过使用@符号传递的只能是字符串
.directive('drink2', function() {
	return {
		restrict: 'AE',
		replace: true,
		template: '<div>{{flavor}}</div>',
		scope: {
			flavor: '@'
		}
	};
});
