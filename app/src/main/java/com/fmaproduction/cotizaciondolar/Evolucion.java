package com.fmaproduction.cotizaciondolar;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;

public class Evolucion extends AppCompatActivity {

    LinearLayout pantallaEvolucion;
    int dia, mes, ano;
    String compra, venta;
    Date fechaEvolucion;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolucion);
        getSupportActionBar().hide();

        sharedPreferences = getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        pantallaEvolucion = findViewById(R.id.pantallaevolucion);

        //fechaEvolucion = (Date) new java.util.Date(System.currentTimeMillis());


        Calendar calendario = Calendar.getInstance();


        for (int a = 1; a <= 10; a++) {

            dia = calendario.get(Calendar.DAY_OF_MONTH);
            mes = calendario.get(Calendar.MONTH) + 1;
            ano = calendario.get(Calendar.YEAR);

            String aux = String.valueOf(dia);
            if (dia < 9) aux = "0" + aux;
            compra = "compra" + aux;
            venta = "venta" + aux;
            aux = String.valueOf(mes);
            if (mes < 9) aux = "0" + aux;
            compra += aux + String.valueOf(ano);
            venta += aux + String.valueOf(ano);

            Log.i("MENSAJE", compra + "\n \n" + venta);

            compra = sharedPreferences.getString(compra, " Sin datos");
            venta = sharedPreferences.getString(venta, " Sin datos");

            TextView cotizacion = new TextView(this);
            cotizacion.setTextColor(getResources().getColor(R.color.black));
            cotizacion.setText(dia + "/" + mes + "/" + ano +
                    "\nCompra: " + compra + "  -  Venta: " + venta);
            LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cotizacion.setLayoutParams(parametros);
            cotizacion.setGravity(Gravity.CENTER);
            cotizacion.setPadding(0, 20, 0, 20);
            pantallaEvolucion.addView(cotizacion);

            calendario.set(ano, mes, -a);
        }

    }
}