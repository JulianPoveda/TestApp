package Util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Julian Poveda on 17/03/2016.
 */
public class Parametros {
    public static String 	NOMBRE_DATABASE	= "TopApp.db";
    public static String 	CARPETA_RAIZ 	= Environment.getExternalStorageDirectory()+ File.separator+"Grability";		//Ruta donde se encuentra la carpeta principal del programa

}
