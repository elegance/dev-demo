var m1 = angular.module('myApp', []);

m1.directive('accordtion', function() {
	return {
		restrict: 'AE',
		replace: true,
		transclude: true,
		template: '<div class="accordtion" ng-transclude></div>',
		controller: function() {
			var expanders = [];

			this.addExpander = function(expander) {
				expanders.push(expander);
			};

			this.hideOtherExpander = function(selectedExpander) {
				angular.forEach(expanders, function(expander) {
					if (selectedExpander != expander) {
						expander.showMe = false;
					}
				});
			};
		}
	};
});

m1.directive('expander', function() {
	return {
		restrict: 'AE',
		require: '^accordtion',
		replace: true,
		transclude: true,
		scope: {
			title: '=title',
			showMe: '=showMe'
		},
		template: '<div class="item">'
				+ '<div class="title" ng-click="toggle()">{{title}}</div>'
				+ '<div class="body" ng-show="showMe" ng-transclude></div>'
				+ '</div>',
		link: function(scope, ele, attrs, accordCtrl) {
			if (!scope.showMe) {
				scope.showMe = false;
			}

			scope.toggle = function() {
				if (!scope.showMe) { //如果当前是关闭，执行toggle则代表来打开，则去关闭其它
					accordCtrl.hideOtherExpander(scope);
				}
				scope.showMe = !scope.showMe;
			};

			accordCtrl.addExpander(scope);
		}
	};
});

m1.controller('mainCtrl', ['$scope', function($scope) {
	$scope.expanders = [
		{
			title: '手风琴效果',
			text: '典型的手风琴效果的UI就是当前这个效果。',
			showMe: true
		},
		{
			title: '有一些变体',
			text: '手风琴效果还有一些变体。模样虽不像，本质一致。'
		},
		{
			title: '甚至这个',
			text: '更多展开与收起效果也算，因此，会在下面展示。'
		}
	];
}]);

