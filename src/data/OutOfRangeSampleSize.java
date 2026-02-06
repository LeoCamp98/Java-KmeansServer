package data;

/**
 * Eccezione che indica che il numero di cluster inserito da tastiera è maggiore rispetto al numero di centroidi
 * generabili dall'insieme di transazioni
 */
public class OutOfRangeSampleSize extends Exception {
    public OutOfRangeSampleSize() {}
}
