package org.example;

import static org.junit.Assert.assertTrue;

import lib.Service;
import lib.ServiceFilter;
import lib.internal.ServiceDatabase;
import org.junit.Test;

import java.util.*;

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
    public void shouldMatchAnyQTypes() {
        Service s = ExampleServices.quickServiceGen(1,"IT","on",new String[]{"A","B"});
        HashSet<String> qtypes = new HashSet<>();
        qtypes.add("A");
        qtypes.add("C");
        ServiceFilter f = ExampleServices.quickQTypeFilter(qtypes);

        assertTrue(f.matches(s));
    }

    @Test
    public void shouldMatchComplementary() {
        Service it = ExampleServices.quickServiceGen(0,"IT","on",new String[]{"A"});
        Service fr = ExampleServices.quickServiceGen(1,"FR","on",new String[]{"B"});
        List<Service> list = new ArrayList<>(Arrays.asList(it,fr));

        ServiceDatabase db = new ServiceDatabase(list,new HashMap<>(),new HashMap<>());
        ServiceFilter filter = ExampleServices.quickCountryFilter("IT");

        assertTrue(db.getComplementaryFilter(filter).types().get().contains("A"));

    }
}
