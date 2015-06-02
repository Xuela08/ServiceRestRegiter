var app = angular.module('ClienteApp', [
    'ClienteApp.services',
    'ClienteApp.controllers',
    'ui.router',
    'ngCookies',
    'ngMaterial',
    'ngAnimate',
    'ngMdIcons'
]);

//damos configuración de ruteo a nuestro sistema de login
app.config(function ($stateProvider, $urlRouterProvider) {


    $urlRouterProvider.otherwise('/login');
    $urlRouterProvider.when('/inicio', '/inicio/noticias');
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
                templateUrl: 'views/registro.html',
                controller: 'registroController'
            })
            .state('inicio', {
                url: '/inicio',
                parent: 'base',
                templateUrl: 'views/inicio.html',
                controller: 'inicioController'
            })
            .state('noticias', {
                url: '/noticias',
                parent: 'inicio',
                templateUrl: 'views/inicio/noticias.html'
            });

}).config(function ($mdThemingProvider) {
    var neonRedMap = $mdThemingProvider.extendPalette('blue', {
        '500': '1976D2'
    });
    $mdThemingProvider.definePalette('neonRed', neonRedMap);
    $mdThemingProvider.theme('default')
            .primaryPalette('neonRed')
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

            $location.path('/inicio/noticias');
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
            var rutasPrivadas = ["/inicio", "/registro", "/login"];
            if (this.in_array($location.path(), rutasPrivadas) && typeof ($cookies.token) == "undefined")
            {
                $location.path("/login");
            }
            //en el caso de que intente acceder al login y ya haya iniciado sesión lo mandamos a la home
            if (this.in_array("/login", rutasPrivadas) && typeof ($cookies.token) != "undefined")
            {
                $location.path("/inicio");
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