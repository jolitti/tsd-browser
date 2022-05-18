package org.example;
import Model.*;
import org.json.*;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World! 2" );

        JSONArray a = new JSONArray("[0,4,5,1]");
        System.out.println(a);

        //JSONObject o = JSONReader.readJsonFromUrl("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list");
    /*    JSONArray o = APIClient.readJsonFromUrl(
                "https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/countries_list"
        );
        System.out.println(o); */

        Model mod = new Model("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list");
        for(Object tsp: mod.getProviders()){
            System.out.println(tsp.toString());
        }
    }
}
