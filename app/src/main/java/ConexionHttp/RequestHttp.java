package ConexionHttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Julian Poveda on 16/03/2016.
 */



public class RequestHttp {
    private URL                 url;
    private HttpURLConnection   conn;
    private JSONObject          jsonObj;

    public RequestHttp() {

    }

    public Bitmap downloadImage(String imageHttpAddress) throws IOException {
        URL imageUrl = null;
        imageUrl = new URL(imageHttpAddress);
        HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        return BitmapFactory.decodeStream(conn.getInputStream());
    }

    public JSONObject getJsonObjectFromUrl(String _url) throws JSONException, IOException{
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(_url);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            //int response = conn.getResponseCode();
            //Log.d("Internet Teest", "The response is: " + response);
            is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            rd.close();

            /*String contentAsString = readIt(is, len);*/
            return new JSONObject(response.toString());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
