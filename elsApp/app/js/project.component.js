app.
  component('project', {
    templateUrl: 'project.html',
    controllerAs: 'projectController',
    controller:  function ($scope, $http,$location) {
    
    this.$onInit= function(){
        if(!userData.company.id){
             $location.path('/');
            return;
        }
        $http.get("http://localhost:8080/EducadorEngineServices/Services/project/list?idEmpresa="+userData.company.id)
            .success(function(data) {
                $scope.projects=data;
                         
            }); 
    };
    
    $scope.showModule= function(projectId){
        $location.path('/home/project/'+projectId);
    }
    
    
    }
    
  });
