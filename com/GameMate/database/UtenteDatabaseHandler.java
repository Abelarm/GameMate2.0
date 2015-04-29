package com.GameMate.database;

import java.util.HashMap;

import com.GameMate.objects.Utente;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UtenteDatabaseHandler  extends DatabaseHandler{
	
	
    protected static class UtenteTable{
    	// Contacts table name
        protected static final String TABLE_NAME = "login";
        
     // Contacts Table Columns names
        protected static final String KEY_ID = "ID";
        protected static final String KEY_UTENTE_ID = "Utente_ID";
        protected static final String KEY_USERNAME = "Username";
        protected static final String KEY_ETA = "Eta";
        protected static final String KEY_CITTA = "Citta";
        protected static final String KEY_NUMAVATAR = "Numero_Avatar";
    	
    }

    public UtenteDatabaseHandler(Context context) {
        super(context);
    }

    public void addUtente(String unique_id, String username,int eta, String citta, int numero_avatar) {
        this.openWrite();    
        ContentValues valori = new ContentValues();
        valori.put(UtenteTable.KEY_UTENTE_ID,unique_id);
        valori.put(UtenteTable.KEY_USERNAME, username); 
        valori.put(UtenteTable.KEY_ETA, eta);
        valori.put(UtenteTable.KEY_CITTA, citta);
        valori.put(UtenteTable.KEY_NUMAVATAR, numero_avatar);
        
        // Inserting Row
        Database.insert(UtenteTable.TABLE_NAME, null, valori);
        this.close();
    }
 
    
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + UtenteTable.TABLE_NAME;
          
        this.openRead();
        Cursor cursor = Database.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	user.put(UtenteTable.KEY_UTENTE_ID, cursor.getString(1));
            user.put(UtenteTable.KEY_USERNAME, cursor.getString(2));
            user.put(UtenteTable.KEY_ETA, String.valueOf(cursor.getInt(3)));
            user.put(UtenteTable.KEY_CITTA, cursor.getString(4));
            user.put(UtenteTable.KEY_NUMAVATAR, String.valueOf(cursor.getInt(5)));
        }
        cursor.close();
        this.close();
        // return user
        return user;
    }
    
 
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + UtenteTable.TABLE_NAME;
        this.openRead();
        Cursor cursor = Database.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        this.close();
        cursor.close();
         
        // return row count
        return rowCount;
    }
    
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
    	this.openWrite();
    	// Delete All Rows
        Database.delete(UtenteTable.TABLE_NAME, null, null);
        this.close();
    }
 
 

}
