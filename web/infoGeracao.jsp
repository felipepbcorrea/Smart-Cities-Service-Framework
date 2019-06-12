<%-- 
    Document   : gerarCodigos
    Created on : 02/12/2018, 19:22:28
    Author     : FELIPE
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
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
        <h1>Geração de Código</h1><br>
        
        <p><h2> A geração de códigos é feita individualmente para cada tabela do banco de dados </p></h2>
        <form action="gerarCodigos.jsp">
            INSIRA O CAMINHO DO DIRETÓRIO DESTINO, NO QUAL DESEJA GERAR O(S) CÓDIGO(S) <br><br>
            <input type="text" name="caminho" style="width: 400px; height: 20px"> <br><br>
            EXEMPLO: C:/Users/FELIPE/Desktop/Gerador<br><br>
            <input type="submit" value="PROSSEGUIR" style="width: 180px; height: 60px"> 
        </form>
        <br><br>
        <form action="index.jsp">
            <input type="submit" value="VOLTAR AO INDEX" style="width: 180px; height: 60px">   
        </form>
    </center>
    </body>

</html>

<% 
    if(session.getAttribute("tabelas_geradas") != null){
        session.setAttribute("tabelas_geradas", null);
    }
%>
