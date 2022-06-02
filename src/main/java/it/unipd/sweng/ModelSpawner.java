package lib;

import lib.interfaces.ModelInterface;
import lib.internal.APIClient;
import lib.internal.DataHolder;
import lib.internal.ServiceDatabase;

import java.io.IOException;
import java.util.Map;

/**
 * This is the class that will be interrogated by the gui to obtain the ServiceDatabase
 * (masked by ModelInterface)
 */
public class ModelSpawner {

    /**
     * The URL that asks the API for a list of Objects that map each
     * country code to the real country name
     */
    private static final String COUNTRIES_API_URL =
            "https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/countries_list";

    /**
     * The most important piece of the API, returns an array of provider objects.
     * The APIClient will extract the list of services and the provider id to name Map
     */
    private static final String SERVICES_API_URL =
            "https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list";

    /**
     * ServiceDatabase singleton
     */
    private static ServiceDatabase db;

    /**
     * Return the singleton of a class that implements ModelInterface
     *
     * Might throw an IOException if either the API is offline or
     * the user is having connection issues
     */
    public static ModelInterface getModelInstance() throws IOException
    {
        // Calls APIClient, gets the two maps and the service list
        // then generates a ServiceDatabase and returns it
        // (or just returns it if it's already present)

        // If the singleton is already initialized, return it
        if (db != null) return db;

        // Retrieve data
        DataHolder holder = APIClient.getData(SERVICES_API_URL);
        Map<String,String> countryCodeMap = APIClient.getCountryMap(COUNTRIES_API_URL);

        // Assign the singleton
        db = new ServiceDatabase(holder.services(),countryCodeMap,holder.tspIDtoNameMap());

        return db;
    }

    /**
     * The constructor is hidden to make the class static
     */
    private ModelSpawner() {}
}
