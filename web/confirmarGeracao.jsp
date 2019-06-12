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
        <br><br>

        <%
            ArrayList<Tabela> lista = new ArrayList();
            if (session.getAttribute("gerador") != null) {
                lista = (ArrayList<Tabela>) session.getAttribute("gerador");
                out.print("<center>");
                out.print("<table border='1'>");
                out.print("<tr>");
                out.print("<th>TABELA</th><th>COLUNA</th><th>TIPO</th><th>TAMANHO</th><th>NOT NULL</th><th>AUTO INCREMENT</th><th>PK</th><th>FK</th><th>TABELA FK</th>");
                out.print("</tr>");
                for (int i = 0; i < lista.size(); i++) {
                    out.print("<tr>");
                    out.print("<td>" + lista.get(i).getNometabela() + "</td>");
                    out.print("<td>" + lista.get(i).getNomecoluna() + "</td>");
                    out.print("<td>" + lista.get(i).getTipo() + "</td>");
                    out.print("<td>" + lista.get(i).getTamanho() + "</td>");
                    out.print("<td>" + lista.get(i).getNn() + "</td>");
                    out.print("<td>" + lista.get(i).getAi() + "</td>");
                    out.print("<td>" + lista.get(i).getPk() + "</td>");
                    out.print("<td>" + lista.get(i).getFk() + "</td>");
                    out.print("<td>" + lista.get(i).getTabelafk() + "</td>");
                    out.print("</tr>");

                }
                out.print("</table>");
                out.print("</center>");
            }
        %>
        <br><br>
        <form action="controlador">
            <input type="submit" value="GERAR" style="width: 180px; height: 60px">   
        </form>

        <br><br>

        <form action="gerarCodigos.jsp">
            <input type="submit" value="VOLTAR" style="width: 180px; height: 60px">   
        </form>
        <br><br>
    </center>
</body>
</html>



