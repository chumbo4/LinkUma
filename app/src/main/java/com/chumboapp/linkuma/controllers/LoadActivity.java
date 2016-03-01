package com.chumboapp.linkuma.controllers;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chumboapp.linkuma.R;
import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class LoadActivity extends AppCompatActivity {
    private ImageView mLogoViewImage = null;
    private TextView loadText = null;
    private ProgressBar progress = null;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final int TIME_TO_GO_NEXT_SCRENN = 1000;
    private static final String SEPARATOR = "@@++@@";
    private static final int CHUMBO_ID = 1;
    private static final String PREFS = "PREFS";
    private static final String LAST_UPDATE = "LAST_UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO Ocultar barra de título
        setContentView(R.layout.activity_load);
        //Change Image (We use logo link)
        mLogoViewImage = (ImageView) findViewById(R.id.logo_image);
        mLogoViewImage.setImageResource(R.drawable.logo_link);
        // Mensajes de carga
        loadText = (TextView) findViewById(R.id.loadText);
        loadText.setText("Cargando...");
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(0);
        progress.setProgress(0);

        // Carga la información desde internet
        new CargaInfo().execute();
    }

    // Clase para cargar la información relativa a eventos
    private class CargaInfo extends AsyncTask<Void, Void, Void> {
        private String urlConsultaEventos = "http://www.umalinkmalaga.com/api/get_posts/?post_type=tribe_events&post_status=publish&count=";
        private List<Event> eventosBD = new ArrayList<>();
        private Date ultimaActualizacion = null;
        private boolean actualizar = true;
        private boolean falloActualizacion = false;
        private int eventsCnt = 0;
        private int eventsTotalCnt = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO Comprobar conexión a internet

            if (actualizar) {
                loadText.setText("Comprobando actualización...");
                // Obtiene fecha última actualización
                SharedPreferences settings = getSharedPreferences(PREFS, 0);
                String lastUpdate = settings.getString("LAST_UPDATE", "");
                if (!lastUpdate.equals("")) {
                    try {
                        ultimaActualizacion = SIMPLE_DATE_FORMAT.parse(lastUpdate);
                        Log.i("Ultima Actualizacion", ultimaActualizacion.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        falloActualizacion = true;
                    }
                }
                // Comprueba última actualización reciente
                if (ultimaActualizacion != null) {
                    actualizar = (DiferenciaFechasHoras(ultimaActualizacion, new Date()) > 24);
                }
            }
            actualizar = true;
            if (actualizar) {
                // Número de eventos
//                JSONObject resConsulta = ConsultaEventosLink(1, 0);
                // Barra de progreso
                new Thread(new Runnable() {
                    public void run() {
                        do {
                            progress.setMax(eventsTotalCnt+1);
                            progress.setProgress(eventsCnt);
                            Log.i(Integer.toString(eventsCnt), Integer.toString(eventsTotalCnt));
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (eventsCnt <= eventsTotalCnt);
                    }
                }).start();
//                if (progress.getMax() == 0) {
//                    try {
//                        // Máximo de progreso
//                        progress.setMax(resConsulta.getInt("count_total"));
//                        // Lector de progreso
//                        new Thread(new Runnable() {
//                            public void run() {
//                                do {
//                                    progress.setProgress(eventsCnt);
//                                } while (eventsCnt <= eventsTotalCnt);
//                            }
//                        }).start();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        actualizar = false;
//                        falloActualizacion = true;
//                    }
//                }
            }

            // Para BORRAR contenido
            ultimaActualizacion = null;
            getContentResolver().delete(CalendarProvider.CONTENT_URI,null,null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (actualizar) {
                try {
                    // Elimina los eventos pasados en BD y obtiene el resto
                    LimpiarEventosPasadosBD();
                    eventosBD = ObtenerEventosBD();
                    // Actualización de eventos de la agenda
                    int count = 30;
                    int page = 1;
                    eventsCnt = 0;
                    eventsTotalCnt = 0;
                    do {
                        // Consulta a la API
                        JSONObject resConsulta = ConsultaEventosLink(count, page);
                        // Actualiza los eventos en la base de datos
                        for (int i = 0; i < resConsulta.getInt("count"); ++i) {
                            //Log.i("Evento", Integer.toString(i));
                            try {
                                JSONObject evento = resConsulta.getJSONArray("posts").getJSONObject(i);
                                if ((eventosBD.size() == 0) || (EventoActualizable(evento))) {
                                    Event ev = new Event();
                                    ev.setEventId(evento.getInt(("id")));
                                    ev.setName(LimpiarTexto(evento.getString("title_plain")));
                                    ev.setDescription(LimpiarTexto(evento.getString("content")));
                                    //InputStream input = new java.net.URL(evento.getString("thumbnail")).openStream();
                                    //ev.setImage(BitmapFactory.decodeStream(input));
                                    ev = CompletarEvento(ev, evento.getString("url"));
                                    AlmacenarEventoEnBD(ev);
                                } else {
                                    Log.i("Evento no actualizable", Integer.toString(i));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // Actualiza contadores de eventos
                        eventsCnt += resConsulta.getInt("count");
                        eventsTotalCnt = resConsulta.getInt("count_total");
                        page += 1;
                        Log.i("eventsCnt", Integer.toString(eventsCnt));
                        Log.i("eventsTotalCnt", Integer.toString(eventsTotalCnt));
                    } while (eventsCnt < eventsTotalCnt);
                } catch (JSONException e) {
                    e.printStackTrace();
                    falloActualizacion = true;
                }
            }

            return null;
        }

        private long DiferenciaFechasHoras (Date d1, Date d2) {
            // Establecer fechas
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(d1);
            cal2.setTime(d2);
            // Fechas en milisegundos
            long milis1 = cal1.getTimeInMillis();
            long milis2 = cal2.getTimeInMillis();
            // Diferencia
            long dif = milis2 - milis1;
            return (dif / (60 * 60 * 1000));
        }

        private String LimpiarTexto(String str) {
            String res;

            // Símbolos HTML
            res = Jsoup.clean(str, Whitelist.simpleText());
            // Símbolo '&nbsp;' por ' '
            res = res.replaceAll("&nbsp;"," ");

            return res;
        }

        private void AlmacenarEventoEnBD(Event ev) {
            Log.i("EventoAlmacenado:", ev.getTitle());
            // Elimina contenido anterior
            EliminarEventoBD(ev);
            // Nueva información
            try {
                ContentValues values = new ContentValues();
                values.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
                values.put(CalendarProvider.ID, ev.getEventId());
                values.put(CalendarProvider.DESCRIPTION, ev.getDescription() + SEPARATOR + ev.getLocation());
                values.put(CalendarProvider.LOCATION, ev.getStartDate("dd/MM/yyyy") + SEPARATOR + ev.getStartDate("hh:mm") + "-" + ev.getEndDate("hh:mm"));
                //Log.i("getStartDate", ev.getStartDate("dd/MM/yyyy") + SEPARATOR + ev.getStartDate("hh:mm") + "-" + ev.getEndDate("hh:mm"));
                values.put(CalendarProvider.EVENT, ev.getTitle());
                Calendar cal = Calendar.getInstance();
                TimeZone tz = TimeZone.getDefault();
                cal.setTime(SIMPLE_DATE_FORMAT.parse(ev.getStartDate("yyyy-MM-dd hh:mm:ss")));
                //Log.i("SDF_StartDate",ev.getStartDate("yyyy-MM-dd hh:mm:ss"));
                int dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
                values.put(CalendarProvider.START, cal.getTimeInMillis());
                values.put(CalendarProvider.START_DAY, dayJulian);
                cal.setTime(SIMPLE_DATE_FORMAT.parse(ev.getEndDate("yyyy-MM-dd hh:mm:ss")));
                int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
                values.put(CalendarProvider.END, cal.getTimeInMillis());
                values.put(CalendarProvider.END_DAY, endDayJulian);
                // Inserta en base de datos
                Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private boolean EliminarEventoBD (Event ev) {
            // Consulta a BD
            int cnt = getContentResolver().delete(CalendarProvider.CONTENT_URI, CalendarProvider.ID + " = ?", new String[]{Long.toString(ev.getEventId())});

            return (cnt > 0);
        }

        // Devuelve si el evento en concreto debe ser actualizado en BD
        private boolean EventoActualizable(JSONObject evento) {
            boolean res = false;
            try {
                Event e = new Event(evento.getInt("id"));
                res = (!eventosBD.contains(e));
                if ((!res) || (ultimaActualizacion != null)) {
                    res = ultimaActualizacion.before(SIMPLE_DATE_FORMAT.parse(evento.getString("modified")));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                res = true;
            }

            return res;
        }

        // Realiza la consulta a través de la API de la web, devolviendo el número de eventos indicado
        private JSONObject ConsultaEventosLink(int count, int page) {
            JSONObject res = new JSONObject();
            try {
                String urlConsulta = urlConsultaEventos + count;
                if (page >= 0) {
                    urlConsulta += ("&page=" + page);
                }
                Connection con = Jsoup.connect(urlConsulta).ignoreContentType(true).timeout(1000000);
                res = new JSONObject(con.execute().body());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res;
        }

        private Event CompletarEvento(Event event, String url) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).timeout(10000).get();
                // Using Elements to get the class data
                Elements fechas = document.select("abbr[class=tribe-events-abbr updated published dtstart]");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                String fecha = fechas.first().attr("title");
                String[] horas = fechas.get(1).html().split("-");
                String horaInicio = "00:00 pm";
                String horaFin = "01:00 pm";
                if (horas.length == 2) {
                    horaInicio = horas[0];
                    horaFin = horas[1];
                }
                Date d = sdf2.parse(fecha + " " + horaInicio);
                event.setStartDate(d);
                d = sdf2.parse(fecha + " " + horaFin);
                event.setEndDate(d);
                Elements lugar = document.select("span[class=street-address]");
                String location = "";
                if (lugar.first() != null) {
                    location = lugar.first().html();
                } else {
                    lugar = document.select("dd[class=author fn org]");
                    if (lugar.first() != null) {
                        location = lugar.first().html();
                    } else {
                        location = "Sin definir";
                    }
                }
                event.setLocation(location);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return event;
        }

        private void LimpiarEventosPasadosBD() {
            // TODO Consulta para eliminar eventos pasados
        }

        private List<Event> ObtenerEventosBD() {
            // Consulta a BD
            Log.i("EventosBD","obteniendo");
            Cursor mCursor = getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.ID}, null, null, null);
            int index = mCursor.getColumnIndex(CalendarProvider.ID);
            Log.i("CursorCnt", Integer.toString(mCursor.getCount()));
            List<Event> eventos = new ArrayList<Event>();
            for (int i = 0; i < mCursor.getCount(); ++i) {
                mCursor.moveToNext();
                Event e = new Event();
                e.setEventId(Integer.parseInt(mCursor.getString(index)));
                eventos.add(e);
            }

            return eventos;
        }

        @Override
        protected void onPostExecute(Void result) {
            // ¡¡Carga de eventos finalizada!!
            if (falloActualizacion) {
                loadText.setText("Fallo en la actualización");
            } else {
                loadText.setText("Información actualizada!");
            }
            // Fecha de última actualización
            SharedPreferences settings = getSharedPreferences(PREFS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(LAST_UPDATE, SIMPLE_DATE_FORMAT.format(new Date()));
            editor.commit();
            // Siguiente pantalla
            Intent intent = new Intent(LoadActivity.this, CalendarActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

//        //TODO delete contain in database
//        getContentResolver().delete(CalendarProvider.CONTENT_URI,null,null);
//
//        //TODO  Init download events
//
//        //TODO ADD in Database (START WHEN FINISH DOWNLOAD)
//        //
//        //(Simulate with some examples)
//        ExtendedCalendarView calendar = (ExtendedCalendarView)findViewById(R.id.calendar);
//
//        ContentValues values = new ContentValues();
//        values.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
//        values.put(CalendarProvider.DESCRIPTION, "Se dará la bienvenida a los candidatos " +
//                "seleccionados para participar en el Programa Yuzz de jóvenes emprendedores."
//                +SEPARATOR+"Link by UMA-Atech, 3ª planta");
//        values.put(CalendarProvider.LOCATION, "14/01/2016"+SEPARATOR+"16:00-19:00");
//        values.put(CalendarProvider.EVENT, "Bienvenida a participantes del Programa Yuzz");
//        Calendar cal = Calendar.getInstance();
//        TimeZone tz = TimeZone.getDefault();
//        cal.set(2016, 0, 14, 16, 00);
//        int dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values.put(CalendarProvider.START, cal.getTimeInMillis());
//        values.put(CalendarProvider.START_DAY, dayJulian);
//        cal.set(2016, 00, 14, 19, 00);
//        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values.put(CalendarProvider.END, cal.getTimeInMillis());
//        values.put(CalendarProvider.END_DAY, endDayJulian);
//
//        ContentValues values1 = new ContentValues();
//        values1.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
//        values1.put(CalendarProvider.DESCRIPTION, "Aprende a hacer aquello que quieres hacer sin que nada te frene\n" +
//                "Disfruta más de lo que haces en tu vida\n" +
//                "Inscríbete en: www.nayadepsicologos.com/inscripcion"+SEPARATOR+"Link by UMA-Atech, 3ª planta");
//        values1.put(CalendarProvider.LOCATION, "14/01/2016"+SEPARATOR+"17:00-21:00");
//        values1.put(CalendarProvider.EVENT, "Curso Iniciación en Mindfulness");
//        cal.set(2016, 0, 14, 17, 00);
//        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values1.put(CalendarProvider.START, cal.getTimeInMillis());
//        values1.put(CalendarProvider.START_DAY, dayJulian);
//        cal.set(2016, 0, 14, 21, 00);
//        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values1.put(CalendarProvider.END, cal.getTimeInMillis());
//        values1.put(CalendarProvider.END_DAY, endDayJulian);
//
//
//
//        ContentValues values2 = new ContentValues();
//        values2.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
//        values2.put(CalendarProvider.DESCRIPTION, "Clases de la asignatura “Emprendedores en Ingeniería” perteneciente al Máster de Ingeniería Industrial. La docencia se impartirà todos los viernes en la sala 201 de Link by UMA-ATech, en horario de 8.00 a 9.15 am.\n" +
//                "\n" +
//                "Coordinador del Máster, Antonio Ruiz Molina. Dpto. Economía y Administración de Empresas Universidad de Málaga."+SEPARATOR+"Link By UMA-Atech, 2ª planta\n" +
//                "Edificio The Green Ray, Avenida Louis Pasteur 47 (ampliación del Campus Teatinos) \n" +
//                "Málaga, Málaga");
//        values2.put(CalendarProvider.LOCATION, "15/01/2016"+SEPARATOR+"08:00-09:30");
//        values2.put(CalendarProvider.EVENT, "Emprendedores en la Ingeniería");
//        cal.set(2016, 0, 15, 8, 00);
//        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values2.put(CalendarProvider.START, cal.getTimeInMillis());
//        values2.put(CalendarProvider.START_DAY, dayJulian);
//        cal.set(2016, 0, 15, 9, 30);
//        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values2.put(CalendarProvider.END, cal.getTimeInMillis());
//        values2.put(CalendarProvider.END_DAY, endDayJulian);
//
//
//        ContentValues values3 = new ContentValues();
//        values3.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
//        values3.put(CalendarProvider.DESCRIPTION, "La sexta sesión abordará los siguientes temas:\n" +
//                "\n" +
//                "10.00 – 11.30 h “El empresario con formación de ingeniero”. D. Juan Castillo Rosa. Empresario. Profesor del Departamento de Ingeniería Eléctrica de la ETSII. Universidad de Málaga."+SEPARATOR+"Link By UMA-Atech, 2ª planta\n" +
//                "Edificio The Green Ray, Avenida Louis Pasteur 47 (ampliación del Campus Teatinos) \n" +
//                "Málaga, Málaga");
//        values3.put(CalendarProvider.LOCATION, "15/01/2016"+SEPARATOR+"10:00-11:30");
//        values3.put(CalendarProvider.EVENT, "III Taller de Emprendimiento en la práctica – ETS Ingeniería Industrial");
//        cal.set(2016, 0, 15, 10, 00);
//        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values3.put(CalendarProvider.START, cal.getTimeInMillis());
//        values3.put(CalendarProvider.START_DAY, dayJulian);
//        cal.set(2016, 0, 15, 11, 30);
//        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//        values3.put(CalendarProvider.END, cal.getTimeInMillis());
//        values3.put(CalendarProvider.END_DAY, endDayJulian);
//
//
//
//        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
//        getContentResolver().insert(CalendarProvider.CONTENT_URI, values1);
//        getContentResolver().insert(CalendarProvider.CONTENT_URI, values2);
//        getContentResolver().insert(CalendarProvider.CONTENT_URI, values3);