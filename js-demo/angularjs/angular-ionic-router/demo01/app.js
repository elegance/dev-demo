angular.module('app', ['ionic'])

.constant('_', window._)

.run(['$ionicPlatform', '$rootScope', function($ionicPlatform, $rootScope) {
	$rootScope._ = window._;

	// ionic 平台配置
	$ionicPlatform.ready(function() {
		if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
			cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
			cordova.plugins.Keyboard.disableScroll(true);
		}
		if (window.StatusBar) {
			StatusBar.styleDefault();
		}
	});
}])
.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/tab/main');

	$stateProvider.state('tab', {
		url: '/tab',
		abstract: true,
		templateUrl: 'tpls/tabs.html'
	})
	.state('tab.main', {
		url: '/main',
		views: {
			'tab-main': {
				templateUrl: 'tpls/main.html'
			}
		}
	})
	.state('tab.product', {
		url: '/product',
		views: {
			'tab-product': {
				templateUrl: 'tpls/product.html'
			}
		}
	})
	.state('tab.product.prodTab', {
		url: '/dtlTabs',
		views: {
			'tab-product@tab': {
				templateUrl: 'tpls/tabs-product.html'
			}
		}
	})
	.state('tab.product.prodTab.hold', {
		url: '/hold',
		views: {
			'tab-hold': {
				templateUrl: 'tpls/hold.html'
			}
		}
	})
	.state('tab.product.prodTab.trans', {
		url: '/trans',
		views: {
			'tab-trans': {
				templateUrl: 'tpls/trans.html'
			}
		}
	})
	.state('tab.product.prodTab.entrust', {
		url: '/entrust',
		views: {
			'tab-entrust': {
				templateUrl: 'tpls/entrust.html'
			}
		}
	})
	.state('tab.product.productMain', {
		url: '/productMain',
		views: {
			'tab-product@tab': {
				templateUrl: 'tpls/product-main.html',
				controller: function($scope, $ionicSlideBoxDelegate) {
					$scope.slideHasChanged = function($index) {
						console.log($index);
					};

					$scope.tabSlider = $ionicSlideBoxDelegate;
				}
			}
		}
	})
	.state('tab.settings', {
		url: '/settings',
		views: {
			'tab-settings': {
				templateUrl: 'tpls/settings.html'
			}
		}
	})
	.state('test', {
		url: '/main',
		template: 'test content'
	})
	
}]);
