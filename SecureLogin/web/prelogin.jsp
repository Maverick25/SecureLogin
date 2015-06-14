<%-- 
    Document   : prelogin
    Created on : Jun 13, 2015, 4:07:09 PM
    Author     : marekrigan
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <link rel="stylesheet" href="style.css" type="text/css"/>

        <title>Secure Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div class="login">
            
            <h1>Login to Web App</h1>
            
            <form method="post" action="loginRequest">
                <p><input type="text" name="username" value="${fn:escapeXml(param.username)}" placeholder="Username"></p>
                <p><input type="password" name="password" value="${fn:escapeXml(param.password)}" placeholder="Password"></p>
                <p class="submit">
                    <input type="submit" value="Login">
                </p>
            </form>
            
        </div>
    </body>
</html>
