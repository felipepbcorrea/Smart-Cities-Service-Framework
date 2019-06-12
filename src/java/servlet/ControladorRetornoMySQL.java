package Servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import model.Tabela;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/conexaoMySQL"})
public class ControladorRetornoMySQL extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String servername = (String) session.getAttribute("servername");
        String database = (String) session.getAttribute("database");
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        Connection con;
        ArrayList<Tabela> retorno = new ArrayList();
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + servername + "/" + database+"?useSSL=true&requireSSL=false", username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao conectar com o banco:" + e);
        }
        String sql = "SELECT TABLE_NAME, "
                + "COLUMN_NAME, DATA_TYPE, COLUMN_TYPE, IS_NULLABLE, EXTRA, COLUMN_KEY "
                + "FROM INFORMATION_SCHEMA.columns where table_schema= '" + database+"';";      
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Tabela t = new Tabela();
                
                t.setNometabela(res.getString("TABLE_NAME"));
                t.setNomecoluna(res.getString("COLUMN_NAME"));
                
                if (res.getString("DATA_TYPE").equals("varchar")){
                   t.setTipo("String"); 
                   t.setTamanho(res.getString("COLUMN_TYPE").substring(8,res.getString("COLUMN_TYPE").length()-1));
                }
                else if (res.getString("DATA_TYPE").equals("timestamp")){
                   t.setTipo("Timestamp");
                   t.setTamanho(res.getString("COLUMN_TYPE").substring(10,res.getString("COLUMN_TYPE").length()-1));
                }
                else if (res.getString("DATA_TYPE").equals("double")){
                    t.setTamanho(res.getString("COLUMN_TYPE").substring(7,res.getString("COLUMN_TYPE").length()-1));
                }
                else{
                    t.setTipo(res.getString("DATA_TYPE"));
                    t.setTamanho(res.getString("COLUMN_TYPE").substring(4,res.getString("COLUMN_TYPE").length()-1));
                }
                
                //t.setTamanho(res.getString("COLUMN_TYPE"));

                if (res.getString("IS_NULLABLE").equals("YES")) {
                    t.setNn("SIM");
                } else {
                    t.setNn(null);
                }

                if (res.getString("EXTRA").equals("auto_increment")) {
                    t.setAi("SIM");
                } else {
                    t.setAi(null);
                }

                if (res.getString("COLUMN_KEY").equals("PRI")) {
                    t.setPk("PK");
                    t.setFk(null);
                    t.setTabelafk(null);
                } else if (res.getString("COLUMN_KEY").equals("MUL")) {
                    t.setPk(null);
                    t.setFk("FK");
                    t.setTabelafk(null);
                } else {
                    t.setPk(null);
                    t.setFk(null);
                    t.setTabelafk(null);
                }

                retorno.add(t);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao listar dados:" + ex);
        }

        for (int i = 0; i < retorno.size(); i++) {
            if (retorno.get(i).getFk() != null) {
                sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE"
                    + " WHERE COLUMN_NAME = '"
                    + retorno.get(i).getNomecoluna()
                    +"' AND CONSTRAINT_NAME = 'PRIMARY' AND TABLE_SCHEMA = '"
                    + database + "'";                   
                try {
                    PreparedStatement pst = con.prepareStatement(sql);
                    ResultSet res = pst.executeQuery();
                    if (res.next()){         
                        retorno.get(i).setTabelafk(res.getString("TABLE_NAME"));                     
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao listar dado:" + e);
                }
            }
        }
        
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexao:" + e);
        }

        req.setAttribute("retorno", retorno);
        req.getRequestDispatcher("mysqlRetornoConexao.jsp").forward(req, resp);
    }
}
