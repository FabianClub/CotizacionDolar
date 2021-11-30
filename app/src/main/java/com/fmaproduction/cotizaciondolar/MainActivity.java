package com.fmaproduction.cotizaciondolar;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView hora, fecha, dolarOficial, dolarBlue, dolarSoja, dolarContado,
            dolarTurista, dolarBolsa;
    FechaYHora fechaYHora;
    int dia, mes, ano, DATOS_FECHA = 98;
    boolean corriendo = true, recargarValores = true;
    SimpleDateFormat dateFormat, timeFormat, dateFormatArchivo;
    Conexion conexion;
    String datosDolar, compra, venta, nombreDolar, variacion, signo;
    JSONObject jsonObject, jsonObject2;
    JSONArray jsonArray;
    Calendar targetCal;
    Intent elegirFecha;
    Toast toast;
    TextView letrasMensaje;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        sharedPreferences = getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        hora = findViewById(R.id.hora);
        fecha = findViewById(R.id.fecha);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormatArchivo = new SimpleDateFormat("ddMMyyyy");
        targetCal = Calendar.getInstance();
        Calendar calendario = Calendar.getInstance();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        ano = calendario.get(Calendar.YEAR);
        elegirFecha = new Intent(MainActivity.this, ElegirFecha.class);

        dolarOficial = findViewById(R.id.dolarOficial);
        dolarBlue = findViewById(R.id.dolarBlue);
        dolarSoja = findViewById(R.id.dolarSoja);
        dolarContado = findViewById(R.id.dolarContadoConLiqui);
        dolarTurista = findViewById(R.id.dolarTurista);
        dolarBolsa = findViewById(R.id.dolarBolsa);

        iniciar();
    }

    public void iniciar() {
        conexion = new Conexion();
        try {
            datosDolar = conexion.execute("https://www.dolarsi.com/api/api.php?type=valoresprincipales").get();
        } catch (Exception e) {
            Log.i("MENSAJE", "Problema SECTOR 1");
            tostada(getResources().getText(R.string.problema_conexion).toString());
        }
        Log.i("MENSAJE", "Leido: ");// + datosDolar);

        if (datosDolar == null) datosDolar = "Sin datos";
        if (datosDolar.equals("Sin datos")) {
            Log.i("MENSAJE", "Problema SECTOR 2");
            tostada(getResources().getText(R.string.problema_conexion).toString());
        } else {
            try {
                jsonArray = new JSONArray(datosDolar);
            } catch (JSONException e) {

            }

            int totalFilas = jsonArray.length();
            Log.i("MENSAJE", "Filas: " + totalFilas);
            for (int a = 0; a < totalFilas; a++) {
                try {
                    jsonObject = jsonArray.getJSONObject(a);
                    jsonObject2 = (JSONObject) jsonObject.get("casa");
                    nombreDolar = jsonObject2.getString("nombre");
                    compra = jsonObject2.getString("compra");
                    venta = jsonObject2.getString("venta");
                    variacion = jsonObject2.getString("variacion");
                } catch (JSONException e) {

                }
                Log.i("MENSAJE", nombreDolar + ": Compra: " + compra
                        + "; Venta: " + venta + "; Variación: " + variacion);

                signo = "";
                if (variacion.charAt(0) != '-') {
                    signo = "+";
                }
                if (variacion.equals("0")) {
                    variacion = "";
                }
                if (!variacion.equals("")) {
                    variacion = " (" + signo + variacion + ")";
                }

                switch (nombreDolar) {
                    case "Dolar Oficial":
                        dolarOficial.append("\n \nCompra: " + compra + " - Venta: " + venta
                                + variacion);
                        editor.putString("compra" + (dateFormatArchivo.format(System.currentTimeMillis())),
                                compra);
                        Log.i("MENSAJE", "REGISTRO: " + ("compra" + (dateFormatArchivo.format(System.currentTimeMillis()))));
                        editor.putString("venta" + (dateFormatArchivo.format(System.currentTimeMillis())),
                                venta);
                        editor.commit();
                        break;
                    case "Dolar Blue":
                        dolarBlue.append("\n \nCompra: " + compra + " - Venta: " + venta
                                + variacion);
                        break;
                    case "Dolar Soja":
                        dolarSoja.append("\n \nCompra: " + compra + " - Venta: " + venta
                                + variacion);
                        break;
                    case "Dolar Contado con Liqui":
                        dolarContado.append("\n \nCompra: " + compra + " - Venta: " + venta
                                + variacion);
                        break;
                    case "Dolar turista":
                        dolarTurista.append("\n \nCompra: " + compra + " - Venta: " + venta
                                + variacion);
                        break;
                    case "Dolar Bolsa":
                        dolarBolsa.append("\n \nCompra: " + compra + " - Venta: " + venta
                                + variacion);
                        break;
                }


            }


        }
    }

    public class FechaYHora extends Thread {
        @Override
        public void run() {
            super.run();
            long displayFechayHora = System.currentTimeMillis() + 1000;
            while (corriendo) {
                if (System.currentTimeMillis() > displayFechayHora) {
                    hora.setText(timeFormat.format(System.currentTimeMillis()));
                    fecha.setText(dateFormat.format(System.currentTimeMillis()));
                    displayFechayHora = System.currentTimeMillis() + 1000;
                }
            }
        }
    }

    public void evolucion(View view) {
        startActivity(new Intent(MainActivity.this, Evolucion.class));
    }

    public void calendario(View view) {
        elegirFecha.putExtra("dia", dia);
        elegirFecha.putExtra("mes", mes);
        elegirFecha.putExtra("ano", ano);
        startActivityForResult(elegirFecha, DATOS_FECHA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DATOS_FECHA && resultCode == RESULT_OK) {

            String aux = data.getStringExtra("dia") + data.getStringExtra("mes")
                    + data.getStringExtra("ano");

            Log.i("MENSAJE", "compra" + aux);

            compra = sharedPreferences.getString("compra" + aux,
                    " Sin datos");
            venta = sharedPreferences.getString("venta" + aux,
                    " Sin datos");
            tostada("\n\nFecha: " + data.getStringExtra("dia") + " / " +
                    data.getStringExtra("mes") + " / " + data.getStringExtra("ano") + "\n \n " +
                    "Compra: $ " + compra + "\nVenta: $ " + venta + "\n\n");

        }
    }

    public void recargar(View view) {
        conexion.cancel(true);
        conexion = null;
        tostada(getResources().getText(R.string.recargando).toString());
        dolarOficial.setText(getResources().getText(R.string.dolar_oficial));
        dolarBlue.setText(getResources().getText(R.string.dolar_blue));
        dolarSoja.setText(getResources().getText(R.string.dolar_soja));
        dolarContado.setText(getResources().getText(R.string.dolar_contado_con_liqui));
        dolarTurista.setText(getResources().getText(R.string.dolar_turista));
        dolarBolsa.setText(getResources().getText(R.string.dolar_bolsa));
        iniciar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        corriendo = true;
        fechaYHora = new FechaYHora();
        fechaYHora.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        corriendo = false;
        recargarValores = false;
    }

    public void tostada(String mensaje) {
        toast = new Toast(this);
        View toast_layout = getLayoutInflater().inflate(R.layout.toast_drawable, (ViewGroup) findViewById(R.id.lytLayout));
        letrasMensaje = toast_layout.findViewById(R.id.toastMessage);
        toast.setView(toast_layout);
        TextView textView = (TextView) toast_layout.findViewById(R.id.toastMessage);
        textView.setText(mensaje);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    } // fin tostada
}