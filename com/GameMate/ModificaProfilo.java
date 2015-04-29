package com.GameMate;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.AlertDialogManager;
import com.GameMate.objects.FunzioniUtente;

public class ModificaProfilo extends Activity {
	
	// JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

 // Contacts Table Columns names
    protected static final String KEY_ID = "ID";
    protected static final String KEY_USERNAME = "Username";
    protected static final String KEY_ETA = "Eta";
    protected static final String KEY_CITTA = "Citta";
    protected static final String KEY_NUMAVATAR = "Numero_Avatar";
    
 // Progress Dialog
    private ProgressDialog pDialog;
    
    //Funzioni Utente
    private FunzioniUtente userFunction;
    
    JSONObject json;
	
 // inizializzo le variabili
 	TextView Username;
 	EditText Oldpass,Newpass,Confirmnewpass;
 	EditText inputEta;
 	EditText inputCitta;
	ImageView avatarGrande,avatarPiccolo;
	ImageButton bottone;
	String num_avatar;
	
	private String textUsername,textOldpass, textNewpass,textCitta;
	private int eta,numAvatar;
	UtenteDatabaseHandler dbUtente;
	private AlertDialogManager alert;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		alert = new AlertDialogManager();
		dbUtente=new UtenteDatabaseHandler(this);
		HashMap<String,String> utente = dbUtente.getUserDetails();
        
		Username= (TextView) findViewById(R.id.UsernameMod);
		Username.setText(utente.get(KEY_USERNAME));
		Oldpass = (EditText) findViewById(R.id.OldPassMod);
		Newpass =(EditText) findViewById(R.id.NewPassMod);
		Confirmnewpass=(EditText)findViewById(R.id.ConfNewPassMod);
		inputEta= (EditText)findViewById(R.id.EtaMod);
		inputCitta=(EditText)findViewById(R.id.CittaMod);
        avatarGrande  =	(ImageView) findViewById(R.id.AvatarModifcaProfilo);
        avatarPiccolo= (ImageView) findViewById(R.id.SceltaAvatar);
		
		
        num_avatar=utente.get(KEY_NUMAVATAR);
		
        String avatar_id = "a"+num_avatar;
    	avatarPiccolo.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("drawable/" + avatar_id, "drawable", getPackageName())));
    	avatarGrande.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("drawable/" + avatar_id, "drawable", getPackageName())));

		avatarPiccolo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent AvatarMenu= new Intent(ModificaProfilo.this,CambiaAvatar.class);
				startActivityForResult(AvatarMenu,100);
				
			}
		});
		
		bottone=(ImageButton) findViewById(R.id.ModificaButton);
		bottone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				textUsername = Username.getText().toString();
				textOldpass = Oldpass.getText().toString();
				textNewpass = Newpass.getText().toString();
				String textConfirmPassword = Confirmnewpass.getText().toString();
				String textEta = inputEta.getText().toString();
				textCitta = inputCitta.getText().toString();
				eta = 0;
				
				try{
					eta = Integer.parseInt(textEta);
					numAvatar=Integer.parseInt(num_avatar);
				}catch(NumberFormatException ex){
					alert.showAlertDialog(ModificaProfilo.this, "Number Error", "Inserire un numero valido", false);
				}
				
				if(textOldpass.equals("") || textNewpass.equals("") || textConfirmPassword.equals("")) {
					alert.showAlertDialog(ModificaProfilo.this, "Password Error", "Inserire una password valida", false);
				}else if(!textNewpass.equals(textConfirmPassword)){
					alert.showAlertDialog(ModificaProfilo.this, "Password Error", "Le due pass non sono uguali", false);
				}else if(textEta.equals("")||eta == 0){
					alert.showAlertDialog(ModificaProfilo.this, "Eta Error", "Inserire un'Eta valida", false);
				}
				else if(textCitta.equals("")){
					alert.showAlertDialog(ModificaProfilo.this, "Citta Error", "Inserire una Citta valida", false);
				}
				
				else{
					new doUpdateProfilo().execute();
				}
				
			}
		});
		
		
		 ImageButton annulla=(ImageButton) findViewById(R.id.annulla);
	        
	        annulla.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Oldpass.setText("");
					Newpass.setText("");
					Confirmnewpass.setText("");
					inputEta.setText("");
					inputCitta.setText("");
					
				}
			});
	
	}
	
	protected void onActivityResult(int requestCode,int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			
			num_avatar = data.getExtras().getString("Avatar");
	        
	        String avatar_id = "a"+num_avatar;
	    	avatarPiccolo.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("drawable/" + avatar_id, "drawable", getPackageName())));
	    	avatarGrande.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("drawable/" + avatar_id, "drawable", getPackageName())));

		}
	}
	
	public class doUpdateProfilo extends AsyncTask<String,String,String>{

    	protected void onPreExecute() {
             super.onPreExecute();
             pDialog = new ProgressDialog(ModificaProfilo.this);
             pDialog.setTitle("Aggiornamento dati Utente");
             pDialog.setMessage("Aggiornamento Utente in corso. Attendere prego....");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
         }
    	
		protected String doInBackground(String... arg0) {
			userFunction = new FunzioniUtente();
			Log.e("Dati", textUsername+textOldpass+textNewpass+eta+textCitta+numAvatar);
			json=userFunction.aggiornaProfilo(textUsername, textOldpass, textNewpass, eta, textCitta, numAvatar);
				
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
							String utente_id = json.getString("TAG_UNIQUE_ID");
							String username = json_user.getString("TAG_USERNAME");
							eta = json_user.getInt("TAG_ETA");
							String citta = json_user.getString("TAG_CITTA");
							numAvatar = json_user.getInt("TAG_NUMERO_AVATAR");
							Log.e("AvatarNuovo",":"+numAvatar);
							// Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							dbUtente.addUtente(utente_id, username, eta, citta, numAvatar);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						alert.showAlertDialog(ModificaProfilo.this, "Aggiornamento", "Aggiornamento Effettuato", true);
						Intent i= new Intent(ModificaProfilo.this,MainMenu.class);
						startActivity(i);
						finish();
					
					}else{
						String result;
						try {
							result = json.getString(KEY_ERROR);
							if(Integer.parseInt(result) == 1){
								// Error in registration
								alert.showAlertDialog(ModificaProfilo.this, "Update Error","Errore durante la modifica", false);
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
