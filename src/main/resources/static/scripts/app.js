'use strict';

/**
 * @ngdoc overview
 * @name MyApp
 * @description
 * # MyApp
 *
 * Main module of the application.
 */

var MyApp = angular.module('MyApp', [
    'ngResource',
    'ngRoute',
    'ngFileUpload',
    'ngAnimate', 
    'ngSanitize',
    'restangular',
    'ui.bootstrap',
    'lodash',
  ])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
//      .when('/', {
//        templateUrl: 'views/main.html',
//        controller: 'MainController',
//        controllerAs: 'main'
//      })
      .when('/', {
        templateUrl: 'views/books.html',
        controller: 'EBooksController',
        controllerAs: 'ebooks'
      })
      .when('/books', {
        templateUrl: 'views/books.html',
        controller: 'EBooksController',
        controllerAs: 'ebooks'
      })
      .when('/categories', {
        templateUrl: 'views/categories.html',
        controller: 'CategoriesController',
        controllerAs: 'categories'
      })
      .when('/users', {
    	redirectTo: '/'
//    	templateUrl: 'views/categories.html',
//        controller: 'CategoriesController',
//        controllerAs: 'categories'
      })
      .otherwise({
        
      });
  }])
  // run se izvrsava pre svega ostalog
  .run(['Restangular', '$rootScope', '$location', '$log', function(Restangular, $rootScope, $location, $log) {
    //postavljamo $rootScope radi zaobilaska unsafe:url...
    //primer ng-href="{{protocol}}{{baseURLHref}}{{baseURLPort}}routingPathName"
    $rootScope.protocol = 'http://';
    $rootScope.baseURLHref = $location.host();
    $rootScope.baseURLPort = ':' + $location.port() + '/';
    // postavimo base url za Restangular da ne bismo morali da ga
    // navodimo svaki put kada se obracamo back endu
    // poziv vrsimo na http://localhost:8080/api/
    Restangular.setBaseUrl("api");
    Restangular.setErrorInterceptor(function(response) {
      if (response.status === 500) {
        $log.info("internal server error");
        return true; // greska je obradjena
      }
      return true; // greska nije obradjena
    });
    
    
    
  }]);
