   
<%@page import="java.util.ArrayList"%>
<%@page import="model.Tabela"%>
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
        <h1>Smart Cities Service Framework</h1>
        <br><br>
        <form action="inserirDados.jsp">
            <input type="submit" value="INSERIR DADOS RELACIONAIS" style="width: 250px; height: 80px">   
        </form>
        <br><br>
        <form action="mostrarDados.jsp">
            <input type="submit" value="LISTAR DADOS INSERIDOS" style="width: 250px; height: 80px">   
        </form>
        <br><br>
        <form action="index.jsp">
            <input type="submit" value="VOLTAR" style="width: 250px; height: 80px">   
        </form>
    </center>
    </body>
</html>

<%
    ArrayList<Tabela> array_tabelas = new ArrayList();
    Tabela dado1 = new Tabela();
    Tabela dado2 = new Tabela();
    Tabela dado3 = new Tabela();
    Tabela dado4 = new Tabela();
    Tabela dado5 = new Tabela();
    Tabela dado6 = new Tabela();
    Tabela dado7 = new Tabela();
    Tabela dado8 = new Tabela();
    Tabela dado9 = new Tabela();
    Tabela dado10 = new Tabela();
    Tabela dado11 = new Tabela();
    Tabela dado12 = new Tabela();
    Tabela dado13 = new Tabela();
    Tabela dado14 = new Tabela();

    dado1.setNometabela("Sensor");
    dado1.setNomecoluna("idSensor");
    dado1.setTipo("int");
    dado1.setTamanho("11");
    dado1.setNn("SIM");
    dado1.setAi("SIM");
    dado1.setPk("PK");
    
    dado2.setNometabela("Sensor");
    dado2.setNomecoluna("tipoSensor");
    dado2.setTipo("String");
    dado2.setTamanho("45");

    dado3.setNometabela("Sensor");
    dado3.setNomecoluna("idCircuito");
    dado3.setTipo("int");
    dado3.setTamanho("11");
    dado3.setFk("FK");
    dado3.setTabelafk("Circuito");
    
    dado4.setNometabela("Circuito");
    dado4.setNomecoluna("idCircuito");
    dado4.setTipo("int");
    dado4.setTamanho("11");
    dado4.setNn("SIM");
    dado4.setAi("SIM");
    dado4.setPk("PK");
    
    dado5.setNometabela("Circuito");
    dado5.setNomecoluna("marca");
    dado5.setTipo("String");
    dado5.setTamanho("45");
    
    dado6.setNometabela("Circuito");
    dado6.setNomecoluna("modelo");
    dado6.setTipo("String");
    dado6.setTamanho("45");
    
    dado7.setNometabela("Dado");
    dado7.setNomecoluna("idDado");
    dado7.setTipo("int");
    dado7.setTamanho("11");
    dado7.setNn("SIM");
    dado7.setAi("SIM");
    dado7.setPk("PK");
    
    dado8.setNometabela("Dado");
    dado8.setNomecoluna("valor");
    dado8.setTipo("String");
    dado8.setTamanho("45");
    
    dado9.setNometabela("Dado");
    dado9.setNomecoluna("tipoDado");
    dado9.setTipo("String");
    dado9.setTamanho("45");
    
    dado10.setNometabela("Dado");
    dado10.setNomecoluna("timestamp");
    dado10.setTipo("Timestamp");
    dado10.setTamanho("1");
      
    dado11.setNometabela("Dado");
    dado11.setNomecoluna("coordenadasGeograficas");
    dado11.setTipo("String");
    dado11.setTamanho("45");
    
    dado12.setNometabela("Dado");
    dado12.setNomecoluna("idSensor");
    dado12.setTipo("int");
    dado12.setTamanho("11");
    dado12.setFk("FK");
    dado12.setTabelafk("Sensor");
    
    dado13.setNometabela("ERRO");
    dado13.setNomecoluna("ERRO");
    dado13.setTipo("ERRO");
    dado13.setTamanho("ERRO");
    dado13.setNn("ERRO");
    dado13.setAi("ERRO");
    dado13.setPk("ERRO");
    dado13.setFk("ERRO");
    dado13.setTabelafk("ERRO");
    
    dado14.setNometabela("Dado");
    dado14.setNomecoluna("idCircuito");
    dado14.setTipo("int");
    dado14.setTamanho("11");
    dado14.setFk("FK");
    dado14.setTabelafk("Circuito");
    

    array_tabelas.add(dado4);
    array_tabelas.add(dado5);
    array_tabelas.add(dado6);
    array_tabelas.add(dado1);
    array_tabelas.add(dado2);
    array_tabelas.add(dado3);
    array_tabelas.add(dado7);
    array_tabelas.add(dado8);
    array_tabelas.add(dado9);
    array_tabelas.add(dado10);
    array_tabelas.add(dado11);
    array_tabelas.add(dado12);
    //array_tabelas.add(dado13);
    //array_tabelas.add(dado14);

    
    
    //if(session.getAttribute("array_tabelas")== null){
        session.setAttribute("array_tabelas", array_tabelas);
    //}
%>