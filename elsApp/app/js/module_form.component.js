app.
  component('moduleForm', {
    templateUrl: 'module_form.html',
  
    controller: function ($routeParams,$scope, $http,$location) {
                var fillForm= function(data){
                    $scope.respuestaAbierta="";
                    $scope.idRespuesta="";
                    $scope.formulario=data.responseBody.formulario;
                    $scope.isFinal=$scope.formulario.final;
                    $scope.isEval=$scope.formulario.formType=='EVALUACION';       
                    if($scope.isEval){
                        if($scope.formulario.evaluacion.estadoEvaluacion=='RESPONDIDO'){
                            if($scope.formulario.evaluacion.preguntaAbierta){
                                $scope.respuestaAbierta=$scope.formulario.evaluacion.respuesta;
                            }
                            else{
                                $scope.idRespuesta=$scope.formulario.evaluacion.idRespuesta+"";
                            }
                        }   
                    }
            
                

           }
        var idModule;
        var idProject;
        this.$onInit= function(){
          //  $http.get("http://localhost:8080/EducadorEngineServices/Services/project/list?idEmpresa"+credentials.company.id)
           idModule=$routeParams.idModule;
            idProject=$routeParams.idProject;
            if($routeParams.idDetail){
                $http.get('http://localhost:8080/EducadorEngineServices/Services/module/ejec/'+$routeParams.idExecution+'/'+$routeParams.idDetail)
                .success(function(data) {
                        fillForm(data);

                    });
            }
            else{
                $http.get('http://localhost:8080/EducadorEngineServices/Services/module/ejec/'+$routeParams.idExecution)
                .success(function(data){
                    
                     fillForm(data);

                    });
                }
            };//init
        
            
        
           $scope.sendResponse= function(){
                $('#requiredInputMessage').hide();
                $('#requiredSelectedMessage').hide();
                    var formParameters;
                    if($scope.isEval){
                        if($scope.formulario.evaluacion.preguntaAbierta){
                            if(!$scope.respuestaAbierta){
                                $('#requiredInputMessage').show();
                                return;
                            }
                        }
                        else{
                             if(!$scope.idRespuesta){
                                $('#requiredSelectedMessage').show();
                                return;
                            }
                        }
                        formParameters= "idEvaluacion="+encodeURIComponent( $scope.formulario.evaluacion.idEvaluacion)+
                                "&idPregunta="+encodeURIComponent( $scope.formulario.evaluacion.idPregunta);
                        if($scope.formulario.evaluacion.preguntaAbierta){
                            formParameters=formParameters+"&respuesta="+encodeURIComponent( $scope.respuestaAbierta);
                        }
                        else{
                            formParameters=formParameters+ "&idRespuesta="+encodeURIComponent( $scope.idRespuesta);
                        }
                    }
                    else{
                        formParameters="";
                    }
                     $http.post('http://localhost:8080/EducadorEngineServices/Services/module/ejec/'+$scope.formulario.idEjecucion+'/'+$scope.formulario.idDetalleEjecucion,
                             
                        formParameters,
                        {
                         headers : {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8','Access-Control-Allow-Origin': '*'} 
                        }
                    ).success(function(data) {
                        if($scope.isFinal){
                            $location.path('/home');
                            return;
                        }
                        fillForm(data);
                     });
            }
           
            $scope.resumeExecByOrder= function(order){
                if(order){
              
                  $http.get('http://localhost:8080/EducadorEngineServices/Services/module/ejec/'+$scope.formulario.idEjecucion+'?orden='+order)
                    .success(function(data){

                         fillForm(data);

                        });
                }
                else{
                    $http.get('http://localhost:8080/EducadorEngineServices/Services/module/ejec/'+$scope.formulario.idExecution)
                        .success(function(data){
                                            
                    
                        fillForm(data);

                    });
                }
                  
               
            }
            
            $scope.back= function(){
                  $location.path('/home/project/'+idProject+'/modules/'+idModule);
               
            }
           
          
    
      }
        
    
  });
