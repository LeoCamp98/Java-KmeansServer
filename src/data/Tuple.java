package data;

import java.io.Serializable;

/**
 * Classe che modella una sequenza di coppie Attributo - Valore
 */
public class Tuple implements Serializable {
    /** Array di item */
    private Item[] tuple;
    /** Codice seriale necessario per identificare l'oggetto una volta serializzato */
    private static final long serialVersionUID = -3127423075771707305L;

    /**
     * Costruisce l'oggetto riferito da "tuple"
     *
     * @param size Numero di item che costituirà la tupla
     */
    Tuple(int size) {
        tuple = new Item[size];
    }

    /**
     * Calcola la lunghezza della tupla
     *
     * @return Restituisce la lunghezza della tupla
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'item in posizione "i"
     *
     * @param i Posizione dell'item interessato
     * @return Restituisce un Item
     */
    public Item get(int i) {
        return tuple[i];
    }

    /**
     * Memorizza "c" nell'array "tuple" in posizione "i"
     *
     * @param c Item da salvare nella tupla
     * @param i Posizione in cui salvaore l'item
     */
    void add(Item c, int i) {
        tuple[i] = c;
    }

    /**
     * Determina la distanza tra due tuple. La distanza è ottenuta come la somma delle distanza tra gli item
     * in posizioni uguali nelle due tuple
     *
     * @param obj Tuple da confrontare
     * @return Restituisce la distanza tra le due tuple
     */
    public double getDistance(Tuple obj) {
        double distance = 0.0;
        for (int i = 0; i < getLength(); i++) {
            distance += obj.get(i).distance(this.get(i).getValue());
        }
        //return Double.parseDouble(String.format(Locale.US,"%.1f", distance));
        return distance;
    }

    /**
     * Calcola la media delle distanze tra la tupla corrente e quelle presenti in "data" aventi indice in
     * "clusteredData"
     *
     * @param data Riferimento all'oggetto Data
     * @param clusteredData Insieme degli indici delle transizioni che fanno parte del cluster
     * @return Restituisce la media delle distanze tra la tupla corrente e clusterizzate
     */
    public double avgDistance(Data data, Object[] clusteredData) {
        double p, sumD = 0.0;
        for (int i = 0; i < clusteredData.length; i++) {
            double d = getDistance(data.getItemSet((Integer) clusteredData[i]));
            sumD += d;
        }
        p = sumD / clusteredData.length;
        //return Double.parseDouble(String.format(Locale.US,"%.1f", p));
        return p;
    }

}
