package com.GameMate.objects;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.GameMate.library.JSONParser;

public class FunzioniAmico {
	private JSONParser jsonParser;
	// Testing in localhost using wamp or xampp 
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
 
    private static String AmicoURL = "http://gamemate.altervista.org/Amico.php";    



    private static String registrazione_Amico_tag = "inserisciAmici";
    private static String get_Amici_tag = "getAmici_di";
    private static String remove_Amico_tag = "rimuoviAmico";
    private static String sono_amici_tag="sonoAmici";
    
    
 // constructor
    public FunzioniAmico(){
        jsonParser = new JSONParser();
    }
    
    public JSONObject registraAmico(String mioNick,String nickAmico){
    	 // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", registrazione_Amico_tag));
        params.add(new BasicNameValuePair("nick1",mioNick ));
        params.add(new BasicNameValuePair("nick2", nickAmico));         
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(AmicoURL, "POST", params);
        // return json
        return json;
    }
    
    
    public JSONObject getAmici(String mioNick){
   	 // Building Parameters
       List<NameValuePair> params = new ArrayList<NameValuePair>();
       params.add(new BasicNameValuePair("tag",get_Amici_tag));
       params.add(new BasicNameValuePair("nick1",mioNick ));         
       // getting JSON Object
       JSONObject json = jsonParser.makeHttpRequest(AmicoURL, "POST", params);
       // return json
       return json;
   }
    
    
    public JSONObject rimuoviAmico(String mioNick,String nickAmico){
   	 // Building Parameters
       List<NameValuePair> params = new ArrayList<NameValuePair>();
       params.add(new BasicNameValuePair("tag", remove_Amico_tag));
       params.add(new BasicNameValuePair("nick1",mioNick ));
       params.add(new BasicNameValuePair("nick2", nickAmico));         
       // getting JSON Object
       JSONObject json = jsonParser.makeHttpRequest(AmicoURL, "POST", params);
       // return json
       return json;
   }
    
    
    public boolean SonoAmici(String mioNick,String nickAmico) throws JSONException{
   	 // Building Parameters
       List<NameValuePair> params = new ArrayList<NameValuePair>();
       params.add(new BasicNameValuePair("tag", sono_amici_tag));
       params.add(new BasicNameValuePair("nick1",mioNick ));
       params.add(new BasicNameValuePair("nick2", nickAmico));         
       // getting JSON Object
       JSONObject json = jsonParser.makeHttpRequest(AmicoURL, "POST", params);
       // return json
		String res = json.getString("success"); 
		Log.e("ControlloFunzione", res);
		if(Integer.parseInt(res) == 1)
			return true;
		else
			return false;
   }
    
}
