package com.GameMate.objects;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.GameMate.library.JSONParser;

public class FunzioniGiocatore {
	
	private JSONParser jsonParser;
	// Testing in localhost using wamp or xampp 
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
 
    private static String GiocatoreURL = "http://gamemate.altervista.org/Giocatore.php";    



    private static String registrazione_Giocatore_tag = "inserisciGiocatore";
    private static String get_Giocatore_tag="getGiocatore";
    private static String get_Giocatori_tag = "getAllGiocatoriBy";
    private static String remove_Giocatore_tag = "rimuoviGiocatore";
    private static String is_online_tag="isOnline";
    
    
 // constructor
    public FunzioniGiocatore(){
        jsonParser = new JSONParser();
    }
    
    public JSONObject inserisciGiocatore(String Username,String NomeGioco,String Piattaforma,String nickname,double lat,double lon){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", registrazione_Giocatore_tag));
        params.add(new BasicNameValuePair("Username",Username));
        params.add(new BasicNameValuePair("NomeGioco",NomeGioco));
        params.add(new BasicNameValuePair("Piattaforma",Piattaforma)); 
        params.add(new BasicNameValuePair("Nickname",nickname));
        params.add(new BasicNameValuePair("lat",String.valueOf(lat)));
        params.add(new BasicNameValuePair("long",String.valueOf(lon)));
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(GiocatoreURL, "POST", params);
        // return json
        return json;
    }
    
    public JSONObject getGiocatore(String Username){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_Giocatore_tag));
        params.add(new BasicNameValuePair("Username",Username));
     // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(GiocatoreURL, "POST", params);
        // return json
        return json;
    }
    
    public JSONObject getGiocatoriBy(String NomeGioco,String Piattaforma,double Latitudine,double Longitudine){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_Giocatori_tag));
        params.add(new BasicNameValuePair("NomeGioco",NomeGioco));
        params.add(new BasicNameValuePair("Piattaforma",Piattaforma)); 
        params.add(new BasicNameValuePair("Latitudine",String.valueOf(Latitudine)));
        params.add(new BasicNameValuePair("Longitudine",String.valueOf(Longitudine))); 
        
     // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(GiocatoreURL, "POST", params);
        // return json
        return json;
    }
    
    public JSONObject isOnline(String Username){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", is_online_tag));
        params.add(new BasicNameValuePair("Username",Username));
     // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(GiocatoreURL, "POST", params);
        // return json
        return json;
    }
    
    
    public JSONObject removeGiocatore(String Username){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", remove_Giocatore_tag));
        params.add(new BasicNameValuePair("Username",Username));
     // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(GiocatoreURL, "POST", params);
        // return json
        return json;
    }
   

}
