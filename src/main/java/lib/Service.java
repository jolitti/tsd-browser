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

    public static Service buildServiceFromJsonObj(JSONObject o) throws JSONException, ClassCastException{
        JSONArray serviceTypes = o.getJSONArray("qServiceTypes");
        List<String> l = new ArrayList<>();
        for (int i = 0; i<serviceTypes.length(); i++) {
            l.add((String) serviceTypes.get(i));
        }

        return new Service(
                o.getInt("tspId"),
                o.getInt("serviceId"),
                o.getString("countryCode"),
                o.getString("serviceName"),
                o.getString("type"),
                o.getString("currentStatus"),
                o.getString("tob"),
                (String[]) l.toArray()
        );
    }
}
