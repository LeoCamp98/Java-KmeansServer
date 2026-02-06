package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import database.TableSchema.Column;

/**
 * Classe che modella l'insieme di transazioni collezionate nella tabella
 */
public class TableData {
	/** Oggetto che modella la connessione al database */
	private DbAccess db;

	/**
	 * Inizializza l'oggetto che riferisce la connessione al database
	 *
	 * @param db Oggetto che modella la connessione al database
	 */
	public TableData(DbAccess db) {
		this.db=db;
	}

	/**
	 * Ottiene lo schema della tabella con nome "table" e esegue una query SQL per estratte tutte le tuple
	 * distinte da tale tabella. Per ogni tupla del ResultSet viene creato un oggetto istanza della classe Example,
	 * il cui riferimento viene inserito nella lista da restituire in output.
	 *
	 * @param table Nome della tabella nel database
	 * @return Restituisce la lista delle transazioni distinte memorizzate nella tabella
	 * @throws SQLException Sollevata se si verifica un errore SQL
	 * @throws EmptySetException Sollevata se viene restituito un ResultSet vuoto
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
		TableSchema schema = new TableSchema(db, table);
		List<Example> transazioni = new LinkedList<Example>();
		Statement stat = db.getConnection().createStatement();
		ResultSet rs = stat.executeQuery("SELECT * FROM " + table);

		if (!rs.next()) {
			throw new EmptySetException();
		} else {
			do {
				Example current = new Example();
				for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
					int j = i;
					if (schema.getColumn(j).isNumber()) current.add((rs.getFloat(j+1)));
					else current.add(rs.getString(j+1));
				}
				transazioni.add(current);
			} while (rs.next());
		}

		rs.close();
		stat.close();
		return transazioni;
	}

	/**
	 * Esegue una query SQL per estrarre i valori distinti ordinati di "column", per poi aggiungerli all'insieme da restituire
	 *
	 * @param table Nome della tabella
	 * @param column Nome della colonna
	 * @return Restituisce l'insieme di valori distinti ordinati, in modo ascendente, in base all'attributo identificato
	 * dalla colonna avente nome uguale a "column"
	 * @throws SQLException Sollevata se si verifica un errore SQL
	 */
	public Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException{
		Set<Object> valori = new TreeSet<>();
		Statement stat = db.getConnection().createStatement();
		ResultSet rs = stat.executeQuery("SELECT " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName() + " ASC");

		while (rs.next()) {
			if (column.isNumber()) {
				valori.add(rs.getFloat(column.getColumnName()));
			} else valori.add(rs.getString(column.getColumnName()));
		}

		rs.close();
		stat.close();

		return valori;
	}

	/**
	 * Esegue una query SQL per estrarre il valore aggregato (minimo o massimo) cercato nella colonna di nome "column"
	 * della tabella di nome "table"
	 *
	 * @param table Nome della tabella
	 * @param column Nome della colonna
	 * @param aggregate Operatore SQL di aggregazione (min, max)
	 * @return Restituisce l'aggregato cercato
	 * @throws SQLException Sollevata se si verifica un errore SQL
	 * @throws NoValueException Sollevata se il valore all'interno del ResultSet è assente
	 */
	public Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
		Statement stat = db.getConnection().createStatement();
		ResultSet rs = stat.executeQuery("SELECT " + aggregate + "(" + column.getColumnName() + ") AS Result FROM " + table);

		Object aggregValue;

		if (!rs.next()) throw new NoValueException();
		else {
			if (column.isNumber()) aggregValue = rs.getFloat("Result");
			else aggregValue = rs.getString("Result");
		}

		rs.close();
		stat.close();

		return aggregValue;
	}

}
