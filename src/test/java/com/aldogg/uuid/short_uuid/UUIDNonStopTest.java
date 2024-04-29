package com.aldogg.uuid.short_uuid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class UUIDNonStopTest {

    public static final int COUNT_MAX = 6_000_000;

    @Test
    public void testGenerationNoStopUUID10() {
        UUID10 uuid10 = new UUID10();
        Set<Long> set = new HashSet<>();
        int duplicated = 0;
        for (int i = 0; i < COUNT_MAX; i++) {
            long id = uuid10.getUUID();
            if (set.contains(id)) {
                duplicated++;
            } else {
                set.add(id);
            }
        }
        System.out.println(" duplicated " + duplicated);
        Assertions.assertTrue(duplicated == 0);
    }

    @Test
    public void testGenerationNoStopUUID11() {
        UUID11 uuid11 = new UUID11();
        Set<Long> set = new HashSet<>();
        int duplicated = 0;
        for (int i = 0; i < COUNT_MAX; i++) {
            long id = uuid11.getUUID();
            if (set.contains(id)) {
                duplicated++;
            } else {
                set.add(id);
            }
        }
        System.out.println(" duplicated " + duplicated);
        Assertions.assertTrue(duplicated == 0);
    }

    @Test
    public void testGenerationNoStopUUID12() {
        UUID12 uuid12 = new UUID12();
        Set<Long> set = new HashSet<>();
        int duplicated = 0;
        for (int i = 0; i < COUNT_MAX; i++) {
            long id = uuid12.getUUID();
            if (set.contains(id)) {
                duplicated++;
            } else {
                set.add(id);
            }
        }
        System.out.println(" duplicated " + duplicated);
        Assertions.assertTrue(duplicated == 0);
    }

    @Test
    public void testGenerationNoStopUUID13() {
        UUID13 uuid13 = new UUID13();
        Set<Long> set = new HashSet<>();
        int duplicated = 0;
        for (int i = 0; i < COUNT_MAX; i++) {
            long id = uuid13.getUUID();
            if (set.contains(id)) {
                duplicated++;
            } else {
                set.add(id);
            }
        }
        System.out.println(" duplicated " + duplicated);
        Assertions.assertTrue(duplicated == 0);
    }


}