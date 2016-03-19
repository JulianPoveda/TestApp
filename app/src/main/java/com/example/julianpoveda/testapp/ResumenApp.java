package com.example.julianpoveda.testapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Util.BitmapManager;
import modelo.EntryVO;
import persistencia.EntryDAO;

public class ResumenApp extends Activity {
    private String      idAplicacion;
    private EntryDAO    entryDAO;
    private EntryVO     entryVO;

    private ImageView   Imagen;
    private TextView    Nombre,Artista,Precio,Derechos,Resumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_resumen_app);

        Bundle bundle = getIntent().getExtras();
        this.idAplicacion 	= bundle.getString("idAplicacion");

        this.entryDAO = new EntryDAO(this);
        this.entryVO = this.entryDAO.getAppById(this.idAplicacion);


        this.Imagen  = (ImageView) findViewById(R.id.detalle_app_imagen);
        this.Nombre  = (TextView) findViewById(R.id.detalle_app_nombre);
        this.Artista = (TextView) findViewById(R.id.detalle_app_artista);
        this.Precio  = (TextView) findViewById(R.id.detalle_app_precio);
        this.Derechos= (TextView) findViewById(R.id.detalle_app_derechos);
        this.Resumen = (TextView) findViewById(R.id.detalle_app_resumen);

        this.Imagen.setImageBitmap(BitmapManager.decodeBitmapFromBase64(entryVO.getImagenB64()));
        this.Nombre.setText(entryVO.getNombre());
        this.Artista.setText(entryVO.getArtista());
        this.Precio.setText(entryVO.getPrecio());
        this.Derechos.setText(entryVO.getDerechos());
        this.Resumen.setText(entryVO.getResumen());
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //It's necessary to capture the KEYCODE_BACK event to animate the screen transition
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            ResumenApp.this.finish();
            overridePendingTransition(R.animator.right_in, R.animator.right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
