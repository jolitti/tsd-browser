package lib;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ServiceDatabase
/**
 * Class that holds a record of all the services and can be queried via ServiceFilters
 */
{
    private List<Service> services;

    private HashSet<String> occurringCountries;
    private HashSet<Integer> occurringProviders;
    private HashSet<String> occurringServiceTypes;
    private HashSet<String> occurringStatuses;

    /**
     * Initialize "database"
     */
    public ServiceDatabase(List<Service> aServices) {
        services = aServices;

        for (Service s : services) {
            occurringCountries.add(s.countryCode());
            occurringProviders.add(s.tspId());
            for (String type : s.qServiceTypes()) {
                occurringServiceTypes.add(type);
            }
            occurringStatuses.add(s.currentStatus());
        }
    }

    public List<Service> getServices(ServiceFilter filter)
    /**
     * Prototype of the query function.
     * Very unoptimized, will only iterate
     */
    {
        ArrayList<Service> answer = new ArrayList<>();

        for (Service s : services) {
            // TODO: check if s respects the 4 parameters
        }

        return answer;
    }
}
