package lib;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JSONReader {

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{'obj':");
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        sb.append('}');
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }
}
