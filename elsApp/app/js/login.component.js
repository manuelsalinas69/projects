app.
  component('login', {
    transclude: true,
    templateUrl: 'login.html',
    controller:  function ($scope, $http,$location) {
                var loginResult; 
                $scope.submitForm = function() {
                    // Posting data to php file
                    
                    var fd= new FormData();
                     fd.append('user',$scope.username);
                     fd.append('pass',$scope.password);

                     $http.post('http://localhost:8080/EducadorEngineServices/Services/login',"user="+encodeURIComponent($scope.username)+
                                                                                              "&pass="+encodeURIComponent($scope.password),
                        {
                         headers : {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8','Access-Control-Allow-Origin': '*'} 
                        }
                    ).success(function(data) {
                         loginResult=data;
                         if(loginResult.responseBody.success){
                            setUserData(loginResult);
                             $location.path('/home');
                        }
                     });

                };
      }
    
  });
