package com.GameMate.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.GameMate.database.AmicoDatabaseHandler.AmicoTable;
import com.GameMate.database.GiocoDatabaseHandler.GiocoTable;
import com.GameMate.database.UtenteDatabaseHandler.UtenteTable;

public abstract class DatabaseHandler {
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    private static String DATABASE_NAME = "GameMate";
    
    public static DbHelper Helper;
    protected final Context Context;
    public static SQLiteDatabase Database;
    
    static class DbHelper extends SQLiteOpenHelper{
    	
    	public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

		@Override
		public void onCreate(SQLiteDatabase db) {
			if (!db.isReadOnly()) {
			    // Enable foreign key constraints
			    db.execSQL("PRAGMA foreign_keys=ON;");
			}
			
			
			// TABELLA UTENTE
			db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT UNIQUE,%s INTEGER,%s TEXT,%s INTEGER)", 
					UtenteTable.TABLE_NAME,
					UtenteTable.KEY_ID,
					UtenteTable.KEY_UTENTE_ID,
					UtenteTable.KEY_USERNAME,
					UtenteTable.KEY_ETA,
					UtenteTable.KEY_CITTA,
					UtenteTable.KEY_NUMAVATAR
			));
			
			//TABELLA GIOCO
			db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT)", 
					GiocoTable.TABLE_NAME,
					GiocoTable.KEY_ID,
					GiocoTable.KEY_NOME_GIOCO,
					GiocoTable.KEY_PIATTAFORMA,
					GiocoTable.KEY_GENERE
			));
			
			//TABELLA AMICO
			db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY,%s TEXT)",
					AmicoTable.TABLE_NAME,
					AmicoTable.KEY_ID,
					AmicoTable.KEY_AMICO_USERNAME
			));
								
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + UtenteTable.TABLE_NAME);
	        db.execSQL("DROP TABLE IF EXISTS " + GiocoTable.TABLE_NAME);
	        db.execSQL("DROP TABLE IF EXISTS " + AmicoTable.TABLE_NAME);
    
	        // ricrea le tabelle
	        onCreate(db);
		}
    }
    
    public DatabaseHandler(Context context){   
        this.Context = context;
    }
    
    public DatabaseHandler openWrite() throws SQLException{
       Helper = new DbHelper(Context);
       Database = Helper.getWritableDatabase();
       return this;
    }
    
    public DatabaseHandler openRead() throws SQLException{
        Helper = new DbHelper(Context);
        Database = Helper.getReadableDatabase();
        return this;
     }

    public void close(){
        if(Database.isOpen())
            Helper.close();
    }

}
