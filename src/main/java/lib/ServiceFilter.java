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

}
