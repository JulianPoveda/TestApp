package persistencia;

import android.content.ContentValues;
import android.content.Context;

import modelo.FeedVO;

/**
 * Created by Julian Poveda on 16/03/2016.
 */
public class FeedDAO {
    private Context ctx;
    private SQLite  FcnSQL;



    public FeedDAO(Context _ctx){
        this.ctx = _ctx;
        this.FcnSQL = new SQLite(this.ctx);
    }


    public boolean existFeed(){
        return this.FcnSQL.ExistRegistros("feed","titulo IS NOT NULL");
    }


    public FeedVO getFeed(){
        FeedVO feedVO;
        ContentValues registro = new ContentValues();

        registro = this.FcnSQL.SelectDataRegistro("feed","autor,fecha_actualizacion,derechos,titulo","titulo IS NOT NULL");
        feedVO = new FeedVO(registro.getAsString("autor"), registro.getAsString("fecha_actualizacion"), registro.getAsString("derechos"),
                registro.getAsString("titulo"));

        return feedVO;
    }


    public boolean setFeed(FeedVO _feed){
        this.FcnSQL.DeleteTabla("feed");

        ContentValues registro = new ContentValues();
        registro.clear();

        registro.put("autor", _feed.getAutor());
        registro.put("fecha_actualizacion", _feed.getFechaActualizacion());
        registro.put("derechos", _feed.getDerechos());
        registro.put("titulo", _feed.getTitulo());

        return this.FcnSQL.InsertRegistro("feed",registro);
    }

}
