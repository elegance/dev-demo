// ******** 指令总览***************
// 1. restrict
// 2. template/templateUrl
// 3. replace/transclude : 替换嵌套的内容，或者在template中使用属性指令ng-transclude 显示嵌套的内容
// 4. compile 函数
// 5. link 函数，angular思想不要在Controller 和其他地方去操作 dom,在link函数中操作dom，给dom元素绑定事件、绑定作用域
// -- 以下内容可在 17.1.js中查看到
// 6. scope = {} 代表独立作用域
// 7. controller 这个实在指令中暴露出 公共属性的出口
// 8. require 依赖，有了require就可以将依赖的模块注入进来，如linke:function(scope, ele, attrs, DepsCtrl), 这里的DepsCtrl就是依赖指令所暴露的Controller
// --- 指令的三个阶段：加载---compile---link
// ***********************
//
//
//
// 本节主要内容：
// 1. restrict 匹配的四种方式
// 2. 使用template 指定指令内容，但是这样子内容不便于维护，可使用templateUrl
// 3. 记得 template 后的内容给一定要包含在标签内
// 
//
var m1 = angular.module('myApp', []);

m1.directive('hello', function() {
	return {
		// restrict: 表示匹配模式
		// A: attribute 属性
		// E： element 元素,默认
		// M：comment 注释, 类似于注解，使用较少
		// C: class 样式， 使用较少
		restrict: 'E',
		template: '<div>Hi everyone(restrict E)!</div>',
		replace: true
	};
})
.directive('hello', function() {
	return {
		restrict: 'A',
		template: '<div>Hi everyone(restrict A)!</div>',
		replace: true
	};
})
.directive('hello', function() {
	return {
		restrict: 'M',
		template: '<div>Hi everyone(restrict M)!</div>',
		replace: true
	};
})
.directive('hello', function() {
	return {
		restrict: 'C',
		template: '<div>Hi everyone(restrict C)!</div>',
		replace: true
	};
})
.directive('hello2', function() {
	return {
		restrict: 'AEMC',
		template: '<div>Hi everyone(restrict AEMC)!</div>',
		replace: true
	};
});
