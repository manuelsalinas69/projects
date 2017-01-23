
var app=angular.module('elsApp',['ngRoute','ngCookies']);

app.config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
    }
]);

app.config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
      $locationProvider.hashPrefix('!');

      $routeProvider.
        when('/', {
          template: '<login></login>'
        }).
        when('/home', {
          template: '<project-home></project-home>'
        }).
        when('/home/project/:idProject', {
          template: '<project-modules></project-modules>'
        }).
       when('/home/project/:idProject/modules/:idModule', {
          template: '<module-home></module-home>'
        }).
       when('/home/project/:idProject/modules/:idModule/form/:idExecution', {
          template: '<module-form></module-form>'
        }).
      when('/home/project/:idProject/modules/:idModule/form/:idExecution/:idDetail', {
          template: '<module-form></module-form>'
        }).
        otherwise('/');
    }
  ])

app.config(['$provide', function($provide) {
  $provide.factory('SessionService', function() {
     var isSessionAvailable=function(){return false};
      
      var shinyNewServiceInstance;
    
      // factory function body that constructs shinyNewServiceInstance
      
      
    return shinyNewServiceInstance;
  });
}]);
app.controller("appController",function($scope,$rootScope,$location,sessionManager){
    $scope.userName = function() {
       sessionManager.getSessionData('username');
    };
    
    $scope.getUserName= function (){
        var userName=  sessionManager.getSessionData('username');
        return userName;
    };
    
    $scope.getCompany= function (){
        var nombreEmpresa=  sessionManager.getSessionData('nombreEmpresa');
        return nombreEmpresa;
    };
    
     $scope.mainPage= function (){
       $location.path('/home'); 
    };
    
    
    $scope.logout= function (){
        if(sessionManager.isSessionId()){
             var logoutPromises=sessionManager.logout();
            logoutPromises.then(function(result){
                                    $location.path('/'); 
                                }

            );
        }
       
       
    };
    
    
    
   
    
    
   
});


