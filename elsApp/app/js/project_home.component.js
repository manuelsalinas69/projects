app.
  component('projectHome', {
    templateUrl: 'project_home.html',
    controllerAs: 'projectController',
    controller:  function ($scope, $http,$location,$rootScope,sessionManager) {
    
    this.$onInit= function(){
         $('.blockingDiv').hide(); 
       
        if(!sessionManager.getSessionId()){
             $location.path('/');
            return;
        }
        $('.blockingDiv').show(); 
        var sessionDataPromises=sessionManager.manageSessionStatus();
        sessionDataPromises.then(function(data){
                if(!data.responseBody){
                    $location.path('/');
                    return;
                }
                if(!("ACTIVO"==data.responseBody.status)){
                    $location.path('/');
                    return;                     
                }
                $scope.userName=sessionManager.getSessionData("username");
                $http.get(projectListUrl+"?idEmpresa="+sessionManager.getSessionData('idEmpresa'),
                 {
                     headers : {'sessionId':sessionManager.getSessionId()} 
                }).success(function(data) {
                    $scope.projects=data;
                    $('.blockingDiv').hide(); 
                });
              
               
        });
        
       
    };//onInit
    
    $scope.showModule= function(projectId){
        $location.path('/home/project/'+projectId);
    }
    
    
    }
    
  });
