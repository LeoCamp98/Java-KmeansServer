package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che modella l'accesso al database
 */
public class DbAccess {
    /** Driver necessario per utilizzare il DBMS mysql */
    private String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    /** Nome del DBMS */
    private final String DBMS = "jdbc:mysql";
    /** Identificativo del server su cui risiede il database */
    private final String SERVER = "localhost";
    /** Nome del database */
    private final String DATABASE = DATABASE;
    /** Porta su cui il DBMS è in esecuzione */
    private final String PORT = PORT;
    /** Nome dell'utente per accedere al database */
    private final String USER_ID = USER_ID;
    /** Password per autenticare l'utente */
    private final String PASSWORD = PASSOWRD;
    /** Oggetto che gestisce la connessione */
    private Connection conn;

    /**
     * Ordina al Class Loader di caricare in memoria il driver mysql e inizializza la connessione
     *
     * @throws DatabaseConnectionException Sollevata se si verifica un errore nella connessione al database
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            System.err.println("Impossibile trovare la classe specificata.");
        }
        String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?user=" + USER_ID + "&password=" + PASSWORD;
        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.err.println("Errore nell'accesso al database.");
        }
    }

    /**
     * Getter
     *
     * @return Restituisce l'oggetto Connection
     */
    Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione
     *
     * @throws SQLException Sollevata se si verifica un errore SQL
     */
    public void closeConnection() throws SQLException {
        conn.close();
    }
}
