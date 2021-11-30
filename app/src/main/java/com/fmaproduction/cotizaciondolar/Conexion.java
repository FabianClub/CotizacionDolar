package com.fmaproduction.cotizaciondolar;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

class Conexion extends AsyncTask<String, String, String> {

    HttpURLConnection httpURLConnection;
    URL url;

    @Override
    protected String doInBackground(String... strings) {
        //Log.i("MENSAJE", "INICIANDO CONEXION");
        try {
            url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            //Log.i("MENSAJE", "CONECTANDO");
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Log.i("MENSAJE", "YA CONECTADO!");
                InputStream is = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                String line = "", leido = "";
                //StringBuffer buffer = new StringBuffer();
                while ((line = bf.readLine()) != null) {
                    //buffer.append(line);
                    leido += line;
                }
                //return buffer.toString();
                return leido;
            }


        } catch (Exception e) {
            //e.printStackTrace();
            //Log.i("MENSAJE", "NO SE PUDO CONECTAR Y DESCARGAR");
        }

        return null;
    }
}
