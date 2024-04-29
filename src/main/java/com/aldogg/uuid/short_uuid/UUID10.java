package com.aldogg.uuid.short_uuid;
import java.util.Random;

public class UUID10 {


    private static final int BITS_TIMESTAMP = 30;
    private static final int BITS_MACHINE = 7;
    private static final int BITS_COUNTER = 20;
    private static final int BITS_RANDOM = 7;

    private static final long machineId = new Random().nextInt(1 << BITS_MACHINE);
    private static long secPerProcess = new Random().nextInt(1 << BITS_COUNTER);

    /**
     * 30-bit timestamp (3 bits for 1/8 second, 27 bits for 4 years three months  to repeat this part)
     *  7-bit machine/process identifier,
     * 20-bit counter, starting with a random value
     *  7-bit random number
     * @return
     */
    public static long getUUID() {
        long maskTime = UUIDUtils.getMask(BITS_TIMESTAMP);
        long maskCounter = UUIDUtils.getMask(BITS_COUNTER);

        long time64 = System.currentTimeMillis();
        long time30 = (time64 >> 7) & maskTime;

        long randPerExec = new Random().nextInt(1 << BITS_RANDOM);
        return  (time30 << 64 - BITS_TIMESTAMP)
                | (machineId << (BITS_RANDOM + BITS_COUNTER))
                | ((secPerProcess++ & maskCounter) << BITS_RANDOM)
                | randPerExec;
    }
}
