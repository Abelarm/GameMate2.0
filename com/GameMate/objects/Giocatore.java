package com.GameMate.objects;

import java.io.Serializable;

public class Giocatore {
	
	public String _id;
	
	public String _username;
	
	public Geometry _geometry;
	
	public String _nomeGioco;
	public String _piattaforma;
	public String _nickname;
	
	public static class Geometry {
		public Location location;
	}
	
	public static class Location {
		public double latitudine;
		public double longitudine;
	}
	
	public String toString(){
		return this._username;
	}

}
