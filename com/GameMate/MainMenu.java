package com.GameMate;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.GameMate.RegistrazioneGioco.doAttivazioneGiocatore;
import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.AlertDialogManager;
import com.GameMate.objects.FunzioniGiocatore;

public class MainMenu extends Activity {
	
	

    private static String KEY_NUM_AVATAR = "Numero_Avatar";
    

	
	private ImageButton registrazioneGioco;
	private ImageButton selezioneGioco;
	private ImageView Avatar;
	private TextView Username,Sessione;
	private ImageButton logout;
	
	
	// Alert Dialog Manager
   private AlertDialogManager alert; 
   private UtenteDatabaseHandler dbUtente;
   private JSONObject json_disattivazioneGiocatore,json_inOlineGiocatore;
   private String UtenteUsername;

    
    private static String KEY_SUCCESS = "success";
    protected static final String KEY_USERNAME = "Username";
    
    FunzioniGiocatore fGiocatore;
    
 // Progress Dialog
    private ProgressDialog pDialog;
 
	public void onCreate (Bundle savedInstanceState){
		  super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_mainmenu);
	        
	        dbUtente=new UtenteDatabaseHandler(this);
	      
	        
	        logout = (ImageButton) findViewById(R.id.logoutMainMenu);
	        registrazioneGioco = (ImageButton) findViewById(R.id.RegistratiImageButton);
	        selezioneGioco = (ImageButton) findViewById(R.id.SelezionaImageButton);
	        Avatar = (ImageView)findViewById(R.id.CambiaProfilo);
	        Username = (TextView)findViewById(R.id.UsernameMenu);
	        Sessione = (TextView) findViewById(R.id.SessioneMainMenu);
	        
	        HashMap<String,String> utente = dbUtente.getUserDetails();
	        String num_avatar=utente.get(KEY_NUM_AVATAR);
	        
	        String avatar_id = "a"+num_avatar;
	        Log.e("Avatar",avatar_id);
	    	Avatar.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("drawable/" + avatar_id, "drawable", getPackageName())));
	        
	        Username.setText(utente.get(KEY_USERNAME));
	        UtenteUsername=Username.getText().toString();
	        
	        Sessione.setClickable(false);
	        Sessione.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openConfirmDialog();
					
				}
			});
	       
	        // quando clicco su logout
	        logout.setOnClickListener(new View.OnClickListener() {
	        	
				public void onClick(View arg0) {
					
					openQuitDialog();
				}
			});
	        
	        // quando clicco su registrazione
	        registrazioneGioco.setOnClickListener(new View.OnClickListener() {
	        	
				public void onClick(View arg0) {
					
					// nuovo intent
					Intent registrazioneMenu = new Intent(MainMenu.this,RegistrazioneGioco.class);
				
					startActivity(registrazioneMenu);
					finish();
				}
			});
	        
	        // quando clicco su seleziona
	        selezioneGioco.setOnClickListener(new View.OnClickListener() {
	        	
				public void onClick(View arg0) {
					
					// nuovo intent
					Intent registrazioneMenu = new Intent(MainMenu.this,CercaGioco.class);
				
					startActivity(registrazioneMenu);
					finish();
				
				}
			});
	        
	        
	        Avatar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent ModificaProfilo=new Intent(MainMenu.this,ModificaProfilo.class);
					startActivityForResult(ModificaProfilo,100);
					
				}
			});
	        
	       new doisOnlineGiocatore().execute();
	        
	        
	}
	
	
	public void onBackPressed(){
		openQuitDialog();
	}


	private void openQuitDialog() {
		AlertDialog.Builder quitDialog=new AlertDialog.Builder(MainMenu.this);
		quitDialog.setTitle("Vuoi uscire?");
		quitDialog.setPositiveButton("Si", new OnClickListener(){
			public void onClick(DialogInterface dialog,int wich){
				 
				new doDisattivazioneGiocatoreEsci().execute();
		
			}
		});
		
		quitDialog.setNegativeButton("No", new OnClickListener(){
			public void onClick(DialogInterface dialog,int wich){
				
			}
		});
		
		quitDialog.show();
	}
	
	private void openConfirmDialog() {
		AlertDialog.Builder quitDialog=new AlertDialog.Builder(MainMenu.this);
		quitDialog.setTitle("Canellazione sessione attiva, sei sicuro?");
		quitDialog.setPositiveButton("Si", new OnClickListener(){
			public void onClick(DialogInterface dialog,int wich){
				 
				new doDisattivazioneGiocatore().execute();
				Sessione.setText("Nessuna Sessione Attiva");
		
			}
		});
		
		quitDialog.setNegativeButton("No", new OnClickListener(){
			public void onClick(DialogInterface dialog,int wich){
				
			}
		});
		
		quitDialog.show();
	}
	
	
	class doDisattivazioneGiocatore extends AsyncTask <String,String,String>{ 
		
    	protected String doInBackground(String... params) {
			
    		fGiocatore = new FunzioniGiocatore();
			json_disattivazioneGiocatore = fGiocatore.removeGiocatore(UtenteUsername);
			
			return null;
		}
    	
    	protected void onPostExecute(String result) {
            
            try {
				if (json_disattivazioneGiocatore.getString(KEY_SUCCESS) != null)
                    json_disattivazioneGiocatore.getString(KEY_SUCCESS); 
          
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    	
    }// fine doDisattivazioneGiocatore
	
	
	class doDisattivazioneGiocatoreEsci extends AsyncTask <String,String,String>{ 
		
    	protected String doInBackground(String... params) {
			
    		fGiocatore = new FunzioniGiocatore();
			json_disattivazioneGiocatore = fGiocatore.removeGiocatore(UtenteUsername);
			
			return null;
		}
    	
    	protected void onPostExecute(String result) {
            
            try {
				if (json_disattivazioneGiocatore.getString(KEY_SUCCESS) != null)
                    json_disattivazioneGiocatore.getString(KEY_SUCCESS);
				finish();
          
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    	
    }// fine doDisattivazioneGiocatore
	
	
class doisOnlineGiocatore extends AsyncTask <String,String,String>{ 
	
	protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(MainMenu.this);
        pDialog.setTitle("Recupero Dati ");
        pDialog.setMessage("Recupero Dati. Attendere prego....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
   } 
		
    	protected String doInBackground(String... params) {
			
    		fGiocatore = new FunzioniGiocatore();
			json_inOlineGiocatore = fGiocatore.isOnline(UtenteUsername);
			
			return null;
		}
    	
    	protected void onPostExecute(String result) {
            
            try {
                if (json_inOlineGiocatore.getString(KEY_SUCCESS) != null) {
                    String res = json_inOlineGiocatore.getString(KEY_SUCCESS); 
                    if(Integer.parseInt(res) == 1){
                    	String testo = "Sessione di gioco attiva,Disattiva";
                        Sessione.setText(testo);
                        Sessione.setClickable(true);
                    }	                    
                    else{
 	               	}
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
        }
    	
    }// fine doisOnlineGiocatore
	
		
		
	}

