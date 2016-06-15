app.
  component('projectModules', {
    templateUrl: 'project_modules.html',
  
    controller:
      function ($routeParams,$scope, $http,$location) {
        var idProject;
          
          this.$onInit= function(){
              idProject=$routeParams.idProject;
          //  $http.get("http://localhost:8080/EducadorEngineServices/Services/project/list?idEmpresa"+credentials.company.id)
            $http.get("http://localhost:8080/EducadorEngineServices/Services/module/list?idEmpresa="+userData.company.id+'&idProyecto='+$routeParams.idProject)
            .success(function(data) {
                $scope.modules=data;
                         
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
