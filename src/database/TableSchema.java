package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe che modella lo schema della tabella del database
 */
public class TableSchema {
	/** Oggetto che modella la connessione al database */
	private DbAccess db;
	/** Lista delle colonne della tabella */
	private List<Column> tableSchema=new ArrayList<Column>();

	/**
	 * Classe che modella la generica colonna della tabella
	 */
	public class Column{
		/** Nome della colonna */
		private String name;
		/** Tipo di dato contenuto nella colonna */
		private String type;

		/**
		 * Inizializza nome e tipo della colonna
		 *
		 * @param name Nome della colonna
		 * @param type Tipo della colonna
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}

		/**
		 * Getter
		 *
		 * @return Restituisce il nome della colonna
		 */
		String getColumnName(){
			return name;
		}

		/**
		 * Verifica se il tipo della colonna è un numero
		 *
		 * @return Restituisce vero se la colonna contiene solo numeri, falso altrimenti
		 */
		boolean isNumber(){
			return type.equals("number");
		}

		/**
		 * Stampa nome e tipo della colonna
		 *
		 * @return Restituisce una stringa che rappresenta lo stato dell'oggetto
		 */
		public String toString(){
			return name+":"+type;
		}
	}

	/**
	 * Inizializza la connessione al database e il nome della tabella. Inoltre associa alle colonne i vari tipi di dato
	 * che possono contenere
	 *
	 * @param db Oggetto che riferisce la connessione al database
	 * @param tableName Nome della tabella
	 * @throws SQLException Sollevata se si verifica un errore SQL
	 */
	TableSchema(DbAccess db, String tableName) throws SQLException{
		this.db=db;
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
	
		Connection con=db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);
		   
		while (res.next()) {
			if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
		}
		res.close();
	}

	/**
	 * Calcola la grandezza della tabella
	 *
	 * @return Restituisce il numero degli attributi della tabella
	 */
	int getNumberOfAttributes(){
			return tableSchema.size();
		}

	/**
	 * Restituisce la colonna avente indice pari a "index"
	 *
	 * @param index Indice della colonna
	 * @return Restituisce un oggetto Column
	 */
	Column getColumn(int index){
			return tableSchema.get(index);
		}

	}

		     


