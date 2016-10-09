'use strict';

/* Services */

app.factory('sessionManager', [ '$rootScope','$cookies','$http','$location',
  function($rootScope,$cookies,$http,$location) {
    return {
                               
        sessionStatus: function(sessionId){
            return $http.get(sessionCheckUrl+"?sid="+sessionId)
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
            $cookies.put('username',data.responseBody.userName);
        },
        remoteSessiondata: function(){
            $cookies.removeAll();
            
        }    
  };
}]);