package com.GameMate.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Gioco implements Parcelable{

	 //private variables
	String _nomeGioco;
	String _piattaforma;
	String _genere;
		
	public Gioco(){
			
	}
	
	public Gioco(String nomeGioco, String piattaforma, String genere){
		this._nomeGioco = nomeGioco;
		this._piattaforma = piattaforma;
		this._genere = genere;
	}
	
    public String getNomeGioco(){
    	return this._nomeGioco;
    }
    
    public void setNomeGioco(String nomeGioco){
    	this._nomeGioco = nomeGioco;
    }
    
    public String getPiattaforma(){
    	return this._piattaforma;
    }
    
    public void setPiattaforma(String piattaforma){
    	this._piattaforma = piattaforma;
    }
    
    public String getGenere(){
    	return this._genere;
    }
    
    public void setGenere(String genere){
    	this._genere = genere ;
    }

 // parcel part
    public Gioco (Parcel in){
    	String[] datiGioco = new String[3];
    	
    	in.readStringArray(datiGioco);
    	this._nomeGioco = datiGioco[0];
    	this._piattaforma = datiGioco[1];
    	this._genere = datiGioco[2];
    }
    
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel destinatario, int flags) {
		
		destinatario.writeStringArray(new String[]{
				this._nomeGioco,
				this._piattaforma,
				this._genere
		});
	}
	
	public static final Parcelable.Creator<Gioco> CREATOR = new Parcelable.Creator<Gioco>(){

		
		public Gioco createFromParcel(Parcel sorgente) {
			return new Gioco(sorgente); // uso il costrutore del Parcelable
		}

		
		public Gioco[] newArray(int size) {
			
			return new Gioco[size];
		}
		
	};
	
	
}
