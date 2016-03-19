package com.example.julianpoveda.testapp;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

import modelo.EntryVO;
import persistencia.EntryDAO;
import personalizados.AdaptadorItemAplicacion;



public class FragmentListApp extends Fragment implements OnItemClickListener{
    private EntryDAO entryDAO;

    private AdaptadorItemAplicacion AdapterAplicaciones;
    private ArrayList<EntryVO>      ArrayAplicaciones = new ArrayList<>();
    private ListView                ListaAplicaciones;


    public FragmentListApp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_list_app, container, false);
        this.entryDAO = new EntryDAO(this.getActivity());


        switch (drawerListAplicaciones.categoria){
            case "All":
                this.ArrayAplicaciones = this.entryDAO.getListAppByCategoria(null);
                break;

            default:
                this.ArrayAplicaciones = this.entryDAO.getListAppByCategoria(drawerListAplicaciones.categoria);
                break;
        }

        this.ListaAplicaciones = (ListView) vista.findViewById(R.id.ListaAplicaciones);
        this.AdapterAplicaciones = new AdaptadorItemAplicacion(this.getActivity(),this.ArrayAplicaciones);
        this.ListaAplicaciones.setAdapter(this.AdapterAplicaciones);

        this.ListaAplicaciones.setOnItemClickListener(this);

        return vista;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.ListaAplicaciones:
                Intent resumenAppIntent = new Intent(this.getActivity(), ResumenApp.class);
                resumenAppIntent.putExtra("idAplicacion",this.ArrayAplicaciones.get(position).getIdAplicacion());
                startActivity(resumenAppIntent);
                this.getActivity().overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);
                break;
        }
    }
}
