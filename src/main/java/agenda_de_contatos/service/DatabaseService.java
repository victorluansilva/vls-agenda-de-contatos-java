package agenda_de_contatos.service;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if(URL == null || USER == null || PASSWORD == null){
            throw new SQLException("Credênciais não foram encontradas no .env");
        }
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

    public static boolean testarConexao(){
        try(Connection conn = getConnection()){
            System.out.println("Conexão: online.");
            return conn != null;
        }catch (SQLException e){
            return false;
        }
    }

}