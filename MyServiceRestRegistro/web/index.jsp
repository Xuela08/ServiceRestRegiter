<%-- 
    Document   : index
    Created on : 13-abr-2015, 18:20:41
    Author     : Xuela
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Formulario</h1>
        <form action="procesar.do" method="post">
            Usuario: <input type="text" name="txtUsuario"/><br/>
            Nombre: <input type="text" name="txtNombre" /> <br/>
            Apellido: <input type="text" name="txtApellido"/><br/>
            Email: <input type="text" name="txtEmail" /> <br/>
            <input type="submit" value="Enviar datos"/>
        </form>
    </body>
</html>
