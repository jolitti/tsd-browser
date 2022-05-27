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

public class APIClient {

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            JSONTokener tokener = new JSONTokener(is);
            return new JSONArray(tokener);
        }
    }

    private static Service buildServiceFromJSONObject(JSONObject o) throws JSONException, ClassCastException{
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
        String tob = o.isNull("tob")? "" : o.getString("tob");
        String[] qServiceTypes = l.toArray(l.toArray(new String[l.size()]));

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
