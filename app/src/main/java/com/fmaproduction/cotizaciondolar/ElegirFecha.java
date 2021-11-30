package com.fmaproduction.cotizaciondolar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ElegirFecha extends AppCompatActivity {

    DatePicker simpleDatePicker;
    int dia, mes, ano;
    Date fechaActual;
    Toast toast;
    TextView letrasMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_fecha);
        getSupportActionBar().hide();
        Calendar calendario = Calendar.getInstance();
        Bundle extras = getIntent().getExtras();
        dia = extras.getInt("dia", 1);
        mes = extras.getInt("mes", 1);
        ano = extras.getInt("ano", 1);
        if (ano == 1) {
            dia = calendario.get(Calendar.DAY_OF_MONTH);
            mes = calendario.get(Calendar.MONTH);
            ano = calendario.get(Calendar.YEAR);
        }

        fechaActual = new Date(System.currentTimeMillis());

        simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
        Calendar cal = new GregorianCalendar(); //Calendar.getInstance();
        cal.set(ano, mes, dia);
        simpleDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                //Toast.makeText(ElegirFecha.this, "Fecha: " + simpleDatePicker.getDayOfMonth() + "/" + (simpleDatePicker.getMonth() + 1) + "/" + simpleDatePicker.getYear(), Toast.LENGTH_SHORT).show();
                dia = simpleDatePicker.getDayOfMonth();
                mes = simpleDatePicker.getMonth();
                ano = simpleDatePicker.getYear();
            }
        });
    }

    public void fechaLista(View view) {

        Calendar targetCal = Calendar.getInstance();
        targetCal.set(ano, mes, dia);

        Intent intentResult = new Intent();

        String aux = String.valueOf(dia);
        if (dia < 9) aux = "0" + aux;
        intentResult.putExtra("dia", aux);
        mes++;
        aux = String.valueOf(mes);
        if (mes < 9) aux = "0" + aux;
        intentResult.putExtra("mes", aux);
        intentResult.putExtra("ano", String.valueOf(ano));
        setResult(RESULT_OK, intentResult);
        finish();
    }

    public void cancelado(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void tostada(String mensaje) {
        toast = new Toast(this);
        View toast_layout = getLayoutInflater().inflate(R.layout.toast_drawable, (ViewGroup) findViewById(R.id.lytLayout));
        letrasMensaje = toast_layout.findViewById(R.id.toastMessage);
        toast.setView(toast_layout);
        TextView textView = (TextView) toast_layout.findViewById(R.id.toastMessage);
        textView.setText(mensaje);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    } // fin tostada

}
