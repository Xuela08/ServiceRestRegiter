angular.module('ClienteApp.controllers', []).
        controller('baseController', function (auth) {
            auth.checkStatus();
        }).
        controller('colaborativaController', function ($scope, $state, auth, personasAPIservice, $cookies) {
            $scope.mostrarMenu=false;
            $scope.$state = $state;
            $scope.logout = function ()
            {
                var response = personasAPIservice.logout($cookies);
                response.success(json = function (data, status, headers, config) {
                    $scope.data = data;
                    auth.logout();
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No se completo correctamente el logout");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            };
            $scope.showMenu = function ()
            {
                if($scope.mostrarMenu){
                    document.getElementById("menuRight").style.display = "none";
                    $scope.mostrarMenu=false;
                }else{
                    document.getElementById("menuRight").style.display = "block";
                    $scope.mostrarMenu=true;
                }
                
            };
        }).
        controller('loginController', function ($scope, personasAPIservice, auth) {
            $scope.progressVisibility = false;
            $scope.errorVisibility = false;
            $scope.login = function ()
            {
                $scope.progressVisibility = true;
                var response = personasAPIservice.login($scope);
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
        controller('registroController', function ($scope, personasAPIservice) {
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
                    var response = personasAPIservice.registrar($scope);
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
        controller('noticiasTodasController', function ($scope, noticiasAPIservice, auth) {
            $scope.currentPage = 1;
            $scope.pageSize = 2;
            $scope.total = 0;
            var response = noticiasAPIservice.getNoticias(1, $scope.pageSize);
            response.success(json = function (data, status, headers, config) {
                $scope.todos = data;
                for (i = 0; i < data.length; i++) {
                    while ($scope.todos[i].descripcion.toString().indexOf("<br>") != -1) {
                        $scope.todos[i].descripcion = data[i].descripcion.replace(new RegExp("<br>"), " ");
                    }
                }
                $scope.status = status;
                //------Hemos obtenido las noticias ahora paginamos------
                var response2 = noticiasAPIservice.getTotal();
                response2.success(json = function (data, status, headers, config) {
                    $scope.total = data;
                });
                response2.error(function (data, status, headers, config) {
                    alert("Paginacion error: " + status);
                });
                //----------------------------------------------------------

            });
            response.error(function (data, status, headers, config) {
                $scope.data = data;
                alert("incorrecto" + status);
                if (status == "401") {
                    alert("No estás autorizado");
                    auth.logout();
                }
                else if (status == "404") {
                    alert("Servicio no encontrado, hable con administracion");
                }
                else if (status == "500") {
                    alert("Error con el servidor, intentelo mas tarde");
                }
                else {
                    alert("Ha ocurrido un error inesperado");
                }
            });

            $scope.cambioPagina = function () {
                var response = noticiasAPIservice.getNoticias($scope.currentPage, $scope.pageSize);
                response.success(json = function (data, status, headers, config) {
                    $scope.todos = data;
                    $scope.status = status;
                    $scope.todos = data;
                    for (i = 0; i < data.length; i++) {
                        while ($scope.todos[i].descripcion.toString().indexOf("<br>") != -1) {
                            $scope.todos[i].descripcion = data[i].descripcion.replace(new RegExp("<br>"), " ");
                        }
                    }
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            }
        }).
        controller('noticiasNuevaController', function ($scope, noticiasAPIservice, $cookies, auth, $location) {
            $scope.idUsuario = $cookies.idUsuario;
            $scope.valIMG = false;
//            $scope.textoFun = function () {
//                alert("aqui entra");
//                $scope.texto = $scope.noticia.descripcion.replace(new RegExp("\n", "g"), "<br>");
//                $scope.arrayTexto = $scope.texto.split("<br>")
//                alert("reemplazado");
//            }
            //Proceso para pasar la imagen a base64 para enviar por json string
            $scope.imageStrings = [];
            $scope.processFiles = function (files) {
                $scope.valIMG = true;
                $scope.base64 = null;
                angular.forEach(files, function (flowFile, i) {
                    var fileReader = new FileReader();
                    fileReader.onload = function (event) {
                        $scope.base64 = event.target.result;
                        if ($scope.base64.length > 8388608) {
                            alert("Imagen muy pesada");
                            $scope.base64 = null;
                        }

                    };
                    fileReader.readAsDataURL(flowFile.file);
                });
            };
            $scope.noIMG = function () {
                $scope.base64 = null;
                $scope.valIMG = false;
            };
            //-----------------------------------------------------------------------
            $scope.submitNoticias = function () {
                var response = noticiasAPIservice.addNoticia($scope);
                response.success(json = function (data, status, headers, config) {
                    $scope.noticia = data;
                    $scope.status = status;
                    //devería llevar a noticia con esa id
                    $location.path('/colaborativa/noticia/' + data.id);
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            }
        }).
        controller('noticiaIdController', function ($scope, noticiasAPIservice, auth, $stateParams) {

            var response = noticiasAPIservice.getNoticiaId($stateParams.noticiaId);
            response.success(json = function (data, status, headers, config) {
                $scope.noticia = data;
                $scope.arrayDescripcion = data.descripcion.split("<br>");
                $scope.status = status;
            });
            response.error(function (data, status, headers, config) {
                $scope.data = data;
                alert("incorrecto" + status);
                if (status == "401") {
                    alert("No estás autorizado");
                    auth.logout();
                }
                else if (status == "404") {
                    alert("Servicio no encontrado, hable con administracion");
                }
                else if (status == "500") {
                    alert("Error con el servidor, intentelo mas tarde");
                }
                else {
                    alert("Ha ocurrido un error inesperado");
                }
            });
        }).
        controller('noticiasMiasController', function ($scope, noticiasAPIservice, auth, verifyDelete, $location) {

            $scope.cargarNoticias = function () {
                var response = noticiasAPIservice.getNoticiaMias();
                response.success(json = function (data, status, headers, config) {
                    $scope.noticias = data;
                    $scope.status = status;
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            }
            $scope.cargarNoticias();

            $scope.borrarNoticias = function (noticia) {
                verifyDelete.confirm(noticia).then(function () {
                    var responseDelete = noticiasAPIservice.eliminarNoticia(noticia);
                    responseDelete.success(json = function (data, status, headers, config) {
                        $scope.noticias = data;
                        $scope.status = status;

                        $scope.cargarNoticias();
                    });
                    responseDelete.error(function (data, status, headers, config) {
                        $scope.data = data;
                        if (status == "401") {
                            alert("No estás autorizado");
                            auth.logout();
                        }
                        else if (status == "404") {
                            alert("Servicio no encontrado, hable con administracion");
                        }
                        else if (status == "500") {
                            alert("Error con el servidor, intentelo mas tarde");
                        }
                        else {
                            alert("Ha ocurrido un error inesperado");
                        }
                    });
                });
            }


        }).
        controller('noticiasEditarController', function ($scope, noticiasAPIservice, auth, $stateParams, $location) {
            $scope.valIMG = true;
            var response = noticiasAPIservice.getNoticiaId($stateParams.noticiaId);
            response.success(json = function (data, status, headers, config) {
                $scope.noticia = data;
                while ($scope.noticia.descripcion.toString().indexOf("<br>") != -1) {
                    $scope.noticia.descripcion = $scope.noticia.descripcion.replace(new RegExp("<br>"), "\n");
                }
                $scope.base64 = data.baseIMG;
                $scope.status = status;
            });
            response.error(function (data, status, headers, config) {
                $scope.data = data;
                alert("incorrecto" + status);
                if (status == "401") {
                    alert("No estás autorizado");
                    auth.logout();
                }
                else if (status == "404") {
                    alert("Servicio no encontrado, hable con administracion");
                }
                else if (status == "500") {
                    alert("Error con el servidor, intentelo mas tarde");
                }
                else {
                    alert("Ha ocurrido un error inesperado");
                }
            });
            $scope.imageStrings = [];
            $scope.processFiles = function (files) {
                $scope.valIMG = true;
                $scope.base64 = null;
                angular.forEach(files, function (flowFile, i) {
                    var fileReader = new FileReader();
                    fileReader.onload = function (event) {
                        $scope.base64 = event.target.result;
                        if ($scope.base64.length > 1048576) {
                            alert("Imagen muy pesada");
                            $scope.base64 = null;
                            $scope.valIMG = false;
                        }

                    };
                    fileReader.readAsDataURL(flowFile.file);
                });
            };
            $scope.noIMG = function () {
                $scope.base64 = null;
                $scope.valIMG = false;
            };
            $scope.submitNoticias = function () {
                var response = noticiasAPIservice.editNoticia($scope);
                response.success(json = function (data, status, headers, config) {
                    $scope.noticia = data;
                    $scope.status = status;
                    $location.path('colaborativa/misNoticias');
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            }
            $scope.cancelarNoticias = function () {
                $location.path('colaborativa/misNoticias');
            }

        }).
        controller('ApuntesTodosController', function ($scope, apuntesAPIservice, auth) {
            $scope.visibilidadCarreras = "block";
            $scope.visibilidadCursos = "none";
            $scope.visibilidadAsignaturas = "none";
            $scope.visibilidadApuntes = "none";
            $scope.filC = "";
            $scope.filteri18n = function (array, expression) {
                var filtra = [];
                $scope.filtrado = array;
                /** Transforma el texto quitando todos los acentos diéresis, etc. **/
                function normalize(texto) {
                    //texto = texto.replace(/[áàäâ]/g, "a");
                    texto = texto.replace(/[\u00e1\u00c1]/g, "a");
                    //texto = texto.replace(/[éèëê]/g, "e");
                    texto = texto.replace(/[\u00e9\u00c9]/g, "e");
                    //texto = texto.replace(/[íìïî]/g, "i");
                    texto = texto.replace(/[\u00ed\u00cd]/g, "i");
                    //texto = texto.replace(/[óòôö]/g, "o");
                    texto = texto.replace(/[\u00f3\u00d3]/g, "o");
                    //texto = texto.replace(/[úùüü]/g, "u");
                    texto = texto.replace(/[\u00fa\u00da]/g, "u");
                    texto = texto.toUpperCase();
                    return texto;
                }

                /** Esta función es el comparator en el filter **/
                function comparador(actual, expected) {
                    if (normalize(actual).indexOf(normalize(expected)) >= 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
                var index;
                for (index = 0; index < array.length; index++) {
                    if (comparador(array[index], expression)) {
                        filtra.push(array[index]);
                    }
                }

                $scope.filtrado = filtra;
            }
            $scope.cargarCarreras = function () {
                var response = apuntesAPIservice.getCarreras();
                response.success(json = function (data, status, headers, config) {
                    $scope.carreras = data;
                    $scope.filtrado = data;
                    $scope.status = status;
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            };
            $scope.cargarCarreras();
            //cambiamos a cursos
            $scope.carreraSeleccionada = function (c) {
                $scope.carreraSeleccionada = c;
                $scope.visibilidadCarreras = "none";
                $scope.visibilidadCursos = "block";
                var response1 = apuntesAPIservice.getCursos($scope.carreraSeleccionada);
                response1.success(json = function (data, status, headers, config) {
                    $scope.cursos = data;
                    $scope.status = status;
                });
                response1.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            };
            $scope.cursoSeleccionado = function (c) {
                $scope.cursoSeleccionado = c;
                $scope.visibilidadCursos = "none";
                $scope.visibilidadAsignaturas = "block";
                var response1 = apuntesAPIservice.getAsignatura($scope.carreraSeleccionada, $scope.cursoSeleccionado);
                response1.success(json = function (data, status, headers, config) {
                    $scope.asignaturas = data;
                    $scope.status = status;
                });
                response1.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            };
            $scope.asignaturaSeleccionada = function (c) {
                $scope.asignaturaSeleccionada = c;
                $scope.visibilidadAsignaturas = "none";
                $scope.visibilidadApuntes = "block";
                var response1 = apuntesAPIservice.getApuntes($scope.carreraSeleccionada, $scope.cursoSeleccionado, $scope.asignaturaSeleccionada);
                response1.success(json = function (data, status, headers, config) {
                    $scope.listApuntes = data;
                    $scope.status = status;
                });
                response1.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            };
        }).
        controller('ApuntesSubirController', function ($scope, $cookies, apuntesAPIservice, auth, $location) {
            $scope.idUsuario = $cookies.idUsuario;
            $scope.mostrarCurso = true;
            var response = apuntesAPIservice.getCarreras();
            response.success(json = function (data, status, headers, config) {
                $scope.carreras = data;
                $scope.status = status;
            });
            response.error(function (data, status, headers, config) {
                $scope.data = data;
                alert("incorrecto" + status);
                if (status == "401") {
                    alert("No estás autorizado");
                    auth.logout();
                }
                else if (status == "404") {
                    alert("Servicio no encontrado, hable con administracion");
                }
                else if (status == "500") {
                    alert("Error con el servidor, intentelo mas tarde");
                }
                else {
                    alert("Ha ocurrido un error inesperado");
                }
            });
            $scope.obtenerCursosCarrera = function (carrera) {
                var response1 = apuntesAPIservice.getCursos(carrera);
                response1.success(json = function (data, status, headers, config) {
                    $scope.cursos = data;
                    $scope.status = status;
                });
                response1.error(function (data, status, headers, config) {
                    $scope.data = data;
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                });
            };
            $scope.obtenerAsignaturaCarrera = function (carrera, curso) {
                var response1 = apuntesAPIservice.getAsignatura(carrera, curso);
                response1.success(json = function (data, status, headers, config) {
                    $scope.asignaturas = data;
                    $scope.status = status;
                });
                response1.error(function (data, status, headers, config) {
                    $scope.data = data;
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                });
            };
            $scope.submitApunte = function () {
                var response1 = apuntesAPIservice.addApunte($scope);
                response1.success(json = function (data, status, headers, config) {
                    $scope.noticia = data;
                    $scope.status = status;
                    $location.path('/colaborativa/apuntes');
                });
                response1.error(function (data, status, headers, config) {
                    $scope.data = data;
                    alert("incorrecto" + status);
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            };
        }).
        controller('apunteIdController', function ($scope, apuntesAPIservice, auth, $stateParams) {
            var response = apuntesAPIservice.getApunte($stateParams.apunteId);
            response.success(json = function (data, status, headers, config) {
                $scope.apunte = data;
                var dateCreacion = new Date();
                dateCreacion.setTime(data.datosGenerales.f_creacion);
                $scope.f_creacion = dateCreacion.toString().split(" ");


                $scope.status = status;
            });
            response.error(function (data, status, headers, config) {
                $scope.data = data;
                alert("incorrecto" + status);
                if (status == "401") {
                    alert("No estás autorizado");
                    auth.logout();
                }
                else if (status == "404") {
                    alert("Servicio no encontrado, hable con administracion");
                }
                else if (status == "500") {
                    alert("Error con el servidor, intentelo mas tarde");
                }
                else {
                    alert("Ha ocurrido un error inesperado");
                }
            });
        }).
        controller('apuntesMiosController', function ($scope, apuntesAPIservice, auth, verifyDeleteApunte, $location) {
            
            $scope.cargarApuntes = function () {
                var response = apuntesAPIservice.getApunteMios();
                response.success(json = function (data, status, headers, config) {
                    $scope.apuntes = data;
                    $scope.status = status;
                });
                response.error(function (data, status, headers, config) {
                    $scope.data = data;
                    if (status == "401") {
                        alert("No estás autorizado");
                        auth.logout();
                    }
                    else if (status == "404") {
                        alert("Servicio no encontrado, hable con administracion");
                    }
                    else if (status == "500") {
                        alert("Error con el servidor, intentelo mas tarde");
                    }
                    else {
                        alert("Ha ocurrido un error inesperado");
                    }
                });
            }
            $scope.cargarApuntes();

            $scope.borrarapunte = function (apunte) {
                verifyDeleteApunte.confirm(apunte).then(function () {
                    var responseDelete = apuntesAPIservice.eliminarApunte(apunte.id);
                    responseDelete.success(json = function (data, status, headers, config) {
                        $scope.noticias = data;
                        $scope.status = status;

                        $scope.cargarApuntes();
                    });
                    responseDelete.error(function (data, status, headers, config) {
                        $scope.data = data;
                        if (status == "401") {
                            alert("No estás autorizado");
                            auth.logout();
                        }
                        else if (status == "404") {
                            alert("Servicio no encontrado, hable con administracion");
                        }
                        else if (status == "500") {
                            alert("Error con el servidor, intentelo mas tarde");
                        }
                        else {
                            alert("Ha ocurrido un error inesperado");
                        }
                    });
                });
            }


        });
