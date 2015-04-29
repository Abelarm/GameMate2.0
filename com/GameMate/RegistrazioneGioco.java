package com.GameMate;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.GameMate.R;
import com.GameMate.database.GiocoDatabaseHandler;
import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.AlertDialogManager;
import com.GameMate.library.ConnectionDetector;
import com.GameMate.library.GPSTracker;
import com.GameMate.objects.FunzioniGiocatore;
import com.GameMate.objects.FunzioniGioco;
import com.GameMate.objects.Gioco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class RegistrazioneGioco extends Activity implements OnItemSelectedListener{
	

	 // inizializzo le variabili
		EditText inputNomeGioco;
		EditText inputNickGame;
		Spinner comboPiattaforma;
		Spinner comboGenere;
		
		String textNomeGioco ;
		String textPiattaforma ;
		String textGenere ;
		String textNickGame ;
		
		String utenteUsername;
		Gioco gioco;
		
		JSONObject json_controllo;
		JSONObject json_getDatiGioco;
		JSONObject json_aggiornamento;
		JSONObject json_registrazione;
		JSONObject json_controlloGiocatore;
		JSONObject json_disattivazioneGiocatore;
		JSONObject json_attivazioneGiocatore;
		
		// flag for Internet connection status
	    Boolean isInternetPresent = false;
	     
	    // Connection detector class
	    ConnectionDetector cd;
	    
	 // Alert Dialog Manager
	    AlertDialogManager alert = new AlertDialogManager();
		
	 // GPS Location
	    GPSTracker gps;
	    
	    double myLatitude ;
		double myLongitude ;
		
		 // Progress Dialog
	    private ProgressDialog pDialog;
	    
	    UtenteDatabaseHandler dbUtente;
	    GiocoDatabaseHandler dbGioco;
	    
	    FunzioniGioco fGioco;
	    FunzioniGiocatore fGiocatore;
	    
	 // JSON Response node names
	    private static String KEY_SUCCESS = "success";
	  //  private static String KEY_ERROR = "error";
	    protected static final String KEY_USERNAME = "Username";

	    private static String KEY_GIOCO_ID = "TAG_GIOCO_ID";
	    private static String KEY_NOME_GIOCO = "TAG_NOME_GIOCO";
	    private static String KEY_PIATTAFORMA = "TAG_PIATTAFORMA";
	    private static String KEY_GENERE = "TAG_GENERE";
	    private static String KEY_NUM_GIOCATORI = "TAG_NUMERO_GIOCATORI";

	   
	    
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_registragioco);
	        
	       // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        //StrictMode.setThreadPolicy(policy);
	        
	        alert = new AlertDialogManager();
	        dbGioco = new GiocoDatabaseHandler(this);
	        dbUtente = new UtenteDatabaseHandler(this);
	     
	        
	        inputNomeGioco 		= 	(EditText) findViewById(R.id.NomeGioco);
	        inputNickGame 		=	(EditText) findViewById(R.id.NickGame);
	        
	        comboPiattaforma = (Spinner) findViewById(R.id.piattaforma);
	        comboPiattaforma.setOnItemSelectedListener(this);
	        // Crea un ArrayAdapter usando l'array piattaforma_liste (contenente i nomi delle piattaforme) e il default spinner layout
	        ArrayAdapter<CharSequence> adapterPiattaforma = ArrayAdapter.createFromResource(this,
	        R.array.piattaforme_lista, android.R.layout.simple_spinner_item);
	        // Specifica il layout  da usare quando la lista scelta compare 
	        adapterPiattaforma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        // Applica l'addapter allo spinner comboPiattaforma
	        comboPiattaforma.setAdapter(adapterPiattaforma);
	        
	        comboGenere = (Spinner) findViewById(R.id.genere);
	        comboGenere.setOnItemSelectedListener(this);
	        // Crea un ArrayAdapter usando l'array generi_giochi (contenente i nomi dei generi di giochi) e il default spinner layout
	        ArrayAdapter<CharSequence> adapterGenere = ArrayAdapter.createFromResource(this,
	        R.array.generi_giochi, android.R.layout.simple_spinner_item);
	        // Specifica il layout  da usare quando la lista scelta compare 
	        adapterGenere.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        // Applica l'addapter allo spinner comboGenere
	        comboGenere.setAdapter(adapterGenere);
	        	        
	        // prendo i miei dati dal database locale
	        HashMap<String,String> utente = dbUtente.getUserDetails();
	        utenteUsername = (String) utente.get(KEY_USERNAME);
	        Log.e("username utente", utenteUsername);
	        
	        cd = new ConnectionDetector(RegistrazioneGioco.this);
	        
	        // Controllo se Internet  è presente
	        isInternetPresent = cd.isConnectingToInternet();
	        if (!isInternetPresent) {
	            // Connessione Internet non presente
	            alert.showAlertDialog(RegistrazioneGioco.this, "Internet Connection Error",
	                    "Connessione Internet non presente", false);
	            return;
	        }
	        
	        gps = new GPSTracker (RegistrazioneGioco.this);
	        
	        ImageButton registrazioneGiocoButton = (ImageButton) findViewById(R.id.RegGameButton);
	        
	        // quando clicco 
	        registrazioneGiocoButton.setOnClickListener(new View.OnClickListener() {
	        	
				public void onClick(View arg0) {
					
					textNomeGioco = inputNomeGioco.getText().toString();
					textPiattaforma = comboPiattaforma.getSelectedItem().toString();
					textGenere = comboGenere.getSelectedItem().toString();
					textNickGame = inputNickGame.getText().toString();
					
					if(textNomeGioco.equals("")){
						alert.showAlertDialog(RegistrazioneGioco.this, "Nome Gioco Error", "Inserire un Nome Gioco valido", false);
					}
					else if(textNickGame.equals("")){
						alert.showAlertDialog(RegistrazioneGioco.this, "Nickname Error", "Inserire un Nick Game valido", false);
					}
					else{
						
						//GPS abbilitato
				        if(gps.canGetLocation()){
				        	        	
				        	myLatitude =gps.getLatitude();
				        	myLongitude = gps.getLongitude();
				        	
				        	Log.e("myLatitude", " ->" + myLatitude);
				        	Log.e("myLongitude", " ->" + myLongitude);
				        	
				        	gioco = dbGioco.getDatiGioco(textNomeGioco, textPiattaforma);
				        	
				        	//Log.e("gioco", gioco.getNomeGioco());
							
							
							if(gioco != null){ // gioco esistente nel mio database
															
								new doControlloGiocatore().execute(); // attivo il giocatore
															
							}
							else{ // gioco non c'e nel database locale
								new doControlloGioco().execute(); // controllo se il gioco c'e nel database centrale
							}							
				        }
				        else{ // abbilitare GPS
				        	gps.showSettingsAlert();
				        }
					}	
				}
	        });
	        
	        
	        ImageButton annulla=(ImageButton) findViewById(R.id.annulla);
	        
	        annulla.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					inputNomeGioco.setText("");
					inputNickGame.setText("");
					
					
				}
			});
	    }
	    
	    
	    public void onBackPressed(){
	    	Intent i=new Intent(RegistrazioneGioco.this,MainMenu.class);
			startActivity(i);
			this.finish();
		}
	    
	    
	    /**
	     *  Background task per controllo gioco
	     */
	    class doControlloGioco extends AsyncTask<String,String,String>{

	    	protected void onPreExecute() {
	             super.onPreExecute();
	             pDialog = new ProgressDialog(RegistrazioneGioco.this);
	             pDialog.setTitle("Controllo Gioco");
	             pDialog.setMessage("Controllo Gioco. Aspettare prego....");
	             pDialog.setIndeterminate(false);
	             pDialog.setCancelable(true);
	             pDialog.show();
	         } 
	    	
			protected String doInBackground(String... params) {
				
				fGioco = new FunzioniGioco();
				
				json_controllo = fGioco.controlloGioco(textNomeGioco, textPiattaforma);
				
				return null;
			}
			
			protected void onPostExecute(String result) {
	            
	            pDialog.dismiss();
	            
	            try {
	                if (json_controllo.getString(KEY_SUCCESS) != null) {
	                    String res = json_controllo.getString(KEY_SUCCESS); 
	                    if(Integer.parseInt(res) == 1){
	                    	//alert.showAlertDialog(RegistrazioneGioco.this, "Controllo Gioco", "Gioco già registrato!", true);
	                    	new doGetDatiGioco().execute(); // prendo i dati dal database centrale
	    				}	                    
	                    else{
							//alert.showAlertDialog(RegistrazioneGioco.this, "Controllo Gioco", "Gioco non ancora registrato", false);
							new doRegistrazioneGioco().execute();// registro il gioco sul database centrale
	 	               	}
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }// fine PostExecute
	    	
	    } // fine doControlloGioco
	    
	    /**
	     * BackGround Task per prendere i dati del gioco
	     */
	    class doGetDatiGioco extends AsyncTask<String, String, String>{ 
	    	 
			protected String doInBackground(String... arg0) {
				fGioco = new FunzioniGioco();
				json_getDatiGioco = fGioco.getDettagliGioco(textNomeGioco, textPiattaforma);
				
				return null;
			}
			
			protected void onPostExecute(String result) {
	            
	            pDialog.dismiss();
	            
	            try {
	                if (json_getDatiGioco.getString(KEY_SUCCESS) != null) {
	                    String res = json_getDatiGioco.getString(KEY_SUCCESS); 
	                    if(Integer.parseInt(res) == 1){
	                    	// gioco registrato nel database centrale 
		                	JSONObject json_gioco = json_getDatiGioco.getJSONObject("gioco");
		                    // prendo i detagli del gioco dal database centrale      
		                	String gioco_id = json_gioco.getString(KEY_GIOCO_ID);
		                	String nomegioco = json_gioco.getString(KEY_NOME_GIOCO);
		                	String piattaforma = json_gioco.getString(KEY_PIATTAFORMA);
		                	String genere = json_gioco.getString(KEY_GENERE);
		                	int num_giocatori = json_gioco.getInt(KEY_NUM_GIOCATORI);
						
		                	// registro il gioco nel database locale
		                	Gioco game = new Gioco(nomegioco,piattaforma,genere);
		                	dbGioco.addGioco(game);
	                    	
		                	new doAggiornamentoGioco().execute(); // aggiorno il numero dei giocatori nel database centrale
	                    }	                    
	                    else{
							alert.showAlertDialog(RegistrazioneGioco.this, "Errore Dati Gioco", "Errore nel recupero dei dati del Gioco", false);
	 	               	}
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }
	    } // fine getDati
	    
	    
	    
	    
	    class doAggiornamentoGioco extends AsyncTask<String, String, String>{
	    	
	    	protected void onPreExecute() {
	             super.onPreExecute();
	             pDialog = new ProgressDialog(RegistrazioneGioco.this);
	             pDialog.setTitle("Aggiornamento dati in corso");
	             pDialog.setMessage("Aggiornamento dati. Attendere prego....");
	             pDialog.setIndeterminate(false);
	             pDialog.setCancelable(true);
	             pDialog.show();
	         }
	    	 
			protected String doInBackground(String... arg0) {
				fGioco = new FunzioniGioco();
				json_aggiornamento = fGioco.aggiornamentoGioco(textNomeGioco, textPiattaforma);
				
				return null;
			}
			
			protected void onPostExecute(String result) {
	            
	            
	            try {
	                if (json_aggiornamento.getString(KEY_SUCCESS) != null) {
	                    String res = json_aggiornamento.getString(KEY_SUCCESS); 
	                    if(Integer.parseInt(res) == 1){
	                    	new doControlloGiocatore().execute(); // attivo il giocatore
	                    }	                    
	                    else{
							alert.showAlertDialog(RegistrazioneGioco.this, "Errore Aggiornamento", "Errore nel aggiornamento", false);
	 	               	}
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }
	    } // fine aggiornamento
	    
	    
	    /**
	     * BackGround Task per registrazione Gioco
	     */
	    class doRegistrazioneGioco extends AsyncTask<String,String,String>{

	    	protected void onPreExecute() {
	             super.onPreExecute();
	             pDialog = new ProgressDialog(RegistrazioneGioco.this);
	             pDialog.setTitle("Gioco non ancora registrato!");
	             pDialog.setMessage("Registrazione Gioco. Aspettare prego....");
	             pDialog.setIndeterminate(false);
	             pDialog.setCancelable(true);
	             pDialog.show();
	        } 
	    	
			protected String doInBackground(String... params) {
				
				fGioco = new FunzioniGioco();
				json_registrazione = fGioco.registraGioco(textNomeGioco, textPiattaforma, textGenere);

				try {
					if (json_registrazione.getString(KEY_SUCCESS) != null) {
						String result = json_registrazione.getString(KEY_SUCCESS); 
						if(Integer.parseInt(result) == 1){
							// gioco registrato nel database centrale successfully registred
							JSONObject json_gioco = json_registrazione.getJSONObject("gioco");
					        // prendo i detagli del gioco dal tatabase centrale      
							String gioco_id = json_gioco.getString(KEY_GIOCO_ID);
							String nomegioco = json_gioco.getString(KEY_NOME_GIOCO);
							String piattaforma = json_gioco.getString(KEY_PIATTAFORMA);
							String genere = json_gioco.getString(KEY_GENERE);
							int num_giocatori = json_gioco.getInt(KEY_NUM_GIOCATORI);
							
							// registro il gioco nel database locale
							Gioco game = new Gioco(nomegioco,piattaforma,genere);
							dbGioco.addGioco(game);
						}
					}
					else{
						// gioco non registrato nel database centrale
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				return null;
			}
			
			protected void onPostExecute(String result) {
	            
	            pDialog.dismiss();
	            
	            try {
	                if (json_registrazione.getString(KEY_SUCCESS) != null) {
	                    String res = json_registrazione.getString(KEY_SUCCESS); 
	                    if(Integer.parseInt(res) == 1){
	                    	new doControlloGiocatore().execute(); // attivo il giocatore
	                    }	                    
	                    else{
							alert.showAlertDialog(RegistrazioneGioco.this, "Errore Registrazione", "Errore nella registrazione", false);
	 	               	}
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }
	    	
	    }
	    
	    /**
	     * BackGround Task per attivazione giocatore
	     */
	    class doControlloGiocatore extends AsyncTask <String,String,String>{
			
	    	protected String doInBackground(String... params) {
				
	    		fGiocatore = new FunzioniGiocatore();
				json_controlloGiocatore = fGiocatore.isOnline(utenteUsername);
				
				return null;
			}
	    	
	    	protected void onPostExecute(String result) {
	            
	            try {
	                if (json_controlloGiocatore.getString(KEY_SUCCESS) != null) {
	                    String res = json_controlloGiocatore.getString(KEY_SUCCESS); 
	                    if(Integer.parseInt(res) == 1){
	                    	new doDisattivazioneGiocatore().execute(); // attivo il giocatore
	                    }	                    
	                    else{
	                    	new doAttivazioneGiocatore().execute(); // attivo il giocatore
	                    	
	 	               	}
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }
	    	
	    }// fine doControlloGiocatore
	    
	    /**
	     * BackGround Task per attivazione giocatore
	     */
	    class doAttivazioneGiocatore extends AsyncTask <String,String,String>{ 
			
	    	protected String doInBackground(String... params) {
				
	    		fGiocatore = new FunzioniGiocatore();
				json_attivazioneGiocatore = fGiocatore.inserisciGiocatore(utenteUsername, textNomeGioco, textPiattaforma, textNickGame, myLatitude , myLongitude);
				
				return null;
			}
	    	
	    	protected void onPostExecute(String result) {
	            
	            try {
	                if (json_attivazioneGiocatore.getString(KEY_SUCCESS) != null) {
	                    String res = json_attivazioneGiocatore.getString(KEY_SUCCESS); 
	                    if(Integer.parseInt(res) == 1){
	                    	alert.showAlertDialog(RegistrazioneGioco.this, "Attivazione Online", "Attivazione Giocatore effettuata", true);
	                    	// nuovo intent
							Intent CercaMenu = new Intent(RegistrazioneGioco.this,CercaGioco.class);
							startActivity(CercaMenu);
							finish();
	                    }	                    
	                    else{
							alert.showAlertDialog(RegistrazioneGioco.this, "Errore Attivazione", "Errore nel attivazione online", false);
	 	               	}
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }
	    	
	    }// fine doAttivazioneGiocatore
	    
	    /**
	     * BackGround Task per attivazione giocatore
	     */
	    class doDisattivazioneGiocatore extends AsyncTask <String,String,String>{ 
			
	    	protected String doInBackground(String... params) {
				
	    		fGiocatore = new FunzioniGiocatore();
				json_disattivazioneGiocatore = fGiocatore.removeGiocatore(utenteUsername);
				
				return null;
			}
	    	
	    	protected void onPostExecute(String result) {
	            
	            pDialog.dismiss();
	            
	            try {
	                if (json_disattivazioneGiocatore.getString(KEY_SUCCESS) != null) {
	                    String res = json_disattivazioneGiocatore.getString(KEY_SUCCESS); 
	                    if(Integer.parseInt(res) == 1){
	                    	new doAttivazioneGiocatore().execute(); // riattivo il giocatore
	                    }	                    
	                    else{
							alert.showAlertDialog(RegistrazioneGioco.this, "Errore Disattivazione", "Errore nella disattivazione online", false);
	 	               	}
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }
	    	
	    }// fine doDisattivazioneGiocatore
	   		
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
					
		}

		
		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}

}
