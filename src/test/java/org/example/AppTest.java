package org.example;

import static org.junit.Assert.assertTrue;

import lib.Service;
import lib.ServiceFilter;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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
}
