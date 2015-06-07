angular.module('ClienteApp.services', [])
        .factory('personasAPIservice', function ($http) {

            var personasAPI = {};

            personasAPI.login = function ($scope) {
                var config = {
                    method: "POST",
                    headers: {authorization: "Basic "
                                + btoa($scope.username + ":"
                                        + $scope.password)},
                    url: "http://localhost:8080/ServiceRestRegiter/rest/acceso/login"
                }


                return $http(config);

            }

            personasAPI.registrar = function ($scope) {

                var config = {
                    method: "POST",
                    data: {usuario: $scope.usuario,
                        password: $scope.password,
                        nombre: $scope.nombre,
                        apellidos: $scope.apellidos,
                        genero: $scope.sexo,
                        email: $scope.usuario + "@red.ujaen.es",
                        administrador: false},
                    url: "http://localhost:8080/ServiceRestRegiter/rest/personas"
                }

                return $http(config);

            }

            personasAPI.logout = function ($cookies) {
                return $http({
                    method: 'DELETE',
                    url: 'http://localhost:8080/ServiceRestRegiter/rest/acceso/logout/' + $cookies.token 
                });
            }

            return personasAPI;
        })
        .factory('noticiasAPIservice', function ($http, $cookies) {

            var noticiasAPI = {};

            noticiasAPI.getTotal = function () {
                var config = {
                    method: "GET",
                    headers: {Token: $cookies.token},
                    url: 'http://localhost:8080/ServiceRestRegiter/rest/noticias/tam'
                }


                return $http(config);

            }

            noticiasAPI.getNoticias = function (pagina, tamPagina) {
                var config = {
                    method: "GET",
                    headers: {Token: $cookies.token},
                    url: 'http://localhost:8080/ServiceRestRegiter/rest/noticias?inicio=' + pagina + '&tamPagina=' + tamPagina
                }


                return $http(config);

            }
            
            noticiasAPI.addNoticia = function ($scope) {
                var descripcion = $scope.noticia.descripcion.replace(new RegExp("\n","g"), "<br>");
                var config = {
                    method: "POST",
                    headers: {Token: $cookies.token},
                    data: {
                        titulo: $scope.noticia.titulo,
                        descripcion: descripcion,
                        urlImagen: $scope.noticia.urlImagen,
                        propietario: $cookies.idUsuario
                    },
                    url: "http://localhost:8080/ServiceRestRegiter/rest/noticias"
                }

                return $http(config);

            }
            
            noticiasAPI.getNoticiaId = function (id) {
                var config = {
                    method: "GET",
                    headers: {Token: $cookies.token},
                    url: 'http://localhost:8080/ServiceRestRegiter/rest/noticias/' + id
                }


                return $http(config);

            }
            
            noticiasAPI.getNoticiaMias = function () {
                var config = {
                    method: "GET",
                    headers: {Token: $cookies.token},
                    url: 'http://localhost:8080/ServiceRestRegiter/rest/noticias/propietario?usuario=' + $cookies.idUsuario
                }
                
                
                return $http(config);

            }
            
            noticiasAPI.editNoticia = function ($scope) {
                var descripcion = $scope.noticia.descripcion.replace(new RegExp("\n","g"), "<br>");
                var config = {
                    method: "PUT",
                    headers: {Token: $cookies.token},
                    data: {
                        titulo: $scope.noticia.titulo,
                        descripcion: descripcion,
                        urlImagen: $scope.noticia.urlImagen,
                        propietario: $cookies.idUsuario
                    },
                    url: 'http://localhost:8080/ServiceRestRegiter/rest/noticias/' + $scope.noticia.id
                }

                return $http(config);

            }
            
            noticiasAPI.eliminarNoticia = function (noticia) {
                var config = {
                    method: "DELETE",
                    headers: {Token: $cookies.token},
                    url: 'http://localhost:8080/ServiceRestRegiter/rest/noticias/' + noticia.id
                }

                return $http(config);

            }

            return noticiasAPI;
        });