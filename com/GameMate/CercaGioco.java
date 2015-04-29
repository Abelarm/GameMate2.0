package com.GameMate;

import com.GameMate.R;
import com.GameMate.library.AlertDialogManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class CercaGioco extends Activity implements OnItemSelectedListener{
	
	 // inizializzo le variabili
	EditText inputNomeGioco;
	EditText inputPlatform;
	
	Spinner comboPiattaforma;
	
	Spinner comboDistanza;
	
	// Alert Dialog Manager
    AlertDialogManager alert; 
	
	



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamesearch);
        
        alert = new AlertDialogManager();
        
        inputNomeGioco		= 	(EditText) findViewById(R.id.GameName);
        
        comboPiattaforma = (Spinner) findViewById(R.id.platform);
        comboPiattaforma.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterPiattaforma = ArrayAdapter.createFromResource(this,
        R.array.piattaforme_lista, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterPiattaforma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        comboPiattaforma.setAdapter(adapterPiattaforma);
                
        comboDistanza = (Spinner) findViewById(R.id.distanza);
        comboDistanza.setOnItemSelectedListener(this);
	     // Create an ArrayAdapter using the string array and a default spinner layout
	     ArrayAdapter<CharSequence> adapterDistanza = ArrayAdapter.createFromResource(this,
	             R.array.distanza_ricerca, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	     adapterDistanza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // Apply the adapter to the spinner
	     comboDistanza.setAdapter(adapterDistanza);
         ImageButton cercaGiocoButton = (ImageButton) findViewById(R.id.SearchGameButton);
        
        // quando clicco su cerca Gioco
        cercaGiocoButton.setOnClickListener(new View.OnClickListener() {
        	
			public void onClick(View arg0) {
				
				String textNomeGioco = inputNomeGioco.getText().toString();
				
				if(textNomeGioco.equals("")){
					alert.showAlertDialog(CercaGioco.this, "Nome Gioco Error", "Inserire un Nome Gioco valido", false);
				}
				else{
					// nuovo intent
					Intent PinPointMenu = new Intent(getApplicationContext(),PinPoint.class);
					
					PinPointMenu.putExtra("nomegioco", inputNomeGioco.getText().toString());
					PinPointMenu.putExtra("piattaforma", comboPiattaforma.getSelectedItem().toString());
					PinPointMenu.putExtra("distanza", comboDistanza.getSelectedItem().toString().substring(0, 2).trim());
					
					startActivity(PinPointMenu);
					finish();
				}
			}
		});
        
        
        ImageButton annulla=(ImageButton) findViewById(R.id.annulla);
        
        annulla.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				inputNomeGioco.setText("");
				
			}
		});
    }


    public void onBackPressed(){
    	Intent i=new Intent(CercaGioco.this,MainMenu.class);
		startActivity(i);
		this.finish();
	}


	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	
		
	}





	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

}
