package lib;

import java.util.*;

public class ServiceFilter {

    private final Optional<List<String>> countries;
    private final Optional<List<String>> providers;
    private final Optional<List<String>> types;
    private final Optional<List<String>> statuses;

    private Optional<Set<String>> countriesSet;
    private Optional<Set<String>> providersSet;
    private Optional<Set<String>> typesSet;
    private Optional<Set<String>> statusesSet;

    public Optional<List<String>> countries() { return countries; }
    public Optional<Set<String>> getCountriesSet() { return countriesSet; }

    public Optional<List<String>> providers() { return providers; }
    public Optional<Set<String>> getProvidersSet() { return providersSet; }

    public Optional<List<String>> types() { return types; }
    public Optional<Set<String>> getTypesSet() { return typesSet; }

    public Optional<List<String>> statuses() { return statuses; }
    public Optional<Set<String>> getStatusesSet() { return statusesSet; }





    public ServiceFilter(
            Optional<List<String>> aCountries,
            Optional<List<String>> aProviders,
            Optional<List<String>> aTypes,
            Optional<List<String>> aStatuses
    ) {

        // null-check (ensures the integrity of the filters)
        // will convert nulls into Optional.empty()
        if (aCountries == null) aCountries = Optional.empty();
        if (aProviders == null) aProviders = Optional.empty();
        if (aTypes == null) aTypes = Optional.empty();
        if (aStatuses == null) aStatuses = Optional.empty();

        // Zero-size check
        // Will convert any zero-size filters into Optional.empty()
        // (otherwise it would not match anything)
        if (aCountries.isPresent()) if (aCountries.get().size() <= 0) aCountries = Optional.empty();
        if (aProviders.isPresent()) if (aProviders.get().size() <= 0) aProviders = Optional.empty();
        if (aTypes.isPresent()) if (aTypes.get().size() <= 0) aTypes = Optional.empty();
        if (aStatuses.isPresent()) if (aStatuses.get().size() <= 0) aStatuses = Optional.empty();

        // Basic assignment
        countries = aCountries;
        providers = aProviders;
        types = aTypes;
        statuses = aStatuses;

        // Functional-style assignment of sets
        // (as recommended by intellij)
        countriesSet = countries.map(HashSet::new);
        providersSet = providers.map(HashSet::new);
        typesSet = types.map(HashSet::new);
        statusesSet = statuses.map(HashSet::new);
    }

    public static ServiceFilter buildFilterFromSets(
            Optional<Set<String>> aCountriesSet,
            Optional<Set<String>> aProvidersSet,
            Optional<Set<String>> aTypesSet,
            Optional<Set<String>> aStatusesSet
    ) {
        // null-check (ensures the integrity of the filters)
        // will convert nulls into Optional.empty()
        if (aCountriesSet == null) aCountriesSet = Optional.empty();
        if (aProvidersSet == null) aProvidersSet = Optional.empty();
        if (aTypesSet == null) aTypesSet = Optional.empty();
        if (aStatusesSet == null) aStatusesSet = Optional.empty();

        // Functional-style assignment of lists
        Optional<List<String>> aCountries = aCountriesSet.map(ArrayList::new);
        Optional<List<String>> aProviders = aProvidersSet.map(ArrayList::new);
        Optional<List<String>> aTypes = aTypesSet.map(ArrayList::new);
        Optional<List<String>> aStatuses = aStatusesSet.map(ArrayList::new);

        return new ServiceFilter(aCountries,aProviders,aTypes,aStatuses);
    }

    public boolean matches(Service s) {
        // What follows is a series of guard clauses:
        // for each parameter, if the filter is present and
        // the parameter isn't in it, the match doesn't happen

        if (countriesSet.isPresent())
            if (!countriesSet.get().contains(s.countryCode()))
                return false;

        if (providersSet.isPresent())
            if (!providersSet.get().contains(s.tspId()))
                return false;

        if (typesSet.isPresent()) {
            boolean atLeastOnePresent = false;

            for (String type : s.qServiceTypes())
                if (typesSet.get().contains(type))
                    atLeastOnePresent = true;

            if (!atLeastOnePresent) return false;
        }

        if (statusesSet.isPresent())
            if (!statusesSet.get().contains(s.currentStatus()))
                return false;

        // Having exhausted all possible negative options
        return true;
    }

    public static final ServiceFilter nullFilter = new ServiceFilter(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}
