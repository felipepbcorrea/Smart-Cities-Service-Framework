
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Cities</title>
    </head>
    <body>
    <center>
        <h1>DADO INSERIDO</h1>
        <form action="mostrarDados.jsp" method="post">
            NOME TABELA: <%= request.getParameter("nometabela") %>
            <br><br>
            NOME COLUNA: <%= request.getParameter("nomecoluna") %>
            <br><br>
            TIPO COLUNA: <%= request.getParameter("tipo") %>
            <br><br>
            TAMANHO COLUNA: <%= request.getParameter("tamanho") %>
            <br><br>
            NOT NULL: <%= request.getParameter("nn") %>
            <br><br>
            AUTO INCREMENT: <%= request.getParameter("ai") %>
            <br><br>
            PRIMARY KEY: <%= request.getParameter("pk") %>
            <br><br>
            FOREIGN KEY: <%= request.getParameter("fk") %>
            <br><br>
            TABELA FOREIGN KEY: <%= request.getParameter("tabelafk") %>
            <br><br>
            
            <input type="submit" value="CONTINUAR" style="width: 125px; height: 60px">   
        </form>
    </center>
    </body>
</html>

<%@page import="java.util.ArrayList"%>   
<%@page import="model.Tabela"%>
<%
    ArrayList<Tabela> array_tabelas = new ArrayList();
    Tabela tabela = new Tabela();
    if( (session.getAttribute("array_tabelas")) != null){
        array_tabelas = (ArrayList<Tabela>) session.getAttribute("array_tabelas");
    }

    tabela.setNometabela(request.getParameter("nometabela"));
    tabela.setNomecoluna(request.getParameter("nomecoluna"));
    tabela.setTipo(request.getParameter("tipo"));
    tabela.setTamanho(request.getParameter("tamanho"));
    tabela.setNn(request.getParameter("nn"));
    tabela.setAi(request.getParameter("ai"));
    tabela.setPk(request.getParameter("pk"));
    tabela.setFk(request.getParameter("fk"));
    tabela.setTabelafk(request.getParameter("tabelafk"));

    array_tabelas.add(tabela);

    session.setAttribute("array_tabelas", array_tabelas);
 
%>