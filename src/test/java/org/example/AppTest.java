package org.example;

import static org.junit.Assert.assertTrue;

import lib.ServiceFilter;
import org.junit.Test;

import org.example.ExampleServices;
import lib.ServiceDatabase;

import java.util.Optional;

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
        assertTrue(ServiceDatabase.matches(ExampleServices.example,ExampleServices.nullFilter));
    }
}
