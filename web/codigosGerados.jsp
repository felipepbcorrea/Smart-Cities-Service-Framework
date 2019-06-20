<%-- 
    Document   : gerarCodigos
    Created on : 02/12/2018, 19:22:28
    Author     : FELIPE
--%>

<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Tabela"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Cities</title>
    </head>
    <body>
    <center>
        <h1>Códigos Gerados</h1> 
        <br>
        MODELO: <% out.print(session.getAttribute("tabela_atual")); %>
        <br>   
        <h1>MongoDB</h1>
        
        <a href=download.jsp?numero=1> 
        
        
        <% 
        
        /* 
        // Imprimir o arquivo
        BufferedReader buf = new BufferedReader(new FileReader(file1));
        String line = null;
        while ((line = buf.readLine()) != null) {
          out.print(line);
        }*/
        
        File file1 = (File) session.getAttribute("file1"); 
        String filename = file1.getName();
        out.print(file1.getName().replaceAll("[0123456789]",""));

        
        %>  
        </a>         
        <br>

        <a href=download.jsp?numero=4> 
        <% File file4 = (File) session.getAttribute("file4"); 
        out.print(file4.getName().replaceAll("[0123456789]","")); %>   
        </a> 
        <br>
                
        <a href=download.jsp?numero=7> 
        <% File file7 = (File) session.getAttribute("file7"); 
        out.print(file7.getName().replaceAll("[0123456789]","")); %>
        </a> 
        <br>
        
        <a href=download.jsp?numero=5> <%File file5 = (File) session.getAttribute("file5"); 
        out.print(file5.getName().replaceAll("[0123456789]","")); %>
        </a> 
        <br><br>
        
        <h2> MySQL </h2>
        <a href=download.jsp?numero=3> <%File file3 = (File) session.getAttribute("file3"); 
        out.print(file3.getName().replaceAll("[0123456789]","")); %>
        </a> 
        <br>
        
        <a href=download.jsp?numero=2> <%File file2 = (File) session.getAttribute("file2"); 
        out.print(file2.getName().replaceAll("[0123456789]","")); %>
        </a> 
        <br>
      
        <a href=download.jsp?numero=8> <%File file8 = (File) session.getAttribute("file8"); 
        out.print(file8.getName().replaceAll("[0123456789]","")); %>
        </a> 
        <br>
        
        <a href=download.jsp?numero=6> <%File file6 = (File) session.getAttribute("file6"); 
        out.print(file6.getName().replaceAll("[0123456789]","")); %>
        </a> 
        <br><br>

        <br><br>
        <form action="gerarCodigos.jsp">
            <input type="submit" value="VOLTAR A GERAÇÃO DE CÓDIGOS" style="width: 300px; height: 80px">   
        </form>
        
    </center>
    </body>

</html>

