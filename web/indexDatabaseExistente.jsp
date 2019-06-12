   
<%@page import="java.util.ArrayList"%>
<%@page import="model.Tabela"%>
<%@page import="model.Tabela"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Cities</title>
    </head>
    <body>
<center>
        <h1>Smart Cities Service Framework</h1>
        <br>
        <h1> DADOS MySQL:</h1>
        
        <form action="mysqlConexao.jsp" method="post">
            <p>Servername: <input type="text" name="servername" value="smartcitiesframeworkfct.mysql.database.azure.com:3306"><br></p>
            <p>Database: <input type="text" name="database" value="sensores"><br></p>
            <p>Username: <input type="text" name="username" value="felipepavan@smartcitiesframeworkfct"><br></p>
            <p>Password: <input type="text" name="password" value="mysql25FCT"><br></p>
            <br><br>
            <input type="submit" value=" VERIFICAR CONEXÃO" style="width: 200px; height: 60px">
        </form> 
        <br>
        <form action="index.jsp" >
            <input type="submit" value="VOLTAR" style="width: 200px; height: 60px">
        </form>
    </center>
    </body>
</html>