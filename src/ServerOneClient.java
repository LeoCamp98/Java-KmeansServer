import data.Data;
import data.OutOfRangeSampleSize;
import mining.KMeansMiner;

import java.io.*;
import java.net.*;

/**
 * Classe che modella una connessione con il client
 */
public class ServerOneClient extends Thread {
    /** Oggetto che permette a due macchine di comunicare */
    private Socket socket;
    /** Stream di input del server */
    private ObjectInputStream in;
    /** Stream di input del client */
    private ObjectOutputStream out;
    /** Riferimento all'oggetto che esegue l'algoritmo kmeans */
    private KMeansMiner kmeans;

    /**
     * Inizializza la socket e i due stream e avvia un nuovo thread
     *
     * @param s Socket che permette la comunicazione tra client e server
     * @throws IOException Sollevata se l'input non e' valido
     */
    ServerOneClient(Socket s) throws IOException {
        socket = s;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        start();
    }

    /**
     * Gestisce le richieste del client
     */
    public void run() {
        try {
            Integer result = (Integer) in.readObject();
            Data data;
            do {
                if (result == 0) {
                    String tabName = (String) in.readObject();
                    data = new Data(tabName);
                    out.writeObject("OK");
                    result = (Integer) in.readObject();
                    do {
                        kmeans = null;
                        Integer numberOfClusters = (Integer) in.readObject();
                        kmeans=new KMeansMiner(numberOfClusters);
                        int numIter = 0;
                        try {
                            numIter = kmeans.kmeans(data);
                        } catch (OutOfRangeSampleSize e) {
                            System.err.println("Nome tabella o numero di cluster non valido.");
                            out.writeObject(e);
                        }
                        out.writeObject("OK");
                        out.writeObject("Numero di iterazioni: " + numIter);
                        out.writeObject(kmeans.getC().toString(data));
                        result = (Integer) in.readObject();
                        if (result == 2) {
                            kmeans.salva(tabName + "_" + numberOfClusters);
                            out.writeObject("OK");
                        }
                        result = (Integer) in.readObject();
                    } while (result == 1);
                }
                if (result == 3) {
                    String tabName = (String) in.readObject();
                    data = new Data(tabName);
                    Integer numberIterations = (Integer) in.readObject();
                    try {
                        kmeans=new KMeansMiner(tabName + "_" + numberIterations);
                        out.writeObject("OK");
                        out.writeObject(kmeans.getC().toString(data));
                    } catch (FileNotFoundException e) {
                        out.writeObject(e.getMessage());
                    }
                    result = (Integer) in.readObject();
                }
            } while (result == 0 || result == 3);
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e);
        }
    }
}
