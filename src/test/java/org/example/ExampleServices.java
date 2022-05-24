package org.example;
import lib.Service;
import lib.ServiceFilter;

import java.util.Optional;

public class ExampleServices {
    public static Service example = new Service(
            0,
            1,
            "IT",
            "PosteIta",
            "QC",
            "active",
            "x",
            new String[]{"PEC"}
    );

    public static ServiceFilter nullFilter = new ServiceFilter(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}
