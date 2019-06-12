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
        <h1>Geração de Código</h1>
        
        <h1>DADOS</h1>
        <% 
            ArrayList<Tabela> lista = new ArrayList();
            lista = (ArrayList<Tabela>) session.getAttribute("array_tabelas");
            
            out.print("<table border='1'>");
            out.print("<tr>");
            out.print("<th>TABELA</th><th>COLUNA</th><th>TIPO</th><th>TAMANHO</th><th>NOT NULL</th><th>AUTO INCREMENT</th><th>PK</th><th>FK</th><th>TABELA FK</th>");
            out.print("</tr>");
            for(int i=0;i<lista.size();i++){
                out.print("<tr>");
                out.print("<td>"+lista.get(i).getNometabela()+"</td>");
                out.print("<td>"+lista.get(i).getNomecoluna()+"</td>");
                out.print("<td>"+lista.get(i).getTipo()+"</td>");
                out.print("<td>"+lista.get(i).getTamanho()+"</td>");
                out.print("<td>"+lista.get(i).getNn()+"</td>");
                out.print("<td>"+lista.get(i).getAi()+"</td>");
                out.print("<td>"+lista.get(i).getPk()+"</td>");
                out.print("<td>"+lista.get(i).getFk()+"</td>");
                out.print("<td>"+lista.get(i).getTabelafk()+"</td>");
                //out.print("<td><a href='removerDado.jsp?posicao="+i+"'>REMOVER</a></td>");
                out.print("</tr>");
                        
            }
            out.print("</table>");
                                     
        %>
        <br><br>
        <% 
            /*if(session.getAttribute("caminho")== null || request.getParameter("caminho") != null){
                session.setAttribute("caminho",request.getParameter("caminho")); 
            }
                   
            if ( request.getParameter("caminho") != null){
                out.print(request.getParameter("caminho"));
            }
            else{
                out.print(session.getAttribute("caminho"));
            }*/
            
            session.setAttribute("caminho","C:/Users/FELIPE/Desktop/Gerador");
            //out.print("https://drive.google.com/open?id=11kR3zKYC7Xx6-_h4p0GeKYX4fGWnvYnT");
            
        %> 

        <b>A(S) RESPECTIVA(S) TABELA(S) EM QUE OS CÓDIGOS JÁ FORAM GERADOS:</b> 
     
        <% 
        if(session.getAttribute("tabelas_geradas")== null){
        }
        else{
            out.print(session.getAttribute("tabelas_geradas"));
        }              
        %>
        
        <br><br>
        
        <form action="gerarCodigosRedirecionamento.jsp">
            INSIRA O NOME DA TABELA DA QUAL DESEJA GERAR OS CÓDIGO <br><br>
            <input type="text" name="nometabela" value=""><br><br>
            <input type="submit" value="PROSSEGUIR" style="width: 180px; height: 60px">   
        </form>
        <br><br>
        <form action="index.jsp">
            <input type="submit" value="VOLTAR AO INDEX" style="width: 180px; height: 60px">   
        </form>
    </center>
    </body>

</html>

