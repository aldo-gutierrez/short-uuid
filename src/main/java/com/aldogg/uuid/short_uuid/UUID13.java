package com.aldogg.uuid.short_uuid;

import java.util.Date;
import java.util.Random;

public class UUID13 {
    private static final int BITS_TIMESTAMP = 33;
    private static final int BITS_MACHINE = 8;
    private static final int BITS_COUNTER = 23;

    private static final long machineId = new Random().nextInt(1<< BITS_MACHINE);
    private static long secPerProcess = new Random().nextInt(1 << BITS_COUNTER);

    /**
     * 33 bit timestamp seconds since 1 jan 1970
     *  8 bit machine/process identifier, initiated on server startup at random value
     * 23 counter, starting with a random value
     * @return
     */
    public static long getUUID() {
        long maskTime = UUIDUtils.getMask(BITS_TIMESTAMP);
        long maskCounter = UUIDUtils.getMask(BITS_COUNTER);

        long time64 = System.currentTimeMillis();
        long time33 = (time64 / 1000L) & maskTime;
        return  (time33 << 64 - BITS_TIMESTAMP)
                | (machineId << BITS_COUNTER)
                | ((secPerProcess++ & maskCounter));
    }


}
