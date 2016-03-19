package persistencia;


/**
 * La version 14 de la BD, incluye el campo de numero telefonico el cual sera enviaado al servidor
 * con fines de crear un directorio telefonico.
 */
import java.io.File;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import Util.Parametros;


public class SQLite {
	private static final int 	versionBD = 1;
    private Context             ctx;
	private BDHelper 			nHelper;
	private SQLiteDatabase 		nBD;
	
	private boolean ValorRetorno;
	

	private static class BDHelper extends SQLiteOpenHelper{
		
		public BDHelper(Context context) {
			super(context, Parametros.CARPETA_RAIZ + File.separator + Parametros.NOMBRE_DATABASE, null, SQLite.versionBD);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			/** Tabla que contendra informacion general del FEED **/
			db.execSQL(	"CREATE TABLE feed (" +
					"autor 					TEXT NOT NULL," +
					"fecha_actualizacion 	TEXT NOT NULL," +
					"derechos 				TEXT NOT NULL," +
					"titulo					TEXT NOT NULL PRIMARY KEY);");


			/** Tabla para el registro del top de las aplicaciones **/
			db.execSQL("CREATE TABLE entry (" +
					"id_aplicacion 	TEXT PRIMARY KEY," +
					"nombre 		TEXT NOT NULL," +
					"imagen 		TEXT NOT NULL," +
					"resumen 		TEXT NOT NULL," +
					"precio			TEXT NOT NULL," +
					"derechos		TEXT NOT NULL," +
					"artista		TEXT NOT NULL," +
					"link_descarga	TEXT NOT NULL," +
					"categoria		TEXT NOT NULL);");
        }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}


	public SQLite (Context _ctx){
        this.ctx = _ctx;
	}

	public int getVersionBD(){
		return versionBD;
	}

	public String getNameBD(){
		return Parametros.NOMBRE_DATABASE;
	}

	private SQLite abrir(){
		nHelper = new BDHelper(this.ctx);
		nBD = nHelper.getWritableDatabase();
		return this;
	}

	private void cerrar() {
		nHelper.close();
	}


	/**Funcion para realizar INSERT
	 * @param _tabla 		-> tabla a la cual se va a realizar el INSERT
	 * @param _informacion	-> informacion que se va a insertar
	 * @return				-> true si se realizo el insert correctamente, false en otros casos
	 */
	public boolean InsertRegistro(String _tabla, ContentValues _informacion){
		abrir();
		ValorRetorno = false;
		try{
			if(nBD.insert(_tabla,null, _informacion)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){
			e.toString();
		}
		cerrar();
		return ValorRetorno;
	}


	/**
	 *
	 * @param Tabla
	 * @param Informacion
	 * @param Condicion
	 * @return
	 */
	public boolean UpdateRegistro(String Tabla, ContentValues Informacion, String Condicion){
		ValorRetorno = false;
		abrir();
		try{
			if(nBD.update(Tabla, Informacion, Condicion, null)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){
		}
		cerrar();
		return ValorRetorno;
	}


	/**Funcion para eliminar un registro de una tabla en particular
	 * @param _tabla		->tabla sobre la cual se va a trabajar
	 * @param _condicion	->condicion que debe cumplirse para ejecutar el delete respectivo
	 * @return				->true si fue ejecutado el delete correctamente, false en caso contrario
	 */
	public boolean DeleteRegistro(String _tabla, String _condicion){
		ValorRetorno = false;
		abrir();
		try{
			if(nBD.delete(_tabla, _condicion, null)>0){
				ValorRetorno = true;
			}
		}catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return ValorRetorno;
	}


    /**Funcion para eliminar un registro de una tabla en particular
     * @param _tabla		->tabla sobre la cual se va a trabajar
     * @return				->true si fue ejecutado el delete correctamente, false en caso contrario
     */
    public boolean DeleteTabla(String _tabla){
        ValorRetorno = false;
        abrir();
        try{
            if(nBD.delete(_tabla, null, null)>0){
                ValorRetorno = true;
            }
        }catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        cerrar();
        return ValorRetorno;
    }


	/**
	 * @param _tabla    ->tabla sobre la cual se va a relizar la consulta
	 * @param _campos   ->campos que se desean obtener datos
	 * @return          ->ArrayList de content values con la informacion obtenida en la consulta
	 */
	public ArrayList<ContentValues> SelectData(String _tabla, String _campos){
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla, null);
			String[] _columnas = c.getColumnNames();

			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_registro.put(_columnas[i], c.getString(i));
				}
				_query.add(_registro);
			}
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		this.cerrar();
		return _query;
	}


	/**Funcion encargada de realizar una consulta y retornarla con un array list de content values, similar a un array de diccionarios
	 * @param _tabla		->tabla sobre la cual va a correr la consulta
	 * @param _campos		->campos que se van a consultar
	 * @param _condicion	->condicion para filtrar la consulta
	 * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
	 */
	public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion){
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion, null);
			String[] _columnas = c.getColumnNames();

			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_registro.put(_columnas[i], c.getString(i));
				}
				_query.add(_registro);
			}
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		this.cerrar();
		return _query;
	}


	/**
	 * @param _tabla		->tabla sobre la cual va a correr la consulta
	 * @param _campos		->campos que se van a consultar
	 * @param _condicion	->condicion para filtrar la consulta
	 * @param _orden        ->orden de como se muestran los resultados
	 * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
	 */
	public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion, String _orden){
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" ORDER BY "+_orden, null);
			String[] _columnas = c.getColumnNames();

			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_registro.put(_columnas[i], c.getString(i));
				}
				_query.add(_registro);
			}
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		this.cerrar();
		return _query;
	}



	/**
	 * @param _tabla		->tabla sobre la cual va a correr la consulta
	 * @param _campos		->campos que se van a consultar
	 * @param _condicion	->condicion para filtrar la consulta
	 * @param _orden        ->orden de como se muestran los resultados
	 * @param _limit        ->limite de la consulta, cuantos registros se quieren tomar
	 * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
	 */
	public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion, String _orden, int _limit){
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" ORDER BY "+_orden+" LIMIT "+_limit, null);
			String[] _columnas = c.getColumnNames();

			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_registro.put(_columnas[i], c.getString(i));
				}
				_query.add(_registro);
			}
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		this.cerrar();
		return _query;
	}



	/**Funcion para realizar la consulta de un registro, retorna un contentvalues con la informacion consultada
	 * @param _tabla		->tabla sobre la cual se va a ejecutar la consulta
	 * @param _campos		->campos que se quieren consultar
	 * @param _condicion	->condicion para ejecutar la consulta
	 * @return				-->ContentValues con la informacion del registro producto de la consulta
	 */
	public ContentValues SelectDataRegistro(String _tabla, String _campos, String _condicion){
		ContentValues _query = new ContentValues();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" LIMIT 1", null);
			String[] _columnas = c.getColumnNames();

			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				//ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_query.put(_columnas[i], c.getString(i));
				}
			}
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		this.cerrar();
		return _query;
	}


	//Relizar la consulta teniendo en cuenta varios JOIN a la izquierda
	public ArrayList<ContentValues> SelectNJoinLeftData(String _tabla, String _campos, String _join_left[], String _on_left[], String _condicion){
		String Query = "";
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Query = "SELECT DISTINCT "+ _campos + " FROM "+ _tabla;
			for(int i=0;i<_join_left.length;i++){
				Query += " LEFT JOIN " +_join_left[i] + " ON "+_on_left[i];
			}
			Query += " WHERE "+ _condicion;
			Cursor c = nBD.rawQuery(Query, null);
			String[] _columnas = c.getColumnNames();

			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					if(c.getString(i) == null){
						_registro.put(_columnas[i], "");
					}else{
						_registro.put(_columnas[i], c.getString(i));
					}
				}
				_query.add(_registro);
			}
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		this.cerrar();
		return _query;
	}



	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un entero
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public int IntSelectShieldWhere(String _tabla, String _campo, String _condicion){
		int intRetorno = -1;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				intRetorno = c.getInt(0);
			}
		}
		catch(Exception e){
			intRetorno = -1;
		}
		cerrar();
		return intRetorno;
	}


	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un double
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public double DoubleSelectShieldWhere(String _tabla, String _campo, String _condicion){
		double intRetorno = -1;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				intRetorno = c.getDouble(0);
			}
		}
		catch(Exception e){
			intRetorno = -1;
		}
		cerrar();
		return intRetorno;
	}


	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un String
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public String StrSelectShieldWhere(String _tabla, String _campo, String _condicion){
		String strRetorno = null;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				strRetorno = c.getString(0);
			}
		}
		catch(Exception e){
			e.toString();
			strRetorno = null;
		}
		cerrar();
		return strRetorno;
	}


	/**Funcion retorna la cantidad de registros de una tabla que cumplen la condicion recibida por parametro
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public int CountRegistrosWhere(String _tabla, String _condicion){
		int ValorRetorno = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) FROM "+ _tabla +" WHERE "+ _condicion, null);
			c.moveToFirst();
			ValorRetorno = c.getInt(0);
		}
		catch(Exception e){
		}
		cerrar();
		return ValorRetorno;
	}


	/**Funcion que retorna true o falso segun existan o no registros que cumplan la condicion recibida por parametro
	 * @param _tabla		->Tabla sobre la cual se va trabajar
	 * @param _condicion	->Condicion de filtro
	 * @return
	 */
	public boolean ExistRegistros(String _tabla, String _condicion){
		ValorRetorno = false;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) as cantidad FROM " + _tabla +" WHERE " + _condicion , null);
			c.moveToFirst();
			if(c.getDouble(0)>0)
				ValorRetorno = true;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();
		return ValorRetorno;
	}


	/**Funcion que retorna la cantidad de minutos transcurridos desde la fecha actual y la recibida por parametro
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->String con el resultado en minutos
	 */
	public String MinutesBetweenDateAndNow(String _oldDate){
		String _retorno = "";
		int _minutos = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','now')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_retorno = c.getString(0);
			_minutos = Integer.parseInt(_retorno)/60;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();
		return String.valueOf(_minutos);
	}


	/**Funcion que retorna la cantidad de minutos transcurridos entre las fechas recibidas por parametro
	 * @param _newDate	->fecha mas reciente contra la cual se quiere caldular la diferencia
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->Strig con el resultado en minutos
	 */
	public String MinutesBetweenDates(String _newDate, String _oldDate){
		String _retorno = "";
		int _minutos = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','"+_newDate+"')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_retorno = c.getString(0);
			_minutos = Integer.parseInt(_retorno)/60;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();
		return String.valueOf(_minutos);
	}
}
