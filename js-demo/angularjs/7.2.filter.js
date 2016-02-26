var appFilter = angular.module('myApp.filter', []);

appFilter.filter('rJs', function() {
	return function(input, n1, n2) {
		input = (n1 ? input + '-' + n1 : input);
		input = (n2 ? input + '-' + n2 : input);

		return input.replace(/js/i, 'javascript');
	};
});
