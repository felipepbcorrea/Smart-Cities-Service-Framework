<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Tabela"%>
<%@page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Cities</title>
    </head>
    <body>
    <center>
        <h1>VERIFICAÇÃO DOS DADOS DE CONEXÃO AO MySQL</h1>
        <br>
        <label>SERVERNAME:</label><br/>
        <input type="text" name="servername" value="<%= request.getParameter("servername")%>" readonly/><br/>
        <label>DATABASE:</label><br/>
        <input type="text" name="database" value="<%= request.getParameter("database")%>" readonly/><br/>
        <label>USERNAME:</label><br/>
        <input type="text" name="username" value="<%= request.getParameter("username")%>" readonly/><br/>     
        <label>PASSWORD:</label><br/>
        <input type="password" name="password" value="<%= request.getParameter("password")%>" readonly/><br/>
        
        <br><br>
        <form action="gerarDatabaseMySQL.jsp">
            <input type="submit" value="ENVIAR" style="width: 200px; height: 60px">
        </form>
        <br>
        <form action="dadosConexaoMySQL.jsp">
            <input type="submit" value="VOLTAR" style="width: 200px; height: 60px">
        </form>
    </center>
    </body>
</html>

<% 
    session.setAttribute("servername", request.getParameter("servername"));
    session.setAttribute("database", request.getParameter("database"));
    session.setAttribute("username", request.getParameter("username"));
    session.setAttribute("password", request.getParameter("password"));
    
    ArrayList<Tabela> lista = new ArrayList();
    lista = (ArrayList<Tabela>) session.getAttribute("array_tabelas");

    ArrayList<String> tabelas_nomes = new ArrayList();

    for (int i = 0; i < lista.size(); i++) {
        if (tabelas_nomes == null) {
            tabelas_nomes.add(lista.get(i).getNometabela());
        } else {
            int aux = 1;
            for (int j = 0; j < tabelas_nomes.size(); j++) {
                if (tabelas_nomes.get(j).equals(lista.get(i).getNometabela())) {
                    aux = 0;
                    break;
                }
            }
            if (aux == 1) {
                tabelas_nomes.add(lista.get(i).getNometabela());
            }
        }
    }
    session.setAttribute("tabelas_nomes", tabelas_nomes);
    
    String sql_aux = "CREATE SCHEMA IF NOT EXISTS "+ request.getParameter("database") +" DEFAULT CHARACTER SET utf8; \n";
    sql_aux += "USE "+ request.getParameter("database")+";\n\n"; 
    String sql="";
    for (int i = 0; i < tabelas_nomes.size(); i++) {
        sql = "";
        sql = "CREATE TABLE "+tabelas_nomes.get(i)+"( \n";
        for (int j = 0; j < lista.size(); j++) {
            if(lista.get(j).getNometabela().equals(tabelas_nomes.get(i))){
                
                sql += "     "+lista.get(j).getNomecoluna();
                
                if(lista.get(j).getTipo().equals("String")){
                    sql += "  varchar"+"("+lista.get(j).getTamanho()+")";
                }
                else{
                    sql += " "+lista.get(j).getTipo()+"("+lista.get(j).getTamanho()+")";
                }
                
                if(lista.get(j).getNn() != null){
                    sql += "  NOT NULL";
                }
                
                if(lista.get(j).getAi() != null){
                    sql += " AUTO_INCREMENT";
                }
                sql += ",\n";
                
                if(lista.get(j).getPk() != null){
                    sql += "     PRIMARY KEY  ("+lista.get(j).getNomecoluna()+"),\n";
                }
                
                if(lista.get(j).getFk() != null){
                    sql += "     CONSTRAINT "+ lista.get(j).getNomecoluna() +" FOREIGN KEY ("+ lista.get(j).getNomecoluna() 
                          +") REFERENCES "+ lista.get(j).getTabelafk() +"("+ lista.get(j).getNomecoluna() +"),\n";
                } 
            }
        }
        sql_aux = sql_aux + sql.substring(0,sql.length()-2) + ");\n\n";
    }
    
    session.setAttribute("sql", sql_aux);
%>