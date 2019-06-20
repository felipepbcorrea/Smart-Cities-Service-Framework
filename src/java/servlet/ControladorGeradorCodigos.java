package servlet;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
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
        
        //Array com toda os dados da tabela recebida, para geracao de codigos
        ArrayList<Tabela> tabela = new ArrayList();
        tabela = ( ArrayList<Tabela>) session.getAttribute("gerador");
        
        //Diretorio de destino dos codigos gerados
        //String destino = (String) session.getAttribute("caminho");
        
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
        
        
        File file1 = gerarApplicationConfig();
        File file2 = gerarModelo(tabela);
        File file3 = gerarConexaoMySQL();    
        File file4 = gerarConexaoMongoDB();     
        File file5 = gerarDAOMongoDB();
        File file6 = gerarDAOMySQL(tabela);
        File file7 = gerarWSMongoDB();
        File file8 = gerarWSMySQL(tabela);

        
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

    public static File gerarApplicationConfig() throws ServletException {
        try {
           /* // CRIAR ARQUIVO
            //File file = new File(destino+"/ApplicationConfig.java");
        	File folder = (File) getServletContext().getAttribute(ServletContext.TEMPDIR);
        	File file = new File(folder, "ApplicationConfig.java");
        	//File file = new File(destino+"/ApplicationConfig.java");
        	
        	//File file = new File("<catalina_home_directory>/temp/ApplicationConfig.java");
        	
        	
            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/

        	File file = File.createTempFile("ApplicationConfig", ".java");
        	
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
    
    public static File gerarModelo(ArrayList<Tabela> tabela) {
        try {
            String nometabela = tabela.get(0).getNometabela().toLowerCase();
            nometabela = StringUtils.capitalize(nometabela);

           /* // CRIAR ARQUIVO
            // File file = new File(destino+"/"+nometabela+".java");
            File file = new File(nometabela+".java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/
            
            File file = File.createTempFile(nometabela, ".java");

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

    public static File gerarConexaoMongoDB() {
        try {
            /*// CRIAR ARQUIVO
            //File file = new File(destino+"/ConexaoMongoDB.java");
            File file = new File("ConexaoMongoDB.java");
            
            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/
        	
        	File file = File.createTempFile("ConexaoMongoDB", ".java");

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"Mongo Java Driver 3.8.2\"\r\n" + 
            		"import com.mongodb.DB;\r\n" + 
            		"import com.mongodb.MongoClient;\r\n" + 
            		"import com.mongodb.MongoClientURI;\r\n" + 
            		"import java.sql.SQLException;\r\n" + 
            		"\r\n" + 
            		"public class ConexaoMongoDB {\r\n" + 
            		"    \r\n" + 
            		"    public static String status = \"false\";\r\n" + 
            		"\r\n" + 
            		"    // Metodo Construtor da Classe\r\n" + 
            		"    public ConexaoMongoDB() {\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    // Metodo de Conexao\r\n" + 
            		"    public static MongoClient getConexaoMongoDB(String servername, String database, String username, String password){\r\n" + 
            		"        \r\n" + 
            		"        MongoClient con = new MongoClient();\r\n" + 
            		"        \r\n" + 
            		"        // Conexao SEM Autenticacao\r\n" + 
            		"        if (username.equals(\"\") &&  password.equals(\"\")){\r\n" + 
            		"\r\n" + 
            		"            MongoClientURI uri = new MongoClientURI\r\n" + 
            		"                (\"mongodb+srv://\"+ servername + \"/\" + database+ \"?retryWrites=true&w=majority\");\r\n" + 
            		"            // Conexao por URI, informando Servername e Database\r\n" + 
            		"            MongoClient mongoClient = new MongoClient(uri);\r\n" + 
            		"            con = new MongoClient(uri);\r\n" + 
            		"\r\n" + 
            		"        }\r\n" + 
            		"        // Conexao COM Autenticacao\r\n" + 
            		"        else{\r\n" + 
            		"            MongoClientURI uri = new MongoClientURI(\r\n" + 
            		"                    \"mongodb+srv://\"+username+\":\"+ password\r\n" + 
            		"                            + \"@\" + servername + \"/\"+ database\r\n" + 
            		"                            + \"?retryWrites=true&w=majority\");            \r\n" + 
            		"            // Conexao por URI, informando Username, Password, Servername e Database       \r\n" + 
            		"            MongoClient mongoClient = new MongoClient(uri);\r\n" + 
            		"            con = new MongoClient(uri);\r\n" + 
            		"        }\r\n" + 
            		"        \r\n" + 
            		"        return(con);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    // Teste de Conexao Sem Autenticacao\r\n" + 
            		"    public String testeConexaoSemAutenticacao(String servername, String database) throws SQLException {\r\n" + 
            		"        MongoClientURI uri = new MongoClientURI\r\n" + 
            		"                (\"mongodb+srv://\"+ servername + \"/\" + database+ \"?retryWrites=true&w=majority\");\r\n" + 
            		"        \r\n" + 
            		"        // Conexao por URI, informando Servername e Database\r\n" + 
            		"        MongoClient mongoClient = new MongoClient(uri);\r\n" + 
            		"        MongoClient con = new MongoClient(uri);\r\n" + 
            		"        \r\n" + 
            		"        DB dataBase = con.getDB(database);\r\n" + 
            		"   \r\n" + 
            		"        if (dataBase != null) {\r\n" + 
            		"            status = \"true\";\r\n" + 
            		"        }\r\n" + 
            		"        else{\r\n" + 
            		"            status = \"false\";\r\n" + 
            		"        }\r\n" + 
            		"        con.close();\r\n" + 
            		"        return status;\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    // Teste de Conexao Com Autenticacao\r\n" + 
            		"    public String testeConexaoComAutenticacao(String servername, String database, String username, String password) throws SQLException {\r\n" + 
            		"        MongoClientURI uri = new MongoClientURI(\r\n" + 
            		"                    \"mongodb+srv://\"+username+\":\"+ password\r\n" + 
            		"                            + \"@\" + servername + \"/\"+ database\r\n" + 
            		"                            + \"?retryWrites=true&w=majority\");            \r\n" + 
            		"            \r\n" + 
            		"        // Conexao por URI, informando Username, Password, Servername e Database       \r\n" + 
            		"        MongoClient mongoClient = new MongoClient(uri);\r\n" + 
            		"        MongoClient con = new MongoClient(uri);\r\n" + 
            		"        \r\n" + 
            		"        DB dataBase = con.getDB(database);\r\n" + 
            		"   \r\n" + 
            		"        if (dataBase != null) {\r\n" + 
            		"            status = \"true\";\r\n" + 
            		"        }\r\n" + 
            		"        else{\r\n" + 
            		"            status = \"false\";\r\n" + 
            		"        }\r\n" + 
            		"        con.close();\r\n" + 
            		"        return status;\r\n" + 
            		"    }\r\n" + 
            		"}");

            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarConexaoMySQL() {
        try {
            /*// CRIAR ARQUIVO
            //File file = new File(destino+"/ConexaoMySQL.java");
            File file = new File("ConexaoMySQL.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/
        	
        	File file = File.createTempFile("ConexaoMySQL", ".java");

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"MySQL Connector Java 5.1.23\"\r\n" + 
            		"import java.sql.Connection;\r\n" + 
            		"import java.sql.DriverManager;\r\n" + 
            		"import java.sql.SQLException;\r\n" + 
            		"\r\n" + 
            		"public class ConexaoMySQL {\r\n" + 
            		"\r\n" + 
            		"    public static String status = \"false\";\r\n" + 
            		"\r\n" + 
            		"    public ConexaoMySQL() {\r\n" + 
            		"    }\r\n" + 
            		"\r\n" + 
            		"    // Metodo de Conexao\r\n" + 
            		"    public static Connection getConexaoMySQL(String servername, String database, String username, String password){\r\n" + 
            		"\r\n" + 
            		"        Connection connection = null;  \r\n" + 
            		"\r\n" + 
            		"        try {\r\n" + 
            		"            // Carregando o JDBC Driver padrao\r\n" + 
            		"            String driverName = \"com.mysql.jdbc.Driver\";\r\n" + 
            		"            Class.forName(driverName);\r\n" + 
            		"\r\n" + 
            		"            // Configuracao da conexao\r\n" + 
            		"            connection = DriverManager.getConnection(\"jdbc:mysql://\" + servername + \"/\" + \r\n" + 
            		"                    database+\"?useSSL=true&requireSSL=false\", username, password);\r\n" + 
            		"\r\n" + 
            		"\r\n" + 
            		"            return connection;\r\n" + 
            		"                     \r\n" + 
            		"        }\r\n" + 
            		"        catch (ClassNotFoundException e) {  // Driver nao encontrado\r\n" + 
            		"\r\n" + 
            		"            System.out.println(\"O driver expecificado nao foi encontrado.\");\r\n" + 
            		"\r\n" + 
            		"            return null;\r\n" + 
            		"        }\r\n" + 
            		"        catch (SQLException e) { // Falha conexao\r\n" + 
            		"         \r\n" + 
            		"            System.out.println(\"Nao foi possivel conectar ao Banco de Dados.\");\r\n" + 
            		"\r\n" + 
            		"            return null;\r\n" + 
            		"        }\r\n" + 
            		"    }\r\n" + 
            		"\r\n" + 
            		"    // Teste de Conexao\r\n" + 
            		"    public String testeConexao(String servername, String database, String username, String password) throws SQLException{\r\n" + 
            		"        \r\n" + 
            		"        // Configuracao da conexao\r\n" + 
            		"        Connection con = DriverManager.getConnection(\"jdbc:mysql://\" + servername + \"/\" + \r\n" + 
            		"                    database+\"?useSSL=true&requireSSL=false\", username, password);\r\n" + 
            		"        \r\n" + 
            		"        if (con != null) {\r\n" + 
            		"            status = \"true\";\r\n" + 
            		"        }\r\n" + 
            		"        else{ \r\n" + 
            		"            status = \"false\";\r\n" + 
            		"        }\r\n" + 
            		"        con.close();\r\n" + 
            		"        return status;\r\n" + 
            		"    }\r\n" + 
            		"}");

            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarDAOMongoDB() {
        try {
            /*// CRIAR ARQUIVO
            //File file = new File(destino +"/DAOMongoDB.java");
            File file = new File("DAOMongoDB.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/
        	
        	File file = File.createTempFile("DAOMongoDB", ".java");

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"Mongo Java Driver 3.8.2\" e a biblioteca \"Gson 2.8.1\"\r\n" + 
            		"import com.mongodb.BasicDBObject;\r\n" + 
            		"import com.mongodb.DB;\r\n" + 
            		"import com.mongodb.DBCollection;\r\n" + 
            		"import com.mongodb.DBCursor;\r\n" + 
            		"import com.mongodb.DBObject;\r\n" + 
            		"import com.mongodb.MongoClient;\r\n" + 
            		"import com.mongodb.MongoException;\r\n" + 
            		"import com.mongodb.WriteResult;\r\n" + 
            		"import com.mongodb.util.JSON;\r\n" + 
            		"import java.util.ArrayList;\r\n" + 
            		"import java.util.List;\r\n" + 
            		"\r\n" + 
            		"public class DAOMongoDB {\r\n" + 
            		"    \r\n" + 
            		"    // METODO CREATE\r\n" + 
            		"    public boolean inserir(String conteudo, String colecao, String servername ,String database,  String username, String password) throws MongoException {\r\n" + 
            		"        Boolean retorno = false;\r\n" + 
            		"        \r\n" + 
            		"        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(servername,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\r\n" + 
            		"        \r\n" + 
            		"        DB dataBase = con.getDB(database);\r\n" + 
            		"        DBCollection collection = dataBase.getCollection(colecao);\r\n" + 
            		"        BasicDBObject documento = (BasicDBObject) JSON.parse(conteudo);\r\n" + 
            		"        try {\r\n" + 
            		"            WriteResult rs = collection.insert(documento);\r\n" + 
            		"            if(rs.wasAcknowledged()){\r\n" + 
            		"                retorno=true;\r\n" + 
            		"            }\r\n" + 
            		"            else{\r\n" + 
            		"                retorno=false;\r\n" + 
            		"            }\r\n" + 
            		"        } catch (MongoException ex) {\r\n" + 
            		"            retorno = false;\r\n" + 
            		"        }\r\n" + 
            		"        \r\n" + 
            		"        con.close();\r\n" + 
            		"        return retorno;\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    // METODO READ - LISTAR\r\n" + 
            		"    public List<String> listar(String colecao, String servername ,String database, String username, String password) throws MongoException {\r\n" + 
            		"        Boolean retorno = false;\r\n" + 
            		"        List array = new ArrayList();\r\n" + 
            		"        \r\n" + 
            		"        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(servername,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\r\n" + 
            		"        \r\n" + 
            		"        DB dataBase = con.getDB(database);\r\n" + 
            		"        DBCollection collection = dataBase.getCollection(colecao);\r\n" + 
            		" \r\n" + 
            		"        \r\n" + 
            		"        DBCursor  cursor = collection.find();\r\n" + 
            		"      \r\n" + 
            		"        try {\r\n" + 
            		"            while(cursor.hasNext()) {\r\n" + 
            		"                array.add(cursor.next());\r\n" + 
            		"            }\r\n" + 
            		"        } finally {\r\n" + 
            		"            cursor.close();\r\n" + 
            		"        }\r\n" + 
            		"        \r\n" + 
            		"        con.close(); \r\n" + 
            		"        return array;\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    // METODO READ - BUSCAR\r\n" + 
            		"    public DBObject buscar(String Valor, String colecao, String servername ,String database,  String username, String password) throws MongoException {\r\n" + 
            		"        DBObject retorno = null;\r\n" + 
            		"       \r\n" + 
            		"        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(servername,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\r\n" + 
            		"        \r\n" + 
            		"        DB dataBase = con.getDB(database);\r\n" + 
            		"        DBCollection collection = dataBase.getCollection(colecao);\r\n" + 
            		"\r\n" + 
            		"        BasicDBObject documento = (BasicDBObject) JSON.parse(Valor);\r\n" + 
            		"\r\n" + 
            		"        DBCursor  cursor = collection.find(documento);\r\n" + 
            		"      \r\n" + 
            		"        try {\r\n" + 
            		"            if(cursor.hasNext()) {\r\n" + 
            		"                retorno = cursor.next();\r\n" + 
            		"            }\r\n" + 
            		"        } finally {\r\n" + 
            		"            cursor.close();\r\n" + 
            		"        }\r\n" + 
            		"        \r\n" + 
            		"        con.close(); \r\n" + 
            		"        return retorno;\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    // METODO UPDATE \r\n" + 
            		"    public boolean alterar(String alteracao, String busca, String colecao, String servername ,String database,  String username, String password) throws MongoException {\r\n" + 
            		"        Boolean retorno = false;\r\n" + 
            		"       \r\n" + 
            		"        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(servername,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\r\n" + 
            		"        \r\n" + 
            		"        DB dataBase = con.getDB(database);\r\n" + 
            		"        DBCollection collection = dataBase.getCollection(colecao);\r\n" + 
            		"       \r\n" + 
            		"        BasicDBObject coletaDB = (BasicDBObject) JSON.parse(busca);\r\n" + 
            		"        BasicDBObject coletaUpdateDB  = (BasicDBObject) JSON.parse(alteracao);\r\n" + 
            		"\r\n" + 
            		"         try {\r\n" + 
            		"            WriteResult rs = collection.update(coletaDB,coletaUpdateDB);\r\n" + 
            		"            \r\n" + 
            		"            if(rs.isUpdateOfExisting()){\r\n" + 
            		"                retorno=true;\r\n" + 
            		"            }\r\n" + 
            		"            else{\r\n" + 
            		"                retorno=false;\r\n" + 
            		"            }\r\n" + 
            		"         } catch (MongoException ex) {\r\n" + 
            		"            retorno = false;\r\n" + 
            		"        }\r\n" + 
            		"\r\n" + 
            		"        \r\n" + 
            		"        con.close();\r\n" + 
            		"        return retorno;\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    // METODO DELETE\r\n" + 
            		"    public boolean excluir(String valor, String colecao, String servername ,String database, String username, String password) throws MongoException {\r\n" + 
            		"        Boolean retorno = false;\r\n" + 
            		"       \r\n" + 
            		"        MongoClient con = ConexaoMongoDB.getConexaoMongoDB(servername,database,username,password); // HOST,PORT,DATABASE,USERNAME,PASSWORD\r\n" + 
            		"        \r\n" + 
            		"        DB dataBase = con.getDB(database);\r\n" + 
            		"        DBCollection collection = dataBase.getCollection(colecao);\r\n" + 
            		"        BasicDBObject documento = (BasicDBObject) JSON.parse(valor);\r\n" + 
            		"        \r\n" + 
            		"        try {\r\n" + 
            		"            WriteResult rs = collection.remove(documento);\r\n" + 
            		"            if(rs.getN()>0){\r\n" + 
            		"                retorno=true;\r\n" + 
            		"            }\r\n" + 
            		"            else{\r\n" + 
            		"                retorno=false;\r\n" + 
            		"            }\r\n" + 
            		"        } catch (MongoException ex) {\r\n" + 
            		"            retorno = false;\r\n" + 
            		"        }\r\n" + 
            		"        \r\n" + 
            		"        con.close();\r\n" + 
            		"        return retorno;\r\n" + 
            		"    }\r\n" + 
            		"}");
            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarDAOMySQL(ArrayList<Tabela> tabela) {
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

            /*// CRIAR ARQUIVO
            //File file = new File(destino+"/" + nometabela + "DAOMySQL.java");
            File file = new File( nometabela + "DAOMySQL.java");
            
            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/
            
            File file = File.createTempFile(nometabela+"DAOMySQL", ".java");

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

    public static File gerarWSMongoDB() {
        try {
            /*// CRIAR ARQUIVO
            //File file = new File(destino+"/WSMongoDB.java");
            File file = new File("WSMongoDB.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/
        	
        	File file = File.createTempFile("WSMongoDB", ".java");

            // PARAMETRO UTILIZADO PARA ESCREVER NO ARQUIVO
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("// Imports necessarios para uso do Banco de dados - Deve-se importar a biblioteca \"Mongo Java Driver 3.8.2\" e a biblioteca \"Gson 2.8.1\"\r\n" + 
            		"import com.google.gson.Gson;\r\n" + 
            		"import com.mongodb.DBObject;\r\n" + 
            		"import java.sql.SQLException;\r\n" + 
            		"import java.util.List;\r\n" + 
            		"import javax.ws.rs.core.Context;\r\n" + 
            		"import javax.ws.rs.core.UriInfo;\r\n" + 
            		"import javax.ws.rs.Produces;\r\n" + 
            		"import javax.ws.rs.Consumes;\r\n" + 
            		"import javax.ws.rs.GET;\r\n" + 
            		"import javax.ws.rs.POST;\r\n" + 
            		"import javax.ws.rs.Path;\r\n" + 
            		"import javax.ws.rs.PathParam;\r\n" + 
            		"import javax.ws.rs.DELETE;\r\n" + 
            		"import javax.ws.rs.PUT;\r\n" + 
            		"\r\n" + 
            		"@Path(\"MongoDB\")\r\n" + 
            		"public class WSMongoDB {\r\n" + 
            		"\r\n" + 
            		"    @Context\r\n" + 
            		"    private UriInfo context;\r\n" + 
            		"\r\n" + 
            		"    public WSMongoDB() {\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    @GET\r\n" + 
            		"    @Produces(\"application/text\")\r\n" + 
            		"    public String getJson() {\r\n" + 
            		"        return \"WS MONGODB RESTFULL\";\r\n" + 
            		"    }\r\n" + 
            		"\r\n" + 
            		"    /* LISTAR ELEMENTOS */\r\n" + 
            		"    @GET\r\n" + 
            		"    @Produces(\"application/json\")\r\n" + 
            		"    @Path(\"listar/{colecao}/{servername}/{database}/{username}/{password}\")\r\n" + 
            		"    public String listarColetasComAutenticacao(\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername,\r\n" + 
            		"                                    @PathParam(\"database\") String database,\r\n" + 
            		"                                    @PathParam(\"username\") String username, \r\n" + 
            		"                                    @PathParam(\"password\") String password) throws SQLException {\r\n" + 
            		"        List<String> lista;\r\n" + 
            		"\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB(); \r\n" + 
            		"        lista = dao.listar(colecao, servername, database, username, password);\r\n" + 
            		"        \r\n" + 
            		"        Gson g = new Gson();\r\n" + 
            		"        return g.toJson(lista);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    @GET\r\n" + 
            		"    @Produces(\"application/json\")\r\n" + 
            		"    @Path(\"listar/{colecao}/{servername}/{database}\")\r\n" + 
            		"    public String listarColetasSemAutenticacao(\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,       \r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database) throws SQLException {\r\n" + 
            		"        List<String> lista;\r\n" + 
            		"        String username =\"\", password = \"\";\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB(); \r\n" + 
            		"        lista = dao.listar(colecao, servername, database, username, password);\r\n" + 
            		"        \r\n" + 
            		"        Gson g = new Gson();\r\n" + 
            		"        return g.toJson(lista);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    /* INSERIR ELEMENTOS */\r\n" + 
            		"    @POST\r\n" + 
            		"    @Consumes({\"application/json\"})\r\n" + 
            		"    @Path(\"inserir/{colecao}/{servername}/{database}/{username}/{password}\")\r\n" + 
            		"    public boolean inserirColetaComAutenticacao(\r\n" + 
            		"                                    String conteudo,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database,\r\n" + 
            		"                                    @PathParam(\"username\") String username, \r\n" + 
            		"                                    @PathParam(\"password\") String password) throws SQLException {\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB();\r\n" + 
            		"        return dao.inserir(conteudo,colecao, servername, database, username, password);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    @POST\r\n" + 
            		"    @Consumes({\"application/json\"})\r\n" + 
            		"    @Path(\"inserir/{colecao}/{servername}/{database}\")\r\n" + 
            		"    public boolean inserirColetaSemAutenticacao(\r\n" + 
            		"                                    String conteudo,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database) throws SQLException {\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB();\r\n" + 
            		"        String username =\"\", password = \"\";\r\n" + 
            		"        \r\n" + 
            		"        return dao.inserir(conteudo,colecao, servername, database, username, password);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    /* BUSCAR ELEMENTO */\r\n" + 
            		"    @GET\r\n" + 
            		"    @Produces(\"application/json\")\r\n" + 
            		"    @Path(\"buscar/{valor}/{colecao}/{servername}/{database}/{username}/{password}\")\r\n" + 
            		"    public String buscarColetaTipoDadoComAutenticacao(\r\n" + 
            		"                                    @PathParam(\"valor\") String valor,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database,   \r\n" + 
            		"                                    @PathParam(\"username\") String username, \r\n" + 
            		"                                    @PathParam(\"password\") String password) throws SQLException{\r\n" + 
            		"        DBObject retorno;\r\n" + 
            		"\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB(); \r\n" + 
            		"        retorno = dao.buscar(valor, colecao, servername, database, username, password);\r\n" + 
            		"        \r\n" + 
            		"        Gson g = new Gson();\r\n" + 
            		"        return g.toJson(retorno);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    @GET\r\n" + 
            		"    @Produces(\"application/json\")\r\n" + 
            		"    @Path(\"buscar/{valor}/{colecao}/{servername}/{database}\")\r\n" + 
            		"    public String buscarColetaTipoDadoSemAutenticacao(\r\n" + 
            		"                                    @PathParam(\"valor\") String valor,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database) throws SQLException{\r\n" + 
            		"        DBObject retorno;\r\n" + 
            		"        String username =\"\", password = \"\";\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB(); \r\n" + 
            		"        retorno = dao.buscar(valor, colecao, servername, database, username, password);\r\n" + 
            		"        \r\n" + 
            		"        Gson g = new Gson();\r\n" + 
            		"        return g.toJson(retorno);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    /* ATUALIZAR ELEMENTO */  \r\n" + 
            		"    @PUT\r\n" + 
            		"    @Consumes(\"application/json\")\r\n" + 
            		"    @Path(\"alterar/{busca}/{colecao}/{servername}/{database}/{username}/{password}\")\r\n" + 
            		"    public boolean alterarColetaComAutenticacao(\r\n" + 
            		"                                    String alteracao,\r\n" + 
            		"                                    @PathParam(\"busca\") String busca,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database,\r\n" + 
            		"                                    @PathParam(\"username\") String username, \r\n" + 
            		"                                    @PathParam(\"password\") String password) throws SQLException {\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB();\r\n" + 
            		"        return dao.alterar(alteracao,busca, colecao, servername, database, username, password);\r\n" + 
            		"    }  \r\n" + 
            		"    \r\n" + 
            		"    @PUT\r\n" + 
            		"    @Consumes(\"application/json\")\r\n" + 
            		"    @Path(\"alterar/{busca}/{colecao}/{servername}/{database}\")\r\n" + 
            		"    public boolean alterarColetaSemAutenticacao(\r\n" + 
            		"                                    String alteracao,\r\n" + 
            		"                                    @PathParam(\"busca\") String busca,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database) throws SQLException {\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB();\r\n" + 
            		"        String username =\"\", password = \"\";\r\n" + 
            		"        return dao.alterar(alteracao,busca, colecao, servername, database, username, password);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    /* REMOVER ELEMENTO */\r\n" + 
            		"    @DELETE\r\n" + 
            		"    @Path(\"excluir/{valor}/{colecao}/{servername}/{database}/{username}/{password}\")\r\n" + 
            		"    public boolean excluirColetaComAutenticacao(\r\n" + 
            		"                                    @PathParam(\"valor\") String valor,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database,\r\n" + 
            		"                                    @PathParam(\"username\") String username, \r\n" + 
            		"                                    @PathParam(\"password\") String password) throws SQLException {\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB();\r\n" + 
            		"        return dao.excluir(valor, colecao, servername, database, username, password);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    @DELETE\r\n" + 
            		"    @Path(\"excluir/{valor}/{colecao}/{servername}/{database}\")\r\n" + 
            		"    public boolean excluirColetaSemAutenticacao(\r\n" + 
            		"                                    @PathParam(\"valor\") String valor,\r\n" + 
            		"                                    @PathParam(\"colecao\") String colecao,\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database) throws SQLException {\r\n" + 
            		"        DAOMongoDB dao = new DAOMongoDB();\r\n" + 
            		"        String username =\"\", password = \"\";\r\n" + 
            		"        return dao.excluir(valor, colecao, servername, database, username, password);\r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    \r\n" + 
            		"    /* VERIFICAR CONEXAO */\r\n" + 
            		"    @GET\r\n" + 
            		"    @Produces(\"application/json\")\r\n" + 
            		"    @Path(\"conexao/{servername}/{database}/{username}/{password}\")\r\n" + 
            		"    public String verificarConexaoMongoDBComAutenticacao(\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database, \r\n" + 
            		"                                    @PathParam(\"username\") String username, \r\n" + 
            		"                                    @PathParam(\"password\") String password) throws SQLException{\r\n" + 
            		"        \r\n" + 
            		"        ConexaoMongoDB c = new ConexaoMongoDB();\r\n" + 
            		"        return c.testeConexaoComAutenticacao(servername, database, username, password);\r\n" + 
            		"        \r\n" + 
            		"    }\r\n" + 
            		"    \r\n" + 
            		"    @GET\r\n" + 
            		"    @Produces(\"application/json\")\r\n" + 
            		"    @Path(\"conexao/{servername}/{database}\")\r\n" + 
            		"    public String verificarConexaoMongoDBSemAutenticacao(\r\n" + 
            		"                                    @PathParam(\"servername\") String servername, \r\n" + 
            		"                                    @PathParam(\"database\") String database) throws SQLException{\r\n" + 
            		"        \r\n" + 
            		"        ConexaoMongoDB c = new ConexaoMongoDB();\r\n" + 
            		"        return c.testeConexaoSemAutenticacao(servername, database); \r\n" + 
            		"    }\r\n" + 
            		"}");
            bw.close();
            return(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File gerarWSMySQL(ArrayList<Tabela> tabela) {
        try {
            // Pegar nome do dado com a primeira letra maiuscula, Exemplo: Dado ou Sensor, ou Circuito
            // Variavel "nometabela"
            String nometabela = tabela.get(0).getNometabela().toLowerCase();
            nometabela = StringUtils.capitalize(nometabela);
            
            /*// CRIAR ARQUIVO
            //File file = new File(destino+"/" + nometabela + "WSMySQL.java");
            File file = new File(nometabela + "WSMySQL.java");

            // SE NAO EXISTIR, CRIA
            if (!file.exists()) {
                file.createNewFile();
            }*/
            
            File file = File.createTempFile(nometabela+"WSMySQL", ".java");

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

