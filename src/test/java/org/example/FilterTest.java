package org.example;

import lib.ServiceFilter;
import it.unipd.sweng.ModelSpawner;
import lib.Service;
import lib.interfaces.ModelInterface;
import lib.internal.ServiceDatabase;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FilterTest {

    @Test
    public void shouldCreateSetsFromLists() {
        List<String> countries = new ArrayList<>(Arrays.asList("SP","FR"));
        List<String> ids = new ArrayList<>(Arrays.asList("SP 1","FR 2","FR 5"));
        List<String> types = new ArrayList<>(Arrays.asList("MAIL","TIME"));
        List<String> statuses = new ArrayList<>(Arrays.asList("active"));

        ServiceFilter filter = new ServiceFilter(
                Optional.of(countries),
                Optional.of(ids),
                Optional.of(types),
                Optional.of(statuses)
        );

        assertTrue(filter.getCountriesSet().isPresent());
        assertTrue(filter.getProvidersSet().isPresent());
        assertTrue(filter.getTypesSet().isPresent());
        assertTrue(filter.getStatusesSet().isPresent());

        assertEquals(2, filter.getCountriesSet().get().size());
        assertTrue(filter.getCountriesSet().get().contains("SP"));
        assertTrue(filter.getCountriesSet().get().contains("FR"));

        assertEquals(3,filter.getProvidersSet().get().size());
        assertTrue(filter.getProvidersSet().get().contains("SP 1"));
        assertTrue(filter.getProvidersSet().get().contains("FR 2"));
        assertTrue(filter.getProvidersSet().get().contains("FR 5"));

        assertEquals(2,filter.getTypesSet().get().size());
        assertTrue(filter.getTypesSet().get().contains("MAIL"));
        assertTrue(filter.getTypesSet().get().contains("TIME"));

        assertEquals(1,filter.getStatusesSet().get().size());
        assertTrue(filter.getStatusesSet().get().contains("active"));
    }

    @Test
    public void shouldCreateListsFromSets() {
        Set<String> countries = new HashSet<>(Arrays.asList("SP","FR"));
        Set<String> ids = new HashSet<>(Arrays.asList("SP 1","FR 2","FR 5"));
        Set<String> types = new HashSet<>(Arrays.asList("MAIL","TIME"));
        Set<String> statuses = new HashSet<>(Arrays.asList("active"));

        ServiceFilter filter = ServiceFilter.buildFilterFromSets(
                Optional.of(countries),
                Optional.of(ids),
                Optional.of(types),
                Optional.of(statuses)
        );

        assertTrue(filter.countries().isPresent());
        assertTrue(filter.providers().isPresent());
        assertTrue(filter.types().isPresent());
        assertTrue(filter.statuses().isPresent());

        assertEquals(2, filter.countries().get().size());
        assertTrue(filter.countries().get().contains("SP"));
        assertTrue(filter.countries().get().contains("FR"));

        assertEquals(3,filter.providers().get().size());
        assertTrue(filter.providers().get().contains("SP 1"));
        assertTrue(filter.providers().get().contains("FR 2"));
        assertTrue(filter.providers().get().contains("FR 5"));

        assertEquals(2,filter.types().get().size());
        assertTrue(filter.types().get().contains("MAIL"));
        assertTrue(filter.types().get().contains("TIME"));

        assertEquals(1,filter.statuses().get().size());
        assertTrue(filter.statuses().get().contains("active"));
    }

    @Test
    public void shouldConvertNullParameters() {
        // This tests if the ServiceFilter constructor converts null into Optional.empty
        ServiceFilter filterWithNulls = new ServiceFilter(
                null,
                Optional.of(Arrays.asList("FR 2","SP 5")),
                null,
                null
        );

        assertEquals(Optional.empty(),filterWithNulls.countries());
        assertEquals(2,filterWithNulls.providers().get().size());
        assertEquals(Optional.empty(),filterWithNulls.types());
        assertEquals(Optional.empty(),filterWithNulls.statuses());
    }
}