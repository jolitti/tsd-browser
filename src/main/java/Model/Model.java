package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<TSP> providers;

    //chiamo il json reader che mi rida un oggetto json che è un array di TSP. quindi vado
    //a costruire una lista di oggetti di classe TSP (passando prima per la costruzione degli oggetti
    //Service?). Lista che volendo poi posso ordinare e/o spezzare in sottoliste che rappresentano
    //il paese. Valutare anche l'implementazione tramite hashmap per le sottoliste

    public Model(String url) throws IOException, JSONException, NullPointerException {
        JSONArray tspJsonArray = APIClient.readJsonFromUrl(url);
        //foreach oggetto nell'array, estrailo e chiama il costruttore di tsp
        providers = new ArrayList<TSP>();

            for (Object service : tspJsonArray)
                this.providers.add(TSP.buildTSPFromJsonObj((JSONObject) service));
    }

    public List<TSP> getProviders(){return providers;}

}
