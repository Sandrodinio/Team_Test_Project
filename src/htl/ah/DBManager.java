package htl.ah;

import java.lang.System.Logger;
import java.sql.*;
import java.util.logging.*;

public class DBManager {
    private static DBManager instance = null;
    private static final Logger logger = FileLogger.getLogger();

    private DBManager() {
        try {


            logger.addHandler(FileLogger.getFileHandler()); 
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/Cds?serverTimezone=UTC", 
            "root", 
            "Simi"
        );
    }

    public void releaseConnection(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }

    public boolean speichereCD(CD x) {
        String insertSQL = "INSERT INTO cd (erscheinungsjahr, kuenstler, album) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); 
            PreparedStatement preparedStatement = con.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, x.getErscheinungsjahr());
            preparedStatement.setString(2, x.getKuenstler());
            preparedStatement.setString(3, x.getAlbum());
            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("CD gespeichert: " + x);
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Fehler beim Speichern der CD: " + e.getMessage());
            return false;
        }
    }

    public boolean loescheCD(CD x) {
        String deleteSQL = "DELETE FROM cd WHERE erscheinungsjahr = ? AND kuenstler = ? AND album = ?";
        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, x.getErscheinungsjahr());
            preparedStatement.setString(2, x.getKuenstler());
            preparedStatement.setString(3, x.getAlbum());
            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("CD gelöscht: " + x);
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Fehler beim Löschen der CD: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCD(CD oldCD, CD newCD)   {
        String updateSQL = "UPDATE cd SET erscheinungsjahr = ?, kuenstler = ?, album = ? WHERE erscheinungsjahr = ? AND kuenstler = ? AND album = ?";
        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(updateSQL)) {
            // Set new values
            preparedStatement.setInt(1, newCD.getErscheinungsjahr());
            preparedStatement.setString(2, newCD.getKuenstler());
            preparedStatement.setString(3, newCD.getAlbum());
            // Set old values for WHERE clause
            preparedStatement.setInt(4, oldCD.getErscheinungsjahr());
            preparedStatement.setString(5, oldCD.getKuenstler());
            preparedStatement.setString(6, oldCD.getAlbum());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("CD aktualisiert: " + newCD);
                return true;
            } else {
                logger.warning("Keine CD gefunden, die den Kriterien entspricht: " + oldCD);
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Fehler beim Aktualisieren der CD: " + e.getMessage());
        }
        return false;
    }
    public ObservableList<CD> ladeCDs() {
        ObservableList<CD> cds = FXCollections.observableArrayList();
        String selectSQL = "SELECT erscheinungsjahr, kuenstler, album FROM CD";
        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                CD x = new CD(resultSet.getInt("erscheinungsjahr"),
                              resultSet.getString("kuenstler"),
                              resultSet.getString("album"));
                cds.add(x);
            }
            logger.info("CDs geladen: " + cds.size());
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Fehler beim Laden der CDs: " + e.getMessage());
        }
        return cds;
    }
}

