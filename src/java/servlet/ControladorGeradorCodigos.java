package Servlet;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Tabela;

@WebServlet(urlPatterns = {"/controlador"})
public class ControladorGeradorCodigos extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(); // REQUISITA AS SESSIONS UTILIZADA NOS JSPs
        
        //Array com toda os dados da tabela recebida, para geração de códigos
        ArrayList<Tabela> tabela = new ArrayList();
        tabela = ( ArrayList<Tabela>) session.getAttribute("gerador");
        
        //Diretório de destino dos códigos gerados
        String destino = (String) session.getAttribute("caminho");
        
        // Acrescenta a nova tabela para a session tabelas_geradas
        String nometabela = tabela.get(0).getNometabela().toLowerCase();
        nometabela = StringUtils.capitalize(nometabela);
        
        session.setAttribute("tabela_atual", nometabela);
        
        if(session.getAttribute("tabelas_geradas")== null){
            session.setAttribute("tabelas_geradas", nometabela);
            
        }
        else{
            session.setAttribute("tabelas_geradas", session.getAttribute("tabelas_geradas") + ", " +nometabela);
        }
        //
        
        
        File file1 = gerarApplicationConfig(destino);
        File file2 = gerarModelo(tabela,destino);
        File file3 = gerarConexaoMySQL(destino);    
        File file4 = gerarConexaoMongoDB(destino);     
        File file5 = gerarDAOMongoDB(destino);
        File file6 = gerarDAOMySQL(tabela,destino);
        File file7 = gerarWSMongoDB(destino);
        File file8 = gerarWSMySQL(tabela, destino);

        
        session.setAttribute("file1", file1);
        session.setAttribute("file2", file2);
        session.setAttribute("file3", file3);
        session.setAttribute("file4", file4);
        session.setAttribute("file5", file5);
        session.setAttribute("file6", file6);
        session.setAttribute("file7", file7);
        session.setAttribute("file8", file8);
        
        //req.getRequestDispatcher("index.jsp").forward(req, resp);
        //req.getRequestDispatcher("download.jsp").forward(req, resp);
        // req.getRequestDispatcher("gerarCodigos.jsp").forward(req, resp);
        req.getRequestDispatcher("codigosGerados.jsp").forward(req, resp);
    }

    public static File gerarApplicationConfig(String destino) throws ServletException {
        try {
            // CRIAR ARQUIVO
            File file = new File(destino+"/ApplicationConfig.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("import java.util.Set;\n"
                    + "import javax.ws.rs.core.Application;\n"
                    + "\n"
                    + "@javax.ws.rs.ApplicationPath(\"webresources\")\n"
                    + "public class ApplicationConfig extends Application {\n"
                    + "\n"
                    + "    @Override\n"
                    + "    public Set<Class<?>> getClasses() {\n"
                    + "        Set<Class<?>> resources = new java.util.HashSet<>();\n"
                    + "        addRestResourceClasses(resources);\n"
                    + "        return resources;\n"
                    + "    }\n"
                    + "\n"
                    + "    /**\n"
                    + "     * Do not modify addRestResourceClasses() method.\n"
                    + "     * It is automatically populated with\n"
                    + "     * all resources defined in the project.\n"
                    + "     * If required, comment out calling this method in getClasses().\n"
                    + "     */\n"
                    + "    private void addRestResourceClasses(Set<Class<?>> resources) {\n"
                    + "\n"
                    + "    }\n"
                    + "    \n"
                    + "}");

            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static File gerarModelo(ArrayList<Tabela> tabela, String destino) {
        try {
            String nometabela = tabela.get(0).getNometabela().toLowerCase();
            nometabela = StringUtils.capitalize(nometabela);

            // CRIAR ARQUIVO
            File file = new File(destino+"/"+nometabela+".java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            // INICIA CLASSE
            bw.write("import java.sql.Timestamp; \n\n"); // Import necessario para tipo Timestamp
            bw.write("public class "+nometabela+"{");
            bw.newLine(); bw.newLine();
            
            //ATRIBUTOS DA CLASSE
            for(int i=0;i<tabela.size();i++){
                if(tabela.get(i).getFk() != null){
                    String tabelafk = tabela.get(i).getTabelafk().toLowerCase();
                    tabelafk = StringUtils.capitalize(tabelafk);
                    
                    bw.write("    private "+tabelafk+" "+tabela.get(i).getNomecoluna()+";");
                    bw.newLine();
                    
                }
                else if(tabela.get(i).getTipo().equals("int")){
                    bw.write("    private int " +tabela.get(i).getNomecoluna()+";");
                    bw.newLine();
                }
                else if(tabela.get(i).getTipo().equals("double")){
                    bw.write("    private double " +tabela.get(i).getNomecoluna()+";");
                    bw.newLine();
                }
                else if(tabela.get(i).getTipo().equals("String")){
                    bw.write("    private String " +tabela.get(i).getNomecoluna()+";");
                    bw.newLine();
                }
                else if(tabela.get(i).getTipo().equals("Timestamp")){
                    bw.write("    private Timestamp " +tabela.get(i).getNomecoluna()+";");
                    bw.newLine();
                }        
            }
            
            //CONSTRUTOR VAZIO
            bw.newLine();
            bw.write("    public " + nometabela + "() {\n    }");
            bw.newLine();
            bw.newLine();

            //GETTERS
            for (int i = 0; i < tabela.size(); i++) {
                if (tabela.get(i).getFk() != null) {
                    String tabelafk = tabela.get(i).getTabelafk().toLowerCase();
                    tabelafk = StringUtils.capitalize(tabelafk);
                    String nomeColuna = StringUtils.capitalize(tabela.get(i).getNomecoluna());

                    bw.write("    public " + tabelafk + "  get" + nomeColuna + "() {");
                    bw.newLine();
                    bw.write("        return " + tabela.get(i).getNomecoluna() + ";");
                    bw.newLine();
                    bw.write("    }");
                    bw.newLine();
                    bw.newLine();

                } else {
                    String nomeColuna = StringUtils.capitalize(tabela.get(i).getNomecoluna());
                    bw.write("    public " + tabela.get(i).getTipo() + "  get" + nomeColuna + "() {");
                    bw.newLine();
                    bw.write("        return " + tabela.get(i).getNomecoluna() + ";");
                    bw.newLine();
                    bw.write("    }");
                    bw.newLine();
                    bw.newLine();

                }
            }

            //SETTERS
            for (int i = 0; i < tabela.size(); i++) {
                if (tabela.get(i).getFk() != null) {
                    String tabelafk = tabela.get(i).getTabelafk().toLowerCase();
                    tabelafk = StringUtils.capitalize(tabelafk);
                    String nomeColuna = StringUtils.capitalize(tabela.get(i).getNomecoluna());

                    bw.write("    public void set" + nomeColuna + "(" + tabelafk +" "+tabela.get(i).getNomecoluna() + ") {");
                    bw.newLine();
                    bw.write("        this." + tabela.get(i).getNomecoluna() + " = " + tabela.get(i).getNomecoluna() + ";");
                    bw.newLine();
                    bw.write("    }");
                    bw.newLine();
                    bw.newLine();

                } else {
                    String nomeColuna = StringUtils.capitalize(tabela.get(i).getNomecoluna());
                    bw.write("    public void set" + nomeColuna + "(" + tabela.get(i).getTipo() + " " + tabela.get(i).getNomecoluna() + ") {");
                    bw.newLine();
                    bw.write("        this." + tabela.get(i).getNomecoluna() + " = " + tabela.get(i).getNomecoluna() + ";");
                    bw.newLine();
                    bw.write("    }");
                    bw.newLine();
                    bw.newLine();

                }
            }

            //FECHA CLASSE
            bw.write("}");

            bw.close();
            return (file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarConexaoMongoDB(String destino) {
        try {
            // CRIAR ARQUIVO
            File file = new File(destino+"/ConexaoMongoDB.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"Mongo Java Driver 3.8.2\"\n"
                    + "import com.mongodb.DB;\n"
                    + "import com.mongodb.MongoClient;\n"
                    + "import com.mongodb.MongoCredential;\n"
                    + "import com.mongodb.ServerAddress;\n"
                    + "import java.sql.SQLException;\n"
                    + "import java.util.ArrayList;\n"
                    + "import java.util.List;\n"
                    + "\n"
                    + "public class ConexaoMongoDB {\n"
                    + "    \n"
                    + "    public static String status = \"false\";\n"
                    + "\n"
                    + "    // Metodo Construtor da Classe\n"
                    + "    public ConexaoMongoDB() {\n"
                    + "    }\n"
                    + "    \n"
                    + "    // Metodo de Conexao\n"
                    + "    public static MongoClient getConexaoMongoDB(String host, int port, String database, String username, String password){\n"
                    + "        \n"
                    + "        MongoClient con = new MongoClient();\n"
                    + "        \n"
                    + "        if (username.equals(\"\") &&  password.equals(\"\")){\n"
                    + "         \n"
                    + "            con = new MongoClient(host,port);// Conexao com servername e porta\n"
                    + "        }\n"
                    + "       else{\n"
                    + "        \n"
                    + "            MongoCredential auth = MongoCredential.createPlainCredential(username, database, password.toCharArray());\n"
                    + "            List<MongoCredential> auths = new ArrayList<MongoCredential>();\n"
                    + "            auths.add(auth);\n"
                    + "\n"
                    + "            ServerAddress serverAddress = new ServerAddress(host,port);\n"
                    + "            con = new MongoClient(serverAddress, auths);\n"
                    + "        }\n"
                    + "        \n"
                    + "        return(con);\n"
                    + "    }\n"
                    + "    \n"
                    + "    // Teste de Conexao\n"
                    + "    public String testeConexao(String host, int port, String database, String username, String password) throws SQLException {\n"
                    + "        MongoClient con = getConexaoMongoDB(host,port, database, username, password);\n"
                    + "        DB dataBase = con.getDB(database);\n"
                    + "   \n"
                    + "        if (dataBase != null) {\n"
                    + "            status = \"true\";\n"
                    + "        }\n"
                    + "        else{\n"
                    + "            status = \"false\";\n"
                    + "        }\n"
                    + "        con.close();\n"
                    + "        return status;\n"
                    + "    }\n"
                    + "}");

            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarConexaoMySQL(String destino) {
        try {
            // CRIAR ARQUIVO
            File file = new File(destino+"/ConexaoMySQL.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"MySQL Connector Java 5.1.23\"\n"
                    + "import java.sql.Connection;\n"
                    + "import java.sql.DriverManager;\n"
                    + "import java.sql.SQLException;\n"
                    + "\n"
                    + "public class ConexaoMySQL {\n"
                    + "\n"
                    + "    public static String status = \"false\";\n"
                    + "\n"
                    + "    public ConexaoMySQL() {\n"
                    + "    }\n"
                    + "\n"
                    + "    // Metodo de Conexao\n"
                    + "    public static Connection getConexaoMySQL(String serverName, String database, String username, String password) {\n"
                    + "\n"
                    + "        Connection connection = null;  \n"
                    + "\n"
                    + "        try {\n"
                    + "            // Carregando o JDBC Driver padrao\n"
                    + "            String driverName = \"com.mysql.jdbc.Driver\";\n"
                    + "            Class.forName(driverName);\n"
                    + "\n"
                    + "            // Configuracao da conexao\n"
                    + "            String url = \"jdbc:mysql://\" + serverName + \"/\" + database;\n"
                    + "\n"
                    + "            connection = DriverManager.getConnection(url, username, password);\n"
                    + "\n"
                    + "            return connection;\n"
                    + "            \n"
                    + "        }catch (ClassNotFoundException e) {  // Driver nao encontrado\n"
                    + "\n"
                    + "            System.out.println(\"O driver expecificado nao foi encontrado.\");\n"
                    + "\n"
                    + "            return null;\n"
                    + "            \n"
                    + "        }catch (SQLException e) { // Falha conexao\n"
                    + "         \n"
                    + "            System.out.println(\"Nao foi possivel conectar ao Banco de Dados.\");\n"
                    + "\n"
                    + "            return null;\n"
                    + "        }\n"
                    + "    }\n"
                    + "\n"
                    + "    // Teste de Conexao\n"
                    + "    public String testeConexao(String servername, String database, String user, String password) throws SQLException {\n"
                    + "        Connection con = getConexaoMySQL(servername, database, user, password);\n"
                    + "        if (con != null) {\n"
                    + "            status = \"true\";\n"
                    + "        }\n"
                    + "        else{ \n"
                    + "            status = \"false\";\n"
                    + "        }\n"
                    + "        con.close();\n"
                    + "        return status;\n"
                    + "    }\n"
                    + "}");

            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarDAOMongoDB(String destino) {
        try {
            // CRIAR ARQUIVO
            File file = new File(destino +"/DAOMongoDB.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"Mongo Java Driver 3.8.2\" e a biblioteca \"Gson 2.8.1\"\n"
                    + "import com.mongodb.BasicDBObject;\n"
                    + "import com.mongodb.DB;\n"
                    + "import com.mongodb.DBCollection;\n"
                    + "import com.mongodb.DBCursor;\n"
                    + "import com.mongodb.DBObject;\n"
                    + "import com.mongodb.MongoClient;\n"
                    + "import com.mongodb.MongoException;\n"
                    + "import com.mongodb.WriteResult;\n"
                    + "import com.mongodb.util.JSON;\n"
                    + "import java.util.ArrayList;\n"
                    + "import java.util.List;\n"
                    + "\n"
                    + "public class DAOMongoDB {\n"
                    + "    \n"
                    + "    // METODO CREATE\n"
                    + "    public boolean inserir(String conteudo, String colecao, String host, int port ,String database,  String username, String password) throws MongoException {\n"
                    + "        Boolean retorno = false;\n"
                    + "        \n"
                    + "        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(host,port,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\n"
                    + "        \n"
                    + "        DB dataBase = con.getDB(database);\n"
                    + "        DBCollection collection = dataBase.getCollection(colecao);\n"
                    + "        BasicDBObject documento = (BasicDBObject) JSON.parse(conteudo);\n"
                    + "        try {\n"
                    + "            WriteResult rs = collection.insert(documento);\n"
                    + "            if(rs.wasAcknowledged()){\n"
                    + "                retorno=true;\n"
                    + "            }\n"
                    + "            else{\n"
                    + "                retorno=false;\n"
                    + "            }\n"
                    + "        } catch (MongoException ex) {\n"
                    + "            retorno = false;\n"
                    + "        }\n"
                    + "        \n"
                    + "        con.close();\n"
                    + "        return retorno;\n"
                    + "    }\n"
                    + "    \n"
                    + "    // METODO READ - LISTAR\n"
                    + "    public List<String> listar(String colecao, String host, int port ,String database, String username, String password) throws MongoException {\n"
                    + "        Boolean retorno = false;\n"
                    + "        List array = new ArrayList();\n"
                    + "        \n"
                    + "        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(host,port,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\n"
                    + "        \n"
                    + "        DB dataBase = con.getDB(database);\n"
                    + "        DBCollection collection = dataBase.getCollection(colecao);\n"
                    + " \n"
                    + "        \n"
                    + "        DBCursor  cursor = collection.find();\n"
                    + "      \n"
                    + "        try {\n"
                    + "            while(cursor.hasNext()) {\n"
                    + "                array.add(cursor.next());\n"
                    + "            }\n"
                    + "        } finally {\n"
                    + "            cursor.close();\n"
                    + "        }\n"
                    + "        \n"
                    + "        con.close(); \n"
                    + "        return array;\n"
                    + "    }\n"
                    + "    \n"
                    + "    // METODO READ - BUSCAR\n"
                    + "    public DBObject buscar(String Valor, String colecao, String host, int port ,String database,  String username, String password) throws MongoException {\n"
                    + "        DBObject retorno = null;\n"
                    + "       \n"
                    + "        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(host,port,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\n"
                    + "        \n"
                    + "        DB dataBase = con.getDB(database);\n"
                    + "        DBCollection collection = dataBase.getCollection(colecao);\n"
                    + "\n"
                    + "        BasicDBObject documento = (BasicDBObject) JSON.parse(Valor);\n"
                    + "\n"
                    + "        DBCursor  cursor = collection.find(documento);\n"
                    + "      \n"
                    + "        try {\n"
                    + "            if(cursor.hasNext()) {\n"
                    + "                retorno = cursor.next();\n"
                    + "            }\n"
                    + "        } finally {\n"
                    + "            cursor.close();\n"
                    + "        }\n"
                    + "        \n"
                    + "        con.close(); \n"
                    + "        return retorno;\n"
                    + "    }\n"
                    + "    \n"
                    + "    // METODO UPDATE \n"
                    + "    public boolean alterar(String alteracao, String busca, String colecao, String host, int port ,String database,  String username, String password) throws MongoException {\n"
                    + "        Boolean retorno = false;\n"
                    + "       \n"
                    + "        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(host,port,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\n"
                    + "        \n"
                    + "        DB dataBase = con.getDB(database);\n"
                    + "        DBCollection collection = dataBase.getCollection(colecao);\n"
                    + "       \n"
                    + "        BasicDBObject coletaDB = (BasicDBObject) JSON.parse(busca);\n"
                    + "        BasicDBObject coletaUpdateDB  = (BasicDBObject) JSON.parse(alteracao);\n"
                    + "\n"
                    + "         try {\n"
                    + "            WriteResult rs = collection.update(coletaDB,coletaUpdateDB);\n"
                    + "            \n"
                    + "            if(rs.isUpdateOfExisting()){\n"
                    + "                retorno=true;\n"
                    + "            }\n"
                    + "            else{\n"
                    + "                retorno=false;\n"
                    + "            }\n"
                    + "         } catch (MongoException ex) {\n"
                    + "            retorno = false;\n"
                    + "        }\n"
                    + "\n"
                    + "        \n"
                    + "        con.close();\n"
                    + "        return retorno;\n"
                    + "    }\n"
                    + "    \n"
                    + "    // METODO DELETE\n"
                    + "    public boolean excluir(String valor, String colecao, String host, int port ,String database, String username, String password) throws MongoException {\n"
                    + "        Boolean retorno = false;\n"
                    + "       \n"
                    + "        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(host,port,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\n"
                    + "        \n"
                    + "        DB dataBase = con.getDB(database);\n"
                    + "        DBCollection collection = dataBase.getCollection(colecao);\n"
                    + "        BasicDBObject documento = (BasicDBObject) JSON.parse(valor);\n"
                    + "        \n"
                    + "        try {\n"
                    + "            WriteResult rs = collection.remove(documento);\n"
                    + "            if(rs.getN()>0){\n"
                    + "                retorno=true;\n"
                    + "            }\n"
                    + "            else{\n"
                    + "                retorno=false;\n"
                    + "            }\n"
                    + "        } catch (MongoException ex) {\n"
                    + "            retorno = false;\n"
                    + "        }\n"
                    + "        \n"
                    + "        con.close();\n"
                    + "        return retorno;\n"
                    + "    }\n"
                    + "}");
            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarDAOMySQL(ArrayList<Tabela> tabela, String destino) {
        try {
            // Pegar nome do dado com a primeira letra maiuscula, Exemplo: Dado ou Sensor, ou Circuito
            // Variavel "nometabela"
            String nometabela = tabela.get(0).getNometabela().toLowerCase();
            nometabela = StringUtils.capitalize(nometabela);
            
            /*// Pegar ArrayList com todas as tabelas que sao referenciadas por chaves estrangeiras
            // Variavel "tabelasfk"
            ArrayList<String> tabelasfk = new ArrayList();          
            for (int i = 0; i < tabela.size(); i++) {
                if(tabela.get(i).getTabelafk() != null){
                    tabelasfk.add(tabela.get(i).getTabelafk().toLowerCase());
                }
                    
            }*/

            // CRIAR ARQUIVO
            File file = new File(destino+"/" + nometabela + "DAOMySQL.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            /*bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"MySQL Connector Java 5.1.23\"\n"
                    // + "import static ConexaoMySQL.getConexaoMySQL;\n"
                    + "import " + nometabela + ";\n");*/

            /*for (int i = 0; i < tabelasfk.size(); i++) {
                bw.write("import " + StringUtils.capitalize(tabelasfk.get(i)) + ";\n");
            }*/

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"MySQL Connector Java 5.1.23\"\n"
                    + "import java.sql.Connection;\n"
                    + "import java.sql.PreparedStatement;\n"
                    + "import java.sql.ResultSet;\n"
                    + "import java.sql.SQLException;\n"
                    + "import java.util.ArrayList;\n"
                    + "import java.util.List;\n"
                    + "\n"
                    + "public class " + nometabela + "DAOMySQL {\n"
                    + "\n"
                    + "    public " + nometabela + "DAOMySQL() {\n"
                    + "    }\n"
                    + "    \n");
            
            //METODO LISTAR
            bw.write( "    public List<" + nometabela + "> listar" + nometabela + "(String servername,String database, String username, String password) throws SQLException {\n"
                    + "        String sql = \"SELECT * FROM " + nometabela + "\";\n"
                    + "        List<" + nometabela + "> retorno = new ArrayList<>();\n"
                    + "        \n"
                    + "        Connection con  = ConexaoMySQL.getConexaoMySQL(servername, database, username,password); // Servername, Database, Username, Password\n"
                    + "       \n"
                    + "        PreparedStatement pst = con.prepareStatement(sql);\n"
                    + "        try {\n"
                    + "\n"
                    + "            ResultSet res = pst.executeQuery();\n"
                    + "            while (res.next()) {\n"
                    + "                " + nometabela + " objeto = new " + nometabela + "();\n");

            for (int i = 0; i < tabela.size(); i++) {
                if (tabela.get(i).getTabelafk() != null) {
                    String tabelafk = tabela.get(i).getTabelafk().toLowerCase();
                    tabelafk = StringUtils.capitalize(tabelafk);
                    bw.write("                objeto.set" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"("+tabelafk+"DAOMySQL.buscar" + tabelafk + "(res.getInt(\"" + tabela.get(i).getNomecoluna() + "\"),servername,database,username,password));\n");
                } else if (tabela.get(i).getTipo().equals("int")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getInt(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                } else if (tabela.get(i).getTipo().equals("double")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getDouble(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                } else if (tabela.get(i).getTipo().equals("String")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getString(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                } else if (tabela.get(i).getTipo().equals("Timestamp")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getTimestamp(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                }
            }
            
            bw.write("              \n"
                    + "                retorno.add(objeto);\n"
                    + "            }\n"
                    + "\n"
                    + "        } catch (SQLException ex) {\n"
                    + "        } \n"
                    + "        \n"
                    + "        con.close(); \n"
                    + "        return retorno;\n"
                    + "    }");
            
                    
            //METODO BUSCAR     
            String id = "";
            for (int i = 0; i < tabela.size(); i++) {
                if(tabela.get(i).getPk()!= null){
                    id = tabela.get(i).getNomecoluna();
                }                  
            }
            
            bw.write( "\n \n    public  static " + nometabela + " buscar" + nometabela + "(int id, String servername,String database, String username, String password) throws SQLException {\n"
                    + "        String sql = \"SELECT * FROM " + nometabela + " WHERE "+id+"=?\";\n"
                    + "        " + nometabela + " objeto = new "+ nometabela +"();\n"
                    + "        \n"
                    + "        Connection con  = ConexaoMySQL.getConexaoMySQL(servername, database, username,password); // Servername, Database, Username, Password\n"
                    + "       \n"
                    + "        PreparedStatement pst = con.prepareStatement(sql);\n"
                    + "        try {\n"
                    + "\n"
                    + "            pst.setInt(1,id);\n"
                    + "            ResultSet res = pst.executeQuery();\n"
                    + "            if (res.next()) {\n");

            for (int i = 0; i < tabela.size(); i++) {
                if (tabela.get(i).getTabelafk() != null) {
                    String tabelafk = tabela.get(i).getTabelafk().toLowerCase();
                    tabelafk = StringUtils.capitalize(tabelafk);
                    bw.write("                objeto.set" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"("+tabelafk+"DAOMySQL.buscar" + tabelafk + "(res.getInt(\"" + tabela.get(i).getNomecoluna() + "\"),servername,database,username,password));\n");
                } else if (tabela.get(i).getTipo().equals("int")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getInt(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                } else if (tabela.get(i).getTipo().equals("double")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getDouble(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                } else if (tabela.get(i).getTipo().equals("String")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getString(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                } else if (tabela.get(i).getTipo().equals("Timestamp")) {
                    bw.write("                objeto.set" + StringUtils.capitalize(tabela.get(i).getNomecoluna()) + "(res.getTimestamp(\"" + tabela.get(i).getNomecoluna() + "\"));\n");
                }
            }
            
            bw.write("              \n"
                    + "            }\n"
                    + "\n"
                    + "        } catch (SQLException ex) {\n"
                    + "        } \n"
                    + "        \n"
                    + "        con.close(); \n"
                    + "        return objeto;\n"
                    + "    }");
            
            //METODO EXCLUIR     
    
            bw.write( "\n \n    public boolean excluir" + nometabela + "(int id, String servername,String database, String username, String password) throws SQLException {\n"
                    + "        String sql = \"DELETE FROM " + nometabela + " WHERE "+id+"=?\";\n"
                    + "        boolean retorno = false;\n"
                    + "        \n"
                    + "        Connection con  = ConexaoMySQL.getConexaoMySQL(servername, database, username,password); // Servername, Database, Username, Password\n"
                    + "       \n"
                    + "        PreparedStatement pst = con.prepareStatement(sql);\n"
                    + "        try {\n"
                    + "\n"
                    + "            pst.setInt(1,id);\n"
                    + "            if (pst.executeUpdate() > 0) {\n"
                    + "                retorno = true;\n"
                    + "            }\n"
                    + "        } catch (SQLException ex) {\n"
                    + "            retorno = false;\n"
                    + "        }\n"
                    + "        con.close();\n"
                    + "        return retorno;\n"
                    + "    }");
            
            // METODO INSERIR
            bw.write("    \n \n    public boolean inserir" + nometabela + "(" + nometabela + " objeto, String servername,String database, String username, String password) throws SQLException {\n");
                            
            bw.write("        String sql = \"INSERT INTO " + nometabela + "(");
                    
            for(int i=0; i < tabela.size(); i++){
                if(i == tabela.size()-1){
                    bw.write(tabela.get(i).getNomecoluna());
                }
                else{
                    bw.write(tabela.get(i).getNomecoluna()+ ",");
                }
            }    
                    
            bw.write(") VALUES (");
            
            for(int i=0; i < tabela.size(); i++){
                if(i == tabela.size()-1){
                    bw.write("?");
                }
                else{
                    bw.write("?,");
                }
            }
            
            bw.write(")\";\n");
                    
            bw.write( "        boolean retorno = false;\n"
                    + "        \n"
                    + "        Connection con = ConexaoMySQL.getConexaoMySQL(servername,database,username,password);\n"
                    + "        PreparedStatement pst = con.prepareStatement(sql);\n"
                    + "        try {\n");

            for (int i = 0; i < tabela.size(); i++) {
                if (tabela.get(i).getTabelafk() != null) {
                    String tabelafk = tabela.get(i).getTabelafk().toLowerCase();
                    tabelafk = StringUtils.capitalize(tabelafk);
                    bw.write("            pst.setInt("+(i+1)+", objeto.get"+StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"().get"+StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                } else if (tabela.get(i).getTipo().equals("int")) {
                    bw.write("            pst.setInt("+(i+1)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                } else if (tabela.get(i).getTipo().equals("double")) {
                    bw.write("            pst.setDouble("+(i+1)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                } else if (tabela.get(i).getTipo().equals("String")) {
                    bw.write("            pst.setString("+(i+1)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                } else if (tabela.get(i).getTipo().equals("Timestamp")) {
                    bw.write("            pst.setTimestamp("+(i+1)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                }
            }

            bw.write( "\n"
                    + "            if (pst.executeUpdate() > 0) {\n"
                    + "                retorno = true;\n"
                    + "            }\n"
                    + "        } catch (SQLException ex) {\n"
                    + "            retorno = false;\n"
                    + "        }\n"
                    + "\n"
                    + "        con.close();\n"
                    + "        return retorno;\n"
                    + "    }");
            
            // METODO ALTERAR
            bw.write("    \n \n    public boolean alterar" + nometabela + "(" + nometabela + " objeto, String servername,String database, String username, String password) throws SQLException {\n");
                            
            bw.write("        String sql = \"UPDATE " + nometabela + " SET ");
           
            /* String sql = "UPDATE dado "
                     + "SET valor=?,tipoDado=?,timestamp=?,coordenadasGeograficas=?,idSensor=?,idCircuito=? "
                     + "WHERE idDado=?";       */
            String chave = "";
            for(int i=0; i < tabela.size(); i++){
                if(tabela.get(i).getPk() != null){
                    chave = tabela.get(i).getNomecoluna();
                }
                else if(i == tabela.size()-1 && tabela.get(i).getPk() == null){
                    bw.write(tabela.get(i).getNomecoluna()+ "=?");
                }
                else{
                    bw.write(tabela.get(i).getNomecoluna()+ "=?,");
                }
            }    
                   
     
            bw.write(" WHERE "+chave+"=?\";\n");
                    
            bw.write( "        boolean retorno = false;\n"
                    + "        \n"
                    + "        Connection con = ConexaoMySQL.getConexaoMySQL(servername,database,username,password);\n"
                    + "        PreparedStatement pst = con.prepareStatement(sql);\n"
                    + "        try {\n");

            for (int i = 0, j=1; i < tabela.size(); i++) {
                if (tabela.get(i).getTabelafk() != null) {
                    String tabelafk = tabela.get(i).getTabelafk().toLowerCase();
                    tabelafk = StringUtils.capitalize(tabelafk);
                    bw.write("            pst.setInt("+(j)+", objeto.get"+StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"().get"+StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                    j++;
                }else if (tabela.get(i).getPk() != null) {
                    bw.write("            pst.setInt("+(tabela.size())+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n"); 
                }else if (tabela.get(i).getTipo().equals("int") && tabela.get(i).getPk() == null) {
                    bw.write("            pst.setInt("+(j)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n"); 
                    j++;
                }else if (tabela.get(i).getTipo().equals("double")) {
                    bw.write("            pst.setDouble("+(j)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                    j++;
                }else if (tabela.get(i).getTipo().equals("String")) { 
                    bw.write("            pst.setString("+(j)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                    j++;
                }else if (tabela.get(i).getTipo().equals("Timestamp")) {
                    bw.write("            pst.setTimestamp("+(j)+", objeto.get" +StringUtils.capitalize(tabela.get(i).getNomecoluna()) +"());\n");
                    j++;
                }
            }

            bw.write( "\n"
                    + "            if (pst.executeUpdate() > 0) {\n"
                    + "                retorno = true;\n"
                    + "            }\n"
                    + "        } catch (SQLException ex) {\n"
                    + "            retorno = false;\n"
                    + "        }\n"
                    + "\n"
                    + "        con.close();\n"
                    + "        return retorno;\n"
                    + "    }");

            bw.write("\n\n}");
            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarWSMongoDB(String destino) {
        try {
            // CRIAR ARQUIVO
            File file = new File(destino+"/WSMongoDB.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"Mongo Java Driver 3.8.2\" e a biblioteca \"Gson 2.8.1\"\n"
                    + "import com.google.gson.Gson;\n"
                    + "import com.mongodb.DBObject;\n"
                    + "import java.sql.SQLException;\n"
                    + "import java.util.List;\n"
                    + "import javax.ws.rs.core.Context;\n"
                    + "import javax.ws.rs.core.UriInfo;\n"
                    + "import javax.ws.rs.Produces;\n"
                    + "import javax.ws.rs.Consumes;\n"
                    + "import javax.ws.rs.GET;\n"
                    + "import javax.ws.rs.POST;\n"
                    + "import javax.ws.rs.Path;\n"
                    + "import javax.ws.rs.PathParam;\n"
                    + "import javax.ws.rs.DELETE;\n"
                    + "import javax.ws.rs.PUT;\n"
                    + "\n"
                    + "@Path(\"MongoDB\")\n"
                    + "public class WSMongoDB {\n"
                    + "\n"
                    + "    @Context\n"
                    + "    private UriInfo context;\n"
                    + "\n"
                    + "    public WSMongoDB() {\n"
                    + "    }\n"
                    + "    \n"
                    + "    @GET\n"
                    + "    @Produces(\"application/text\")\n"
                    + "    public String getJson() {\n"
                    + "        return \"WS MONGODB RESTFULL\";\n"
                    + "    }\n"
                    + "\n"
                    + "    /* LISTAR ELEMENTOS */\n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"listar/{colecao}/{host}/{port}/{database}/{username}/{password}\")\n"
                    + "    public String listarColetasComAutenticacao(\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database,\n"
                    + "                                    @PathParam(\"username\") String username, \n"
                    + "                                    @PathParam(\"password\") String password) throws SQLException {\n"
                    + "        List<String> lista;\n"
                    + "\n"
                    + "        DAOMongoDB dao = new DAOMongoDB(); \n"
                    + "        lista = dao.listar(host, colecao, port, database, username, password);\n"
                    + "        \n"
                    + "        Gson g = new Gson();\n"
                    + "        return g.toJson(lista);\n"
                    + "    }\n"
                    + "    \n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"listar/{colecao}/{host}/{port}/{database}\")\n"
                    + "    public String listarColetasSemAutenticacao(\n"
                    + "                                    @PathParam(\"colecao\") String colecao,       \n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database) throws SQLException {\n"
                    + "        List<String> lista;\n"
                    + "        String username =\"\", password = \"\";\n"
                    + "        DAOMongoDB dao = new DAOMongoDB(); \n"
                    + "        lista = dao.listar(colecao, host, port, database, username, password);\n"
                    + "        \n"
                    + "        Gson g = new Gson();\n"
                    + "        return g.toJson(lista);\n"
                    + "    }\n"
                    + "    \n"
                    + "    /* INSERIR ELEMENTOS */\n"
                    + "    @POST\n"
                    + "    @Consumes({\"application/json\"})\n"
                    + "    @Path(\"inserir/{colecao}/{host}/{port}/{database}/{username}/{password}\")\n"
                    + "    public boolean inserirColetaComAutenticacao(\n"
                    + "                                    String conteudo,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database,\n"
                    + "                                    @PathParam(\"username\") String username, \n"
                    + "                                    @PathParam(\"password\") String password) throws SQLException {\n"
                    + "        DAOMongoDB dao = new DAOMongoDB();\n"
                    + "        return dao.inserir(conteudo,colecao, host, port, database, username, password);\n"
                    + "    }\n"
                    + "    \n"
                    + "    @POST\n"
                    + "    @Consumes({\"application/json\"})\n"
                    + "    @Path(\"inserir/{colecao}/{host}/{port}/{database}\")\n"
                    + "    public boolean inserirColetaSemAutenticacao(\n"
                    + "                                    String conteudo,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database) throws SQLException {\n"
                    + "        DAOMongoDB dao = new DAOMongoDB();\n"
                    + "        String username =\"\", password = \"\";\n"
                    + "        \n"
                    + "        return dao.inserir(conteudo,colecao, host, port, database, username, password);\n"
                    + "    }\n"
                    + "    \n"
                    + "    /* BUSCAR ELEMENTO */\n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"buscar/{valor}/{colecao}/{host}/{port}/{database}/{username}/{password}\")\n"
                    + "    public String buscarColetaTipoDadoComAutenticacao(\n"
                    + "                                    @PathParam(\"valor\") String valor,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database,   \n"
                    + "                                    @PathParam(\"username\") String username, \n"
                    + "                                    @PathParam(\"password\") String password) throws SQLException{\n"
                    + "        DBObject retorno;\n"
                    + "\n"
                    + "        DAOMongoDB dao = new DAOMongoDB(); \n"
                    + "        retorno = dao.buscar(valor, colecao, host, port, database, username, password);\n"
                    + "        \n"
                    + "        Gson g = new Gson();\n"
                    + "        return g.toJson(retorno);\n"
                    + "    }\n"
                    + "    \n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"buscar/{valor}/{colecao}/{host}/{port}/{database}\")\n"
                    + "    public String buscarColetaTipoDadoSemAutenticacao(\n"
                    + "                                    @PathParam(\"valor\") String valor,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database) throws SQLException{\n"
                    + "        DBObject retorno;\n"
                    + "        String username =\"\", password = \"\";\n"
                    + "        DAOMongoDB dao = new DAOMongoDB(); \n"
                    + "        retorno = dao.buscar(valor, colecao, host, port, database, username, password);\n"
                    + "        \n"
                    + "        Gson g = new Gson();\n"
                    + "        return g.toJson(retorno);\n"
                    + "    }\n"
                    + "    \n"
                    + "    /* ATUALIZAR ELEMENTO */  \n"
                    + "    @PUT\n"
                    + "    @Consumes(\"application/json\")\n"
                    + "    @Path(\"alterar/{busca}/{colecao}/{host}/{port}/{database}/{username}/{password}\")\n"
                    + "    public boolean alterarColetaComAutenticacao(\n"
                    + "                                    String alteracao,\n"
                    + "                                    @PathParam(\"busca\") String busca,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database,\n"
                    + "                                    @PathParam(\"username\") String username, \n"
                    + "                                    @PathParam(\"password\") String password) throws SQLException {\n"
                    + "        DAOMongoDB dao = new DAOMongoDB();\n"
                    + "        return dao.alterar(alteracao,busca, colecao, host, port, database, username, password);\n"
                    + "    }  \n"
                    + "    \n"
                    + "    @PUT\n"
                    + "    @Consumes(\"application/json\")\n"
                    + "    @Path(\"alterar/{busca}/{colecao}/{host}/{port}/{database}\")\n"
                    + "    public boolean alterarColetaSemAutenticacao(\n"
                    + "                                    String alteracao,\n"
                    + "                                    @PathParam(\"busca\") String busca,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database) throws SQLException {\n"
                    + "        DAOMongoDB dao = new DAOMongoDB();\n"
                    + "        String username =\"\", password = \"\";\n"
                    + "        return dao.alterar(alteracao,busca, colecao, host, port, database, username, password);\n"
                    + "    }\n"
                    + "    \n"
                    + "    /* REMOVER ELEMENTO */\n"
                    + "    @DELETE\n"
                    + "    @Path(\"excluir/{valor}/{colecao}/{host}/{port}/{database}/{username}/{password}\")\n"
                    + "    public boolean excluirColetaComAutenticacao(\n"
                    + "                                    @PathParam(\"valor\") String valor,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database,\n"
                    + "                                    @PathParam(\"username\") String username, \n"
                    + "                                    @PathParam(\"password\") String password) throws SQLException {\n"
                    + "        DAOMongoDB dao = new DAOMongoDB();\n"
                    + "        return dao.excluir(valor, colecao, host, port, database, username, password);\n"
                    + "    }\n"
                    + "    \n"
                    + "    @DELETE\n"
                    + "    @Path(\"excluir/{valor}/{colecao}/{host}/{port}/{database}\")\n"
                    + "    public boolean excluirColetaSemAutenticacao(\n"
                    + "                                    @PathParam(\"valor\") String valor,\n"
                    + "                                    @PathParam(\"colecao\") String colecao,\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database) throws SQLException {\n"
                    + "        DAOMongoDB dao = new DAOMongoDB();\n"
                    + "        String username =\"\", password = \"\";\n"
                    + "        return dao.excluir(valor, colecao, host, port, database, username, password);\n"
                    + "    }\n"
                    + "    \n"
                    + "    \n"
                    + "    /* VERIFICAR CONEXAO */\n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"conexao/{host}/{port}/{database}/{username}/{password}\")\n"
                    + "    public String verificarConexaoMongoDBComAutenticacao(\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database, \n"
                    + "                                    @PathParam(\"username\") String username, \n"
                    + "                                    @PathParam(\"password\") String password) throws SQLException{\n"
                    + "        \n"
                    + "        ConexaoMongoDB c = new ConexaoMongoDB();\n"
                    + "        return c.testeConexao(host, port, database, username, password);\n"
                    + "        \n"
                    + "    }\n"
                    + "    \n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"conexao/{host}/{port}/{database}\")\n"
                    + "    public String verificarConexaoMongoDBSemAutenticacao(\n"
                    + "                                    @PathParam(\"host\") String host,\n"
                    + "                                    @PathParam(\"port\") int port, \n"
                    + "                                    @PathParam(\"database\") String database) throws SQLException{\n"
                    + "        \n"
                    + "        ConexaoMongoDB c = new ConexaoMongoDB();\n"
                    + "        String username =\"\", password = \"\";\n"
                    + "        return c.testeConexao(host, port, database, username, password); \n"
                    + "    }\n"
                    + "}");
            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarWSMySQL(ArrayList<Tabela> tabela, String destino) {
        try {
            // Pegar nome do dado com a primeira letra maiuscula, Exemplo: Dado ou Sensor, ou Circuito
            // Variavel "nometabela"
            String nometabela = tabela.get(0).getNometabela().toLowerCase();
            nometabela = StringUtils.capitalize(nometabela);
            // CRIAR ARQUIVO
            File file = new File(destino+"/" + nometabela + "WSMySQL.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"Gson 2.8.1\"\n"
                    + "import com.google.gson.Gson;\n"
                    + "import java.sql.SQLException;\n"
                    + "import java.util.List;\n"
                    + "import javax.ws.rs.core.Context;\n"
                    + "import javax.ws.rs.core.UriInfo;\n"
                    + "import javax.ws.rs.Produces;\n"
                    + "import javax.ws.rs.Consumes;\n"
                    + "import javax.ws.rs.GET;\n"
                    + "import javax.ws.rs.POST;\n"
                    + "import javax.ws.rs.Path;\n"
                    + "import javax.ws.rs.PathParam;\n"
                    + "import javax.ws.rs.DELETE;\n"
                    + "import javax.ws.rs.PUT;\n"
                    + "\n"
                    + "@Path(\"" + nometabela + "MySQL\")\n"
                    + "public class " + nometabela + "WSMySQL {\n"
                    + "\n"
                    + "    @Context\n"
                    + "    private UriInfo context;\n"
                    + "\n"
                    + "    public " + nometabela + "WSMySQL() {\n"
                    + "    }\n"
                    + "\n"
                    + "    @GET\n"
                    + "    @Produces(\"application/text\")\n"
                    + "    public String getJson() {\n"
                    + "        return \"WS MySQL RESTFULL\";\n"
                    + "    }\n"
                    + "    \n"
                    + "    /* LISTAR TODOS ELEMENTOS */\n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"listar/" + nometabela.toLowerCase() + "/{servername}/{database}/{username}/{password}\")\n"
                    + "    public String listar" + nometabela + "(\n"
                    + "                             @PathParam(\"servername\") String servername, \n"
                    + "                             @PathParam(\"database\") String database, \n"
                    + "                             @PathParam(\"username\") String username, \n"
                    + "                             @PathParam(\"password\") String password) throws SQLException {\n"
                    + "        List lista;\n"
                    + "\n"
                    + "        " + nometabela + "DAOMySQL dao = new " + nometabela + "DAOMySQL(); \n"
                    + "        lista = dao.listar" + nometabela + "(servername,database,username,password);\n"
                    + "\n"
                    + "        Gson g = new Gson();\n"
                    + "        return g.toJson(lista);\n"
                    + "    }\n\n"
                    + "    /* BUSCAR ELEMENTO POR ATRIBUTO ESPECIFICO*/\n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"buscar/" + nometabela.toLowerCase() + "/{id"+ nometabela +"}/{servername}/{database}/{username}/{password}\")\n"
                    + "    public String buscar"+ nometabela +"(\n"
                    + "                             @PathParam(\"id"+ nometabela +"\") int id"+ nometabela +",\n"
                    + "                             @PathParam(\"servername\") String servername, \n"
                    + "                             @PathParam(\"database\") String database, \n"
                    + "                             @PathParam(\"username\") String username, \n"
                    + "                             @PathParam(\"password\") String password) throws SQLException{\n\n"
                    + "        "+ nometabela +" objeto = new "+ nometabela +"();\n"
                    + "\n"
                    + "        "+ nometabela +"DAOMySQL dao = new "+ nometabela +"DAOMySQL();\n"
                    + "        objeto = dao.buscar"+ nometabela +"(id"+ nometabela +",servername,database, username, password);\n"
                    + "\n"
                    + "        Gson g = new Gson();\n"
                    + "        return g.toJson(objeto);\n"
                    + "    }\n\n"
                    + "    /* INSERIR ELEMENTOS */\n"
                    + "    \n"
                    + "    @POST\n"
                    + "    @Consumes({\"application/json\"})\n"
                    + "    @Path(\"inserir/"+ nometabela.toLowerCase() +"/{servername}/{database}/{username}/{password}\")\n"
                    + "    public boolean inserir"+ nometabela +"(\n"
                    + "                            String conteudo,\n"
                    + "                            @PathParam(\"servername\") String servername, \n"
                    + "                            @PathParam(\"database\") String database, \n"
                    + "                            @PathParam(\"username\") String username, \n"
                    + "                            @PathParam(\"password\") String password) throws SQLException {\n\n"
                    + "        Gson g = new Gson();\n"
                    + "        "+ nometabela +" objeto = ("+ nometabela +") g.fromJson(conteudo, "+ nometabela +".class);\n"
                    + "        "+ nometabela +"DAOMySQL dao = new "+ nometabela +"DAOMySQL();\n"
                    + "        return dao.inserir"+ nometabela +"(objeto,servername,database, username, password);\n"
                    + "    }\n\n"
                    + "    /* ALTERAR ELEMENTOS */\n"
                    + "    \n"
                    + "    @PUT\n"
                    + "    @Consumes(\"application/json\")\n"
                    + "    @Path(\"alterar/"+ nometabela.toLowerCase() +"/{servername}/{database}/{username}/{password}\")\n"
                    + "    public boolean alterar"+ nometabela +"(\n"
                    + "                            String content,\n"
                    + "                            @PathParam(\"servername\") String servername, \n"
                    + "                            @PathParam(\"database\") String database, \n"
                    + "                            @PathParam(\"username\") String username, \n"
                    + "                            @PathParam(\"password\") String password) throws SQLException {\n\n"
                    + "        Gson g = new Gson();\n"
                    + "        "+ nometabela +" objeto = ("+ nometabela +") g.fromJson(content, "+ nometabela +".class);\n"
                    + "        "+ nometabela +"DAOMySQL dao = new "+ nometabela +"DAOMySQL();\n"
                    + "        return dao.alterar"+ nometabela +"(objeto,servername,database, username, password);\n"
                    + "    }\n\n"
                    + "    /* REMOVER ELEMENTOS */\n"
                    + "    @DELETE\n"
                    + "    @Path(\"excluir/"+ nometabela.toLowerCase() +"/{id"+ nometabela +"}/{servername}/{database}/{username}/{password}\")\n"
                    + "    public boolean excluir"+ nometabela +"(\n"
                    + "                            @PathParam(\"id"+ nometabela +"\") int id"+ nometabela +",\n"
                    + "                            @PathParam(\"servername\") String servername, \n"
                    + "                            @PathParam(\"database\") String database, \n"
                    + "                            @PathParam(\"username\") String username, \n"
                    + "                            @PathParam(\"password\") String password) throws SQLException {\n\n"
                    + "        "+ nometabela +"DAOMySQL dao = new "+ nometabela +"DAOMySQL();\n"
                    + "        return dao.excluir"+ nometabela +"(id"+ nometabela +",servername,database, username, password);\n"
                    + "    }\n\n"
                    + "    /* VERIFICAR CONEXAO */\n"
                    + "    @GET\n"
                    + "    @Produces(\"application/json\")\n"
                    + "    @Path(\"conexao/{servername}/{database}/{username}/{password}\")\n"
                    + "    public String verificarConexaoMySQL(\n"
                    + "                            @PathParam(\"servername\") String servername, \n"
                    + "                            @PathParam(\"database\") String database, \n"
                    + "                            @PathParam(\"username\") String username, \n"
                    + "                            @PathParam(\"password\") String password) throws SQLException{\n"
                    + "       \n"
                    + "        ConexaoMySQL c = new ConexaoMySQL();\n"
                    + "        return c.testeConexao(servername, database, username, password);    \n"
                    + "    }\n"
                    + "}");

            bw.close();
            return(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
    /*public static void gerador() {
        try {
            // CRIAR ARQUIVO
            File file = new File("C:/Users/FELIPE/Desktop/teste.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("TESTE");
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

