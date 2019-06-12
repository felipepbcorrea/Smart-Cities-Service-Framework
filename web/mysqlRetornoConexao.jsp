<%@page import="model.Tabela"%>
<%@page import="java.util.ArrayList"%>
    
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Cities</title>
    </head>
    <body>
    <center>
        <h1>ESTRUTURA CARREGADA DO BANCO DE DADOS</h1>
        <% 
            ArrayList<Tabela> lista = new ArrayList();
            lista = (ArrayList<Tabela>) request.getAttribute("retorno");
            session.setAttribute("array_tabelas", lista);
            out.print("DATABASE: " + session.getAttribute("database")+"<br><br>"); 
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
        <form action="gerarCodigos.jsp">
            <input type="submit" value="GERAR CÓDIGOS" style="width: 250px; height: 80px">  
            <br> Obs: Ao clicar nessa opção será possível voltar somente ao index 
        </form>
        <br><br>
        <form action="index.jsp">
            <input type="submit" value="VOLTAR AO INDEX" style="width: 250px; height: 80px">   
        </form>
    </center>
    </body>
</html>

