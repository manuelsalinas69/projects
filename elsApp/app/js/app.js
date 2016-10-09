'use strict';
var protocol="http://"
var host="localhost:8080";
var rootTree="/EducadorEngineServices";
var servicesRootTree=rootTree+"/services";
var surveyRootTree=servicesRootTree+"/survey";
var authenticationTree=servicesRootTree+"/authentication";
var loginRootTree=authenticationTree+"/login";
var sessionRootTree=authenticationTree+"/session";
var sessionCheckRootTree=sessionRootTree+"/check";

var sessionCheckUrl=protocol+host+sessionCheckRootTree;
var surveyUrl=protocol+host+surveyRootTree;
var loginUrl=protocol+host+loginRootTree;
//proyect
var projectUrl=surveyUrl+"/project";
var projectListUrl=projectUrl+"/list";

//modules
var moduleUrl=surveyUrl+"/module";
var moduleListUrl=moduleUrl+"/list";
var moduleEjecUrl=moduleUrl+"/ejec";
var moduleEjecListUrl=moduleEjecUrl+"/list";
var moduleNewUrl=moduleUrl+"/new";


/*
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
}*/
