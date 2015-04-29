package com.GameMate.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Utente implements Parcelable{

	 //private variables
   private String _username;
   private String _password;
   private int _eta;
   private String _citta;
   private int _numAvatar;
    
    // Empty constructor
    public Utente(){
         
    }
    
    // constructor
    public Utente(String username,String password, int eta, String citta,int num){
    	this._username = username;
    	this._password = password;
    	this._eta = eta;
    	this._citta = citta;
    	this._numAvatar=num;
    }
       
    // get Username
    public String getUsername(){
    	return this._username;
    }
    // set Username
    public void setUsername(String username){
    	this._username = username;
    }
  
    // get Password
    public String getPassword(){
    	return this._password;
    }
    // set Password
    public void setPassword(String password){
    	this._password = password;
    }
	
    // get Eta
    public int getEta(){
    	return this._eta;
    }
    // set Eta
    public void setEta(int eta){
    	this._eta = eta;
    }
    
    // get Citta
    public String getCitta(){
    	return this._citta;
    }
    // set Citta
    public void setCitta(String citta){
    	this._citta = citta;
    }

    // parcel part
    public Utente (Parcel in){
    	String[] datiUtente = new String[5];
    	
    	in.readStringArray(datiUtente);
    	this._username = datiUtente[0];
    	this._password = datiUtente[1];
    	this._eta = Integer.parseInt(datiUtente[2]);
    	this._citta = datiUtente[3];
    	this._numAvatar=Integer.parseInt(datiUtente[4]);
    }
    
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel destinatario, int flags) {
		
		destinatario.writeStringArray(new String[]{
				this._username,
				this._password,
				String.valueOf(_eta),
				this._citta,
				String.valueOf(_numAvatar)
		});
	}
	
	public int get_numAvatar() {
		return _numAvatar;
	}

	public void set_numAvatar(int _numAvatar) {
		this._numAvatar = _numAvatar;
	}

	public static final Parcelable.Creator<Utente> CREATOR= new Parcelable.Creator<Utente>(){

		
		public Utente createFromParcel(Parcel sorgente) {
			return new Utente(sorgente); // uso il costrutore del Parcelable
		}

		
		public Utente[] newArray(int size) {
			
			return new Utente[size];
		}
		
	};
}
