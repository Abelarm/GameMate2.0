package com.GameMate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CambiaAvatar extends Activity {
	
	ImageView Avatar1,Avatar2,Avatar3,Avatar4,Avatar5,Avatar6,Avatar7,Avatar8,Avatar9,Avatar10,Avatar11,Avatar12;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avatar);
		
		Avatar1=(ImageView) findViewById(R.id.Avatar1);
		Avatar2=(ImageView) findViewById(R.id.Avatar2);
		Avatar3=(ImageView) findViewById(R.id.Avatar3);
		Avatar4=(ImageView) findViewById(R.id.Avatar4);
		Avatar5=(ImageView) findViewById(R.id.Avatar5);
		Avatar6=(ImageView) findViewById(R.id.Avatar6);
		Avatar7=(ImageView) findViewById(R.id.Avatar7);
		Avatar8=(ImageView) findViewById(R.id.Avatar8);
		Avatar9=(ImageView) findViewById(R.id.Avatar9);
		Avatar10=(ImageView) findViewById(R.id.Avatar10);
		Avatar11=(ImageView) findViewById(R.id.Avatar11);
		Avatar12=(ImageView) findViewById(R.id.Avatar12);
		
		
		Avatar1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i= new Intent();
				i.putExtra("Avatar","1");
				
				setResult(100,i);
				finish();
				
			}
		});
		
		Avatar2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i= new Intent();
				i.putExtra("Avatar","2");
				
				setResult(100,i);
				finish();
				
			}
		});
		
		Avatar3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i= new Intent();
				i.putExtra("Avatar","3");
				
				setResult(100,i);
				finish();
				
			}
		});
		
		Avatar4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i= new Intent();
				i.putExtra("Avatar","4");
				
				setResult(100,i);
				finish();
				
			}
		});

		Avatar5.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				Intent i= new Intent();
				i.putExtra("Avatar","5");
		
				setResult(100,i);
				finish();
		
			}
		});

		Avatar6.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				Intent i= new Intent();
				i.putExtra("Avatar","6");
		
				setResult(100,i);
				finish();
		
			}
		});

		Avatar7.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				Intent i= new Intent();
				i.putExtra("Avatar","7");
		
				setResult(100,i);
				finish();
		
			}
		});

		Avatar8.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				Intent i= new Intent();
				i.putExtra("Avatar","8");
		
				setResult(100,i);
				finish();
		
			}
		});

		Avatar9.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
	
				Intent i= new Intent();
				i.putExtra("Avatar","9");
		
				setResult(100,i);
				finish();
		
			}
		});

		Avatar10.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				Intent i= new Intent();
				i.putExtra("Avatar","10");
		
				setResult(100,i);
				finish();
		
			}
		});

		Avatar11.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				Intent i= new Intent();
				i.putExtra("Avatar","11");
		
				setResult(100,i);
				finish();
		
			}
		});

		Avatar12.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				Intent i= new Intent();
				i.putExtra("Avatar","12");
		
				setResult(100,i);
				finish();
		
			}
		});

	}
}
