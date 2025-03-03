package htl.ah;
import java.util.logging.*;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


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


	public boolean speichereBenutzer(Benutzer benutzer) {
	    String insertSQL = "INSERT INTO Benutzer (vorname, nachname, geburtsdatum) VALUES (?, ?, ?)";
	    try (Connection con = getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(insertSQL)) {
	        preparedStatement.setString(1, benutzer.getVorname());
	        preparedStatement.setString(2, benutzer.getNachname());
	        preparedStatement.setString(3, benutzer.getGeburtsdatum());
	        int rowsAffected = preparedStatement.executeUpdate();
	        logger.info("Benutzer gespeichert: " + benutzer);
	        return rowsAffected > 0;
	    } catch (SQLException | ClassNotFoundException e) {
	        logger.severe("Fehler beim Speichern des Benutzers: " + e.getMessage());
	        return false;
	    }
	}
	
	public boolean loescheBenutzer(Benutzer benutzer) {
	    String deleteSQL = "DELETE FROM Benutzer WHERE vorname = ? AND nachname = ? AND geburtsdatum = ?";
	    try (Connection con = getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(deleteSQL)) {
	        preparedStatement.setString(1, benutzer.getVorname());
	        preparedStatement.setString(2, benutzer.getNachname());
	        preparedStatement.setString(3, benutzer.getGeburtsdatum());
	        int rowsAffected = preparedStatement.executeUpdate();
	        logger.info("Benutzer gelöscht: " + benutzer);
	        return rowsAffected > 0;
	    } catch (SQLException | ClassNotFoundException e) {
	        logger.severe("Fehler beim Löschen des Benutzers: " + e.getMessage());
	        return false;
	    }
	}
	
	public boolean updateBenutzer(Benutzer oldBenutzer, Benutzer newBenutzer) {
	    String updateSQL = "UPDATE Benutzer SET vorname = ?, nachname = ?, geburtsdatum = ? WHERE vorname = ? AND nachname = ? AND geburtsdatum = ?";
	    try (Connection con = getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(updateSQL)) {
	        // Set new values
	        preparedStatement.setString(1, newBenutzer.getVorname());
	        preparedStatement.setString(2, newBenutzer.getNachname());
	        preparedStatement.setString(3, newBenutzer.getGeburtsdatum());
	        // Set old values for WHERE clause
	        preparedStatement.setString(4, oldBenutzer.getVorname());
	        preparedStatement.setString(5, oldBenutzer.getNachname());
	        preparedStatement.setString(6, oldBenutzer.getGeburtsdatum());
	
	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            logger.info("Benutzer aktualisiert: " + newBenutzer);
	            return true;
	        } else {
	            logger.warning("Kein Benutzer gefunden, der den Kriterien entspricht: " + oldBenutzer);
	            return false;
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        logger.severe("Fehler beim Aktualisieren des Benutzers: " + e.getMessage());
	        return false;
	    }
	}
	
	public ObservableList<Benutzer> ladeBenutzer() {
	    ObservableList<Benutzer> benutzerList = FXCollections.observableArrayList();
	    String selectSQL = "SELECT vorname, nachname, geburtsdatum FROM Benutzer";
	    try (Connection con = getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
	         ResultSet resultSet = preparedStatement.executeQuery()) {
	        while (resultSet.next()) {
	            Benutzer benutzer = new Benutzer(
	                resultSet.getString("vorname"),
	                resultSet.getString("nachname"),
	                resultSet.getString("geburtsdatum")
	            );
	            benutzerList.add(benutzer);
	        }
	        logger.info("Benutzer geladen: " + benutzerList.size());
	    } catch (SQLException | ClassNotFoundException e) {
	        logger.severe("Fehler beim Laden der Benutzer: " + e.getMessage());
	    }
	    return benutzerList;
	}
}

