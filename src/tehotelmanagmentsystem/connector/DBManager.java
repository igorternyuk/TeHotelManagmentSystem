package tehotelmanagmentsystem.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class DBManager {
    private static final String PATH_TO_CONFIG_FILE = "config/config.dat";
    private static DBManager instance = null;
    String server;
    String port;
    String user;
    String pass;
    String database;
    String url;
    private Connection con = null;
    private PreparedStatement cmd = null;

    public static DBManager getInstance() throws ClassNotFoundException,
            SQLException,
            IOException {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() throws ClassNotFoundException, SQLException, IOException {
        readDBSettingsFromFile(PATH_TO_CONFIG_FILE);
        url = "jdbc:mysql://" + server + ":" + port + "/" + database;
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, user, pass);
    }

    public void updateURL(String pathToFile) throws SQLException, IOException {
        readDBSettingsFromFile(pathToFile);
        url = "jdbc:mysql://" + server + ":" + port + "/" + database;
        con = DriverManager.getConnection(url, user, pass);
    }

    public Connection getConnection() {
        return con;
    }

    public void executeUpdate(String sql) throws SQLException {
        cmd = con.prepareStatement(sql);
        cmd.executeUpdate();
        disconnect();
    }

    public ResultSet executeSelect(String sql) throws SQLException {
        cmd = con.prepareStatement(sql);
        ResultSet rs = cmd.executeQuery();
        return rs;
    }

    public void disconnect() throws SQLException {
        if (cmd != null) {
            cmd.close();
        }
    }

    private enum LoadState {
        SERVER,
        PORT,
        USER,
        PASS,
        DATABASE,
        STOP
    };

    private void readDBSettingsFromFile(String pathToFile) throws
            UnsupportedEncodingException, IOException {
        InputStream is = getClass().getResourceAsStream(pathToFile);
        if(is == null){
            System.out.println("is == null :" + (is == null));
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String str;
            LoadState ls = LoadState.STOP;
            while ((str = br.readLine()) != null) {
                if (str.equalsIgnoreCase("[Server]")) {
                    ls = LoadState.SERVER;
                } else if (str.equalsIgnoreCase("[Port]")) {
                    ls = LoadState.PORT;
                } else if (str.equalsIgnoreCase("[User]")) {
                    ls = LoadState.USER;
                } else if (str.equalsIgnoreCase("[Password]")) {
                    ls = LoadState.PASS;
                } else if (str.equalsIgnoreCase("[Database]")) {
                    ls = LoadState.DATABASE;
                } else {
                    ls = LoadState.STOP;
                }
                str = br.readLine();
                switch (ls) {
                    case SERVER:
                        server = str;
                        break;
                    case PORT:
                        port = str;
                        break;
                    case USER:
                        user = str;
                        break;
                    case PASS:
                        pass = str;
                        break;
                    case DATABASE:
                        database = str;
                        break;
                    case STOP:
                        break;
                }
                if (ls == LoadState.STOP) {
                    break;
                }
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            DBManager manager = DBManager.getInstance();
//            ResultSet rs = manager.executeSelect("select id_customer,"
//                    + " first_name, surname, date_of_birth, check_in_date,"
//                    + "check_out_date from customers;");
//            while (rs.next()) {
//                int id = rs.getInt(1);
//                String name = rs.getString(2);
//                String surname = rs.getString(3);
//                String dob = rs.getString(4);
//                String date1 = rs.getString(5);
//                String date2 = rs.getString(6);
//                System.out.format("id = %d name = %s surname = %s dob = %s"
//                        + " date1 = %s date2 = %s", id, name, surname, dob, date1, date2);
//                System.out.println("------------");
//            }
//            manager.disconnect();
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
