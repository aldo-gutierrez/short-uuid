package com.aldogg.uuid.short_uuid;

public class UUIDUtils {

    public static long getMask(final long nBits) {
        return ((1L << nBits) - 1L);
    }

}
