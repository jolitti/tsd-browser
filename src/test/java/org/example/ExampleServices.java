package org.example;
import lib.Service;
import lib.ServiceFilter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ExampleServices {

    public static Service quickServiceGen(int tspId, String countryCode, String status, String[] qServiceTypes) {
        return new Service(
                tspId,
                0,
                countryCode,
                "example service",
                "X",
                status,
                "y",
                qServiceTypes
        );
    }

    public static ServiceFilter quickQTypeFilter(Set<String> types) {
        return new ServiceFilter(
                Optional.empty(),
                Optional.empty(),
                Optional.ofNullable(types),
                Optional.empty()
        );
    }

    public static ServiceFilter quickQTypeFilter(String... types) {
        Set<String> typeSet = new HashSet<>();
        typeSet.addAll(Arrays.asList(types));
        return new ServiceFilter(
                Optional.empty(),
                Optional.empty(),
                Optional.of(typeSet),
                Optional.empty()
        );
    }
    public static ServiceFilter quickCountryFilter(String... countries) {
        return new ServiceFilter(
                Optional.of(Set.of(countries)),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }
    public static final Service example = new Service(
            0,
            1,
            "IT",
            "PosteIta",
            "QC",
            "active",
            "x",
            new String[]{"PEC","Pinco","Pallino","ESIG","TIME"}
    );



    public static final ServiceFilter nullFilter = new ServiceFilter(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}
