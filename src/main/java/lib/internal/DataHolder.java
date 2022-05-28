package lib.internal;

import lib.Service;

import java.util.List;
import java.util.Map;

public record DataHolder(
        List<Service> services,
        Map<Integer,String> tspIDtoNameMap
) {}
