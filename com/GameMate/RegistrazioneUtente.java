package com.GameMate;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.GameMate.R;
import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.AlertDialogManager;
import com.GameMate.objects.FunzioniUtente;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RegistrazioneUtente extends Activity{

	 // inizializzo le variabili
		EditText inputUsername;
		EditText inputPassword;
		EditText inputConfirmPassword;
		EditText inputEta;
		EditText inputCitta;
		ImageView inputAvatar;
		
		// JSON Response node names
	    private static String KEY_SUCCESS = "success";
	    private static String KEY_ERROR = "error";

	    private static String KEY_UTENTE_ID = "TAG_UNIQUE_ID";
	    private static String KEY_USERNAME = "TAG_USERNAME";
	    private static String KEY_ETA = "TAG_ETA";
	    private static String KEY_CITTA = "TAG_CITTA";
	    private static String KEY_NUM_AVATAR = "TAG_NUMERO_AVATAR";
	    
	    //Variabili per le funzioni
	    private String textUsername;
		private String textPassword;
		private String textCitta;
		private int Eta = 0;
		private int numAvatar=0;
	    
	    // Progress Dialog
	    private ProgressDialog pDialog;
	    
	    //Funzioni Utente
	    private FunzioniUtente userFunction;
		
		// Alert Dialog Manager
	    private AlertDialogManager alert ;

	    private UtenteDatabaseHandler dbUtente; 
	    
	    private JSONObject json;


	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_regform);
	        
	        alert = new AlertDialogManager();
	        dbUtente = new UtenteDatabaseHandler(this);
	       
	        
	        inputUsername 		= 	(EditText) findViewById(R.id.editUsername);
	        inputPassword 	=	(EditText) findViewById(R.id.editPassword);
	        inputConfirmPassword 	=	(EditText) findViewById(R.id.editConfirmPassword);  
	        inputEta 		=	(EditText) findViewById(R.id.editEta);
	        inputCitta	 	=	(EditText) findViewById(R.id.editCitta);
	        inputAvatar  =	(ImageView) findViewById(R.id.RegAvatar);
	        
	        inputAvatar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent AvatarMenu= new Intent(RegistrazioneUtente.this,CambiaAvatar.class);
					startActivityForResult(AvatarMenu,100);
					
				}
			});


	        ImageButton registrazioneButton = (ImageButton) findViewById(R.id.registrazioneButton);
	        
	        // quando clicco su Login
	        registrazioneButton.setOnClickListener(new View.OnClickListener() {
	        	
				public void onClick(View arg0) {
					
					textUsername = inputUsername.getText().toString();
					textPassword = inputPassword.getText().toString();
					String textConfirmPassword = inputConfirmPassword.getText().toString();
					String textEta = inputEta.getText().toString();
					textCitta = inputCitta.getText().toString();
					Eta = 0;
					try{
						Eta = Integer.parseInt(textEta);
					}catch(NumberFormatException ex){
						alert.showAlertDialog(RegistrazioneUtente.this, "Number Error", "Inserire un numero valido", false);
					}
					
					if(textUsername.equals("")){
						alert.showAlertDialog(RegistrazioneUtente.this, "Username Error", "Inserire un Username valido", false);
					}
					else if(textPassword.equals("")){
						alert.showAlertDialog(RegistrazioneUtente.this, "Password Error", "Inserire un Password valido", false);
					}
					else if(textConfirmPassword.equals("")){
						alert.showAlertDialog(RegistrazioneUtente.this, "Confirm Password Error", "Inserire un  Confirm Password valido", false);
					}
					else if(textEta.equals("")||Eta == 0){
						alert.showAlertDialog(RegistrazioneUtente.this, "Eta Error", "Inserire un'Eta valida", false);
					}
					else if(textCitta.equals("")){
						alert.showAlertDialog(RegistrazioneUtente.this, "Citta Error", "Inserire una Citta valida", false);
					}
					else if (!textPassword.equals(textConfirmPassword)){
						alert.showAlertDialog(RegistrazioneUtente.this, "Password Error", "Le Password sono diverse", false);
					}
					else if(numAvatar==0){
						alert.showAlertDialog(RegistrazioneUtente.this, "NumAvatar Error", "Scegli un Avatar", false);
					}
					
					else{
						new doRegistrazioneUtente().execute();
						
					}
				}
			});
	        
	        
	        ImageButton annulla=(ImageButton) findViewById(R.id.annulla);
	        
	        annulla.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					inputUsername.setText("");
					inputPassword.setText("");
					inputConfirmPassword.setText("");
					inputEta.setText("");
					inputCitta.setText("");
					
				}
			});
	    }
	    
	    
	    protected void onActivityResult(int requestCode,int resultCode, Intent data){
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode==100){
				
				String num_avatar = data.getExtras().getString("Avatar");
				numAvatar=Integer.parseInt(num_avatar);
		        
		        String avatar_id = "a"+num_avatar;
		    	inputAvatar.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("drawable/" + avatar_id, "drawable", getPackageName())));
		    	

			}
		}
	    
	    
	    public class doRegistrazioneUtente extends AsyncTask<String,String,String>{

	    	protected void onPreExecute() {
	             super.onPreExecute();
	             pDialog = new ProgressDialog(RegistrazioneUtente.this);
	             pDialog.setTitle("Registrazione Utente");
	             pDialog.setMessage("Registrazione Utente. Attendere prego....");
	             pDialog.setIndeterminate(false);
	             pDialog.setCancelable(true);
	             pDialog.show();
	         }
	    	
			protected String doInBackground(String... arg0) {
				userFunction = new FunzioniUtente();
				json = userFunction.registerUser(textUsername, textPassword,Eta,textCitta,numAvatar);
					
				// user successfully registred
				// Store user details in SQLite Database
				UtenteDatabaseHandler dbUtente = new UtenteDatabaseHandler(getApplicationContext());	                            
							
				return null;
			}
			
			protected void onPostExecute(String caso) {
	            
	            pDialog.dismiss();
	            try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							JSONObject json_user;
							try {
								json_user = json.getJSONObject("user");
								String utente_id = json.getString(KEY_UTENTE_ID);
								String username = json_user.getString(KEY_USERNAME);
								Eta = json_user.getInt(KEY_ETA);
								String citta = json_user.getString(KEY_CITTA);
								numAvatar = json_user.getInt(KEY_NUM_AVATAR);
								// Clear all previous data in database
								userFunction.logoutUser(getApplicationContext());
								dbUtente.addUtente(utente_id, username, Eta, citta, numAvatar);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							alert.showAlertDialog(RegistrazioneUtente.this, "Registrazione", "Registrazione Effettuata", true);
							// nuovo intent
							Intent loginMenu = new Intent(RegistrazioneUtente.this,Login.class);
							
							startActivity(loginMenu);
							finish();
						
						}else{
							String result;
							try {
								result = json.getString(KEY_ERROR);
								if(Integer.parseInt(result) == 1){
									// Error in registration
									alert.showAlertDialog(RegistrazioneUtente.this, "Errore Registrazione","Errore durante la registrazione", false);
								}
								else{
									alert.showAlertDialog(RegistrazioneUtente.this, "Errore Registrazione", "Username gia esistente", false);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							
						}
						
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	      }	}
}