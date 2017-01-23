app.
  component('projectModules', {
    templateUrl: 'project_modules.html',
  
    controller:
      function ($routeParams,$scope, $http,$location,$rootScope, sessionManager) {
        var idProject;
          
          this.$onInit= function(){
               $rootScope.lastPath='/home/project/'+$routeParams.idProject;
              $('.blockingDiv').hide(); 
       
                if(!sessionManager.getSessionId()){
                     $location.path('/');
                    return;
                }
              //  $(".back-template").attr("onclick", "backFn();");
                //$(".back-template").html("<i class=\"fa fa-angle-left\" aria-hidden=\"true\"></i>");
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
                       idProject=$routeParams.idProject;
                      //  $http.get("http://localhost:8080/EducadorEngineServices/Services/project/list?idEmpresa"+credentials.company.id)
                        $http.get(moduleListUrl+"?idEmpresa="+sessionManager.getSessionData('idEmpresa')+'&idProyecto='+$routeParams.idProject,
                         {
                             headers : {'sessionId':sessionManager.getSessionId()} 
                        })
                        .success(function(data) {
                            $scope.modules=data.responseBody.modulos;
                            $scope.project=data.responseBody.proyecto;
                          

                        });

                      $('.blockingDiv').hide(); 

                });
           
                  
        };//init
        
           $scope.showModuleHome= function(moduleId){
                $location.path('/home/project/'+idProject+'/modules/'+moduleId);
            }
           
             $scope.back= function(){
                  $location.path('/home/');
               
            }
    
    
      }
    
    
    
    
  
    
    
    
    
    
  });
