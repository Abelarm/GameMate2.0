package com.GameMate;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.GameMate.database.AmicoDatabaseHandler;
import com.GameMate.database.UtenteDatabaseHandler;
import com.GameMate.library.AlertDialogManager;
import com.GameMate.library.ConnectionDetector;
import com.GameMate.library.GPSTracker;
import com.GameMate.objects.FunzioniGiocatore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PinPoint extends Activity implements OnMarkerClickListener{

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
    
    MapFragment Map;
    GoogleMap googleMap;
    
    Marker myMarker;
    Marker playerMarker;
    Marker friendMarker;
    
    LatLng myLatLng;
    Location myLocation;
    
    String NomeGioco;
    String Piattaforma;
    String Distanza;
    
    private int utenteIcon;
    private int playerIcon;
    private int friendIcon;
    
    // Progress Dialog
    private ProgressDialog pDialog;
    
    UtenteDatabaseHandler dbUtente;
    AmicoDatabaseHandler dbAmico;
   
    protected static final String KEY_UTENTE_USERNAME = "Username";
    
    private static String KEY_SUCCESS = "success";
    
    private static String KEY_NOMEGIOCO = "TAG_NOMEGIOCO";
    private static String KEY_PIATTAFORMA = "TAG_PIATTAFORMA";
    private static String KEY_USERNAME = "TAG_USERNAME";
    private static String KEY_NICKNAME = "TAG_NICKNAME";
    private static String KEY_LATITUDINE = "TAG_LAT";
    private static String KEY_LONGITUDINE = "TAG_LON";
    private static String KEY_DISTANZA = "TAG_DISTANZA";
    
    private String utenteUsername;
    private int distanza_cercata; 
    
    FunzioniGiocatore fGiocatore;
    
    JSONObject json_cercaGiocatori;
    
    private ArrayList<Marker> listaMarker;
    
    ArrayList<HashMap<String, String>> listaGiocatori; // arraylist per tutti i giocatori
    
    JSONArray Giocatori = null; // array per il giocatore
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpoint); 
        
        utenteIcon = R.drawable.mark_red;
        playerIcon = R.drawable.mark_blue;
        friendIcon = R.drawable.mark_green;
        
        listaMarker=new ArrayList<Marker>();
        
        dbUtente = new UtenteDatabaseHandler(this);
        dbAmico = new AmicoDatabaseHandler(this);
        
        // prendo i miei dati dal database locale
        HashMap<String,String> utente = dbUtente.getUserDetails();
        utenteUsername = (String) utente.get(KEY_UTENTE_USERNAME);
        Log.e("username utente", utenteUsername);
     
        Intent i = getIntent();
        // Receiving the Data
        NomeGioco = i.getStringExtra("nomegioco");
        Piattaforma = i.getStringExtra("piattaforma");
        Distanza = i.getStringExtra("distanza");
        
        distanza_cercata = Integer.parseInt(Distanza);
        
        cd = new ConnectionDetector(PinPoint.this);
        
        // Controllo se Internet  è presente
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Connessione Internet non presente
            alert.showAlertDialog(PinPoint.this, "Internet Connection Error",
                    "Connessione Internet non presente", false);
            
        }
        
        gps = new GPSTracker (PinPoint.this);
        
        listaGiocatori = new ArrayList<HashMap<String, String>> ();
        
        //GPS abbilitato
        if(gps.canGetLocation()){
        	        	
        	myLatitude = gps.getLatitude();
        	myLongitude = gps.getLongitude();
        	
        	// la mia posizione
        	myLatLng = new LatLng(myLatitude,myLongitude);
        	        	
        	Map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            
            googleMap = Map.getMap();
            
            if(googleMap == null){
            	alert.showAlertDialog(PinPoint.this, "Google Map Error", "Impossibile visualizzare la Mappa", false);
            }
            else{
            	googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				googleMap.setOnMarkerClickListener(this);
            	
            	// aggiungo alla mappa un marker della mia posizione
            	myMarker =  googleMap.addMarker(new MarkerOptions()
            				.position(myLatLng)
            				.title(utenteUsername)
            				.icon(BitmapDescriptorFactory.fromResource(utenteIcon))
            	);
            	
            	CameraPosition cameraPosition = new CameraPosition.Builder()
                								.target(myLatLng) // Sets the center of the map to Mountain View
                								.zoom(14)                   // Sets the zoom
                								.build();    // Creates a CameraPosition from the builder
            	
            	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            	
            	new doGetGiocatoriAttivi().execute();
            	
            	
            	
            	
            	
            }
         
            /*
            mapView.setBuiltInZoomControls(true);
            
            MapController mc = mapView.getController();
           
            GeoPoint geoPoint = new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
            mc.animateTo(geoPoint);
            mc.setZoom(15);
           
            List<Overlay> mapOverlays = mapView.getOverlays();
            Drawable drawable = this.getResources().getDrawable(R.drawable.mark_red);
            PinMarker itemizedOverlay = new PinMarker(drawable, this);
                     
                     
            OverlayItem overlayitem = new OverlayItem(geoPoint, "Hello", "Sample Overlay item");
                     
            itemizedOverlay.addOverlay(overlayitem);
            mapOverlays.add(itemizedOverlay);
            mapView.invalidate(); */
        }
        else{ // abbilitare GPS
        	gps.showSettingsAlert();
        }
        
        
        
       
         
    }
 
	class doGetGiocatoriAttivi extends AsyncTask<String,String,String>{
		
		protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PinPoint.this);
            pDialog.setTitle("Ricerca Giocatori");
            pDialog.setMessage("Ricerca Giocatori online. Aspettare prego....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        } 
		
		protected String doInBackground(String... arg0) {
			
			fGiocatore = new FunzioniGiocatore();
			json_cercaGiocatori = fGiocatore.getGiocatoriBy(NomeGioco, Piattaforma, myLatitude, myLongitude);
			
			Log.e("QUI", NomeGioco+Piattaforma+myLatitude+myLongitude);
			Log.e("Lista Giocatori", json_cercaGiocatori.toString());
			
			try{
				// controllo se la ricerca ha avuto successo
				int success = json_cercaGiocatori.getInt(KEY_SUCCESS);
				
				if(success == 1){ //giocatori trovati
					// prendo l'array di giocatori
					Giocatori = json_cercaGiocatori.getJSONArray("players");
					
					// cliclo per prendere tutti i giocatori
					for(int i = 0; i<Giocatori.length(); i++){
						// prendo un giocatore
						JSONObject player = Giocatori.getJSONObject(i);
					
						// prendo i suoi dati
						String playerUsername = player.getString(KEY_USERNAME);
						String playerNomeGioco = player.getString(KEY_NOMEGIOCO);
						String playerPiattaforma = player.getString(KEY_PIATTAFORMA);
						String playerNickname = player.getString(KEY_NICKNAME);
						String playerLatitudine = player.getString(KEY_LATITUDINE);
						String playerLongitudine = player.getString(KEY_LONGITUDINE);
						String playerDistanza = player.getString(KEY_DISTANZA);
						
						
						if(playerUsername.equals(utenteUsername))
							continue;
						// creo una mappa hash dove memmorizzare le info
						HashMap<String, String> map = new HashMap<String, String>();
					
						map.put(KEY_USERNAME, playerUsername);
						map.put(KEY_NOMEGIOCO, playerNomeGioco);
						map.put(KEY_PIATTAFORMA, playerPiattaforma);
						map.put(KEY_NICKNAME, playerNickname);
						map.put(KEY_LATITUDINE, playerLatitudine);
						map.put(KEY_LONGITUDINE, playerLongitudine);
						map.put(KEY_DISTANZA, playerDistanza);
						
						//aggiungo la mappa all'Arraylist
						listaGiocatori.add(map);
					}
				}
				else{
					//nessun giocatore trovato
				}
				
			} catch (JSONException e) {
                e.printStackTrace();
            }
			
			
			return null;
		}
		
		protected void onPostExecute(String result) {
            boolean flag=false;
            pDialog.dismiss();
            
            // check for login response
            try {
                if (json_cercaGiocatori.getString(KEY_SUCCESS) != null) {
                    String res = json_cercaGiocatori.getString(KEY_SUCCESS); 
                    if(Integer.parseInt(res) == 1){

                        for(HashMap<String,String> M : listaGiocatori){
                    		double D = Double.parseDouble(M.get(KEY_DISTANZA));
                    		Log.e("Distanza player", ":"+D);
                    		Log.e("Distanza cercata",":"+distanza_cercata);
                    		if(D <= distanza_cercata){
                    			Log.e("log","qui entra?");
                    			flag=true;
                    			Log.e("flag", ":"+flag);
                    			String playerUsername = M.get(KEY_USERNAME);
                    			double playerLatitudine = Double.parseDouble(M.get(KEY_LATITUDINE));
                    			double playerLongitudine = Double.parseDouble(M.get(KEY_LONGITUDINE));
                    			
                    			// la posizione del player
                            	LatLng playerLatLng = new LatLng(playerLatitudine,playerLongitudine);
                    			if(!dbAmico.areFriends(playerUsername)){
                    				Log.e("Sono amici",":"+dbAmico.areFriends(playerUsername));
                    				playerMarker =  googleMap.addMarker(new MarkerOptions()
                    								.position(playerLatLng)
                    								.title(playerUsername)
                    								.icon(BitmapDescriptorFactory.fromResource(playerIcon))
                    				
                    				
                    				);
                    				listaMarker.add(playerMarker);

                    				
                    			}
                    			else{
                    				Log.e("Sono amici",":"+dbAmico.areFriends(playerUsername));
                    				friendMarker =  googleMap.addMarker(new MarkerOptions()
        							.position(playerLatLng)
        							.title(playerUsername)
        							.icon(BitmapDescriptorFactory.fromResource(friendIcon))
        			
        			
                    				);
                    				listaMarker.add(friendMarker);
                    			}
                       		}
                    		Log.e("log","è più lontano");
                    	}
                        if(flag)
                        	alert.showAlertDialog(PinPoint.this, "Ricerca Giocatori", "Giocatori trovati", true);
                        else
                        	alert.showAlertDialog(PinPoint.this, "Ricerca Giocatori", "Giocatori  non trovati", true);
                    }	                    
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
           
        }
		
	}
  
	
	
	
    protected boolean isRouteDisplayed() {
        return false;
    }




	@Override
	public boolean onMarkerClick(final Marker marker) {
		if(marker.equals(playerMarker) || marker.equals(friendMarker)){
			Log.e("Entrato", "sono entrato nell if");
			Intent GestioneMarkerMenu = new Intent(PinPoint.this,GestioneMarker.class);
			
			String playerMarkerUsername=marker.getTitle();
			
			GestioneMarkerMenu.putExtra("MarkerUsername", playerMarkerUsername);
			
        	startActivityForResult(GestioneMarkerMenu,100);
        	
        	
		}
		return false;
	}
	
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			String playerUsername=(String) data.getExtras().get("MarkerUsername");
			for(Marker marker:listaMarker){
				if(marker.getTitle().equals(playerUsername))
					marker.setIcon(BitmapDescriptorFactory.fromResource(friendIcon));
			}
		}
		if(resultCode==200){
			String playerUsername=(String) data.getExtras().get("MarkerUsername");
			for(Marker marker:listaMarker){
				if(marker.getTitle().equals(playerUsername))
					marker.setIcon(BitmapDescriptorFactory.fromResource(playerIcon));
			}
		}
	}
	
	public void onBackPressed(){
    	Intent i=new Intent(PinPoint.this,MainMenu.class);
		startActivity(i);
		this.finish();
	}
}
