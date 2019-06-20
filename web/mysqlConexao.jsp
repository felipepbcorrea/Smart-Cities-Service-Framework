<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Cities</title>
    </head>
    <body>
    <center>
        <h1>VERIFICAÇÃO DA CONEXÃO AO MySQL</h1>
        <br>
        <label>SERVERNAME:</label><br/>
        <input type="text" name="servername" value="<%= request.getParameter("servername")%>" readonly/><br/>
        <label>DATABASE:</label><br/>
        <input type="text" name="database" value="<%= request.getParameter("database")%>" readonly/><br/>
        <label>USERNAME:</label><br/>
        <input type="text" name="username" value="<%= request.getParameter("username")%>" readonly/><br/>
        <label>PASSWORD:</label><br/>
        <input type="password" name="password" value="<%= request.getParameter("password")%>" readonly/><br/>
        
        <br><br>
        <form action="conexaoMySQL">
            <input type="submit" value="CONECTAR" style="width: 200px; height: 60px">
        </form>
        <br>
        <form action="indexDatabaseExistente.jsp">
            <input type="submit" value="VOLTAR" style="width: 200px; height: 60px">
        </form>
    </center>
    </body>
</html>

<% 
    session.setAttribute("servername", request.getParameter("servername"));
    session.setAttribute("database", request.getParameter("database"));
    session.setAttribute("username", request.getParameter("username"));
    session.setAttribute("password", request.getParameter("password"));
%>