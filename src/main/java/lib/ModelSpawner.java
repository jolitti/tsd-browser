package lib;

import lib.interfaces.ModelInterface;
import lib.internal.ServiceDatabase;

import java.io.IOException;

public class ModelSpawner {

    private static ServiceDatabase db;

    /**
     * Return the singleton of a class that implements ModelInterface
     */
    public static ModelInterface getModelInstance() throws IOException
    {
        // Calls APIClient, gets the two maps and the service list
        // then generates a ServiceDatabase and returns it
        // (or just returns it if it's already present)

        return null; // TODO
    };

    /**
     * The constructor is hidden to make the class static
     */
    private ModelSpawner() {};
}
