package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//chiamo il json reader che mi rida un oggetto json che è un array di TSP. quindi vado
//a costruire una lista di oggetti di classe TSP (passando prima per la costruzione degli oggetti
//Service?). Lista che volendo poi posso ordinare e/o spezzare in sottoliste che rappresentano
//il paese. Valutare anche l'implementazione tramite hashmap per le sottoliste

//update:quando creo gli oggetti tsp li aggiungo anche ad una lista che poi verrà mappata
// nella hash table delle nazioni

public class Model {
    private List<TSP> providers;
    private HashMap<String, List<TSP>> nations; //le chiavi sono "AT", "BE", ecc... che rappresentano le nazioni


    public Model(String url)
        throws IOException, JSONException, NullPointerException
    {
        JSONArray tspJsonArray = APIClient.readJsonFromUrl(url);
        this.providers = new ArrayList<TSP>();
        this.nations = new HashMap<String, List<TSP>>();
            //foreach oggetto nell'array, estrailo e chiama il costruttore di tsp
            for (Object tsp: tspJsonArray) {
                TSP currentTSP = TSP.buildTSPFromJsonObj((JSONObject) tsp);
                this.providers.add(currentTSP);
                //popolo la tabella di hash
                if(!nations.containsKey(currentTSP.countryCode())){ //se la nazione non c'è nella mappa
                    List<TSP> listOfThisNation = new ArrayList<TSP>();//crea una lista associata a questa nazione
                    listOfThisNation.add(currentTSP);//e inserisci la prima entry nella lista
                    nations.put(currentTSP.countryCode(), listOfThisNation);//e crea il record chiave-valore
                }
                else
                    nations.get(currentTSP.countryCode()).add(currentTSP);
            }

    }
    //ADES RUA 'L BEL

    /*serve un algoritmo che permetta di filtrare il contenuto della/e lista/e in base a dei parametri
    esempio: in ingresso ricevo il parametro (Belgio, tsp3, servicetype5, whithdrwawn)-che sono tutte stringhe-
    e devo restituire una lista di TSP che matchano con i parametri
    Se permetto di selezionare solo una entità per ogni filtro il problema è presto risolto.
    Se invece permetto che ohni parametro sia un array (es. il parametro "nazione" può essere costituito anche
    da più nazioni de non solo da "Belgio" allora il problema è più grosso

    Inoltre devo creare un metodo per ogni filtro, dato che man mano che selezioniono i filtri devo aggiornare la View
    per eliminare tutti i servizi che non matchano, oppure potrei usare i metodi per creare le segnalazioni
    automatiche dal Model verso il View, cosa che però non so fare

    per ottimizzare la ricerca un'idea potrebbe essere quella di tenere altre hashmap ancora, ogniuna che ha come chiave
    un un tipo di filtro diverso.
    */

    public HashMap<String, List<TSP>> getNationLists(){return nations;}

    public List<TSP> getProviders(){return providers;}

    public List<TSP> getProvidersByNation(String countryCode){return nations.get(countryCode);}


}
