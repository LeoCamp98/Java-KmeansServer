package data;

/**
 * Classe che modella una coppia Attributo discreto - Valore discreto
 */
public class DiscreteItem extends Item {

    /**
     * Invoca il costruttore della superclasse
     *
     * @param attribute Attributo coinvolto nell'item
     * @param value Valore assegnato all'attributo
     */
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra due istanze di DiscreteItem
     *
     * @param a Item da confrontare
     * @return Restituisce 0 se i due valori sono uguali, 1 altrimenti
     */
    float distance(Object a) {
        if (getValue().equals(a)) return 0;
        else return 1;
    }
}
