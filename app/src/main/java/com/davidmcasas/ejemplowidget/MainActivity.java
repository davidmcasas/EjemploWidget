package com.davidmcasas.ejemplowidget;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.preference.PreferenceManager.*;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp; // Se usará para almacenar los datos de la app en el sistema Android
    EditText ed1, ed2;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenemos el SharedPreferences de nuestra App
        sp = getDefaultSharedPreferences(getApplicationContext());

        // Almacenamos nuestros EditText
        ed1 = findViewById(R.id.editText1);
        ed2 = findViewById(R.id.editText2);

        // Obtenemos los valores guardados previamente, si no existen ponemos un String vacío
        ed1.setText(sp.getString("edittext1", ""));
        ed2.setText(sp.getString("edittext2", ""));

        // Almacenamos el Button
        b = findViewById(R.id.button);
    }

    /**
     * Guarda los valores de los EditText en SharedPreferences y finaliza el Activity
     * @param view
     */
    public void botonGuardar(View view) {

        // Guardamos en el sistema los valores de nuestros EditText usando nuestro SharedPreferences
        sp.edit().putString("edittext1", ed1.getText().toString()).apply();
        sp.edit().putString("edittext2", ed2.getText().toString()).apply();

        // Llamamos al método para actualizar los widgets
        actualizarWidgets();

        // finalizamos el Activity
        finish();
    }

    /**
     * Este método hace que se llame al método OnUpdate de MyWidget, pasándole un array
     * con todos los IDs de los widgets asociados a nuestra App, para que se actualicen todos.
     */
    public void actualizarWidgets() {
        Intent intent = new Intent(this, MyWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MyWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }
}