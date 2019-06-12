<%@page import="java.util.ArrayList"%>
<%@page import="model.Tabela"%>
<%
    String tabela = request.getParameter("nometabela");

    ArrayList<Tabela> lista = new ArrayList();
    ArrayList<Tabela> gerador = new ArrayList();
    lista = (ArrayList<Tabela>) session.getAttribute("array_tabelas");
    
    for(int i=0;i<lista.size();i++){
        if(lista.get(i).getNometabela().toLowerCase().equals(tabela.toLowerCase())){
            gerador.add(lista.get(i));
        }
    }
    
    session.setAttribute("gerador", gerador);
    response.sendRedirect("confirmarGeracao.jsp");
%>