package data;

/**
 * Classe che modella un attributo continuo
 */
public class ContinuousAttribute extends Attribute {
    /** Valore massimo che l'attributo può assumere */
    private double max;
    /** Valore minimo che l'attributo può assumere */
    private double min;

    /**
     * Invoca il costruttore della superclasse e inizializza i due estremi
     *
     * @param name Nome dell'attributo
     * @param index Identificativo numerico dell'attributo
     * @param min Valore minimo dell'attributo
     * @param max Valore massimo dell'attributo
     */
    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.max = max;
        this.min = min;
    }

    /**
     * Calcola il valore normalizzato del parametro passato in input.
     *
     * @param v Valore dell'attributo da normalizzare
     * @return Restituisce il valore normalizzato
     */
    protected float getScaledValue (float v) {
        return (float) ((v - min) / (max - min));
    }
}
