'use strict'

// START UserController
var UserController = function($scope, Restangular, $http, $uibModal, $location, authService, $log, _){
	
	// START openLoginModal
	$scope.openLoginModal = function (){
	
		var modalInstance = $uibModal.open({
			templateUrl: 'views/modals/login.html',
	        controller: LoginModalController,
	        scope: $scope,
	    }).result.then(function(){}, function(res){});
		
	}
	// END openLoginModal
	
	
	$scope.logout = function () {
	    // TODO
	}
	

		
}
UserController.$inject = ['$scope', 'Restangular', '$http', '$uibModal', '$location', 'authService', '$log', '_'];
MyApp.controller('UserController', UserController);
//END UserController



//START LoginModalController
var LoginModalController = function ($scope, Restangular, $http, $uibModalInstance, authService, $log, _){

	$scope.modalType= 'login';
	
	$scope.changeModalType = function (strType) {
		$scope.modalType = strType;
	} 
	
	$scope.login = function (username, password) {
	   // TODO
	};
	
	$scope.register = function(){
		
	}
	
	$scope.cancel = function() {
	  $uibModalInstance.dismiss('cancel');
	}
	
}
LoginModalController.$inject =  ['$scope', 'Restangular', '$http', '$uibModalInstance', 'authService', '$log', '_'];
MyApp.controller('LoginModalController', LoginModalController);
//END LoginModalController


























