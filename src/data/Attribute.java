package data;

import java.io.Serializable;

/**
 * Classe che modella l'entità attributo
 */
public abstract class Attribute implements Serializable {
    /** Nome simbolico dell'attributo */
    private String name;
    /** Identificativo numerico dell'attributo */
    private int index;
    /** Codice seriale necessario per identificare l'oggetto una volta serializzato */
    protected static final long serialVersionUID = 636891980013353479L;

    /**
     * Inizializza nome e indice dell'attributo
     *
     * @param name Nome dell'attributo
     * @param index Identificativo numerico dell'attributo
     */
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Getter
     *
     * @return Restituisce il nome dell'attributo
     */
    String getName() {
        return name;
    }

    /**
     * Getter
     *
     * @return Restituisce l'identificativo numerico dell'attributo
     */
    int getIndex() {
        return index;
    }

    /**
     * Stampa il nome dell'attributo
     *
     * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
     */
    public String toString() {
        return name;
    }
}
