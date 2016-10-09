app.
  component('login', {
    transclude: true,
    templateUrl: 'login_elegant.html',
    controller:  function ($scope, $http,$location, $rootScope,$cookies, sessionManager ) {
            
         this.$onInit= function(){
            $('.blockingDiv').hide(); 
         }
        
        $scope.submitForm = function() {
                    // Posting data to php file
                    
                    var fd= new FormData();
                     fd.append('user',$scope.username);
                     fd.append('pass',$scope.password);

                     $http.post(loginUrl,"user="+encodeURIComponent($scope.username)+
                                                                                              "&pass="+encodeURIComponent($scope.password),
                        {
                         headers : {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8','Access-Control-Allow-Origin': '*'} 
                        }
                    ).success(function(data) {
                         if(data.responseBody.success){
                           sessionManager.storeSessionData(data);
                           $location.path('/home');
                        }
                     });

                };
      }
    
  });
