package com.davidmcasas.ejemplowidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity de configuración de {@link MyWidget MyWidget} AppWidget.
 */
public class MyWidgetConfigureActivity extends Activity {

    // Nombre de las preferencias
    private static final String PREFS_NAME = "com.davidmcasas.ejemplowidget.MyWidget";

    // prefijo de la clave del widget, ira seguido del id de cada widget
    private static final String PREF_PREFIX_KEY = "appwidget_";

    // ID del widget, empieza con valor inválido
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    // Botones para seleccionar un EditText u otro
    Button b1, b2;

    // Listener con la acción que realizarán los botones
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MyWidgetConfigureActivity.this;

            // Comprobamos qué botón se ha pulsado
            if (v == b1) { // Si ha sido el botón 1, guardamos el valor "1"
                guardarPreferencia(context, mAppWidgetId, "1");
            } else if (v == b2) { // Si ha sido el botón 2, guardamos el valor "2"
                guardarPreferencia(context, mAppWidgetId, "2");
            }

            // Actualizamos el widget recién creado
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MyWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Decimos que la configuración del Widget ha concluido correctamente, con su ID
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            // Finalizamos este activty
            finish();
        }
    };

    public MyWidgetConfigureActivity() {
        super();
    }

    /**
     * Guarda la preferencia de un widget.
     *
     * @param context Context de la App
     * @param appWidgetId ID del widget
     * @param text Valor a guardar
     */
    static void guardarPreferencia(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    /**
     * Carga la preferencia de un widget.
     *
     * @param context Context de la App
     * @param appWidgetId ID del widget
     * @return String con el valor guardado, o un valor por defecto en caso de no existir
     */
    static String cargarPreferencia(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    /**
     * Borra la preferencia asociada a un widget
     *
     * @param context Context de la App
     * @param appWidgetId ID del widget cuya preferencia borrar
     */
    static void borrarPreferencia(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // El resultado del Widget empieza valiendo CANCELED, de esta forma, si el usuario
        // cierra el activty de configuración antes de terminar de configurarlo, podremos
        // detectarlo y el widget se borrará
        setResult(RESULT_CANCELED);

        // Se establece el layout
        setContentView(R.layout.my_widget_configure);

        // Obtenemos los Button y le asignamos el listener
        b1 = findViewById(R.id.widget_button1);
        b2 = findViewById(R.id.widget_button2);

        b1.setOnClickListener(mOnClickListener);
        b2.setOnClickListener(mOnClickListener);

        // Bloque con el que obtenemos el ID de este widget
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Si el ID del widget es inválido, finalizar.
        // (La verdad es que no sé cuándo puede darse este caso, pero por defecto este Activty
        // se crea con esta verificación)
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }
}

