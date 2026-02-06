package mining;

import data.Data;
import data.OutOfRangeSampleSize;
import java.io.*;

/**
 * Classe che modella l'implementazione dell'algoritmo kmeans
 */
public class KMeansMiner implements Serializable {
    /** Insieme di cluster */
    private ClusterSet C;
    /** Codice seriale necessario per identificare l'oggetto una volta serializzato */
    private static final long serialVersionUID = -1234571266343333368L;

    /**
     * Crea l'oggetto riferito da "C"
     *
     * @param k Numero di cluster da generare
     */
    public KMeansMiner(int k) {
        C = new ClusterSet(k);
    }

    /**
     * Costruttore che deserializza il contenuto del file avente nome "fileName" e lo assegna a "C"
     *
     * @param fileName Nome del file da deserializzare
     * @throws FileNotFoundException Sollevata se si verifica un errore nell'apertura del file
     * @throws IOException Sollevata se l'input non e' valido
     * @throws ClassNotFoundException Sollevata se si cerca di caricare l'Object Class di una classe che non e' stata definita
     */
    public KMeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName + ".dat"));
        C = (ClusterSet) in.readObject();
        in.close();
    }

    /**
     * Apre il file avente nome "fileName" e salva l'oggetto riferito da "C" in tale file
     *
     * @param fileName Nome del file in cui serializzare
     * @throws FileNotFoundException Sollevata se si verifica un errore nell'apertura del file
     * @throws IOException Sollevata se l'input non e' valido
     */
    public void salva(String fileName) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName + ".dat"));
        out.writeObject(C);
        out.close();
    }

    /**
     * Getter
     *
     * @return Restituisce il ClusterSet
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     *
     * @param data Riferimento all'insieme delle transazioni
     * @return Restituisce il numero di iterazioni effettuate
     * @throws OutOfRangeSampleSize Sollevata se il numero di cluster inserito da tastiera è maggiore rispetto al numero
     * di centroidi generabili dall'insieme di transazioni
     */
    public int kmeans (Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        // 1. Scelta casuale di centroidi per k clusters
        C.initializeCentroids(data); // non necessario se leggo da file il clusterSet
        boolean changedCluster = false;
        do {
            numberOfIterations++;
            // 2. Assegnazione di ciascuna riga della matrice in data al cluster avente centroide più vicino all'esempio
            changedCluster = false;
            for (int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if (currentChange) changedCluster = true;
                // rimuovo la tupla dal vecchio cluster
                if (currentChange && oldCluster != null) {
                    // il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
                }
            }
            // 3. Calcolo dei nuovi centroidi per ciascun cluster
            C.updateCentroids(data);
        }
        // 4. Ripeto i passi 2 e 3 finché due iterazioni consecutive non restituiscono centroidi uguali
        while (changedCluster);
        return numberOfIterations;
    }
}
