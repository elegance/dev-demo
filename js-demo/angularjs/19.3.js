var m1 = angular.module('myApp', []);

m1
	.controller('mainCtrl', ['$scope', function($scope) {
		$scope.expanders = [
			{
				title: 'Click me to expand',
				text: 'Hi there folks'
			},
			{
				title: 'Click me to expand 2',
				text: 'Hi there folks 2'
			},
			{
				title: 'Click me to expand 3 gogo',
				text: 'Hi there folks gogo'
			}
		];
	}])
	.directive('accordtion', function() {
		return {
			restrict: 'EA',
			replace: true,
			transclude: true,
			template: '<div ng-transclude></div>',
			controller: function() {
				var expanders = [];

				this.addExpander = function(expander) {
					expanders.push(expander);
				};

				// 闭合 其它 expander
				this.gotOpened = function(selectedExpander) {
					angular.forEach(expanders, function(expander) {
						if (selectedExpander != expander) {
							expander.showMe = false;
						}
					});
				};
			}
		};
	})
	.directive('expander', function() {
		return {
			restrict: 'EA',
			require: '^accordtion',
			replace: true,
			transclude: true,
			scope: {
				title: '=expanderTitle'
			},
			template: '<div>'
					+ '<div class="title" ng-click="toggle()">{{title}}</div>'
					+ '<div class="body" ng-show="showMe" ng-transclude></div>'
					+ '</div>',
			link: function(scope, ele, attrs, accordCtrl) {
				scope.showMe = false;
				accordCtrl.addExpander(scope);

				scope.toggle = function() {
					scope.showMe = !scope.showMe;
					accordCtrl.gotOpened(scope);
				};
			}
		};
	});
