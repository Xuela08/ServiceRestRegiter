var app = angular.module('ClienteApp', [
    'ClienteApp.services',
    'ClienteApp.controllers',
    'ui.router',
    'ngCookies',
    'ngMaterial',
    'ngAnimate',
    'ngMdIcons',
    "brantwills.paging",
    "flow"
]);

//damos configuración de ruteo a nuestro sistema de login
app.config(function ($stateProvider, $urlRouterProvider) {


    $urlRouterProvider.otherwise('/login');
    $urlRouterProvider.when('/colaborativa', '/colaborativa/noticias');
    $stateProvider
            .state('base', {
                abstract: true,
                url: '',
                templateUrl: 'views/base.html',
                controller: 'baseController'
            })
            .state('login', {
                url: '/login',
                parent: 'base',
                templateUrl: 'views/login.html',
                controller: 'loginController'
            })
            .state('registro', {
                url: '/registro',
                parent: 'base',
                templateUrl: 'views/regis.html',
                controller: 'registroController'
            })
            .state('colaborativa', {
                url: '/colaborativa',
                parent: 'base',
                templateUrl: 'views/colaborativa.html',
                controller: 'colaborativaController'
            })
            .state('noticias', {
                url: '/noticias',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/noticia.html'
            })
            .state('noticiasTodas', {
                url: '/noticiasTodas',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/noticias/todasNoticias.html',
                controller: 'noticiasTodasController'
            })
            .state('noticiasNueva', {
                url: '/noticiasNueva',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/noticias/nueva.html',
                controller: 'noticiasNuevaController'
            })
            .state('noticiasId', {
                url: '/noticia/:noticiaId',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/noticias/noticia.html',
                controller: 'noticiaIdController'
            })
            .state('noticiasMias', {
                url: '/misNoticias',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/noticias/misNoticias.html',
                controller: 'noticiasMiasController'
            })
            .state('noticiasEditar', {
                url: '/noticiasEditar/:noticiaId',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/noticias/edita.html',
                controller: 'noticiasEditarController'
            })
            .state('apuntes', {
                url: '/apuntes',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/apunte.html'
            })
            .state('apuntesTodos', {
                url: '/apuntesTodos',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/apuntes/buscarApuntes.html',
                controller: 'ApuntesTodosController'
            })
            .state('apuntesSubir', {
                url: '/apuntesSubir',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/apuntes/subir.html',
                controller: 'ApuntesSubirController'
            })
            .state('apuntesId', {
                url: '/apunte/:apunteId',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/apuntes/apunte.html',
                controller: 'apunteIdController'
            })
            .state('misApuntes', {
                url: '/misApuntes',
                parent: 'colaborativa',
                templateUrl: 'views/colaborativa/apuntes/apuntesMios.html',
                controller: 'apuntesMiosController'
            });

}).config(function ($mdThemingProvider) {
    var pBlueMap = $mdThemingProvider.extendPalette('blue', {
        '500': '1976D2'
    });
    $mdThemingProvider.definePalette('pBlueRed', pBlueMap);
    $mdThemingProvider.theme('default')
            .primaryPalette('pBlueRed')
});
//  config(['$routeProvider', function ($routeProvider) {
//        $routeProvider.
//                when("/login", {templateUrl: "views/login.html", controller: "loginController"}).
//                when("/registro", {templateUrl: "views/registro.html", controller: "registroController"}).
//                when("/home", {templateUrl: "views/home.html", controller: "homeController"}).
//                when("/drivers/:id", {templateUrl: "views/driver.html", controller: "driverController"}).
//                otherwise({redirectTo: '/login'});
//    }]);

app.factory("auth", function ($cookies, $cookieStore, $location)
{
    return{
        login: function (data)
        {
            //creamos la cookie con el nombre que nos han pasado
            $cookies.idUsuario = data.idUsuario,
                    $cookies.token = data.token,
                    $cookies.caducidad = data.caducidad;

            $location.path('/colaborativa/noticias');
        },
        logout: function ()
        {
            //al hacer logout eliminamos la cookie con $cookieStore.remove
            $cookieStore.remove("idUsuario"),
                    $cookieStore.remove("caducidad"),
                    $cookieStore.remove("token");
            //mandamos al login
            $location.path("/login");
        },
        getCookie: function ()
        {
            return $cookies.token;
        },
        checkStatus: function ()
        {
            //creamos un array con las rutas que queremos controlar
            var rutasPrivadas = ["/colaborativa", "/registro", "/login"];
            if (this.in_array($location.path(), rutasPrivadas) && typeof ($cookies.token) == "undefined")
            {
                $location.path("/login");
            }
            //en el caso de que intente acceder al login y ya haya iniciado sesión lo mandamos a la home
            if (this.in_array("/login", rutasPrivadas) && typeof ($cookies.token) != "undefined")
            {
                $location.path("/colaborativa");
            }
        },
        in_array: function (needle, haystack)
        {
            var key = '';
            for (key in haystack)
            {
                if (haystack[key] == needle)
                {
                    return true;
                }
            }

            return false;
        }
    }
});

//ESTO NO VA BIEN POR HACERLO CON ui.router... buscar el modo para hacerlo de forma similar a ngRouter
//mientras corre la aplicación, comprobamos si el usuario tiene acceso a la ruta a la que está accediendo
//app.run(function ($rootScope, auth)
//{
//    alert("run");
//    //al cambiar de rutas
//    $rootScope.$on('$routeChangeStart', function ()
//    {
//        alert("run1");
//        //llamamos a checkStatus, el cual lo hemos definido en la factoria auth
//        //la cuál hemos inyectado en la acción run de la aplicación
//        auth.checkStatus();
//    })
//})

app.factory('verifyDelete', function ($mdDialog) {
    return{
        confirm: function (noticia)
        {
            var confirm = $mdDialog.confirm()
                    .title('Confirmaci\u00F3n para ' + noticia.titulo)
                    .content('\u00BFEst\u00E1s seguro de que deseas eliminar la noticia? ')
                    .ariaLabel('Eliminar')
                    .ok('Eliminar')
                    .cancel('Cancelar');
            return $mdDialog.show(confirm);
        }
    }
});
app.factory('verifyDeleteApunte', function ($mdDialog) {
    return{
        confirm: function (apunte)
        {
            var confirm = $mdDialog.confirm()
                    .title('Confirmaci\u00F3n para ' + apunte.datosGenerales.titulo)
                    .content('\u00BFEst\u00E1s seguro de que deseas eliminar la noticia? ')
                    .ariaLabel('Eliminar')
                    .ok('Eliminar')
                    .cancel('Cancelar');
            return $mdDialog.show(confirm);
        }
    }
});

