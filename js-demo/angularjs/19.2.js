var m1 = angular.module('myApp', []);
var i = 0;

m1
	.directive('expander', function() {
		return {
			restrict: 'AE',
			replace: true,
			transclude: true,
			scope: {
				cTitle: '=sSTTitle',
				cTitle2: '=cATitle2'
			},
			template: '<div>'
					+ '<div class="title" ng-click="toggle()">{{cTitle}}</div>'
					+ '<div class="title">{{cTitle2}}--{{cTitle}}</div>'
					+ '<div class="body" ng-show="showMe" ng-transclude></div>'
					+ '</div>',
			link: function(scope, ele, attrs) {
				scope.showMe = false;
				scope.toggle = function() {
					scope.showMe = !scope.showMe;
				};
			}
		};
	})
	.controller('mainCtrl', ['$scope', function($scope) {
		$scope.cTitle = '点击这里展开';
		$scope.cTitle2 = '标题2测试';
		$scope.text = '这里是内部内容';
	}])
	;
