package com.GameMate.objects;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.GameMate.library.JSONParser;

import android.content.Context;

public class FunzioniGioco {
	 private JSONParser jsonParser;
     
	    // Testing in localhost using wamp or xampp 
	    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
	 
	    private static String GiocoURL = "http://gamemate.altervista.org/Gioco.php";    
	   

	     
	    private static String controllo_gioco_tag = "contolloGioco";
	    private static String registrazione_gioco_tag = "registrazioneGioco";
	    private static String aggiornamento_gioco_tag = "aggiornamentoGioco";
	    private static String get_dettagli_gioco_tag = "getGioco";
	    private static String get_all_giochi_tag = "getAllGiochi";

	     
	    // constructor
	    public FunzioniGioco(){
	        jsonParser = new JSONParser();
	    }
	     
	   
	    public JSONObject controlloGioco(String nomegioco, String piattaforma){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("tag", controllo_gioco_tag));
	        params.add(new BasicNameValuePair("nomegioco", nomegioco));
	        params.add(new BasicNameValuePair("piattaforma", piattaforma));
	        JSONObject json = jsonParser.makeHttpRequest(GiocoURL, "POST",params);
	        // return json
	        // Log.e("JSON", json.toString());
	        return json;
	    }
	     
	    /**
	     *  Registra un gioco nel database
	     * @param nomegioco
	     * @param piattaforma
	     * @param genere
	     * @return il gioco registrato
	     */
	    public JSONObject registraGioco(String nomegioco, String piattaforma,String genere){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("tag", registrazione_gioco_tag));
	        params.add(new BasicNameValuePair("nomegioco", nomegioco));
	        params.add(new BasicNameValuePair("piattaforma", piattaforma));
	        params.add(new BasicNameValuePair("genere", genere));
	         
	        // getting JSON Object
	        JSONObject json = jsonParser.makeHttpRequest(GiocoURL, "POST", params);
	        // return json
	        return json;
	    }
	    
	    /**
	     * Restituisce i dati del gioco richiesto
	     * @param nomegioco
	     * @param piattaforma
	     * @return
	     */
	    public JSONObject getDettagliGioco(String nomegioco, String piattaforma){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("tag", get_dettagli_gioco_tag));
	        params.add(new BasicNameValuePair("nomegioco", nomegioco));
	        params.add(new BasicNameValuePair("piattaforma", piattaforma));
	        JSONObject json = jsonParser.makeHttpRequest(GiocoURL, "POST",params);
	        // return json
	        // Log.e("JSON", json.toString());
	        return json;
	    }
	    
	    
	    public JSONObject aggiornamentoGioco(String nomegioco, String piattaforma){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("tag", aggiornamento_gioco_tag));
	        params.add(new BasicNameValuePair("nomegioco", nomegioco));
	        params.add(new BasicNameValuePair("piattaforma", piattaforma));
	        JSONObject json = jsonParser.makeHttpRequest(GiocoURL, "POST",params);
	        // return json
	        // Log.e("JSON", json.toString());
	        return json;
	    }
	    
	    public JSONObject getAllGiochi(){
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("tag", get_all_giochi_tag));
	       
	        JSONObject json = jsonParser.makeHttpRequest(GiocoURL, "POST",params);
	        // return json
	        // Log.e("JSON", json.toString());
	        return json;
	    }
	     
	     
	  
	     
	}