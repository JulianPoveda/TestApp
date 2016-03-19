package personalizados;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julianpoveda.testapp.R;

import java.util.ArrayList;

import Util.BitmapManager;
import modelo.EntryVO;

/**
 * Created by Julian Poveda on 17/03/2016.
 */
public class AdaptadorItemAplicacion extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<EntryVO> items;


    public AdaptadorItemAplicacion(Activity activity, ArrayList<EntryVO> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.detalle_aplicacion, null);
        }

        EntryVO itemApp = items.get(position);

        ImageView   Imagen  = (ImageView) v.findViewById(R.id.app_imagen);
        TextView    Nombre  = (TextView) v.findViewById(R.id.app_nombre);
        TextView    Artista = (TextView) v.findViewById(R.id.app_artista);
        TextView    Precio  = (TextView) v.findViewById(R.id.app_precio);
        TextView    Derechos= (TextView) v.findViewById(R.id.app_derechos);


        Imagen.setImageBitmap(BitmapManager.decodeBitmapFromBase64(itemApp.getImagenB64()));
        Nombre.setText(itemApp.getNombre());
        Artista.setText(itemApp.getArtista());
        Precio.setText(itemApp.getPrecio());
        Derechos.setText(itemApp.getDerechos());
        return v;
    }
}
