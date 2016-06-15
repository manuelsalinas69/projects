app.component('credentials',{
     require: {
        login: '^login'
    },
    controller: function(){
                    var subscriberId;
                    var company={
                       var id;
                       var name;
                    }  
                    this.$onInit= function(){
                        company.id=login.loginResult.responseBody.idEmpresa;
                        company.name=login.loginResult.responseBody.nombreEmpresa;
                        subscriberId=login.loginResult.responseBody.idSuscriptor;
                    }    

    },
});