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
        boolean matchesQType = true;
        boolean matchesStatus = true;

        if(this.countries().isPresent())
            matchesCountry =
                    this.countries().get().contains(service.countryCode());

        if(this.providers().isPresent())
            matchesTSP =
                    this.providers().get().contains(service.tspId());

        if(this.types().isPresent())
            for(String qType: service.qServiceTypes()) {
                matchesQType = this.types().get().contains(qType);
                if(matchesQType) break; //si ferma appena trova un QService che matcha
            }
        if(this.statuses().isPresent())
            matchesStatus =
                    this.statuses().get().contains(service.currentStatus());

        return matchesCountry && matchesTSP && matchesQType && matchesStatus;
    }
}

