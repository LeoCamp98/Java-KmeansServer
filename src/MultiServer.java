import java.io.IOException;
import java.net.*;

/**
 * Classe che modella il server
 */
public class MultiServer {
    /** Porta su cui il server si porrà in ascolto */
    private static int PORT = 8080;
    public static void main(String[] args) {
        try {
            new MultiServer(PORT);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Inizializza la porta ed invoca il metodo run()
     *
     * @param port Porta su cui il server si metterà in ascolto
     * @throws IOException Sollevata se l'input non e' valido
     */
    private MultiServer(int port) throws IOException {
        PORT = port;    // inizializzo la porta
        run();
    }

    /**
     * Istanzia un oggetto istanza della classe ServerSocket che si pone in attesa di richieste di connessioni da
     * parte del client. Ad ogni nuova richiesta di connessione viene istanziato un oggetto della classe ServerOneClient
     *
     * @throws IOException Sollevata se l'input non e' valido
     */
    private void run() throws IOException {
        ServerSocket s = new ServerSocket(PORT);    // creo l'oggetto ServerSocket
        System.out.println("Server started");
        try {
            while (true) {  // rimane in attesa di una connessione
                Socket socket = s.accept();
                System.out.println("Connessione effettuata");
                try {
                    new ServerOneClient(socket);    // ad ogni nuova richiesta creo un'istanza di ServerOneClient
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            s.close();
        }
    }
}
