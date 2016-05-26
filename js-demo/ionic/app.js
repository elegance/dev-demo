angular.module('app', ['ionic'])

.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/tab/tab1');

	$stateProvider
		.state('tab', {
			url: '/tab',
			abstract: true,
			templateUrl: 'tabs.html',
			controller: 'AppCtrl'
		})
		//独立页面，不继承与 tabs，有回退按钮
		.state('single', {
			abstract: true,
			templateUrl: 'single.html',
			controller: 'AppCtrl'
		})
		.state('tab.tab1', {
			url: '/tab1',
			views: {
				'tab1': {
					templateUrl: 'tab1.html'
				}
			}
		})
		.state('tab.tab1-dtl', {
			url: '/tab1-dtl',
			views: {
				'tab1': {
					templateUrl: 'tab1-dtl.html'
				}
			}
		})
		.state('tab2', {
			url: '/tab2',
			parent: 'tab',
			views: {
				'tab2': {
					templateUrl: 'tab2.html'
				}
			}
		})
		.state('sigle1', {
			url: '/sigle1',
			views: {
				'': {
					templateUrl: 'sigle1.html',
					controller: 'AppCtrl'
				}
			}
		})
		.state('single2', {
			url: '/single2',
			parent: 'single',
			templateUrl: 'single2.html'
		})
})
.controller('AppCtrl', function($scope, $ionicHistory) {
	$scope.name = 'AppCtrl name!';

	$scope.back = function() {
		$ionicHistory.goBack();
	};
});
