package mining;

import data.Data;
import data.Tuple;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe che modella un cluster
 */
public class Cluster implements Serializable {
	/** Tupla che rappresenta il centroide del cluster */
	private Tuple centroid;
	/** Insieme degli indici delle transizioni appartenenti al cluster */
	private Set<Integer> clusteredData = new HashSet<Integer>();
	/** Codice seriale necessario per identificare l'oggetto una volta serializzato */
	private static final long serialVersionUID = 2243471266343333368L;

	/**
	 * Inizializza il centroide del cluster
	 *
	 * @param centroid Tupla che diventerà il centroide del cluster
	 */
	Cluster(Tuple centroid){
		this.centroid=centroid;
	}

	/**
	 * Getter
	 *
	 * @return Restituisce il centroide del cluster
	 */
	Tuple getCentroid(){
		return centroid;
	}

	/**
	 * Calcola il centroide
	 *
	 * @param data Riferimento all'insieme delle transazioni
	 */
	void computeCentroid(Data data){
		for(int i=0;i<centroid.getLength();i++){
			centroid.get(i).update(data, (HashSet<Integer>) clusteredData);
		}
	}

	/**
	 * Verifica se la tupla avente indice "id" ha cambiato cluster
	 *
	 * @param id Indice della tupla
	 * @return Restituisce vero se la tupla ha cambiato cluster
	 */
	boolean addData(int id){
		return clusteredData.add(id);
	}

	/**
	 * Verifica se la transazione avente indice "id" è clusterizzata nell'insieme "clusteredData" corrente
	 *
	 * @param id Indice della tupla
	 * @return Restituisce vero se la transazione è contenuta nell'insieme "clusteredData" corrente
	 */
	boolean contain(int id){
		return clusteredData.contains(id);
	}

	/**
	 * Rimuove la tupla avente indice id dall'insieme "clusteredData" corrente (quella che ha cambiato cluster)
	 *
	 * @param id Indice della tupla
	 */
	void removeTuple(int id){
		clusteredData.remove(id);
	}

	/**
	 * Stampa il centroide del cluster
	 *
	 * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
	 */
	public String toString(){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i);
		str+=")";
		return str;
	}

	/**
	 * Stampa, per ogni cluster, l'insieme di transazioni clusterizzate con relativa distanza rispetto al centroide e
	 * distanza media di tutte le transazioni
	 *
	 * @param data Riferimento all'insieme delle transazioni
	 * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
	 */
	public String toString(Data data){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i)+ " ";
		str+=")\nExamples:\n";
		Object[] array =clusteredData.toArray();
		for(int i=0;i<array.length;i++){
			str+="[";
			str+=data.getAttributeValue((Integer) array[i])+" ";
			str+="] dist="+getCentroid().getDistance(data.getItemSet((Integer) array[i]))+"\n";
			
		}
		str+="AvgDistance="+getCentroid().avgDistance(data, array)+"\n";
		return str;
	}
}
