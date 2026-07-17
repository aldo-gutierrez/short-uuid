package com.aldogg.uuid.short_uuid;

public class Morton {

    // Expands 16 bits into 32 bits
    public static int spread1By1From16To32(int x) {
        x &= 0xFFFF;
        x = (x | (x << 8)) & 0x00FF00FF;
        x = (x | (x << 4)) & 0x0F0F0F0F;
        x = (x | (x << 2)) & 0x33333333;
        x = (x | (x << 1)) & 0x55555555;
        return x;
    }


    // Compacts every other bit back into 16 bits
    public static int compact1By1From32To16(int x) {
        x &= 0x55555555;
        x = (x | (x >>> 1)) & 0x33333333;
        x = (x | (x >>> 2)) & 0x0F0F0F0F;
        x = (x | (x >>> 4)) & 0x00FF00FF;
        x = (x | (x >>> 8)) & 0x0000FFFF;
        return x;
    }

    public static long spread1By1From32To64(int x) {

        long v = x & 0xFFFFFFFFL;

        v = (v | (v << 16)) & 0x0000FFFF0000FFFFL;
        v = (v | (v << 8))  & 0x00FF00FF00FF00FFL;
        v = (v | (v << 4))  & 0x0F0F0F0F0F0F0F0FL;
        v = (v | (v << 2))  & 0x3333333333333333L;
        v = (v | (v << 1))  & 0x5555555555555555L;

        return v;
    }

    public static int compact1By1From64To32(long x) {

        x &= 0x5555555555555555L;

        x = (x | (x >>> 1))  & 0x3333333333333333L;
        x = (x | (x >>> 2))  & 0x0F0F0F0F0F0F0F0FL;
        x = (x | (x >>> 4))  & 0x00FF00FF00FF00FFL;
        x = (x | (x >>> 8))  & 0x0000FFFF0000FFFFL;
        x = (x | (x >>> 16)) & 0x00000000FFFFFFFFL;

        return (int)x;
    }


    public static int interleave32(int x, int y) {
        return spread1By1From16To32(x) | (spread1By1From16To32(y) << 1);
    }

    public static long interleave64(long x, long y) {
        return spread1By1From32To64((int)x) | (spread1By1From32To64((int)y) << 1);
    }

    /**
     * Deinterleaves a 32-bit Morton code into two 16-bit values.
     *
     * @return int[]{x, y}
     */
    public static int[] deinterleave32(int morton) {
        int x = compact1By1From32To16(morton);
        int y = compact1By1From32To16(morton >>> 1);
        return new int[]{x, y};
    }

}
