package com.aldogg.uuid.short_uuid;

import java.util.Random;

public class UUID11 {

    private static final int BITS_TIMESTAMP = 33;
    private static final int BITS_MACHINE = 8;
    private static final int BITS_COUNTER = 23;
    private static final long machineId = new Random().nextInt(1<< BITS_MACHINE);
    private static long secPerProcess = new Random().nextInt(1 << BITS_COUNTER);

    /**
     * 33 bit timestamp (3 bits for 1/8 second, 30 bits for 34 years time to repeat this part)
     *  8 bit machine/process identifier,
     * 23 bit  counter, starting with a random value
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
