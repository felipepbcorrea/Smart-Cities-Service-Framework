package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/gerarMySQL"})
public class ControladorGeradorMySQL extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        
        String servername = (String) session.getAttribute("servername");
        String database = (String) session.getAttribute("database");
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        String sql = (String) session.getAttribute("sql");

        String[] comandos;
        //ArrayList<String> comandos = new ArrayList();
        
        comandos = sql.split(";");
        
        Connection con;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+servername+"?useSSL=true&requireSSL=false&user="+username+"&password="+password);
        } 
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao conectar com o banco:" + e);
        }

        PreparedStatement pst;
        try {
            for(int i = 0; i < comandos.length-1; i++) {
                if(comandos[i] != null){
                    pst = con.prepareStatement(comandos[i]);
                    pst.executeUpdate();  
                }      
            }
            /*pst = con.prepareStatement("CREATE SCHEMA IF NOT EXISTS FETESTE");
            pst.executeUpdate();
            pst = con.prepareStatement("USE FETESTE");
            pst.executeUpdate();
            pst = con.prepareStatement("CREATE TABLE Circuito( " +
"     idCircuito int(11)  NOT NULL AUTO_INCREMENT," +
"     PRIMARY KEY  (idCircuito)," +
"     marca  varchar(45)," +
"     modelo  varchar(45));");
            pst.executeUpdate();*/
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar database:" + e);
        }

        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexao:" + e);
        }

        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
