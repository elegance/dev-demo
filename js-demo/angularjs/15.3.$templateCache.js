var m1 = angular.module('myApp', []);

m1.run(function($templateCache) {
	$templateCache.put('hello.html', '<div>Hello everyOne!</div>')
});

m1.directive('hello', function($templateCache) {
	return {
		restrict: 'AECM',
		template: $templateCache.get('hello.html'),
		replace: true
	};
});
