package com.davidmcasas.ejemplowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Clase del widget
 *
 * Para crear un nuevo widget en nuestra App, es recomendable
 * hacerlo desde File > New > Widget > App Widget, y marcar la casilla Configuration Screen.
 * De esta forma, se creará también el Activity de configuración por defecto, y se actualizará
 * el AndroidManifest.xml automáticamente
 */
public class MyWidget extends AppWidgetProvider {

    /**
     * Método encargado de actualizar el contenido del Widget
     * @param context Context de la App
     * @param appWidgetManager Objeto AppWidgetManager
     * @param appWidgetId ID que Android le asignó al widget al crearlo
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Obtenemos el SharedPreferences de la App, para acceder a los valores guardados
        SharedPreferences sp = getDefaultSharedPreferences(context.getApplicationContext());

        // Obtenemos un RemoteViews, que nos permite acceder y manipular los componentes del widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);

        // Obtenemos la eleccion almacenada asociada a este widget
        String eleccion = MyWidgetConfigureActivity.cargarPreferencia(context, appWidgetId);

        // Según la elección, cargamos el valor almacenado correspondiente
        switch (eleccion) {
            case "1" : views.setTextViewText(R.id.appwidget_text, sp.getString("edittext1", "")); break;
            case "2" : views.setTextViewText(R.id.appwidget_text, sp.getString("edittext2", "")); break;
        }

        /*
        Estas cuatro instrucciones harán que al hacer click en el widget, se abrá el MainActivty.
        Se está añadiendo el ID del widget mediante putExtra(), de esta forma podremos determinar
        desde qué widget estamos abriendo el Activty, aunque en este ejemplo no se hace uso de ello.
         */
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("appWidgetId", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        // Finalmente aplicamos la actualización
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Actualizar todos los widgets que se han indicado
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Cuando se borra el widget, se borra la configuración asociada a él, en la
        // que se almacenaba la elección de EditText
        for (int appWidgetId : appWidgetIds) {
            MyWidgetConfigureActivity.borrarPreferencia(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Este método se ejecuta cuando se crea el primero de los widgets
    }

    @Override
    public void onDisabled(Context context) {
        // Este método se ejecuta cuando se eliminan todos los widgets
    }
}

