'use strict';

/* Services */

app.factory('sessionManager', [ '$rootScope','$cookies','$http','$location',
  function($rootScope,$cookies,$http,$location) {
    return {
                               
        sessionStatus: function(sessionId){
            return $http.get(sessionCheckUrl+"?sid="+sessionId,
                        {
                             headers : {'sessionId':sessionId} 
                        })
            
                .then(function(result) {
                   return result.data;
                });                        
        },
        
        manageSessionStatus: function() {
            return this.sessionStatus(this.getSessionId());
        },

        getSessionData: function(key){
          return $cookies.get(key);                         
        },
        getSessionId(){
            return this.getSessionData("sessionId");                           
        },                           

        clearExpiredSessionData: function(){
            this.removeSessionData();
        },

        storeSessionData: function(data){

            $cookies.put('idEmpresa',data.responseBody.idEmpresa);
            $cookies.put('nombreEmpresa',data.responseBody.nombreEmpresa);
            $cookies.put('idSuscriptor',data.responseBody.idSuscriptor);
            $cookies.put('sessionId',data.responseBody.sessionId);
            $cookies.put('username',data.responseBody.userName);
            $rootScope.userName=data.responseBody.userName;
        },
        remoteSessiondata: function(){
            $cookies.removeAll();
            
        },
        logout: function(){
             return $http.get(logoutUrl+"?sid="+sessionId,
                        {
                             headers : {'sessionId':sessionId} 
                        })
            
                .then(function(result) {
                   return result.data;
                }); 
        }
  };
}]);