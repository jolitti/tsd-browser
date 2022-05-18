package Model;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public record Service(
        String countryCode,
        String currentStatus,
        String[] qServiceTypes,
        int serviceID,
        String serviceName,
        String tob,
        int tspID,
        String type
/**        int tspId,
        int serviceId,
        String countryCode,
        String serviceName,
        String type,
        String currentStatus,
        String tob,
        String[] qServiceTypes */
) {

    public static Service buildServiceFromJsonObj(JSONObject o) throws JSONException, ClassCastException{
        JSONArray serviceTypes = o.getJSONArray("qServiceTypes");
        List<String> l = new ArrayList<>();
        for (int i = 0; i<serviceTypes.length(); i++) {
            l.add((String) serviceTypes.get(i));
        }
        String tb;
        try {
            tb = o.getString("tob");
        }
        catch (JSONException je){
            tb = null;
        }

        return new Service(
                o.getString("countryCode"),
                o.getString("currentStatus"),
                l.toArray(new String[0]),
                o.getInt("serviceId"),
                o.getString("serviceName"),
                tb, //o.getString("tob"),
                o.getInt("tspId"),
                o.getString("type")

               /** o.getInt("tspId"),
                o.getInt("serviceId"),
                o.getString("countryCode"),
                o.getString("serviceName"),
                o.getString("type"),
                o.getString("currentStatus"),
                o.getString("tob"),
                (String[]) l.toArray() */
        );
    }
}
