'use strict';

/**
 * @ngdoc function
 * @name MyApp.controller:CategoriesController
 * @description
 * # CategoriesController
 * Controller of the MyApp
 */

var CategoriesController =  function($scope, Restangular, $uibModal, $log, _) {
	
	//Restangular koristimo za rest pozive

    //Categories
    Restangular.all("categories").getList().then(function(entries) {
      $scope.categories = entries;
    });
    
    //START BookModalController
    var CategoryModalController = ['$scope', '$uibModalInstance', 'category', function ($scope, $uibModalInstance, book){
		
    	$scope.category=category;
    	
        $scope.ok = function() {
            $uibModalInstance.dismiss('cancel');
          };
    	
		$scope.cancel = function() {
          $uibModalInstance.dismiss('cancel');
        };
        
    }];
    //END NewBookModalController
    
    $scope.openModalCategory = function (category) {
    	if (!category) {
            category = {
              name: ''
            };
          }
	    var modalInstance = $uibModal.open({
	        templateUrl: 'views/modals/category.html',
	        controller: CategoryModalController,
	        scope: $scope,
	        resolve: {
	        	category: function() {
	            return category;
	          }
	        }
	    });
	};

}

EBooksController.$inject = ['$scope', 'Restangular', '$uibModal', '$log', '_'];
MyApp.controller('CategoriesController', CategoriesController);