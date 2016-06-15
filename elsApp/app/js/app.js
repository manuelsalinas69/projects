'use strict';



var userData={
    subscriberId:"",
    sessionId:"",
    token:"",
    company:{
           id:"",
           name:""
    }  
}

function setUserData(loginResult){
     userData.company.id=loginResult.responseBody.idEmpresa;
     userData.company.name=loginResult.responseBody.nombreEmpresa;
     userData.subscriberId=loginResult.responseBody.idSuscriptor;
}