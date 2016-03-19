package com.example.julianpoveda.testapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import persistencia.EntryDAO;

public class drawerListAplicaciones extends Activity implements OnItemClickListener, DrawerListener {
    private EntryDAO entryDAO;
    private DrawerLayout            drawerLayout;
    private ListView                drawerList;
    private ActionBarDrawerToggle   toogleButton;
    private ArrayList<String>       opcionesCategoria;

    private FragmentManager fragmentManager = getFragmentManager();
    private Fragment        fragment_list_app;

    public static String categoria = "All";

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

        setContentView(R.layout.activity_drawer_list_aplicaciones);

        this.entryDAO = new EntryDAO(this);
        this.opcionesCategoria = this.entryDAO.getListCategorias();

        this.fragment_list_app  = new FragmentListApp();
        this.drawerLayout       = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerList         = (ListView) findViewById(R.id.left_drawer);


        this.toogleButton =  new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_opciones_menu,
                R.string.btn_save,
                R.string.app_name
        );


        this.drawerList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, this.opcionesCategoria));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.drawerList.setOnItemClickListener(this);
        this.drawerLayout.setDrawerListener(toogleButton);


        fragmentManager.beginTransaction().replace(R.id.content_frame, this.fragment_list_app).commit();
        drawerList.setItemChecked(0, true);
        setTitle("TOP APP " + this.opcionesCategoria.get(0).toUpperCase());

    }


    private void selectItem(int position) {
        categoria = this.opcionesCategoria.get(position);

        fragmentManager.beginTransaction().detach(this.fragment_list_app).attach(this.fragment_list_app).commit();

        drawerList.setItemChecked(position, true);
        setTitle("TOP APP "+this.opcionesCategoria.get(position).toUpperCase());
        drawerLayout.closeDrawer(drawerList);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(drawerList)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(drawerList);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sincronizar el estado del drawer
        toogleButton.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Cambiar las configuraciones del drawer si hubo modificaciones
        toogleButton.onConfigurationChanged(newConfig);
    }

}
