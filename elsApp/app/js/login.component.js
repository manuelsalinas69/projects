app.
  component('login', {
    transclude: true,
    templateUrl: 'login_elegant.html',
    controller:  function ($scope, $http,$location, $rootScope,$cookies, sessionManager,$exceptionHandler,$log) {
            
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
                    ).then(
                    function successCallback(response) {
                       //$log.log('Success Login');
                         if(response.data.responseBody.success){
                                                    sessionManager.storeSessionData(response.data);
                                                    $location.path('/home');
                                                 }
                     }, function errorCallback(response) {
                         // Well-handled error (details are logged)
                         $exceptionHandler('An error has occurred.\nHTTP error: ' + response.status + '(' + response.statusText + ')');
                         alert('An error has occurred.');
                     });

                };
      }
    
  });
