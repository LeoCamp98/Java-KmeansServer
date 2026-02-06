package data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Claase che modella un attributo discreto
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {
    /** Insieme dei valori che l'attributo discreto può assumere */
    private TreeSet<String> values;

    /**
     * Invoca il costruttore della superclasse e inizializza "values"
     *
     * @param name Nome dell'attributo
     * @param index Identificativo numerico dell'attributo
     * @param values Dominio dell'attributo
     */
    DiscreteAttribute(String name, int index, TreeSet<String> values) {
        super(name, index);
        this.values = values;
    }

    /**
     * Calcola la dimensione di "values"
     *
     * @return Restituisce il numero di valori discreti nel dominio dell'attributo
     */
    protected int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Determina il numero di volte che il valore "v" compare in corrispondenza dell'attributo corrente negli
     * esempi memorizzati in "data" e indicizzate da "idList"
     *
     * @param data Riferimento all'oggetto Data
     * @param idList Insieme degli indici delle transizioni che fanno parte del cluster
     * @param v Valore discreto
     * @return Numero di occorrenze del valore discreto
     */
    int frequency(Data data, HashSet<Integer> idList, String v) {
        int count = 0;
        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            if (idList.contains(i)) {
                if (data.getAttributeValue(i).get(this.getIndex()).equals(v)) count++;
            }
        }
        return count;
    }

    /**
     * Genera un iteratore
     *
     * @return Restituisce un iteratore del TreeSet
     */
    public Iterator<String> iterator() {
        return values.iterator();
    }
}

