package com.GameMate.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.GameMate.database.GiocoDatabaseHandler.GiocoTable;
import com.GameMate.database.UtenteDatabaseHandler.UtenteTable;
import com.GameMate.objects.Gioco;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;


public class AmicoDatabaseHandler extends DatabaseHandler {
	
	protected static class AmicoTable{
    	// Contacts table name
        protected static final String TABLE_NAME = "Amico";
        
     // Contacts Table Columns names
        protected static final String KEY_ID = "ID";
        protected static final String KEY_AMICO_USERNAME = "Amico_Username";
        
    	
    }

	public AmicoDatabaseHandler(android.content.Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void addAmico(String Username_amico) {
        this.openWrite();    
        ContentValues valori = new ContentValues();
        valori.put(AmicoTable.KEY_AMICO_USERNAME, Username_amico); 
             
        // Inserting Row
        Database.insert(AmicoTable.TABLE_NAME, null, valori);
        this.close();
    }
	
	
	public List<String> getAmici(){
		List<String> ListaAmici = new ArrayList<String>();
	       // Select All Query
	       String selectQuery = "SELECT  * FROM " + AmicoTable.TABLE_NAME;
	    
	       this.openRead();
	       Cursor cursor = Database.rawQuery(selectQuery, null);
	       try{
	       // ciclo su tutte le righe e le agiungo alla lista
	       if (cursor.moveToFirst()) {
	           do {
	               // aggiungo alla lista
	               ListaAmici.add(cursor.getString(1));
	           } while (cursor.moveToNext());
	       	}
	       }finally{
	    	   cursor.close();
	       }
	       cursor.close();
	       this.close();
	       // return la lista dei giochi
	       return ListaAmici;
	}
	
	 public boolean areFriends(String nickname){
		// Select All Query
	       String selectQuery = "SELECT  * FROM " + AmicoTable.TABLE_NAME +" WHERE "+ AmicoTable.KEY_AMICO_USERNAME + " = " + "'" + nickname + "'";
	    
	       this.openRead();
	       Cursor cursor = Database.rawQuery(selectQuery, null);
	       if(cursor!=null){
	    	   if(cursor.getCount()==0){
	    		   Log.e("Controllo", "Non sono amici");
	    		   cursor.close();
	    		   this.close();
	    		   return false;
	    	   }
	    	   else{
	    		   Log.e("Controllo", "Sono amici");
	    		   cursor.close();
	    		   this.close();
	    		   return true;
	    	   }
	    	   
	       }
		return false;
	       
	 }
	 
	 
	 public void deleteFriends(String nickname){
		 this.openWrite();
	       Database.delete(AmicoTable.TABLE_NAME , AmicoTable.KEY_AMICO_USERNAME+" = ?",
	    		   new String[] { nickname });
	       this.close();
	 }

}
