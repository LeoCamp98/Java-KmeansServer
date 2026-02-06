package mining;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;
import java.io.Serializable;

/**
 * Classe che modella un insieme di cluster
 */
public class ClusterSet implements Serializable {
    /** Array di cluster */
    private Cluster C[];
    /** Posizione valida per la memorizzazione di un nuovo cluster in C */
    private int i = 0;
    /** Codice seriale necessario per identificare l'oggetto una volta serializzato */
    private static final long serialVersionUID = 6341206266343347468L;

    /**
     * Crea l'oggetto riferito da C
     *
     * @param k Numero di cluster da generare
     */
    ClusterSet(int k) {
        C = new Cluster[k];
    }

    /**
     * Assegna il cluster "c" al ClusterSet in posizione "i" e incrementa "i"
     *
     * @param c Cluster da aggiungere al ClusterSet
     */
    private void add(Cluster c) {
        C[i] = c;
        i++;
    }

    /**
     * Restituisce il cluster che si trova in posizione "i" nel ClusterSet
     *
     * @param i Posizione del cluster interessato
     * @return Restituisce il cluster in posizione "i"
     */
    private Cluster get(int i) {
        return C[i];
    }

    /**
     * Sceglie i centroidi, crea un cluster per ogni centroide e lo memorizza nel ClusterSet
     *
     * @param data Riferimento all'insieme delle transazioni
     * @throws OutOfRangeSampleSize Sollevata se il numero di cluster inserito da tastiera è maggiore rispetto al numero di centroidi
     * generabili dall'insieme di transazioni
     */
    void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int centroidIndexes[] = data.sampling(C.length);
        for (int i = 0; i < centroidIndexes.length; i++) {
            Tuple centroidI = data.getItemSet(centroidIndexes[i]);
            add(new Cluster(centroidI));
        }
    }

    /**
     * Calcola la distanza tra la tupla passata in input ed il centroide di ciascun cluster presente nel ClusterSet
     *
     * @param tuple Riferimento ad un oggetto Tuple
     * @return Restituisce il cluster più vicino alla tupla passata in input
     */
    Cluster nearestCluster (Tuple tuple) {
        double minDistance = tuple.getDistance(get(0).getCentroid());
        // mining.Cluster nearestCluster = get(0);
        int index = 0;
        for (int i = 1; i < C.length; i++) {
            double actualDistance = tuple.getDistance(get(i).getCentroid());
            if (actualDistance < minDistance) {
                minDistance = actualDistance;
                index = i;
            }
        }
        return get(index);
    }

    /**
     * Identifica il cluster di appartenenza di una tupla
     *
     * @param id Indice di una transazione presente in Data
     * @return Restituisce il cluster di appartenenza della tupla avente indice pari a "id"
     */
    Cluster currentCluster(int id) {
        for (int i = 0; i < C.length; i++) {
            if (get(i).contain(id)) return get(i);
        }
        return null;
    }

    /**
     * Calcola il nuovo centroide per ciascun cluster presente nel ClusterSet
     *
     * @param data Riferimento all'insieme delle transazioni
     */
    void updateCentroids(Data data) {
        for (Cluster c : C) c.computeCentroid(data);
    }

    /**
     * Stampa il centroide di ciascun cluster presente nel ClusterSet
     *
     * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            str += get(i).toString();
        }
        return str;
    }

    /**
     * Stampa lo stato di ciascun cluster presente nel ClusterSet
     *
     * @param data Riferimento all'insieme delle transazioni
     * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
     */
    public String toString(Data data) {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ":" + C[i].toString(data) + "\n";
            }
        }
        return str;
    }
}
