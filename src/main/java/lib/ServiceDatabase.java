package lib;

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

    private HashSet<String> occurringCountries;
    private HashSet<Integer> occurringProviders;
    private HashSet<String> occurringServiceTypes;
    private HashSet<String> occurringStatuses;

    /**
     * Initialize "database"
     */
    public ServiceDatabase(List<Service> aServices) {
        services = aServices;

        for (Service s: services) {
            occurringCountries.add(s.countryCode());
            occurringProviders.add(s.tspId());
            for (String type: s.qServiceTypes()) {
                occurringServiceTypes.add(type);
            }
            occurringStatuses.add(s.currentStatus());
        }
    }

    @Override
    public Map<String, String> getCountryCodeToNames() {
        return null; // TODO
    }

    @Override
    public Map<Integer, String> getCodeToProviderNames() {
        return null; // TODO
    }

    @Override
    public ServiceFilter getComplementaryFilter(ServiceFilter partial) {
        return null; // TODO
    }

    @Override
    public List<Service> getServices(ServiceFilter filter) {
        return null; // TODO
    }

    private static boolean matches(Service service, ServiceFilter filter){

        boolean matchesCountry = false;
        boolean matchesTSP = false;
        boolean matchesType = false;
        boolean matchesStatus = false;

        if(filter.countries().isPresent())
            matchesCountry =
                    filter.countries().get().contains(service.countryCode());

        if(filter.providers().isPresent())
            matchesTSP =
                    filter.providers().get().contains(service.tspId());

        if(filter.types().isPresent())
            matchesType =
                    filter.types().get().contains(service.type());

        if(filter.statuses().isPresent())
            matchesStatus =
                    filter.types().get().contains(service.currentStatus());

        return matchesCountry && matchesTSP && matchesType && matchesStatus;
    }
}
