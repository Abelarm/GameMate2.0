package com.GameMate.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    
    private Context Context;
     
    public ConnectionDetector(Context context){
        this.Context = context;
    }
 
    @SuppressWarnings("static-access")
	public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) Context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null){
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
}