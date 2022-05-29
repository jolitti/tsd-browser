package lib.internal;

import lib.Service;

import java.util.List;
import java.util.Map;

/**
 * Holds necessary data for ServiceDatabase initialization
 * @param services
 * @param tspIDtoNameMap
 */
public record DataHolder(
        List<Service> services,
        Map<Integer,String> tspIDtoNameMap
) {}
