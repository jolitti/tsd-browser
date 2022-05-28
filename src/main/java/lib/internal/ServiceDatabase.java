package lib.internal;

import lib.Service;
import lib.ServiceFilter;
import lib.interfaces.ModelInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Class that holds a record of all the services and can be queried via ServiceFilters
 */
public class ServiceDatabase implements ModelInterface
{
    private List<Service> services;
    private Map<String,String> countryCodeMap;
    private Map<Integer,String> providerIdMap;

    private HashSet<String> occurringCountries;
    private HashSet<Integer> occurringProviders;
    private HashSet<String> occurringServiceTypes;
    private HashSet<String> occurringStatuses;

    /**
     * Initialize "database"
     */
    public ServiceDatabase(List<Service> aServices, Map<String,String> aCountryCodeMap, Map<Integer,String> aProviderIdMap) {
        services = aServices;
        countryCodeMap = aCountryCodeMap;
        providerIdMap = aProviderIdMap;

        for (Service s : services) {
            occurringCountries.add(s.countryCode());
            occurringProviders.add(s.tspId());
            for (String type : s.qServiceTypes()) {
                occurringServiceTypes.add(type);
            }
            occurringStatuses.add(s.currentStatus());
        }
    }

    @Override
    public Map<String, String> getCountryCodeToNames() {
        return countryCodeMap; // TODO
    }

    @Override
    public Map<Integer, String> getCodeToProviderNames() {
        return providerIdMap; // TODO
    }

    @Override
    public ServiceFilter getComplementaryFilter(ServiceFilter partial) {
        return null; // TODO
    }

    @Override
    public List<Service> getServices(ServiceFilter filter) {
        ArrayList<Service> answer = new ArrayList<>();

        for (Service s: services) {
            if (filter.matches(s)) answer.add(s);
        }
        return answer;
    }
}
