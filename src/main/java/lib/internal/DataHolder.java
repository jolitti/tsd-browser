package lib.internal;

import lib.Service;

import java.util.List;
import java.util.Map;

/**
 * Holds necessary data for ServiceDatabase initialization
 * @param services the list of Service objects
 * @param tspIDtoNameMap the Map that translates service id to the readable name
 */
public record DataHolder(
        List<Service> services,
        Map<Integer,String> tspIDtoNameMap
) {}
