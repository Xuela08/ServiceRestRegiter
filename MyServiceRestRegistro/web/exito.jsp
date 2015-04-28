<%-- 
    Document   : exito
    Created on : 13-abr-2015, 18:37:55
    Author     : Xuela
--%>
<%@page import="es.tfg.modelo.Persona"%>
<%
    Persona p1 = (Persona)request.getSession().getAttribute("persona1");
    
    
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Datos recibidos exitosamente</h1>
        <p>Nombre: <%= p1.getUsuario()%></p>
        <p>Edad: <%= p1.getNombre()%></p>
    </body>
</html>
