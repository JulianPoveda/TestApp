package com.example.julianpoveda.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ConexionHttp.ConexionData;
import ConexionHttp.RequestHttp;
import Util.BitmapManager;
import modelo.EntryVO;
import modelo.FeedVO;
import persistencia.EntryDAO;
import persistencia.FeedDAO;

public class SplashActivity extends Activity {
    private static final long SPLASH_DELAY = 2000;
    private static final String URL_RSS = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

    private DisplayMetrics      displayMetrics;
    private RequestAsyncTask    requestAsyncTask;

    private EntryDAO            entryDAO;
    private FeedDAO             feedDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.feedDAO        = new FeedDAO(SplashActivity.this);
        this.entryDAO       = new EntryDAO(SplashActivity.this);
    }


    @Override
    public void onStart(){
        super.onStart();

        if(ConexionData.verificarConexion(this)){
            requestAsyncTask = new RequestAsyncTask();
            requestAsyncTask.execute(URL_RSS);

        }else {
            if(this.feedDAO.existFeed()){
                FeedVO feedVO = this.feedDAO.getFeed();

                new AlertDialog.Builder(this)
                        .setTitle("TOP APP OFFLINE")
                        .setMessage("Tu movil no tiene acceso a internet!\n" +
                                "Ultima Actualizacion: "+feedVO.getFechaActualizacion()+"\n" +"Deseas Continuar ?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                Intent topAppsIntent = new Intent(SplashActivity.this, drawerListAplicaciones.class);
                                startActivity(topAppsIntent);
                                overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);
                                SplashActivity.this.finish();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                SplashActivity.this.finish();
                                overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);
                            }
                        })
                        .show();
            }else{
                //Without connection or entries stored in DB, then... Bye bye!*/
                new AlertDialog.Builder(this)
                        .setTitle("TOP APP")
                        .setMessage("No hay conexion a internet, para ver el ranking de las 20 aplicaciones mas descargadas es necesario tener disponible una conexion a internet, habilitela e intente de nuevo!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                SplashActivity.this.finish();
                                overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);

                            }
                        })
                        .show();
            }

        }

    }

    /* Tarea asincrona encargada de realizar la conexion a la URL RSS, capturar el JSON y extraer la informacion relevante */
    class RequestAsyncTask extends AsyncTask<String, Integer, Boolean> {
        private RequestHttp         httpConnection;
        private FeedVO              feedVO;
        private ArrayList<EntryVO>  listEntryVO;
        private Timer               timerSplash;

        @Override
        protected void onPreExecute() {
            this.httpConnection = new RequestHttp();
            this.listEntryVO    = new ArrayList<>();
            this.timerSplash    = new Timer();


            /*splashTextView.setVisibility(View.VISIBLE);
            splashProgressBar.setVisibility(View.VISIBLE);
            splashProgressBar.setProgress(0);*/

        }

        @Override
        protected Boolean doInBackground(String... urlRequest) {
            boolean processResponse = false;
            JSONObject jsonResponse;
            JSONArray  arrayEntryJSON;
            //int progressBarValue = 0;
            //double progressRate = 0;

            try {

                jsonResponse = httpConnection.getJsonObjectFromUrl(urlRequest[0]);

                this.feedVO = new FeedVO(jsonResponse.getJSONObject("feed").getJSONObject("author").getJSONObject("name").getString("label"),
                        jsonResponse.getJSONObject("feed").getJSONObject("updated").getString("label"),
                        jsonResponse.getJSONObject("feed").getJSONObject("rights").getString("label"),
                        jsonResponse.getJSONObject("feed").getJSONObject("title").getString("label"));

                arrayEntryJSON = new JSONArray(jsonResponse.getJSONObject("feed").getJSONArray("entry").toString());

                for(int i=0; i< arrayEntryJSON.length(); i++){
                    EntryVO entryVO = new EntryVO();

                    switch(displayMetrics.densityDpi){
                        case DisplayMetrics.DENSITY_MEDIUM:
                            entryVO.setLinkImagen(arrayEntryJSON.getJSONObject(i).getJSONArray("im:image").getJSONObject(0).getString("label"));
                            break;

                        case DisplayMetrics.DENSITY_HIGH:
                            entryVO.setLinkImagen(arrayEntryJSON.getJSONObject(i).getJSONArray("im:image").getJSONObject(1).getString("label"));
                            break;

                        case DisplayMetrics.DENSITY_XHIGH:
                        case DisplayMetrics.DENSITY_XXHIGH:
                        case DisplayMetrics.DENSITY_XXXHIGH:
                            entryVO.setLinkImagen(arrayEntryJSON.getJSONObject(i).getJSONArray("im:image").getJSONObject(2).getString("label"));
                            break;

                        default:
                            entryVO.setLinkImagen(arrayEntryJSON.getJSONObject(i).getJSONArray("im:image").getJSONObject(0).getString("label"));
                            break;
                    };

                    if(Double.parseDouble(arrayEntryJSON.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("amount")) == 0){
                        entryVO.setPrecio("Gratis");
                    }else{
                        entryVO.setPrecio(arrayEntryJSON.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("amount") +
                                " " + arrayEntryJSON.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("currency"));
                    }


                    entryVO.setIdAplicacion(arrayEntryJSON.getJSONObject(i).getJSONObject("id").getJSONObject("attributes").getString("im:id"));
                    entryVO.setNombre(arrayEntryJSON.getJSONObject(i).getJSONObject("im:name").getString("label"));
                    entryVO.setArtista(arrayEntryJSON.getJSONObject(i).getJSONObject("im:artist").getString("label"));
                    entryVO.setCategoria(arrayEntryJSON.getJSONObject(i).getJSONObject("category").getJSONObject("attributes").getString("label"));
                    entryVO.setDerechos(arrayEntryJSON.getJSONObject(i).getJSONObject("rights").getString("label"));
                    entryVO.setLinkDescarga(arrayEntryJSON.getJSONObject(i).getJSONObject("link").getJSONObject("attributes").getString("href"));
                    entryVO.setResumen(arrayEntryJSON.getJSONObject(i).getJSONObject("summary").getString("label"));
                    entryVO.setImagenB64(BitmapManager.encodeBitmapToBase64(httpConnection.downloadImage(entryVO.getLinkImagen()),100));
                    this.listEntryVO.add(entryVO);
                }

                //guardar en la bd
                feedDAO.setFeed(this.feedVO);
                entryDAO.setEntrys(this.listEntryVO);




                //Log.d("Mensaje",jsonResponse.toString());

                /*//Log.i(TAG+">>doBack", "iTunes RSS Response: " + iTunesRssJsonResponse);

                iTunesRssTranslator = new ITunesRssTranslator();

                iTunesFeedProperties = iTunesRssTranslator.getRssFeedProperties(iTunesRssJsonResponse);
                //Log.i(TAG+">>doBack", "iTunes RSS Feed Properties: " + iTunesFeedProperties.toString());

                if(!iTunesFeedProperties.getLastUpdate().equalsIgnoreCase(mngrPreferences.getRssUpdate())){

                    mngrPreferences.setRssAuthor(iTunesFeedProperties.getAuthor());
                    mngrPreferences.setRssRights(iTunesFeedProperties.getRights());
                    mngrPreferences.setRssUpdate(iTunesFeedProperties.getLastUpdate());

                    iTunesTopApplications = iTunesRssTranslator.getTopAppsFromRss(iTunesRssJsonResponse, displayMetrics.densityDpi);
                    //Log.i(TAG+">>doBack", "iTunes RSS Entry[0]: " + iTunesTopApplications.get(0).toString());

                    //The progress showed in the progress bar must be dynamic,
                    // depending of the amount of entries contained in the RSS Feed
                    progressRate = 100/iTunesTopApplications.size();

                    synchronized (dbITunesRss) {

                        dbITunesRss.open();
                        dbITunesRss.deleteAll();

                        for(ITunesEntry iTunesEntry : iTunesTopApplications){


                            //The image downloaded is saved in the DB like a encoded String Base64
                            Bitmap entryBitmapImage = httpConnection.downloadImage(iTunesEntry.getImageLink());
                            iTunesEntry.setImageB64(ImageProcessing.encodeBitmapToBase64(entryBitmapImage, 100));

                            //Log.i(TAG+">>doBack", "iTunes RSS Entry: " + iTunesEntry.toString());

                            long countTemp = dbITunesRss.insertEntry(iTunesEntry);
                            //Log.i(TAG+">>doBack", "iTunes row insert: " + countTemp);

                            progressBarValue += (int) progressRate;
                            publishProgress(progressBarValue);

                        }

                    }

                    if (dbITunesRss != null) {
                        dbITunesRss.close();
                    }

                    processResponse = true;

                }else{

                    processResponse = false;

                }*/
            } catch (Exception e) {
                //Log.e(TAG+">>doBack", ">>> Error procesando respuesta JSON");
                e.printStackTrace();
                processResponse = false;
            }

            return processResponse;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            //Updating progress bar...
            //splashProgressBar.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Boolean result) {

            /*int time = 1000;

            if(!result) {

                splashTextView.setVisibility(View.INVISIBLE);
                splashProgressBar.setVisibility(View.INVISIBLE);
                time = 2000;
            }*/

            this.timerSplash.schedule(new TimerTask() {
                public void run() {
                    //Intent topAppsIntent = new Intent(SplashActivity.this, ListAplicacionesActivity.class);
                    Intent topAppsIntent = new Intent(SplashActivity.this, drawerListAplicaciones.class);
                    startActivity(topAppsIntent);
                    overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);

                    try {
                        SplashActivity.this.finish();
                        overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);
                    } catch (Throwable e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, SPLASH_DELAY);
        }
    }
}
