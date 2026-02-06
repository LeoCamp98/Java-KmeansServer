package data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import database.*;

/**
 * Classe che modella l'insieme delle transazioni che saranno oggetto dell'attività di clustering
 */
public class Data implements Serializable {
	/**	Lista di esempi */
	private List<Example> data = new ArrayList<Example>();
	/** Numero delle transazioni/esempi */
	private int numberOfExamples;
	/** Lista degli attributi */
	private List<Attribute> attributeSet = new LinkedList<Attribute>();
	/** Numero di tuple distinte */
	private int distinctTuples;
	/** Codice seriale necessario per identificare l'oggetto una volta serializzato */
	private static final long serialVersionUID = 636891380013313479L;

	/**
	 * Costruttore che carica i dati di addestramento dalla tabella di nome "table"
	 *
	 * @param table Nome della tabella
	 */
	public Data(String table){
		DbAccess accesso = new DbAccess();
		try {
			accesso.initConnection();
		} catch (DatabaseConnectionException e) {
			System.err.println("Errore nella connessione al database.");
		}

		TableData tableD = new TableData(accesso);

		try {
			data = tableD.getDistinctTransazioni(table);
		} catch (SQLException e) {
			System.err.println("Errore nella esecuzione della query.");
		} catch (EmptySetException e) {
			System.err.println("Il result set è vuoto.");
		}

		try {
			accesso.closeConnection();
		} catch (SQLException e) {
			System.err.println("Errore nella chiusura della connessione al database.");
		}

		numberOfExamples = data.size();
		distinctTuples = data.size();

		TreeSet<String> outLookValues = new TreeSet<String>();
		outLookValues.add("overcast");
		outLookValues.add("rain");
		outLookValues.add("sunny");
		attributeSet.add(0, new DiscreteAttribute("Outlook", 0, outLookValues));

		attributeSet.add(1, new ContinuousAttribute("Temperature", 1, 3.2, 38.7));

		TreeSet<String> humidityValues = new TreeSet<String>();
		humidityValues.add("high");
		humidityValues.add("normal");
		attributeSet.add(2, new DiscreteAttribute("Humidity", 2, humidityValues));

		TreeSet<String> windValues = new TreeSet<String>();
		windValues.add("weak");
		windValues.add("strong");
		attributeSet.add(3, new DiscreteAttribute("Wind", 3, windValues));

		TreeSet<String> playTennisValues = new TreeSet<String>();
		playTennisValues.add("no");
		playTennisValues.add("yes");
		attributeSet.add(4, new DiscreteAttribute("PlayTennis", 4, playTennisValues));
	}

	/**
	 * Calcola il numero degli esempi
	 *
	 * @return Restituisce la lunghezza della lista delle transazioni
	 */
	public int getNumberOfExamples(){
		return numberOfExamples;
	}

	/**
	 * Calcola il numero degli attributi
	 *
	 * @return Restituisce la lunghezza della lista degli attributi
	 */
	private int getNumberOfAttributes(){
		return attributeSet.size();
	}

	/**
	 * Restituisce lo schema di un attributo
	 *
	 * @param index Indice dell'attributo
	 * @return Restituisce lo schema dei dati dell'attributo avente indice "index"
	 */
	List<Attribute> getAttribute(int index){
		return attributeSet;
	}

	/**
	 * Restituisce una specifica transazione della lista
	 *
	 * @param exampleIndex Indice della transazione
	 * @return Restituisce la transazione avente indice pari a "exampleIndex"
	 */
	public Example getAttributeValue(int exampleIndex){
		return data.get(exampleIndex);
	}

	/**
	 * Stampa tutte le transazioni presenti nella lista
	 *
	 * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
	 */
	public String toString() {
		String string = "";
		for (Attribute attribute : attributeSet) {
			System.out.print(attribute + " ");
		}
		for (Example example : data) {
			string += "\n" + example;
		}
		return string;
	}

	/**
	 * Crea un'istanza di Tuple che modella la transazione con indice di riga "index" nella lista e restituisce
	 * il riferimento a tale istanza
	 *
	 * @param index Indice di riga della transazione
	 * @return Restituisce la tupla avente indice pari a "index"
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(getNumberOfAttributes());
		for (int i = 0; i < getNumberOfAttributes(); i++) {
			if (attributeSet.get(i) instanceof ContinuousAttribute)	tuple.add(new ContinuousItem((ContinuousAttribute) attributeSet.get(i), (Float) data.get(index).get(i)), i);
			if (attributeSet.get(i) instanceof DiscreteAttribute) tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i), (String) data.get(index).get(i)), i);
		}
		return tuple;
	}

	/**
	 * Sceglie casualmente k centroidi in "data"
	 *
	 * @param k Numero di cluster da generare
	 * @return Restituisce un array di k interi rappresentante gli indici di riga in "data" per le tuple
	 * inizialmente scelte come centroidi
	 * @throws OutOfRangeSampleSize Sollevata se il numero di cluster inserito da tastiera è maggiore rispetto al numero
	 * di centroidigenerabili dall'insieme di transazioni
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize {
		if (k <= 0 || k > distinctTuples) throw new OutOfRangeSampleSize();
		int centroidIndexes[] = new int[k];
		// choose k random different centroids in data
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		for (int i = 0; i < k; i++) {
			boolean found = false;
			int c;
			do {
				found = false;
				c = rand.nextInt(getNumberOfExamples());
				// verify that centroid[c] is not equal to a centroid already stored in CentroidIndexes
				for (int j = 0; j < i; j++) {
					if (compare(centroidIndexes[j], c)) {
						found = true;
						break;
					}
				}
			}
			while (found);
			centroidIndexes[i] = c;
		}
		return centroidIndexes;
	}

	/**
	 * Verifica se due transazioni sono uguali
	 *
	 * @param i Indice di una transazione in Data
	 * @param j Indice di una transazione in data
	 * @return Restituisce vero se le due transazioni contengono gli stessi valori, falso altrimenti
	 */
	private boolean compare(int i, int j) {
		for (int columnIndex = 0; columnIndex < getNumberOfAttributes(); columnIndex++) {
			if (!getAttributeValue(i).equals(getAttributeValue(j))) return false;
		}
		return true;
	}

	/**
	 * Utilizza lo RTTI per determinare se "attribute" riferisce un'istanza di ContinuousAttribute o di
	 * DiscreteAttribute e chiama il relativo metodo per calcolare il valore prototipo
	 *
	 * @param idList Insieme degli indici delle transizioni appartenenti ad un cluster
	 * @param attribute Attributo rispetto al quale calcolare il prototipo (centroide)
	 * @return Restituisce il valore prototipo calcolato rispetto all'attributo passato in input
	 */
	Object computePrototype(HashSet<Integer> idList, Attribute attribute) {
		if (attribute instanceof ContinuousAttribute) return computePrototype(idList, (ContinuousAttribute) attribute);
		else return computePrototype(idList, (DiscreteAttribute) attribute);
	}

	/**
	 * Determina il valore prototipo come media dei valori osservati per "attribute" nelle transazioni della lista
	 * aventi indice di riga in "idList"
	 *
	 * @param idList Insieme degli indici delle transizioni appartenenti ad un cluster
	 * @param attribute Attributo continuo rispetto al quale calcolare il prototipo (centroide)
	 * @return Restituisce il valore prototipo calcolato rispetto all'attributo passato in input
	 */
	private Float computePrototype(HashSet<Integer> idList, ContinuousAttribute attribute) {
		float sum = 0.0F;
		for (int i = 0; i < getNumberOfExamples(); i++) {
			if (idList.contains(i)) sum += (Float) getAttributeValue(i).get(attribute.getIndex());
		}
		return Float.parseFloat(String.format(Locale.US,"%.2f", sum / idList.size()));
	}

	/**
	 * Determina il valore prototipo come media dei valori osservati per "attribute" nelle transazioni della lista
	 * aventi indice di riga in "idList"
	 *
	 * @param idList Insieme degli indici delle transizioni appartenenti ad un cluster
	 * @param attribute Attributo discreto rispetto al quale calcolare il prototipo (centroide)
	 * @return Restituisce il valore prototipo calcolato rispetto all'attributo passato in input
	 */
	private String computePrototype(HashSet<Integer> idList, DiscreteAttribute attribute) {
		int maxFreq = 0;
		String maxFreqElem = "";
		for (String element:attribute) {
			int frequency = attribute.frequency(this, idList, element);
			if (frequency > maxFreq) {
				maxFreq = frequency;
				maxFreqElem = element;
			}
		}
		return maxFreqElem;
	}

	public static void main(String args[]){
		Data trainingSet=new Data("playtennis");
		System.out.println(trainingSet);
	}
}
