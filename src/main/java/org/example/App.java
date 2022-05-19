package org.example;
import Model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class App 
{
    public static void main( String[] args ) throws IOException
    {

        Model mod = new Model("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list");
    /*    for(Object tsp: mod.getProviders()){
            System.out.println(tsp.toString());
        } */

        System.out.println("\n\n Ora testo i getter del Model");

        HashMap<String, List<TSP>> NatList = mod.getNationLists();
        List<TSP> providers = mod.getProviders();
        List<TSP> BelgianProviders = mod.getProvidersByNation("BE");

/*        System.out.println("\n Getter della lista di tutti i provider");
        for(Object tsp: providers){
            System.out.println(tsp.toString());
        }

        System.out.println("\n Getter della mappa delle nazioni, estraggo la Francia" +
                "e stampo un suo provider");
        HashMap<String, List<TSP>> NationMap = mod.getNationLists();
        System.out.println(NationMap.get("FR").get(3));
*/

        System.out.println("\n Getter della lista dei provider belgi");
        for(Object tsp: BelgianProviders)
            System.out.println(tsp.toString());


    }
}
