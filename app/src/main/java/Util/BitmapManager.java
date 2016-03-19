package Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Julian Poveda on 17/03/2016.
 */

public class BitmapManager {

    public static String encodeBitmapToBase64(Bitmap _bitmap, int _compress) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.PNG, _compress, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static Bitmap decodeBitmapFromBase64(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}