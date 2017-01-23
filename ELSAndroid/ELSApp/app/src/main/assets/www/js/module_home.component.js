app.
  component('moduleHome', {
    templateUrl: 'module_home.html',
    controller:
      function ($routeParams,$scope, $http,$location,$rootScope,sessionManager) {
          var idProject;
       
        this.$onInit= function(){
          //  $http.get("http://localhost:8080/EducadorEngineServices/Services/project/list?idEmpresa"+credentials.company.id)
          
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
                        $scope.moduleId=$routeParams.idModule;
                        idProject=$routeParams.idProject;
                         $http.get(moduleEjecListUrl+"?idSuscriptor="+sessionManager.getSessionData('idSuscriptor')+'&idModulo='+$routeParams.idModule,
                         {
                             headers : {'sessionId':sessionManager.getSessionId()} 
                        })
                        .success(function(data) {
                            $scope.ejecuciones=data.responseBody.ejecuciones;
                            $scope.modulo=data.responseBody.modulo;
                            $scope.proyecto=data.responseBody.proyecto;

                        });

                      $('.blockingDiv').hide(); 

                });
            
            
            
            
            
                  
        };//init
        
           $scope.createNew= function(moduleId){
               $http.get(moduleNewUrl+"?idSuscriptor="+sessionManager.getSessionData('idSuscriptor')+'&idModulo='+moduleId,
                         
                         {
                            headers : {'sessionId':sessionManager.getSessionId()} 
                        
                        }
                        
                        )
                .success(function(data) {
                     
                    $location.path('/home/project/'+idProject+'/modules/'+moduleId+'/form/'+data.responseBody.formulario.idEjecucion+'/'+data.responseBody.formulario.idDetalleEjecucion);
                });
               
            };
          
           $scope.resumeExec= function(moduleId,execId){
                  $location.path('/home/project/'+idProject+'/modules/'+moduleId+'/form/'+execId);
               
            }
           
            $scope.back= function(){
                  $location.path('/home/project/'+idProject);
               
            }
            
              $scope.toDate= function(longDate){
                 var d = new Date(longDate);
                var datestring = d.getDate()  + "-" + (d.getMonth()+1) + "-" + d.getFullYear() + " " +d.getHours() + ":" + d.getMinutes();
                  return datestring;
               
            }
    
      }
        
    
  });


