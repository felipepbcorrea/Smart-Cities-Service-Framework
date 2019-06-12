   
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
        <br> <h2> Framework de Armazenamento e Recuperação de Dados Estruturados e Não Estruturados, utilizando Web Services</h2>
        <br><br><br><br>
        <form action="indexDatabaseExistente.jsp">
            <input type="submit" value="GERAR SERVIÇOS PARA BANCO DE DADOS EXISTENTE" style="width: 500px; height: 100px">   
        </form>
        <br><br><br>
        <form action="indexDatabaseNovo.jsp">
            <input type="submit" value="GERAR SERVIÇOS PARA BANCO DE DADOS QUE SERÁ CRIADO" style="width: 500px; height: 100px">   
        </form>
        <br><br>
           
    </center>
    </body>
</html>