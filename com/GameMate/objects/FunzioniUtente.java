package com.GameMate.objects;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.JSONParser;
 
import android.content.Context;
import android.util.Log;
 
public class FunzioniUtente {
     
    private JSONParser jsonParser;
     
    // Testing in localhost using wamp or xampp 
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String utenteURL = "http://gamemate.altervista.org/Utente.php";    
     
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String data_tag = "getDati";
    private static String upadate_tag="aggiornaProfilo";

     
    // constructor
    public FunzioniUtente(){
        jsonParser = new JSONParser();
    }
     
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String username, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(utenteURL, "POST",params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
     
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String username, String password,int eta,String citta,int numAvatar){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("eta", String.valueOf(eta)));
        params.add(new BasicNameValuePair("citta", citta));
        params.add(new BasicNameValuePair("numero_avatar", String.valueOf(numAvatar)));
         
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(utenteURL, "POST",params);
        // return json
        return json;
    }
    
    public JSONObject getData(String username){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", data_tag));
        params.add(new BasicNameValuePair("username", username));
        JSONObject json = jsonParser.makeHttpRequest(utenteURL, "POST",params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
    
     
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        UtenteDatabaseHandler db = new UtenteDatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
    
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        UtenteDatabaseHandler db = new UtenteDatabaseHandler(context);
        db.resetTables();
        return true;
    }
    
    public JSONObject aggiornaProfilo(String username,String oldpass,String newpass,int eta,String citta,int numAvatar){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", upadate_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("oldpass", oldpass));
        params.add(new BasicNameValuePair("newpass", newpass));
        params.add(new BasicNameValuePair("eta", String.valueOf(eta)));
        params.add(new BasicNameValuePair("citta", citta));
        params.add(new BasicNameValuePair("numero_avatar", String.valueOf(numAvatar)));
         
        Log.e("DA quest'altra parte",username+oldpass+newpass+eta+citta+numAvatar);
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(utenteURL, "POST",params);
        // return json
        return json;
    }
    
     
}