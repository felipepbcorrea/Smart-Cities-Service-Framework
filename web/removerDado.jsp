<%@page import="java.util.ArrayList"%>
<%@page import="model.Tabela"%>
<%
    String tabela = request.getParameter("nometabela");

    ArrayList<Tabela> lista = new ArrayList();
    lista = (ArrayList<Tabela>) session.getAttribute("array_tabelas");
    
    int i = Integer.parseInt(request.getParameter("posicao"));
    
    lista.remove(i);

    session.setAttribute("array_tabelas", lista);
    response.sendRedirect("mostrarDados.jsp");
%>