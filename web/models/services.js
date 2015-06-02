angular.module('ClienteApp.services', [])
        .factory('ergastAPIservice', function ($http) {

            var ergastAPI = {};

            ergastAPI.login = function ($scope) {
                var config = {
                    method: "POST",
                    headers: {authorization: "Basic "
                            + btoa($scope.username + ":"
                                    + $scope.password)},
                    url: "http://localhost:8080/ServiceRestRegiter/rest/acceso/login"
                }


                return $http(config);

            }
            
            ergastAPI.registrar = function ($scope) {
                
                var config = {
                    method: "POST",
                    data: {usuario: $scope.usuario, 
                        password: $scope.password, 
                        nombre: $scope.nombre,
                        apellidos: $scope.apellidos,
                        genero: $scope.sexo,
                        email: $scope.usuario+"@red.ujaen.es",
                        administrador: false},
                    url: "http://localhost:8080/ServiceRestRegiter/rest/personas"
                }

                return $http(config);

            }


            ergastAPI.getDrivers = function () {
                return $http({
                    method: 'JSONP',
                    url: 'http://ergast.com/api/f1/2013/driverStandings.json?callback=JSON_CALLBACK'
                });
            }

            ergastAPI.getDriverDetails = function (id) {
                return $http({
                    method: 'JSONP',
                    url: 'http://ergast.com/api/f1/2013/drivers/' + id + '/driverStandings.json?callback=JSON_CALLBACK'
                });
            }

            ergastAPI.getDriverRaces = function (id) {
                return $http({
                    method: 'JSONP',
                    url: 'http://ergast.com/api/f1/2013/drivers/' + id + '/results.json?callback=JSON_CALLBACK'
                });
            }

            return ergastAPI;
        });