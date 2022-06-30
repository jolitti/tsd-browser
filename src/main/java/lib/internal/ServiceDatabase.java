package lib.internal;

import lib.ServiceFilter;
import lib.Service;
import lib.interfaces.ModelInterface;

import java.util.*;

/**
 * Class that holds a record of all the services and can be queried via ServiceFilters
 */
public class ServiceDatabase implements ModelInterface
{
    private final List<Service> services;
    private final Map<String,String> countryCodeMap;
    private final Map<String,String> providerIdMap;

    /**
     * Initialize "database"
     */
    public ServiceDatabase(List<Service> aServices, Map<String,String> aCountryCodeMap, Map<String,String> aProviderIdMap) {
        services = aServices;
        countryCodeMap = aCountryCodeMap;
        providerIdMap = aProviderIdMap;

        // Checking for the validity of the country name and provider name maps
        for (Service service : services) {
            if (countryCodeMap.getOrDefault(service.countryCode(),"Not found") == "Not found")
                throw new IllegalArgumentException("Country "+service.countryCode()+" not found in map!");
            if (providerIdMap.getOrDefault(service.tspId(),"Not found") == "Not found")
                throw new IllegalArgumentException("Provider "+service.tspId()+" not found in map!");
        }
    }

    @Override
    public Map<String, String> getCountryCodeToNames() {
        return countryCodeMap; // TODO
    }

    @Override
    public Map<String, String> getCodeToProviderNames() {
        return providerIdMap; // TODO
    }

    @Override
    public ServiceFilter getComplementaryFilter(ServiceFilter partial) {
        // Construct complementary parameters for new filter
        Optional<Set<String>> countries = partial.countries().isPresent()?
                Optional.empty(): Optional.of(new HashSet<>());
        Optional<Set<String>> providers = partial.providers().isPresent()?
                Optional.empty(): Optional.of(new HashSet<>());
        Optional<Set<String>> types = partial.types().isPresent()?
                Optional.empty(): Optional.of(new HashSet<>());
        Optional<Set<String>> statuses = partial.statuses().isPresent()?
                Optional.empty(): Optional.of(new HashSet<>());

        // Iterate on services, add only parameters that match onto existing sets
        for (Service s: services) {
            if (partial.matches(s)) {



                if (countries.isPresent()) countries.get().add(s.countryCode());
                if (providers.isPresent()) providers.get().add(s.tspId());
                if (types.isPresent()) {
                    for (String t: s.qServiceTypes()) types.get().add(t);
                }
                if (statuses.isPresent()) statuses.get().add(s.currentStatus());
            }
        }

        // Substitute null parameters with ones from the partial filter
        countries = countries.isPresent()? countries : partial.getCountriesSet();
        providers = providers.isPresent()? providers : partial.getProvidersSet();
        types = types.isPresent()? types : partial.getTypesSet();
        statuses = statuses.isPresent()? statuses : partial.getStatusesSet();

        return ServiceFilter.buildFilterFromSets(countries,providers,types,statuses);
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


