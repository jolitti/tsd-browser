package lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 Class that represents a particular filter for a service database query.

 For each option, if the value is None it means we aren't filtering based on that parameter
 */
public record ServiceFilter (
    Optional<Set<String>> countries,
    Optional<Set<Integer>> providers,
    Optional<Set<String>> types,
    Optional<Set<String>> statuses
    )
{
    //private static Set<String> acceptedFilterTypes = Set.of("country","provider","type","status");

    public boolean matches(Service service){

        boolean matchesCountry = true;
        boolean matchesTSP = true;
        boolean matchesType = true;
        boolean matchesStatus = true;

        if(this.countries().isPresent())
            matchesCountry =
                    this.countries().get().contains(service.countryCode());

        if(this.providers().isPresent())
            matchesTSP =
                    this.providers().get().contains(service.tspId());

        if(this.types().isPresent())
            matchesType =
                    this.types().get().contains(service.type());

        if(this.statuses().isPresent())
            matchesStatus =
                    this.types().get().contains(service.currentStatus());

        return matchesCountry && matchesTSP && matchesType && matchesStatus;
    }
}

