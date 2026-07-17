package com.aldogg.uuid.short_uuid;

public class GeoCode64 {
    public static final int E7FACTOR_I = 10000000;
    public static final double E7FACTOR_D = 10000000.0;

    /**
     * @param latitude latitude GPS format
     * @param longitude longitude GPS format
     * @return encoded interleaved searchable indexable Geo Code
     */
    public long encode(double latitude, double longitude) {
        int lat = (int) (latitude * E7FACTOR_I);
        int lon = (int) (longitude * E7FACTOR_I);
        return encode(lat, lon);
    }

    /**
     * @param latitude latitude GPS E7 format
     * @param longitude longitude GPS E7 format
     * @return encoded interleaved searchable indexable Geo Code
     */
    public long encode(int latitude, int longitude) {
        return pack(latitude, longitude);
    }

    public int[] decodeToIntA(int encoded) {
        long unpacked = unpack(encoded);
        long lat = unpacked & 0xFFFFFFFFL; // Extract the lower 32 bits for latitude
        long lon = (unpacked >> 32) & 0xFFFFFFFFL; // Extract the higher 32 buts for longitude
        return new int[]{(int) (lat), (int) (lon)};
    }

    public double[] decodeToDoubleA(long encoded) {
        long unpacked = unpack(encoded);
        int lat = (int) (unpacked & 0xFFFFFFFFL); // Extract the lower 32 bits for latitude
        int lon = (int) (unpacked >> 32); // Extract the higher 32 buts for longitude
        return new double[]{lat / E7FACTOR_D, lon / E7FACTOR_D};
    }

    protected static long pack(int lon32, int lat32) {
        // 1. Interleave:
        return Morton.interleave64(lat32, lon32);
    }

    protected static long unpack(long packed) {
        int lon32 = Morton.compact1By1From64To32(packed);
        int lat32 = Morton.compact1By1From64To32(packed >>> 1);
        return (((long) lon32) << 32) | (((long) lat32) & 0xFFFFFFFFL);
    }
}
