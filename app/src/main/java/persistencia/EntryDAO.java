package persistencia;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import modelo.EntryVO;

/**
 * Created by Julian Poveda on 16/03/2016.
 */
public class EntryDAO {
    private Context ctx;
    private SQLite  FcnSQL;



    public EntryDAO(Context _ctx){
        this.ctx = _ctx;
        this.FcnSQL = new SQLite(this.ctx);
    }



    public void setEntrys(ArrayList<EntryVO> _entrys){
        this.FcnSQL.DeleteTabla("entry");

        for(EntryVO entryVO : _entrys){
            ContentValues registro = new ContentValues();
            registro.clear();

            registro.put("id_aplicacion", entryVO.getIdAplicacion());
            registro.put("nombre", entryVO.getNombre());
            registro.put("imagen", entryVO.getImagenB64());
            registro.put("resumen", entryVO.getResumen());
            registro.put("precio", entryVO.getPrecio());
            registro.put("derechos", entryVO.getDerechos());
            registro.put("artista", entryVO.getArtista());
            registro.put("link_descarga", entryVO.getLinkDescarga());
            registro.put("categoria", entryVO.getCategoria());

            this.FcnSQL.InsertRegistro("entry",registro);
        }
    }

    public ArrayList<String> getListCategorias(){
        ArrayList<ContentValues> tabla = new ArrayList<>();
        ArrayList<String> listCategorias = new ArrayList<>();
        listCategorias.add("All");

        tabla = this.FcnSQL.SelectData("entry","categoria","categoria IS NOT NULL", "categoria");

        for(ContentValues registro : tabla){
            listCategorias.add(registro.getAsString("categoria"));
        }
        return listCategorias;
    }


    public EntryVO getAppById(String _idAplicacion){
        EntryVO entryVO = new EntryVO();
        ContentValues registro = new ContentValues();

        registro = this.FcnSQL.SelectDataRegistro("entry",
                "id_aplicacion,imagen,nombre,resumen,precio,derechos,artista,link_descarga",
                "id_aplicacion ='"+_idAplicacion+"'");

        entryVO.setIdAplicacion(registro.getAsString("id_aplicacion"));
        entryVO.setNombre(registro.getAsString("nombre"));
        entryVO.setResumen(registro.getAsString("resumen"));
        entryVO.setPrecio(registro.getAsString("precio"));
        entryVO.setArtista(registro.getAsString("artista"));
        entryVO.setDerechos(registro.getAsString("derechos"));
        entryVO.setImagenB64(registro.getAsString("imagen"));
        entryVO.setLinkDescarga(registro.getAsString("link_descarga"));

        return entryVO;
    }


    public ArrayList<EntryVO> getListAppByCategoria(String _categoria){
        ArrayList<EntryVO> listaEntry = new ArrayList<>();
        ArrayList<ContentValues> tabla = new ArrayList<>();


        if(_categoria == null){

            tabla = this.FcnSQL.SelectData("entry",
                    "id_aplicacion,imagen,nombre,resumen,precio,derechos,artista,link_descarga",
                    "categoria IS NOT NULL",
                    "nombre");

        }else{

            tabla = this.FcnSQL.SelectData("entry",
                    "id_aplicacion,imagen,nombre,resumen,precio,derechos,artista,link_descarga",
                    "categoria = '" + _categoria + "'",
                    "nombre");

        }

        for(ContentValues registro : tabla){
            EntryVO entryVO = new EntryVO();
            entryVO.setIdAplicacion(registro.getAsString("id_aplicacion"));
            entryVO.setNombre(registro.getAsString("nombre"));
            entryVO.setResumen(registro.getAsString("resumen"));
            entryVO.setPrecio(registro.getAsString("precio"));
            entryVO.setArtista(registro.getAsString("artista"));
            entryVO.setDerechos(registro.getAsString("derechos"));
            entryVO.setImagenB64(registro.getAsString("imagen"));
            entryVO.setLinkDescarga(registro.getAsString("link_descarga"));
            listaEntry.add(entryVO);
        }
        return listaEntry;
    }
}
