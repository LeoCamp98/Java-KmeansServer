package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Claase che modella una transazione letta dalla tabella del database
 */
public class Example implements Comparable<Example>, Serializable {
	/** Lista dei campi della transazione */
	private List<Object> example=new ArrayList<Object>();
	/** Codice seriale necessario per identificare l'oggetto una volta serializzato */
	private static final long serialVersionUID = -636891981598353440L;

	/**
	 * Aggiunge all'oggetto Example un nuovo campo con valore "o"
	 *
	 * @param o Oggetto da aggiungere
	 */
	void add(Object o){
		example.add(o);
	}

	/**
	 * Restituisce l'oggetto avente indice pari a "i"
	 *
	 * @param i Indice del campo da cui leggere l'oggetto
	 * @return Restituisce l'oggetto memorizzato nel campo avente indice "i"
	 */
	public Object get(int i){
		return example.get(i);
	}

	/**
	 * Compara due transazioni e restituisce:
	 * 	0 - se i due campi sono uguali
	 * 	1 - se il campo della transazione passata in input è minore
	 * 	-1 - se il campo della transazione passata in input è maggiore
	 *
	 * @param ex La transazione da comparare
	 * @return Intero che indica quale transazione è "più grande"
	 */
	public int compareTo(Example ex) {
		int i=0;
		for(Object o:ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}

	/**
	 * Stampa ogni campo della transazione
	 *
	 * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
	 */
	public String toString(){
		String str="";
		for(Object o:example)
			str+=o.toString()+ " ";
		return str;
	}
	
}