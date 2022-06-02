package lib;

import java.util.*;

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
    public static ServiceFilter filterFromLists(
            Optional<List<String>> nat,
            Optional<List<Integer>> t,
            Optional<List<String>> ty,
            Optional<List<String>> st) {

        Optional<Set<String>> countries = nat.isPresent()?
                Optional.of(new HashSet<>(nat.get())) : Optional.empty();
        Optional<Set<Integer>> ids = t.isPresent()?
                Optional.of(new HashSet<>(t.get())) : Optional.empty();
        Optional<Set<String>> states = ty.isPresent()?
                Optional.of(new HashSet<>(ty.get())) : Optional.empty();
        Optional<Set<String>> serviceTypes = st.isPresent()?
                Optional.of(new HashSet<>(st.get())) : Optional.empty();

        return new ServiceFilter(
                countries,ids,states,serviceTypes
        );
    }
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

    public static final ServiceFilter nullFilter = new ServiceFilter(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}

