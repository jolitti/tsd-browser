package org.example;

import lib.ServiceFilter;
import it.unipd.sweng.ModelSpawner;
import lib.Service;
import lib.interfaces.ModelInterface;
import lib.internal.ServiceDatabase;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class ServiceDatabaseTest {
    @Test
    public void shouldMaintainCoherence() {
        // Generating country map for ServiceDatabase
        Map<String,String> countryMap = new HashMap<>();
        countryMap.put("EE", "Estonia");
        countryMap.put("DK","Denmark");
        countryMap.put("LU","Luxembourg");

        // Generating provider full name map
        Map<String,String> providerMap = new HashMap<>();
        providerMap.put("EE 1","Central estonian bank");
        providerMap.put("EE 2", "ZA/UM studio");
        providerMap.put("DK 1","Crazy Joel's emporium of timestamps");
        providerMap.put("DK 2", "PointInfo server farm");
        providerMap.put("LU 1","Afurto mail provider");
        providerMap.put("LU 2", "Rosa Luxembourg's house of clocks");

        // Generating service list
        final int SERVICE_NUMBER = 7;
        Service estBankSign = ExampleServices.quickServiceGen("EE 1","EE","granted",new String[]{"TIME","SIGN"});
        Service estStudioAuth =  ExampleServices.quickServiceGen("EE 2","EE","withdrawn",new String[]{"TIME","WEBAUTH"});
        Service dkTimeAuth = ExampleServices.quickServiceGen("DK 1","DK","granted",new String[]{"SEAL","SIGN","TIME"});
        Service dkServerJob = ExampleServices.quickServiceGen("DK 2","DK","pending",new String[]{"DELIVERY"});
        Service luCertification = ExampleServices.quickServiceGen("LU 1","LU","withdrawn",new String[]{"SIGN","SEAL"});
        Service luESignature = ExampleServices.quickServiceGen("LU 1","LU","granted",new String[]{"SIGN","TIME"});
        Service luESignature2 = ExampleServices.quickServiceGen("LU 2","LU","granted",new String[]{"TIME","SIGN"});
        List<Service> services = Arrays.asList(
                estBankSign,
                estStudioAuth,
                dkTimeAuth,
                dkServerJob,
                luCertification,
                luESignature,
                luESignature2
        );

        // Finally, generating the database from the variables
        ServiceDatabase database = new ServiceDatabase(services,countryMap,providerMap);

        // What follows is a series of tests meant to ensure the database responds correctly

        // nullFilter => returns every service it holds
        List<Service> everyService = database.getServices(ServiceFilter.nullFilter);
        assertEquals(SERVICE_NUMBER, everyService.size());
        // ensure each element is different from each other
        for (int i = 0; i<SERVICE_NUMBER-1; i++) {
            for (int j = i+1; j<SERVICE_NUMBER; j++) {
                assertNotEquals(everyService.get(i),everyService.get(j));
            }
        }
    }
}
