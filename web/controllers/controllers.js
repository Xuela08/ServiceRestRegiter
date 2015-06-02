angular.module('ClienteApp.controllers', []).
        controller('baseController', function (auth) {
            auth.checkStatus();
        }).
        controller('inicioController', function ($scope, $state, auth) {
            
            $scope.$state = $state;
            $scope.logout = function ()
            {
                auth.logout();
            };
        }).
        controller('loginController', function ($scope, ergastAPIservice, auth) {
            $scope.progressVisibility = false;
            $scope.errorVisibility = false;
            $scope.login = function ()
            {
                $scope.progressVisibility = true;
                var response = ergastAPIservice.login($scope);
                response.success(json = function (data, status, headers, config) {
                    $scope.data = data;
                    if ($scope.data != null && status == "201") {
                        auth.login(data);
                    }
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    if (status == "401") {
                        $scope.errorVisibility = true;
                    }
                    if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    $scope.progressVisibility = false;
                });
            }
        }).
        controller('registroController', function ($scope, ergastAPIservice, auth) {
            //la función login que llamamos en la vista llama a la función
            //login de la factoria auth pasando lo que contiene el campo
            //de texto del formulario
            $scope.progressVisibility = false;
            $scope.correctVisibility = false;
            $scope.sexo = "Hombre";
            $scope.registro = function ()
            {
                $scope.progressVisibility = true;
                if ($scope.password == $scope.password2) {
                    var response = ergastAPIservice.registrar($scope);
                    response.success(json = function (data, status, headers, config) {
                        $scope.data = data;
                        $scope.status = status;
                        $scope.correctVisibility = true;
                        $scope.progressVisibility = false;
                    });
                    response.error(function (data, status, headers, config) {
                        $scope.data = data;
                        if (status == "401") {
                            alert("No estás autorizado");
                        }
                        if (status == "404") {
                            alert("Servicio no encontrado, hable con administracion");
                        }
                        if (status == "500") {
                            alert("Error con el servidor, intentelo mas tarde");
                        } else {
                            alert("Ha ocurrido un error inesperado");
                        }
                        $scope.progressVisibility = false;
                    });
                } else {
                    $scope.progressVisibility = false;
                    alert("Los campos password difieren, introduzca los campos de nuevo");
                    $scope.password = "";
                    $scope.password2 = "";
                }
            }
        }).
        controller('noticiasController', function ($scope, ergastAPIservice, $cookies, auth) {


            $scope.nameFilter = null;
            $scope.driversList = [];
            $scope.idUsuario = $cookies.idUsuario;
            $scope.token = $cookies.token;
            //la función logout que llamamos en la vista llama a la función
            //logout de la factoria auth
            $scope.logout = function ()
            {
                auth.logout();
            };

            $scope.searchFilter = function (driver) {
                var re = new RegExp($scope.nameFilter, 'i');
                return !$scope.nameFilter || re.test(driver.Driver.givenName) || re.test(driver.Driver.familyName);
            };

            ergastAPIservice.getDrivers().success(function (response) {
                //Digging into the response to get the relevant data
                $scope.driversList = response.MRData.StandingsTable.StandingsLists[0].DriverStandings;
            });
        }).
        /* Driver controller */
        controller('driverController', function ($scope, $routeParams, ergastAPIservice) {
            $scope.id = $routeParams.id;
            $scope.races = [];
            $scope.driver = null;

            ergastAPIservice.getDriverDetails($scope.id).success(function (response) {
                $scope.driver = response.MRData.StandingsTable.StandingsLists[0].DriverStandings[0];
            });

            ergastAPIservice.getDriverRaces($scope.id).success(function (response) {
                $scope.races = response.MRData.RaceTable.Races;
            });
        });