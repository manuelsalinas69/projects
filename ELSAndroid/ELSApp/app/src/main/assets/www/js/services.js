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
        getSessionId: function(){
            return this.getSessionData("sessionId");                           
        }, 
         isSessionId: function(){
           if(this.getSessionId()){
               return true;
           }
           return false;
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
            $rootScope.empresa=data.responseBody.nombreEmpresa;
        },
        remoteSessiondata: function(){
            
            $cookies.remove('idEmpresa');
            $cookies.remove('nombreEmpresa');
            $cookies.remove('idSuscriptor');
            $cookies.remove('sessionId');
            $cookies.remove('username');
            
        },
        logout: function(){
             return $http.get(logoutUrl+"?sid="+this.getSessionId(),
                        {
                             headers : {'sessionId':this.getSessionId()} 
                        })
            
                .then(function(result) {
                   return result.data;
                }); 
        },
        
        login: function(username,password){
             return  $http.post(loginUrl,"user="+encodeURIComponent(username)+"&pass="+encodeURIComponent(password),
                        {
                         headers : {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8','Access-Control-Allow-Origin': '*'} 
                        }
                    )
                 .then(function(result) {
                   return result;
                });
        }
  };
}]);