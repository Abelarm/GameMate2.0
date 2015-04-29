package com.GameMate;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.AlertDialogManager;
import com.GameMate.library.ConnectionDetector;
import com.GameMate.objects.FunzioniUtente;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Login extends Activity {

    // inizializzo le variabili
	EditText inputUsername;
	EditText inputPassword;
	ImageButton loginButton;
	String textUsername;
	String textPassword;
	JSONObject json;
	
	// JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    private static String KEY_UTENTE_ID = "TAG_UNIQUE_ID";
    private static String KEY_USERNAME = "TAG_USERNAME";
    private static String KEY_ETA = "TAG_ETA";
    private static String KEY_CITTA = "TAG_CITTA";
    private static String KEY_NUM_AVATAR = "TAG_NUMERO_AVATAR";
	
    //variabili utili
    private static String username;
    private int num_avatar;
   
    // Progress Dialog
    private ProgressDialog pDialog;
	
	// Alert Dialog Manager
    AlertDialogManager alert; 
    AlertDialog prova;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        alert=new AlertDialogManager();
        
        ConnectionDetector cd = new ConnectionDetector(Login.this);
        
        // Controllo se Internet  è presente
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            alert.showControlInternetDialog(Login.this, "Internet non presente");
        }
        
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        
        alert = new AlertDialogManager();
        
        inputUsername 	= 	(EditText) findViewById(R.id.editUsernameLogin);
        inputPassword 	=	(EditText) findViewById(R.id.editPasswordLogin);
        loginButton		= 	(ImageButton) findViewById(R.id.LoginButton);
        
        TextView linkRegistrazione = (TextView )findViewById(R.id.reg);
        
        String testo = "Non sei ancora registrato?Registrati!";
        linkRegistrazione.setText(testo);
        Pattern pattern = Pattern.compile("Registrati");
        
        Linkify.addLinks(linkRegistrazione, pattern, "registrazione-activity://");
        
        
        // quando clicco su Login
        loginButton.setOnClickListener(new View.OnClickListener() {
        	
			public void onClick(View arg0) {
				textUsername = inputUsername.getText().toString();
				textPassword = inputPassword.getText().toString();
				
				if(textUsername.equals("")){
					alert.showAlertDialog(Login.this, "Username Error", "Inserire un Username valido", false);
				}
				else if(textPassword.equals("")){
					alert.showAlertDialog(Login.this, "Password Error", "Inserire un Password valido", false);
				}
				else {
					new doLogin().execute(); // effetuo il login in background
				}
			}
		});
        
        
    }

    /**
     *  Fa il Login 
     */
    class doLogin extends AsyncTask<String,String,String>{
    	
    	/**
         * Before starting background thread Show Progress Dialog
         * */
       
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Login");
            pDialog.setMessage("Controllo dei dati. Aspettare prego....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Background Async Task per effettuare il Login
         * */
		protected String doInBackground(String... args) {
			
			FunzioniUtente userFunction = new FunzioniUtente();
            json = userFunction.loginUser(textUsername, textPassword);
 
            // user successfully logged in
            // Store user details in SQLite Database
            UtenteDatabaseHandler dbUtente = new UtenteDatabaseHandler(Login.this);
           
            try{
            	JSONObject json_user = json.getJSONObject("user");
                           
            	String utente_id = json.getString(KEY_UTENTE_ID);
            	username = json_user.getString(KEY_USERNAME);
            	int eta = json_user.getInt(KEY_ETA);
            	String citta = json_user.getString(KEY_CITTA);
            	num_avatar = json_user.getInt(KEY_NUM_AVATAR);
            
            	// cancello tutti dati precedenti nella tabella utente
            	userFunction.logoutUser(Login.this);
            	dbUtente.addUtente(utente_id, username, eta, citta, num_avatar);
             
            }catch (JSONException e) {
                e.printStackTrace();
            }
           	return null;
		}
    	
		 /**
         * After completing background task Dismiss the progress dialog
         * 
         **/
        protected void onPostExecute(String file_url) {
            
            pDialog.dismiss();
            
            // check for login response
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS); 
                    if(Integer.parseInt(res) == 1){
                                               
                        alert.showAlertDialog(Login.this, "Login", "Login effettuato", true);
    					// nuovo intent
                        Intent mainMenu = new Intent(Login.this,MainMenu.class);
                        mainMenu.putExtra("Username", username);
                        mainMenu.putExtra("num_avatar", num_avatar);
                        startActivity(mainMenu);
                        // Close Login Screen
                        finish();
                    }	                    
                    else{
                    	 String result = json.getString(KEY_ERROR); 
 	                     if(Integer.parseInt(result) == 1){
 	                    	 // Error in login
 	                    	 alert.showAlertDialog(Login.this, "Login Error", "Username o Password erratti", false);
 	                     }
 	                     else{
 	                    	 alert.showAlertDialog(Login.this, "Registrazione Error", "Non sei Registrato", false);
 	                    	 // nuovo intent
 	                    	 Intent RegistrazioneMenu = new Intent(Login.this,RegistrazioneUtente.class);
 	                    	 startActivity(RegistrazioneMenu);
 	                    	 // Close Login Screen
 	                    	 finish();
 	                     }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
	 }
    
    @SuppressLint("ValidFragment")
	public class NienteInternet extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("La connessione ad internet non è presente")
                   .setPositiveButton("Attiva Internet", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           // FIRE ZE MISSILES!
                       }
                   })
                   .setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           finish();                       }
                   });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
    
}
