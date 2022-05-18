package Model; //in realtà non farebbe parte del modello ma di un pacchetto di supporto

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;

public class APIClient {

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            JSONTokener tokener = new JSONTokener(is);
            return new JSONArray(tokener);//ritorna array json che è costituito da tutto il file
                                              //trasmesso dall'url
                                              //nel caso specifico è un array di TSP
        }
    }
}
