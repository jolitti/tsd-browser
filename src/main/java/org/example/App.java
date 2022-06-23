package org.example;
import lib.ModelSpawner;
import lib.interfaces.ModelInterface;
import org.json.*;
import lib.Service;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        /*System.out.println( "Hello World! 2" );

        JSONArray a = new JSONArray("[0,4,5,1]");
        System.out.println(a);*/

        ModelInterface model = ModelSpawner.getModelInstance();


        //JSONObject o = JSONReader.readJsonFromUrl("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list");
        /* JSONObject o = JSONReader.readJsonFromUrl(
                "https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/countries_list"
        ); */
        //Service s = new Service();
        //System.out.println(s);
    }
}
