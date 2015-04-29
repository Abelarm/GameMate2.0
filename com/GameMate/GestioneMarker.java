package com.GameMate;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.GameMate.database.AmicoDatabaseHandler;
import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.AlertDialogManager;
import com.GameMate.objects.FunzioniAmico;
import com.GameMate.objects.FunzioniGiocatore;
import com.GameMate.objects.FunzioniUtente;

@SuppressLint("NewApi")
public class GestioneMarker extends Activity {
	
	ImageView Avatar;
	
	TextView giocatoreUsername;
	TextView giocatoreNomeGioco;
	TextView giocatorePiattaforma;
	TextView giocatoreNickname;
	
	ImageButton gestioneGiocatore;
	ImageButton inviaMessaggio;
	ImageButton ritorna;
	
	// Alert Dialog Manager
    AlertDialogManager alert; 
    UtenteDatabaseHandler dbUtente;
    AmicoDatabaseHandler dbAmico;
    
    // Progress Dialog
    private ProgressDialog pDialog;
    
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    protected static final String KEY_USERNAME = "Username";
    
    private static String KEY_MIO_USERNAME = "TAG_NICKNAME1";
    private static String KEY_AMICO_USERNAME = "TAG_NICKNAME2";
    private static String KEY_NOME_GIOCO = "TAG_NOMEGIOCO";
    private static String KEY_PIATTAFORMA = "TAG_PIATTAFORMA";
    private static String KEY_NICKNAME = "TAG_NICKNAME";
    private static String KEY_NUM_AVATAR = "TAG_NUMERO_AVATAR";

    private String utenteUsername,playerUsername;
    private JSONObject jsongetdata,jsongetgiocatore,jsonaddamico,jsonremoveamico;
    
    public void onCreate (Bundle savedInstanceState){
		  super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_gestionemarker);
	      
	      alert = new AlertDialogManager();
	      dbUtente = new UtenteDatabaseHandler(this);
	      dbAmico = new AmicoDatabaseHandler(this);
	      
	      giocatoreUsername = (TextView) findViewById(R.id.GiocatoreUsername);
	      giocatoreNomeGioco = (TextView) findViewById(R.id.GiocatoreNomeGioco);
	      giocatorePiattaforma = (TextView) findViewById(R.id.GiocatorePiattaforma);
	      giocatoreNickname = (TextView) findViewById(R.id.textView1);
	      
	      gestioneGiocatore = (ImageButton) findViewById(R.id.buttonGestioneAmico);
	      inviaMessaggio = (ImageButton) findViewById(R.id.buttonMessaggio);
	      ritorna = (ImageButton) findViewById(R.id.returnToMap);
	      
	      // prendo i miei dati dal database locale
	      HashMap<String,String> utente = dbUtente.getUserDetails();
		  utenteUsername = (String) utente.get(KEY_USERNAME);
		  Log.e("username utente", utenteUsername);  
		  
		  
		  Intent i= getIntent();
		  playerUsername=i.getStringExtra("MarkerUsername");
		  giocatoreUsername.setText(playerUsername);
		  
		  
		  
		  new doGetData().execute();
		  
		  
		  
		  playerUsername = giocatoreUsername.getText().toString();
	      Log.e("username amico",playerUsername);
	      
	      
         
	      
	      // se il marker clicato non appartiene agli amici
	      if(!dbAmico.areFriends(playerUsername)){ // nome da cambiare
	    	  gestioneGiocatore.setBackground(getResources().getDrawable(getResources().getIdentifier("drawable/" + "addfriend", "drawable", getPackageName())));
	    	  gestioneGiocatore.setTag("Aggiungi Amico");
	    	  
	      }
	      else{
	    	  gestioneGiocatore.setBackground(getResources().getDrawable(getResources().getIdentifier("drawable/" + "removefriend", "drawable", getPackageName())));
	    	  gestioneGiocatore.setTag("Rimuovi Amico");
	      }
	      	      
	      // quando clicco su gestioneGiocatore
	      gestioneGiocatore.setOnClickListener(new View.OnClickListener() {
	        	
				public void onClick(View arg0) {
					String label = (String)gestioneGiocatore.getTag();
					
					if(label.equals("Aggiungi Amico")){ // aggiungo il giocatore come amico
						
						
						new doAddAmico().execute();
						gestioneGiocatore.setBackground(getResources().getDrawable(getResources().getIdentifier("drawable/" + "removefriend", "drawable", getPackageName())));
				    	gestioneGiocatore.setTag("Rimuovi Amico");
						
					}
					else{// rimuovo giocatore dagli amici
						
						new doRemoveAmico().execute();
						gestioneGiocatore.setBackground(getResources().getDrawable(getResources().getIdentifier("drawable/" + "addfriend", "drawable", getPackageName())));
				    	gestioneGiocatore.setTag("Aggiungi Amico");
						
					}
					
					
				}
			});
	      
	      
	   // quando clicco su gestioneGiocatore
	      ritorna.setOnClickListener(new View.OnClickListener() {
	        	
				public void onClick(View arg0) {
					
					String label = (String)gestioneGiocatore.getTag();
					
					if(label.equals("Aggiungi Amico")){ // deve cambiare il marker in blue
						
						Intent i= new Intent();
						i.putExtra("MarkerUsername", playerUsername);
						
						setResult(200,i);
						finish();
						
					}
					else{// deve cambiare il marker in verde
						
						Intent i= new Intent();
						i.putExtra("MarkerUsername", playerUsername);
						
						setResult(100,i);
						finish();
						
					}
					
					
				}
			});
	      
	 
	      
    }
    
    public class doGetData extends AsyncTask<String,String,String>{   	 
    	
		@Override
		protected String doInBackground(String... arg0) {
			FunzioniUtente fUtente=new FunzioniUtente();
		    jsongetdata = fUtente.getData(playerUsername);
		     
			return null;
		}
		
		protected void onPostExecute(String result){
			try {
		    	  if (jsongetdata.getString(KEY_SUCCESS) != null) {
		    		  String res = jsongetdata.getString(KEY_SUCCESS); 
				      if(Integer.parseInt(res) == 1){
				    	  JSONObject json_user = jsongetdata.getJSONObject("user");
				    	  
				    	  int num_avatar = json_user.getInt(KEY_NUM_AVATAR);
				    	  String avatar_id = "a"+num_avatar;
				    	  
				    	  Avatar = (ImageView) findViewById(R.id.Avatar);
				    	  Avatar.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("drawable/" + avatar_id, "drawable", getPackageName())));

				    	  
				    	  new doGetGiocatore().execute();
				      }
				      else{
				    	  alert.showAlertDialog(GestioneMarker.this, "Avatar Error", "Errore Avatar", false);
				      }
				  }
		    	  else{
						alert.showAlertDialog(GestioneMarker.this, "Avatar Error", "Errore Avatar", false);

		    	  }
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
    	
    }
    
    
    public class doGetGiocatore extends AsyncTask<String,String,String>{
    	
    	@Override
		protected String doInBackground(String... arg0) {
    		FunzioniGiocatore fGiocatore=new FunzioniGiocatore();
    	    jsongetgiocatore= fGiocatore.getGiocatore(playerUsername);
		     
			return null;
		}
		
		protected void onPostExecute(String result){
			try {
		    	  if (jsongetgiocatore.getString(KEY_SUCCESS) != null) {
		    		  String res = jsongetgiocatore.getString(KEY_SUCCESS); 
				      if(Integer.parseInt(res) == 1){
				    	  JSONObject json_player = jsongetgiocatore.getJSONObject("player");
				    	  
				    	  String NomeGioco=json_player.getString(KEY_NOME_GIOCO);
				    	  String Piattaforma=json_player.getString(KEY_PIATTAFORMA);
				    	  String Nickname=json_player.getString(KEY_NICKNAME);
				    	  
				    	  
				    	  giocatoreNomeGioco.setText("Nome Gioco: "+NomeGioco);
					      giocatorePiattaforma.setText("Piattaforma: "+Piattaforma);
					      giocatoreNickname.setText("Nickname: "+Nickname);
				      }
				      else{
				    	  alert.showAlertDialog(GestioneMarker.this, "Player Error", "Errore nella ricerca del player", false);
				      }
				  }
		    	  else{
						alert.showAlertDialog(GestioneMarker.this, "Player Error", "Errore nella ricerca del player", false);

		    	  }
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
    	
    }
    
    public class doAddAmico extends AsyncTask<String,String,String>{
    	
    	protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GestioneMarker.this);
            pDialog.setTitle("Salvataggio Amico");
            pDialog.setMessage("Salvataggio in corso. Attendere prego....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    	
		@Override
		protected String doInBackground(String... arg0) {
			FunzioniAmico friendFunction = new FunzioniAmico();
			jsonaddamico = friendFunction.registraAmico(utenteUsername, playerUsername);

			return null;
		}
		
		protected void onPostExecute(String result){
			pDialog.dismiss();
			Log.e("è qui che appaiono 3 json?","forse");
			try {
				if (jsonaddamico.getString(KEY_SUCCESS) != null) { 
				    String res = jsonaddamico.getString(KEY_SUCCESS); 
				    if(Integer.parseInt(res) == 1){ // registrazione amico sul database centrale effettuata
				    	
				    	dbAmico.addAmico(playerUsername); // registro sul database locale
				    	
				    	
				    	
				    	alert.showAlertDialog(GestioneMarker.this, "Registrazione Amico", "Registrazione Amico Effettuata", true);
				    	
				    	this.cancel(true);
				    }
				}
				else{
					alert.showAlertDialog(GestioneMarker.this, "Registrazione Amico Error", "Errore nella registrazione", false);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    	
    }
    
    
    public class doRemoveAmico extends AsyncTask<String,String,String>{
    	
    	protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GestioneMarker.this);
            pDialog.setTitle("Rimozione Amico");
            pDialog.setMessage("Rimozione in corso. Attendere prego....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    	
		@Override
		protected String doInBackground(String... arg0) {
			FunzioniAmico friendFunction = new FunzioniAmico();
			jsonremoveamico = friendFunction.rimuoviAmico(utenteUsername, playerUsername);
			
			return null;
		}
    	
		
		protected void onPostExecute(String result){
			pDialog.dismiss();
			try {
				if (jsonremoveamico.getString(KEY_SUCCESS) != null) { 
				    String res = jsonremoveamico.getString(KEY_SUCCESS); 
				    if(Integer.parseInt(res) == 1){ // cancellazione amico sul database centrale effettuata
				    	
				    	dbAmico.deleteFriends(playerUsername); // cancello sul database locale
				    	
				    	alert.showAlertDialog(GestioneMarker.this, "Cancellazione Amico", "Cancellazione Amico Effettuata", true);
				    	
				    }
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
    }
    
}
