package org.example;

import lib.ServiceFilter;
import it.unipd.sweng.ModelSpawner;
import lib.Service;
import lib.interfaces.ModelInterface;
import lib.internal.ServiceDatabase;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ServiceTest {
    @Test
    public void shouldExceptForNullParameters() {
        // null country
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    null,
                    1,
                    "FR",
                    "X",
                    "Y",
                    "granted",
                    "t",
                    new String[]{}
            );
        });
        // null countryCode
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "FR 1",
                    1,
                    null,
                    "X",
                    "Y",
                    "granted",
                    "t",
                    new String[]{}
            );
        });

        // null serviceName
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "FR 1",
                    1,
                    "FR",
                    null,
                    "Y",
                    "granted",
                    "t",
                    new String[]{}
            );
        });

        // null type
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "FR 1",
                    1,
                    "FR",
                    "X",
                    null,
                    "granted",
                    "t",
                    new String[]{}
            );
        });

        // null currentStatus
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "FR 1",
                    1,
                    "FR",
                    "X",
                    "Y",
                    null,
                    "t",
                    new String[]{}
            );
        });

        // null tob
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "FR",
                    1,
                    "FR",
                    "X",
                    "Y",
                    "granted",
                    null,
                    new String[]{}
            );
        });

        // null serviceTypes
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "FR",
                    1,
                    "FR",
                    "X",
                    "Y",
                    "granted",
                    "t",
        null
            );
        });
    }

    @Test
    public void shouldExceptForEmptyStrings() {
        // empty service ID string
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "",
                    1,
                    "FR",
                    "X",
                    "Y",
                    "granted",
                    "t",
                    new String[]{}
            );
        });

        // empty country string
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "FR 1",
                    1,
                    "",
                    "X",
                    "Y",
                    "granted",
                    "t",
                    new String[]{}
            );
        });
    }

    @Test
    public void shouldRequireCountryPrefix() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                    "NOT_FR 1",
                    1,
                    "FR",
                    "X",
                    "Y",
                    "granted",
                    "t",
                    new String[]{}

            );
        });
    }
}
