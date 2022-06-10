package org.example;

import lib.ServiceFilter;
import lib.ModelSpawner;
import lib.Service;
import lib.interfaces.ModelInterface;
import lib.internal.ServiceDatabase;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void shouldMatchFilter() {
        assertTrue(ExampleServices.nullFilter.matches(ExampleServices.example));
    }

    @Test
    public void shouldGenerateSets() {
        ArrayList<String> aCountries = new ArrayList<String>(Arrays.asList("IT","FR"));
        ServiceFilter filter = new ServiceFilter(
                Optional.of(aCountries),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());

        assertTrue(filter.getCountriesSet().isPresent());
        assertFalse(filter.getProvidersSet().isPresent());
    }

    @Test
    public void shouldMatchAnyQTypes() {
        Service s = ExampleServices.quickServiceGen(1,"IT","on",new String[]{"A","B"});
        HashSet<String> qtypes = new HashSet<>();
        qtypes.add("A");
        qtypes.add("C");
        ServiceFilter f = ExampleServices.quickQTypeFilter(qtypes);

        assertTrue(f.matches(s));
    }

    @Test
    public void shouldMatchComplementaryCountries() {
        Service it = ExampleServices.quickServiceGen(0,"IT","on",new String[]{"A"});
        Service fr = ExampleServices.quickServiceGen(1,"FR","on",new String[]{"B"});
        List<Service> list = new ArrayList<>(Arrays.asList(it,fr));

        ServiceDatabase db = new ServiceDatabase(list,new HashMap<>(),new HashMap<>());
        ServiceFilter filter = ExampleServices.quickCountryFilter("IT");

        ServiceFilter complementary = db.getComplementaryFilter(filter);

        // Complementary tests follow
        assertTrue(complementary.providers().get().contains(0));

        assertTrue(complementary.countries().get().contains("IT"));
        assertFalse(complementary.countries().get().contains("FR"));

        assertTrue(complementary.types().get().contains("A"));
        assertFalse(complementary.types().get().contains("B"));
    }

    @Test
    public void shouldMatchComplementaryTypes() {
        Service ab = ExampleServices.quickServiceGen(0,"SP","on",new String[]{"A","B"});
        Service ac = ExampleServices.quickServiceGen(1,"DE","on",new String[]{"A","C"});
        Service abc = ExampleServices.quickServiceGen(2,"PO","on",new String[]{"A","B","C"});
        List<Service> list = new ArrayList<>(Arrays.asList(ab,ac,abc));

        ServiceDatabase db = new ServiceDatabase(list,new HashMap<>(),new HashMap<>());
        assertEquals(db.getServices(ServiceFilter.nullFilter).size(),3);

        // "A" filter complementary test
        ServiceFilter aFilter = ExampleServices.quickQTypeFilter("A");
        ServiceFilter aComplementary = db.getComplementaryFilter(aFilter);
        Set<String> aComplCountries = aComplementary.getCountriesSet().get();

        assertTrue(aComplCountries.contains("SP"));
        assertTrue(aComplCountries.contains("DE"));
        assertTrue(aComplCountries.contains("PO"));

        // "B" filter complementary test
        ServiceFilter bFilter = ExampleServices.quickQTypeFilter("B");
        ServiceFilter bComplementary = db.getComplementaryFilter(bFilter);
        Set<String> bComplCountries = bComplementary.getCountriesSet().get();

        assertTrue(bComplCountries.contains("SP"));
        assertFalse(bComplCountries.contains("DE"));
        assertTrue(bComplCountries.contains("PO"));
    }

    @Test
    public void shouldFilterServices() {
        Service ab = ExampleServices.quickServiceGen(0,"SP","on",new String[]{"A","B"});
        Service ac = ExampleServices.quickServiceGen(1,"DE","on",new String[]{"A","C"});
        Service abc = ExampleServices.quickServiceGen(2,"PO","on",new String[]{"A","B","C"});
        List<Service> list = new ArrayList<>(Arrays.asList(ab,ac,abc));

        ServiceDatabase db = new ServiceDatabase(list,new HashMap<>(),new HashMap<>());

        List<Service> filtered = db.getServices(ExampleServices.quickQTypeFilter("A"));

        assertEquals(3, filtered.size());
    }

    @Test
    public void shouldBuildDatabase() throws java.io.IOException {
        ModelInterface db = ModelSpawner.getModelInstance();
        List<Service> services = db.getServices(ServiceFilter.nullFilter);

        assertTrue(services.size() > 100);
    }
}
