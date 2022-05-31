package lib.internal;

import lib.Service;
import lib.ServiceFilter;
import lib.interfaces.ModelInterface;

import java.util.*;

/**
 * Class that holds a record of all the services and can be queried via ServiceFilters
 */
public class ServiceDatabase implements ModelInterface
{
    private final List<Service> services;
    private final Map<String,String> countryCodeMap;
    private final Map<Integer,String> providerIdMap;

    /**
     * Initialize "database"
     */
    public ServiceDatabase(List<Service> aServices, Map<String,String> aCountryCodeMap, Map<Integer,String> aProviderIdMap) {
        services = aServices;
        countryCodeMap = aCountryCodeMap;
        providerIdMap = aProviderIdMap;
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
        // Construct complementary parameters for new filter
        Optional<Set<String>> countries = partial.countries().isPresent()?
                Optional.empty(): Optional.of(new HashSet<>());
        Optional<Set<Integer>> providers = partial.providers().isPresent()?
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
        countries = countries.isPresent()? countries : partial.countries();
        providers = providers.isPresent()? providers : partial.providers();
        types = types.isPresent()? types : partial.types();
        statuses = statuses.isPresent()? statuses : partial.statuses();

        return new ServiceFilter(countries,providers,types,statuses);
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
