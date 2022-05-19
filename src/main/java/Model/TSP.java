package Model;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record TSP(
        String countryCode,
        String name,
        String[] qServiceTypes,
        Service[] services,
        String trustmark,
        int tspID
){

    public static TSP buildTSPFromJsonObj(JSONObject o)
            throws JSONException, ClassCastException
    {
        JSONArray servicesArray = o.getJSONArray("services");
        List<Service> services = new ArrayList<>();
        for(Object service: servicesArray)
            services.add(Service.buildServiceFromJsonObj((JSONObject) service));

        JSONArray qServiceTypesArray = o.getJSONArray("qServiceTypes");
        List<String> types = new ArrayList<>();
        for(Object type: qServiceTypesArray)
            types.add((String) type); //non troppo sicuro, sarebbe meglio fare una lista di jsonObjects
                                      //e prima di castare in stringa controllare che effettivamente sia una stringa
                                      //e non altri tipi di jsonObjects
        return new TSP(
                o.getString("countryCode"),
                o.getString("name"),
                types.toArray(new String[0]),
                services.toArray(new Service[0]),
                o.getString("trustmark"),
                o.getInt("tspId")
        );
    }

    public String toString()
    {
        return new String(
                this.countryCode +
                "\n" +
                this.name +
                "\n" +
                Arrays.toString(this.qServiceTypes) +
                "\n" +
                Arrays.toString(this.services) +
                "\n" +
                this.trustmark +
                "\n" +
                this.tspID);
    }
}
