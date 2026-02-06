package data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Classe che modella un generico item (coppia attributo-valore)
 */
public abstract class Item implements Serializable {
    /** Attributo coinvolto nell'item */
    protected Attribute attribute;
    /** Valore assegnato all'attributo */
    protected Object value;
    /** Codice seriale necessario per identificare l'oggetto una volta serializzato */
    private static final long serialVersionUID = 1667840417547366742L;

    /**
     * Inizializza i valori degli attributi
     *
     * @param attribute Attributo da coinvolgere nell'item
     * @param value Valore da assegnare all'attributo
     */
    Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Getter
     *
     * @return Restituisce l'attributo coinvolto nell'item
     */
    Attribute getAttribute() {
        return attribute;
    }

    /**
     * Getter
     *
     * @return Restituisce il valore dell'attributo
     */
    Object getValue() {
        return value;
    }

    /**
     * Stampa il valore dell'attributo
     *
     * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
     */
    public String toString() {
        return (String) value;
    }

    /**
     * Calcola la distanza tra due item, dipendentemente se sono istanze discrete o continue
     *
     * @param a Item da confrontare
     * @return Distanza tra i due item
     */
    abstract float distance(Object a);

    /**
     * Modifica il valore dell'attributo, assegnandoli il valore prototipo calcolato
     *
     * @param data Riferimento ad un oggetto della classe Data
     * @param clusteredData Insieme degli indici delle transizioni che fanno parte del cluster
     */
    public void update(Data data, HashSet<Integer> clusteredData) {
        value = data.computePrototype(clusteredData, attribute);
    }
}
