<%-- 
    Document   : criarMongoDB
    Created on : 26/11/2018, 22:57:12
    Author     : FELIPE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Smart Cities</title>
    </head>
    <body>
    <center>
        <h1>INSERINDO DADOS</h1> <br>
        <form action="confirmarDados.jsp" method="post">
            NOME TABELA:
            <input type="text" name="nometabela" value=""><br><br>
            
            NOME COLUNA:
            <input type="text" name="nomecoluna" value=""><br><br>

            TIPO COLUNA:
            <select name="tipo">
                <option value="int">INT</option>
                <option value="double">DOUBLE</option>
                <option value="String">VARCHAR</option>
                <option value="Timestamp">TIMESTAMP</option>
            </select><br><br>
            
            TAMANHO:<input type="text" name="tamanho" value=""><br><br>

            <input type="checkbox" name="nn" value="SIM">NOT NULL<br><br>

            <input type="checkbox" name="ai" value="SIM">AUTO INCREMENT<br><br>

            <input id="pk" type="checkbox" name="pk" value="PK" onclick=" func(this);" >PRIMARY KEY<br><br>

            <input id="fk" type="checkbox" name="fk" value="FK" onclick=" func(this);">FOREIGN KEY<br>
            TABELA FK:
            <input id="tabelafk" type="text" name="tabelafk" value="" disabled>    
            <br><br>
            <input type="submit" value="ADICIONAR" style="width: 125px; height: 60px">   
        </form>


        <script>
            function func(obj) {
                if (obj.checked === true){ 

                    if (obj.id === 'pk'){
                        document.getElementById('fk').checked = false;
                        document.getElementById('tabelafk').disabled = true;
                    } 
                    else if (obj.id === 'fk'){
                        document.getElementById('pk').checked = false;
                        document.getElementById('tabelafk').disabled = false;
                    }
                }
                else if(!(document.getElementById('fk').checked) && !(document.getElementById('pk').checked)) {
                    document.getElementById('tabelafk').disabled = true;
                }   
            }
        </script>

        <br>
        <form action="indexDatabaseNovo.jsp">
            <input type="submit" value="VOLTAR" style="width: 125px; height: 60px">     
        </form>
    </center>
    </body>
</html>

