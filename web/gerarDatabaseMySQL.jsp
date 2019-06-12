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
          
            /*ArrayList<String> aux = new ArrayList();  
            aux = (ArrayList<String>) session.getAttribute("tabelas_nomes");    

            for (int i = 0; i < aux.size();  i++) {
                out.print(aux.get(i));
            }*/
                
            
            
            out.print("<br><br> <h1> CÓDIGO DATABASE </h1> <br><textarea id='sql' rows='30' cols='100' readonly>");
            out.print(session.getAttribute("sql"));
            out.print("</textarea>");
            

        %>


        <br><br>
        <form action="gerarMySQL">
            <input type="submit" value="CRIAR DATABASE" style="width: 250px; height: 80px">   
        </form>
        <br><br>
        <form action="mostrarDados.jsp">
            <input type="submit" value="VOLTAR" style="width: 250px; height: 80px">   
        </form>
        <br><br>

    </center>
    </body>
</html>