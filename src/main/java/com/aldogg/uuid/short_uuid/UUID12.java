package com.aldogg.uuid.short_uuid;

import java.util.Random;

public class UUID12 {

    private static final int BITS_TIMESTAMP = 34;
    private static final int BITS_MACHINE = 8;
    private static final int BITS_COUNTER = 22;

    private static final long machineId = new Random().nextInt(1<< BITS_MACHINE);
    private static long secPerProcess = new Random().nextInt(1 << BITS_COUNTER);

    /**
     * 34 bit timestamp (3 bits for 1/8 second, 31 bits for 68 years time to repeat this part
     *  8 bit machine/process identifier, initiated on server startup at random value
     * 22  counter, starting with a random value
     * @return
     */
    public static long getUUID() {
        long maskTime = UUIDUtils.getMask(BITS_TIMESTAMP);
        long maskCounter = UUIDUtils.getMask(BITS_COUNTER);

        long time64 = System.currentTimeMillis();
        long time33 = (time64 >> 7) & maskTime;
        return  (time33 << 64 - BITS_TIMESTAMP)
                | (machineId << BITS_COUNTER)
                | ((secPerProcess++ & maskCounter));
    }

}
