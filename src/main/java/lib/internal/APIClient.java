package lib.internal;

import lib.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class APIClient {

    /**
     * Builds a DataHolder record with all data needed
     * @param url URL of the API
     * @return a LDataHolder record containing a List of Services, a Map CountryCode-CountryName and a Map tspID-tspName
     */
    public static DataHolder getData(String url) throws IOException, JSONException {
        JSONArray tspJsonArray = readJsonFromUrl(url);
        List<Service> serviceList = new ArrayList<Service>();
        Map<Integer,String> tspMap = new TreeMap<Integer,String>();
        //SortedMap<String,String> countryMap = new TreeMap<String,String>();

        for(int i=0; i<tspJsonArray.length(); i++){
            JSONObject tsp = (JSONObject) tspJsonArray.get(i);
            tspMap.put((Integer) tsp.get("tspId"),(String) tsp.get("name"));     //scorro i tsp e aggiungo la entry nella mappa degli id-nomi

            JSONArray serviceArray = (JSONArray) tsp.get("services");
            for(Object serviceObject: serviceArray) {                            //per ogni oggetto tsp scorro la lista dei suoi servizi e creo i Service
                Service service = buildServiceFromJSONObject((JSONObject) serviceObject);
                serviceList.add(service);                                        //e li aggiungo alla lista
            }
        }

        return new DataHolder(serviceList, tspMap);
    }



    /**
     *
     * @param url url to the api
     * @return a map containing countryCode-CountryName pairs
     */
    public static Map<String,String> getCountryMap(String url) throws IOException, JSONException {
        JSONArray countryArray = readJsonFromUrl(url);
        Map<String,String> countryMap = new TreeMap<String,String>();

        for(int i=0; i<countryArray.length(); i++){
            JSONObject country = (JSONObject) countryArray.get(i);
            countryMap.put((String) country.get("countryCode"), (String) country.get("countryName"));
        }
        return countryMap;
    }








    /**
     * Queries the API and gets the JSON array from url
     * @param url url of the api
     * @return a JSONArray containing all TSPs.
     * @throws IOException
     * @throws JSONException
     */
    private static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            JSONTokener tokener = new JSONTokener(is);
            return new JSONArray(tokener);
        }
    }



    /**
     * Builds a Service Java object from a JSONObject Java object (that represents an object, in sense of JSON standard)
     * @param o JSONObject Java object
     * @return a Service Java object
     * @throws JSONException if json is not correctly formatted
     * @throws ClassCastException
     */
    private static Service buildServiceFromJSONObject(JSONObject o) throws JSONException, ClassCastException{
        JSONArray serviceTypes = o.getJSONArray("qServiceTypes");
        String[] qServiceTypes = new String[serviceTypes.length()];
        for (int i = 0; i<serviceTypes.length(); i++)
            qServiceTypes[i] = (String) serviceTypes.get(i);

        //bug nella libreria json che non permette di leggere valori null
        String tb;
        try { tb = o.getString("tob)"); }
        catch(JSONException je){ tb = "/NULL"; }


        return new Service(
                        o.getInt("tspId"),
                        o.getInt("serviceId"),
                        o.getString("countryCode"),
                        o.getString("serviceName"),
                        o.getString("type"),
                        o.getString("currentStatus"),
                        tb,
                        qServiceTypes);
    }

}
