package lib;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public record Service(
        int tspId,
        int serviceId,
        String countryCode,
        String serviceName,
        String type,
        String currentStatus,
        String tob,
        String[] qServiceTypes
) {



/*    public boolean matches(ServiceFilter filter) {
        // TODO
        //spostata nella classe service Database per diminuire il coupling: serivice esiste infdipendentemente dai filtri
        return false;
    } */
}
