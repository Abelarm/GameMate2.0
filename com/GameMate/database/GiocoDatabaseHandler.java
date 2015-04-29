package com.GameMate.database;

import java.util.ArrayList;
import java.util.List;

import com.GameMate.database.AmicoDatabaseHandler.AmicoTable;
import com.GameMate.objects.Gioco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class GiocoDatabaseHandler extends DatabaseHandler{
	
	protected static class GiocoTable{
    	// Contacts table name
        protected static final String TABLE_NAME = "gioco";
        
     // Contacts Table Columns names
        protected static final String KEY_ID = "id";
        protected static final String KEY_NOME_GIOCO = "nome_gioco";
        protected static final String KEY_PIATTAFORMA = "piattaforma";
        protected static final String KEY_GENERE = "genere";
    	
    }

    public GiocoDatabaseHandler(Context context) {
        super(context);
    }

	public void addGioco(Gioco gioco) {
        
		this.openWrite();
        ContentValues valori = new ContentValues();
        valori.put(GiocoTable.KEY_NOME_GIOCO, gioco.getNomeGioco()); 
        valori.put(GiocoTable.KEY_PIATTAFORMA, gioco.getPiattaforma()); 
        valori.put(GiocoTable.KEY_GENERE, gioco.getGenere());
        
        // Inserisce la riga
        Database.insert(GiocoTable.TABLE_NAME, null, valori);
        this.close();
    }
    
	 
	public Gioco getDatiGioco(String nomegioco,String piattaforma){
		Gioco gioco = null;
		
	    String selectQuery = "SELECT  * FROM " + GiocoTable.TABLE_NAME +" WHERE "+ GiocoTable.KEY_NOME_GIOCO + " = " + "'" + nomegioco + "'" + " AND " + GiocoTable.KEY_PIATTAFORMA + " = " + "'" + piattaforma + "'";
		this.openRead();
		Cursor cursor = Database.rawQuery(selectQuery, null);
		if (cursor != null){
            if(cursor.moveToFirst()){
     
            	gioco = new Gioco(cursor.getString(1), 
            							cursor.getString(2),
            							cursor.getString(3));
            }
        }
        cursor.close();
        this.close();
        // return gioco
        return gioco;
		
	}
    // Restituisce un gioco 
    public Gioco getGioco(String nome,String piattaforma) {
    	Gioco gioco = null;
    	this.openRead();
        Cursor cursor = Database.query(GiocoTable.TABLE_NAME, 
        		new String[] { 
        			GiocoTable.KEY_NOME_GIOCO,
    				GiocoTable.KEY_PIATTAFORMA,
    				GiocoTable.KEY_GENERE
           		}, GiocoTable.KEY_NOME_GIOCO + "=?" + " AND " + GiocoTable.KEY_PIATTAFORMA+ "=?",
                new String[] { 
        			nome, piattaforma }, null, null, null);
        
        if (cursor != null){
            if(cursor.moveToFirst()){
     
            	gioco = new Gioco(cursor.getString(1), 
            							cursor.getString(2),
            							cursor.getString(3));
            }
        }
        cursor.close();
        this.close();
        // return gioco
        return gioco;
    }
    
    // Restituisce tutti i giochi
    public List<Gioco> getAllGiochi() {
       List<Gioco> ListaGiochi = new ArrayList<Gioco>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + GiocoTable.TABLE_NAME;
    
       this.openRead();
       Cursor cursor = Database.rawQuery(selectQuery, null);
       
       if(cursor!=null){
    	   // ciclo su tutte le righe e le agiungo alla lista
    	   if (cursor.moveToFirst()) {
    		   do {
    			   Gioco gioco = new Gioco();
    			   gioco.setNomeGioco(cursor.getString(0));
    			   gioco.setPiattaforma(cursor.getString(1));
    			   gioco.setGenere(cursor.getString(2));
               // aggiungo alla lista
               ListaGiochi.add(gioco);
           } while (cursor.moveToNext());
       }
    }
       cursor.close();
       this.close();
       // return la lista dei giochi
       return ListaGiochi;
   }
    
 // Getting contacts Count
    public int getNrGiochi() {
        String countQuery = "SELECT  * FROM " + GiocoTable.TABLE_NAME;
        this.openRead();
        Cursor cursor = Database.rawQuery(countQuery, null);
        cursor.close();
        this.close();
        // return count
        return cursor.getCount();
    }
    
 // Updating single contact
    public int updateGioco(Gioco gioco) {
       
    	this.openWrite();
        ContentValues Valori = new ContentValues();
        Valori.put(GiocoTable.KEY_NOME_GIOCO, gioco.getNomeGioco());
        Valori.put(GiocoTable.KEY_PIATTAFORMA, gioco.getPiattaforma());
        Valori.put(GiocoTable.KEY_GENERE, gioco.getGenere());
     
        // updating row
        int i = Database.update(GiocoTable.TABLE_NAME, Valori, GiocoTable.KEY_NOME_GIOCO + " = ?" + GiocoTable.KEY_PIATTAFORMA+"=?",
                new String[] { gioco.getNomeGioco(),gioco.getPiattaforma() });
        this.close();
        return i;
    }
    
 // Deleting single contact
    public void deleteGioco(Gioco gioco) {
       this.openWrite();
       Database.delete(GiocoTable.TABLE_NAME, GiocoTable.KEY_NOME_GIOCO + " = ?" + GiocoTable.KEY_PIATTAFORMA+"=?",
               new String[] { gioco.getNomeGioco(),gioco.getPiattaforma() });
       this.close();
    }
 

}
