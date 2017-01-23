app.
  component('login', {
    transclude: true,
    templateUrl: 'login.html',
    controller:  function ($scope, $http,$location, $rootScope,$cookies, sessionManager,$exceptionHandler,$log,$timeout) {
            
         this.$onInit= function(){
            $('.blockingDiv').hide(); 
             sessionManager.remoteSessiondata();
         }
        
        $scope.submitForm = function() {
                    // Posting data to php file
                    $('.blockingDiv').show(); 

                    var loginPromises=sessionManager.login($scope.username,$scope.password);
                    loginPromises.then(function(response){
                        if(response.data.responseBody.success){
                                                            sessionManager.storeSessionData(response.data);
                                                            
                                                                $timeout(function(){
                                                                    $location.path('/home')
                                                                }
                                                                       
                                                                       , 2000);
                                                           
                                                         }

                    });
            
                     /*$http.post(loginUrl,"user="+encodeURIComponent($scope.username)+
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
                     });*/

                
      }
    
    }//controller
});
