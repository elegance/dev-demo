// 指令中之前未提到的属性
// 1. scope 代表独立作用域
// 2. controller 这个实在指令中暴露出 公共属性的出口
// 3. require 依赖，有了require就可以将依赖的模块注入进来，如linke:function(scope, ele, attrs, DepsCtrl), 这里的DepsCtrl就是依赖指令所暴露的Controller
var m1 = angular.module('myApp', []);

m1
.directive('superman', function() {
	return {
		restrict: 'E',
		scope: {}, //独立作用域
		controller: function($scope) {
			$scope.abilities = [];
			this.addStrength = function() {
				$scope.abilities.push('strength');
			},
			this.addSpeed = function() {
				$scope.abilities.push('speed');
			},
			this.addLight = function() {
				$scope.abilities.push('light');
			}
		},
		link: function(scope, element, attrs) {
			element.addClass('btn btn-primary');
			element.bind('mouseenter', function() {
				console.log(scope.abilities);
			});
		}
	};
})
.directive('strength', function() {
	return {
		require: '^superman',
		restrict: 'A',
		link: function(scope, ele, attrs, supermanCtrl) {
			supermanCtrl.addStrength();
		}
	};
})
.directive('speed', function() {
	return {
		require: '^superman',
		link: function(scope, ele, attrs, supermanCtrl) {
			supermanCtrl.addSpeed();
		}
	};
})
.directive('light', function() {
	return {
		require: '^superman',
		link: function(scope, ele, attrs, supermanCtrl) {
			supermanCtrl.addLight();
		}
	};
});
