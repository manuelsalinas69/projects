app.
  component('moduleHome', {
    templateUrl: 'module_home.html',
    controller:
      function ($routeParams,$scope, $http,$location) {
          var idProject;
        this.$onInit= function(){
          //  $http.get("http://localhost:8080/EducadorEngineServices/Services/project/list?idEmpresa"+credentials.company.id)
           
            $scope.moduleId=$routeParams.idModule;
            idProject=$routeParams.idProject;
            $http.get("http://localhost:8080/EducadorEngineServices/Services/module/ejec/list?idSuscriptor="+userData.subscriberId+'&idModulo='+$routeParams.idModule)
            .success(function(data) {
                $scope.ejecuciones=data.responseBody.ejecuciones;
                         
            });
                  
        };//init
        
           $scope.createNew= function(moduleId){
               $http.get("http://localhost:8080/EducadorEngineServices/Services/module/new?idSuscriptor="+userData.subscriberId+'&idModulo='+moduleId)
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


