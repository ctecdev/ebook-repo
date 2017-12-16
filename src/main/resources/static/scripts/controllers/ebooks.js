'use strict';

/**
 * @ngdoc function
 * @name EBooksController
 * @description
 * # EBooksController
 * Controller of the MyApp
 */

//START EBooksController
var EBooksController =  function($scope, Restangular, $uibModal, $log, _) {
	
	$scope.search = {
	  template: "search-details-popover.html",
      attributes: {},
      types: {},
      occurs: {},
      keywsB: false,  // pagination > search results
      detailed: {
    	  isOn: false,
    	  haveInput: false
      }
	};
	
	//show hide
	$scope.detailedSearch = function(){
		if ($('#detailedSearch').is(':hidden')) {
			  console.log("Detailed search");
			  $('#detailedSearch').show();
			  $('#sSearchIn').prop('disabled','true');
			  $scope.search.detailed.isOn = true;
		  } else {
			  console.log("Simple search");
			  $('#detailedSearch').hide();
			  $('#sSearchIn').removeAttr("disabled");
			  $scope.search.detailed.isOn = false;
		}
	}
	//Change Value - type; occur
	$scope.changeVal = function(msg, $event){
      var el = event.target
	  console.log(el.name +" changed: " + $event);
      $scope[el.name] = $event;
    }
	
	$scope.searchExe = function() {
	  if ($scope.search.detailed.isOn == true) {
		  $scope.search.detailed.haveInput = true;
		  if($scope.titleKW == null && $scope.authorKW == null && $scope.keywordsKW == null && $scope.contentKW == null && $scope.languageKW == null){
            $scope.search.detailed.haveInput = false;
		  }else if($scope.titleKW == "" && $scope.authorKW == "" && $scope.keywordsKW == "" && $scope.contentKW == "" && $scope.languageKW == ""){
            $scope.search.detailed.haveInput = false;
		  }
		  console.log("DetailedSearch");
		  $(".preS").removeAttr("hidden");
	      $(".preN").prop('hidden','hidden');
	      $("#clear-search").removeAttr("hidden");
	      //$scope.search.keywsB = true; // pagination > search results
	      $scope.getPageEB(); //GET
	  } else {
	    if ($scope.keyws == null || $scope.keyws == ""){
		  $(".preN").removeAttr("hidden");
	      $(".preS").prop('hidden','hidden');
		  $("#clear-search").prop('hidden','hidden');
	      $scope.search.keywsB = false; // pagination > search results
	      $scope.getPageEB(); //GET
	    } else {
		  $(".preS").removeAttr("hidden");
	      $(".preN").prop('hidden','hidden');
	      $("#clear-search").removeAttr("hidden");
	      $scope.search.keywsB = true; // pagination > search results
	      $scope.getPageEB(); //GET
	    }
	  }
	}
	
	$scope.searchClear = function(){
      $scope.clearSearchInputs(); //clear search inputs
	  $scope.setSearchTypes($scope.types[0]); //reset - select - search types
	  $scope.setSearchOccures($scope.occures[0]); //reset - select - occures
	  
      $(".preN").removeAttr("hidden");
      $(".preS").prop('hidden','hidden');
      $("#clear-search").prop('hidden','hidden'); //button
      $scope.search.keywsB = false; // pagination > search results
      $scope.search.detailed.haveInput = false; // no search > get
      
      $scope.getPageEB(); //GET
	   
	}
	
	$scope.clearSearchInputs = function(){
      $scope.keyws = "";
      $scope.titleKW = "";
      $scope.authorKW = "";
      $scope.keywordsKW = "";
      $scope.contentKW = "";
      $scope.languageKW = "";
	}
	$scope.setSearchTypes = function(type){
	  $scope.titleST = type;
      $scope.authorST = type;
      $scope.keywordsST = type;
      $scope.contentST = type;
      $scope.languageST = type;
	}
	$scope.setSearchOccures = function(occur){
      $scope.titleOC = occur; 
  	  $scope.authorOC = occur; 
  	  $scope.keywordsOC = occur; 
  	  $scope.contentOC = occur; 
  	  $scope.languageOC = occur; 
	}
	
	/** get EBooks - page */
	$scope.getPageEB = function() {
	  var data = {
        catId: $scope.category.id,
        page: $scope.currentPage,
        size: $scope.itemsPerPage,
        sort: $scope.sort,
        keyws: $scope.keyws,
        titleKW: $scope.titleKW,
		authorKW: $scope.authorKW, 
		keywordsKW: $scope.keywordsKW,
		contentKW: $scope.contentKW,
		languageKW: $scope.languageKW,
		titleST: $scope.titleST,
		authorST: $scope.authorST,
		keywordsST: $scope.keywordsST,
		contentST: $scope.contentST,
		languageST: $scope.languageST,
		titleOC: $scope.titleOC,
		authorOC: $scope.authorOC,
		keywordsOC: $scope.keywordsOC,
		contentOC: $scope.contentOC,
		languageOC: $scope.languageOC
	  }
	  if ($scope.search.detailed.isOn == true) {  /** detailed searcn */
        if ($scope.search.detailed.haveInput == true){
          console.log("detailed-search-page");
		  Restangular.all("ebooks/dsearch").post(data).then(function(res) {
		    if(res==undefined){
		    	refreshPage(data);
		    }else{
	            $scope.ebooks = res.content;
	            $scope.totalItems = res.totalElements; // pagination
		    }
		  });
        } else {
        	refreshPage(data);
        }
	  } else { /** simple search */
        if ($scope.search.keywsB == true) { // pagination > search results
		  console.log("simple-search-page");
		  data.keyws = $scope.keyws//keyws
		  Restangular.all("ebooks/ssearch").post(data).then(function(res) {
		    $scope.ebooks = res.content;
		    $scope.totalItems = res.totalElements; // pagination
		  });
	    } else {
	    	refreshPage(data);
		}
	  }
	}
	/** Restangular - get EBooks page (no search) */
    var refreshPage = function(data) {
	  Restangular.all("ebooks/get-post").post(data).then(function(res) {
        $scope.ebooks = res.content;
        $scope.totalItems = res.totalElements; // pagination
      });
    }
	
	$scope.sorts = {} // get sort types
	//START pagination
	$scope.maxSize = 7; //Number of pager buttons to show
	$scope.ippOptions = [1,3,5,10,20,30,40,50]; // itemsPerPage options
	$scope.itemsPerPage = $scope.ippOptions[3];
	$scope.currentPage = 1;
	
	$scope.pageChanged = function() {
	  console.log('Page changed to: ' + $scope.currentPage);
	  $scope.getPageEB(); //GET
	};
	$scope.setItemsPerPage = function(num) {
      console.log('Items per page: ' + num);
	  $scope.itemsPerPage = num;
	  $scope.currentPage = 1; //reset to first page
	  $scope.getPageEB(); //GET
	}
	/*$scope.setPage = function (pageNo) {
	  $scope.currentPage = pageNo;
	};*/
	//END pagination
	
	//Categories - with books
    Restangular.all("categories/wb").getList().then(function(entries) {
      $scope.categories = entries;
      $scope.category = $scope.categories[0]; //defaul category
    }).then(function() {
    	//sort options
    	Restangular.all("ebooks/sorts").getList().then(function(entries) {
    	  $scope.sorts = entries;
    	  $scope.sort = $scope.sorts[1]; //defaul sort
    	  
    	  //if not SEARCH remove sort by relevance
    	  
    	}).then(function() {
    		Restangular.all("ebooks/search-occures").getList().then(function(entries) {
	    	  $scope.occures = entries;
	    	  $scope.setSearchOccures($scope.occures[0]); //set occures to default 
	    	}).then(function() {
	    		Restangular.all("ebooks/search-types").getList().then(function(entries) {
    	    	  $scope.types = entries;
    	    	  $scope.setSearchTypes($scope.types[0]); //set types tp default
    	    	  $scope.getPageEB();
    	    	})
			})
		})
	});
    
    //category changed
	$scope.changeCategory = function(cat){
    	console.log("Category changed: " + cat.name);
  	    $scope.currentPage = 1; //reset to first page
    	$scope.category = cat;
    	$scope.getPageEB(); //GET
    }
	
	//sort changed
	$scope.changeSort = function(sort) {
		console.log("Sort changed: " + sort);
		$scope.sort = sort;
		$scope.getPageEB(); //GET
	}
	
    //START download file
    $scope.downloadBook = function(book){
    	Restangular.one('ebooks/'+book.uuid+'/file').withHttpConfig({ responseType: 'arraybuffer' }).get().then(function (response) {
            
    		var fileName = book.author + " - " + book.title + ".pdf";
            var a = document.createElement('a');
            document.body.appendChild(a);
            a.style = 'display: none';
            var file = new Blob([response], {type: 'application/pdf;  charset=charset=utf-8'});
            var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
            a.href = fileURL;
            a.download = fileName;
            a.click();
            (window.URL || window.webkitURL).revokeObjectURL(file);
            
        }, function (response) {
            //error
        	console.log("ERROR" + responce);
        });

    }
    //END download file
    
    //START deleteBook
    $scope.deleteBook = function(book) {
    	var modalInstance = $uibModal.open({
	        templateUrl: 'views/modals/confirm.html',
	        controller: function($scope, $uibModalInstance, confirm){        	
	        	$scope.confirm = confirm;
	        	$scope.ok = function() {
	                Restangular.one("ebooks", book.uuid).remove().then(function() {
	                    // uklanjamo knjigu sa zadatim id-jem iz kolekcije
	                    _.remove($scope.ebooks, {
	                  	  uuid: book.uuid
	                    });
	                });
	                $uibModalInstance.dismiss('ok');
	        	}
	        	$scope.cancel = function() {
	        	      $uibModalInstance.dismiss('cancel');
	        	}
	        },
	        scope: $scope,
	        resolve: {
	        	confirm: function(){
		            confirm = {
		            	header : "Confirm deleting book...",
		        	    message : "Are you sure want to delete book `" + book.title + " - " + book.author + "` ?"
		            }
	        		return confirm;
	        	}
	        }
	    }).result.then(function(){}, function(res){});
    };
    //END deleteBook
    
    //START openModalBook - create, update
    $scope.openModalBook = function (book) {
    	if (!book) {
          book = {
            title: '',
            author: ''
          }
        }else{
          book.Original = angular.copy(book);
        }
    	book.file = {} //set input value - size, type
	    var modalInstance = $uibModal.open({
	        templateUrl: 'views/modals/book.html',
	        controller: BookModalController,
	        scope: $scope,
	        resolve: {
	        	book: function() {
	            return book;
	          }
	        }
	    }).result.then(function () {
	        // Successful no need to do anything
	    }, function (result) {
	        // Dismiss
	    	if(book.uuid!=null){
	    		book = book.Original;
		          angular.forEach($scope.ebooks, function (eb, key) {
		            if (eb.uuid == book.uuid) {
		              $scope.ebooks[key] = angular.copy(book);
		              return;
		            }
		        });	
	    	}
	  });	

	};
	//END openModalBook
	
	//START openModalBookDetails - view
    $scope.openModalBookDetails = function (book) {
      	book.file = {} //set input value - size, type
  	    var modalInstance = $uibModal.open({
  	        templateUrl: 'views/modals/book-details.html',
  	        controller: BookModalController,
  	        scope: $scope,
  	        resolve: {
  	        	book: function() {
  	            return book;
  	          }
  	        }
  	    }).result.then(function () {}, function (result) {} );	
	};
	//END openModalBookDetails
	
	//START openModalPDF - view
    $scope.openModalPDF = function (book) {
    	
	    var modalInstance = $uibModal.open({
	        templateUrl: 'views/modals/pdf-view.html',
	        controller: PDFModalController,
	        scope: $scope,
	        resolve: {
	        	book: function() {
	            return book;
	          }
	        }
	    }).result.then(function(){}, function(res){});
	};
	//END openModalPDF

}
EBooksController.$inject = ['$scope', 'Restangular', '$uibModal', '$log', '_'];
MyApp.controller('EBooksController', EBooksController);
//END EBooksController

//START BookModalController
var BookModalController = function ($scope, Restangular, $uibModalInstance, multipartForm, book, $log, _){
	
	$scope.book=book;
	$scope.book.file.size=book.fileSize;
	$scope.book.file.type=book.mimeName;
	$scope.book.fileName = book.author + " - " + book.title + ".pdf";
	
	//Languages
    Restangular.all("languages").getList().then(function(entries) {
      $scope.languages = entries;
    });
	//Categories
    Restangular.all("categories").getList().then(function(entries) {
      $scope.categories = entries;
    });
    
    $scope.ok = function() {
    	
    	var fd = new FormData();
    	fd.append('title', $scope.book.title);
		fd.append('author', $scope.book.author);
		fd.append('keywords', $scope.book.keywords);
		fd.append('publicationYear', $scope.book.publicationYear);
		fd.append('fileSize', $scope.book.file.size);
		fd.append('mimeName', $scope.book.file.type);
		fd.append('categoryId', $scope.book.category.id);
		fd.append('languageId', $scope.book.language.id);
		//fd.append('userId', $scope.book.userId);
		fd.append('userId', "1");
		
		//UPDATE e-Book data
		if($scope.book.uuid != null){ //
			fd.append('uuid', $scope.book.uuid);
			//New file selected
			if($scope.file != null){
				fd.append('file', $scope.book.file);
				Restangular.one('ebooks/'+$scope.book.uuid+"/upload")
		        .withHttpConfig({transformRequest: angular.identity})
		        .customPUT(fd, '', undefined, {'Content-Type': undefined});
			//No file selected
			}else{
				Restangular.one('ebooks/'+$scope.book.uuid).customPUT($scope.book);
			}
			//refresh data
			$scope.getPageEB(); //GET function - refresh ebooks page
    	//CREATE New e-Book
		}else{
			//New file selected
			if($scope.book.file != null){
				fd.append('file', $scope.book.file);
				Restangular.one('ebooks')
		        .withHttpConfig({transformRequest: angular.identity})
		        .customPOST(fd, '', undefined, {'Content-Type': undefined})
		        .then(function(data) {
		        	$scope.getPageEB(); //GET
		        });
			//No file selected
			}else{
				alert('Error! To create e-Book u must select File to upload.');
				$log.info('Error! To create e-Book u must select File to upload.');
			}
		}	
		$uibModalInstance.close('ok');
    };
	
	$scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    }
	
}
BookModalController.$inject =  ['$scope', 'Restangular', '$uibModalInstance', 'multipartForm', 'book', '$log', '_'];
MyApp.controller('BookModalController', BookModalController);
//END BookModalController

//START BookModalController
var BookModalDetailsController = function ($scope, Restangular, $uibModalInstance, book, $log, _){
	
	$scope.book=book;
	
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	}
	
}
BookModalDetailsController.$inject =  ['$scope', 'Restangular', '$uibModalInstance', 'book', '$log', '_'];
MyApp.controller('BookModalDetailsController', BookModalDetailsController);
//END BookModalController
	
	
//START PDFModalController
var PDFModalController = function ($scope, Restangular,  $uibModalInstance, $sce, book, $log, _){
	
	$scope.book = book;
	
	Restangular.one('ebooks/'+book.uuid+'/file').withHttpConfig({ responseType: 'arraybuffer' }).get().then(function (response) {
        
		$scope.book.fileName = book.title + " - " + book.author + ".pdf";
        var file = new Blob([response], {type: 'application/pdf;  charset=charset=utf-8'});
        var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
        $scope.book.fileURL = $sce.trustAsResourceUrl(fileURL);
        
    }, function (response) {
        //error
    	console.log("ERROR" + responce);
    });
	
	$scope.close = function() {
		$uibModalInstance.dismiss('close');
	}
	
}
BookModalController.$inject =  ['$scope', 'Restangular', '$uibModalInstance', '$sce', 'book', '$log', '_'];
MyApp.controller('PDFModalController', PDFModalController);
//END PDFModalController


