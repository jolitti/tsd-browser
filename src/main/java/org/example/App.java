package org.example;
import org.json.*;
import lib.Service;
import lib.JSONReader;

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
        JSONObject o = JSONReader.readJsonFromUrl(
                "https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/countries_list"
        );
        System.out.println(o);
    }
}
