package com.aldogg.uuid.short_uuid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class MortonTest {

    /**
     * Slow reference implementation.
     * Interleaves each bit with a zero bit.
     */
    private static int referencePart1By1(int x) {
        x &= 0xFFFF;

        int result = 0;

        for (int i = 0; i < 16; i++) {
            result |= ((x >>> i) & 1) << (i * 2);
        }

        return result;
    }

    /**
     * Slow reference implementation.
     * Extracts every even bit from a 32-bit Morton value.
     */
    private static int referenceCompact1By1(int x) {

        int result = 0;

        for (int i = 0; i < 16; i++) {
            result |= ((x >>> (i * 2)) & 1) << i;
        }

        return result;
    }

    @Test
    void testKnownValues() {

        assertEquals(0x00000000, Morton.spread1By1From16To32(0));
        assertEquals(0x00000001, Morton.spread1By1From16To32(1));
        assertEquals(0x00000004, Morton.spread1By1From16To32(2));
        assertEquals(0x00000005, Morton.spread1By1From16To32(3));
        assertEquals(0x00005555, Morton.spread1By1From16To32(0x00FF));
        assertEquals(referencePart1By1(0x1234), Morton.spread1By1From16To32(0x1234));
        assertEquals(referencePart1By1(0x7FFF), Morton.spread1By1From16To32(0x7FFF));
    }

    @Test
    void testAll16BitValues() {

        for (int value = 0; value < 0xFFFF; value++) {

            int expected = referencePart1By1(value);
            int actual = Morton.spread1By1From16To32(value);

            assertEquals(expected, actual, "Mismatch for value " + value);
        }
    }

    @Test
    void testOnlyEvenBitsAreSet() {

        for (int value = 0; value <= 0xFFFF; value++) {

            int actual = Morton.spread1By1From16To32(value);

            // Odd positions must always be zero.
            assertEquals(0, actual & 0xAAAAAAAA, "Odd bits set for " + value);
        }
    }

    @Test
    void testInputMasking() {

        // Upper bits should be ignored.
        assertEquals(Morton.spread1By1From16To32(0x7FFFF), Morton.spread1By1From16To32(0xFFFF));

    }

    @Test
    @Disabled
    void speedTestPart1By1() {

        final int ITERATIONS = 100_000_000;

        long sum = 0;

        // Warm up JIT
        for (int i = 0; i < 5_000_000; i++) {
            sum += Morton.spread1By1From16To32(i & 0xFFFF);
            sum += referencePart1By1(i & 0xFFFF);
        }

        long start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            sum += referencePart1By1(i & 0xFFFF);
        }

        long referenceTime = System.nanoTime() - start;

        start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            sum += Morton.spread1By1From16To32(i & 0xFFFF);
        }

        long magicTime = System.nanoTime() - start;

        System.out.printf("Reference : %.3f ms%n", referenceTime / 1_000_000.0);
        System.out.printf("MagicBits : %.3f ms%n", magicTime / 1_000_000.0);
        //2.67X
        System.out.printf("Speedup   : %.2fx%n", (double) referenceTime / magicTime);

        // Prevent dead-code elimination
        assertNotEquals(0, sum);
    }


    @Test
    void testKnownValuesCompact() {

        assertEquals(0x0000, Morton.compact1By1From32To16(0));
        assertEquals(0x0001, Morton.compact1By1From32To16(0x00000001));
        assertEquals(0x0002, Morton.compact1By1From32To16(0x00000004));
        assertEquals(0x0003, Morton.compact1By1From32To16(0x00000005));

        assertEquals(0x00FF, Morton.compact1By1From32To16(0x00005555));
        assertEquals(0x7FFF, Morton.compact1By1From32To16(0x15555555));
    }

    @Test
    void testAllMortonValues() {

        for (int value = 0; value <= 0xFFFF; value++) {

            int morton = Morton.spread1By1From16To32(value);

            assertEquals(value, Morton.compact1By1From32To16(morton), "Failed for " + value);
        }
    }

    @Test
    void testReferenceImplementation() {

        for (int value = 0; value <= 0xFFFF; value++) {
            int morton = Morton.spread1By1From16To32(value);
            assertEquals(referenceCompact1By1(morton), Morton.compact1By1From32To16(morton));
        }
    }

    @Test
    void testRoundTrip() {
        for (int value = 0; value <= 0xFFFF; value++) {
            assertEquals(value, Morton.compact1By1From32To16(Morton.spread1By1From16To32(value)));
        }
    }

    @Test
    void testIgnoresOddBits() {
        for (int value = 0; value <= 0xFFFF; value++) {
            int morton = Morton.spread1By1From16To32(value);

            // Set every odd bit to 1
            morton |= 0xAAAAAAAA;

            assertEquals(value, Morton.compact1By1From32To16(morton));
        }
    }

    @Test
    void testRandomGarbageOddBits() {

        java.util.Random rnd = new java.util.Random(12345);

        for (int i = 0; i < 100_000; i++) {

            int value = rnd.nextInt(1 << 15);

            int morton = Morton.spread1By1From16To32(value);

            // Randomize only odd positions
            morton ^= (rnd.nextInt() & 0xAAAAAAAA);

            assertEquals(value, Morton.compact1By1From32To16(morton));
        }
    }
}