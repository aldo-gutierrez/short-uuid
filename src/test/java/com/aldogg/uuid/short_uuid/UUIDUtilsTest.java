package com.aldogg.uuid.short_uuid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UUIDUtilsTest {

    @Test
    public void testGetMask() {
        assertEquals(0L, UUIDUtils.getMask(0));
        assertEquals(1L, UUIDUtils.getMask(1));
        assertEquals(3L, UUIDUtils.getMask(2));
        assertEquals(7L, UUIDUtils.getMask(3));
        assertEquals(0x3FFFFFFF, UUIDUtils.getMask(30));
        assertEquals(0x1FFFFFFFFL, UUIDUtils.getMask(33));
    }
}
