package data;

/**
 * Classe che modella una coppia Attributo continuo - valore numerico
 */
public class ContinuousItem extends Item {
    /**
     * Richiama il costruttore della superclasse
     *
     * @param attribute Attributo continuo
     * @param value Valore numerico
     */
    ContinuousItem(ContinuousAttribute attribute, Float value) {
        super(attribute, value);
    }

    /**
     * Determina la distanza (in valore assoluto) tra il valore scalato memorizzato nell'item corrente e quello
     * associato al parametro "a"
     *
     * @param a Item da confrontare
     * @return Distanza tra i due item
     */
    float distance(Object a) {
        float sub = ((ContinuousAttribute) attribute).getScaledValue((Float) this.getValue()) - ((ContinuousAttribute) attribute).getScaledValue((Float) a);
        return Math.abs(sub);
    }

    /**
     * Stampa il valore dell'item
     *
     * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
     */
    public String toString() {
        return getValue().toString();
    }
}
