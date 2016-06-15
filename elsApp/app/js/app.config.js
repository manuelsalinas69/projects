
var app=angular.module('elsApp',['ngRoute']);

app.config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
    }
]);

app.config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
      $locationProvider.hashPrefix('!');

      $routeProvider.
        when('/', {
          template: '<login></login>'
        }).
        when('/home', {
          template: '<project></project>'
        }).
        when('/home/project/:idProject', {
          template: '<project-modules></project-modules>'
        }).
       when('/home/project/:idProject/modules/:idModule', {
          template: '<module-home></module-home>'
        }).
       when('/home/project/:idProject/modules/:idModule/form/:idExecution', {
          template: '<module-form></module-form>'
        }).
      when('/home/project/:idProject/modules/:idModule/form/:idExecution/:idDetail', {
          template: '<module-form></module-form>'
        }).
        otherwise('/');
    }
  ])

