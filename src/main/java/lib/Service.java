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

    public Service buildServiceFromJSONObject(JSONObject o) throws JSONException, ClassCastException{
        JSONArray serviceTypes = o.getJSONArray("qServiceTypes");
        List<String> l = new ArrayList<>();
        for (int i = 0; i<serviceTypes.length(); i++) {
            l.add((String) serviceTypes.get(i));
        }

        int tspId = o.getInt("tspId");
        int serviceId = o.getInt("serviceId");
        String countryCode = o.getString("countryCode");
        String serviceName = o.getString("serviceName");
        String type = o.getString("type");
        String currentStatus = o.getString("currentStatus");
        String tob = o.getString("tob");
        String[] qServiceTypes = (String[]) l.toArray();

        return new Service(
                tspId,
                serviceId,
                countryCode,
                serviceName,
                type,
                currentStatus,
                tob,
                qServiceTypes
                );
    }
}
