package com.aldogg.uuid.short_uuid;

import static com.aldogg.uuid.short_uuid.GeoCode32.*;

public class GeoCode32B {

    public static final double MAGIC_VALUE = 91; //91,01953125 // 91 + 5/256
    public static final double lat_factor = 4 * MAGIC_VALUE;
    public static final double lon_factor = 2 * MAGIC_VALUE;

    public int encode(double latitude, double longitude) {
        int latitudeInt = (int) (latitude * lat_factor);
        int longitudeInt = (int) (longitude * lon_factor);
        return Morton.interleave32(latitudeInt, longitudeInt);
    }

    public int encode(int Latitude, int longitude) {
        return encode(longitude / E7FACTOR_D, Latitude / E7FACTOR_D);
    }

    public int[] decodeToIntA(int encoded) {
        int lat16 = Morton.compact1By1From32To16(encoded);
        int lon16 = Morton.compact1By1From32To16(encoded >>> 1);
        double lat = lat16 / (lat_factor);
        double lon = lon16 / (lon_factor);
        return new int[]{(int) (lat * E7FACTOR_I), (int) (lon * E7FACTOR_I)};
    }

    public double[] decodeToDoubleA(int encoded) {
        int lat16 = Morton.compact1By1From32To16(encoded);
        int lon16 = Morton.compact1By1From32To16(encoded >>> 1);
        lat16 = (lat16 << 16) >> 16;
        lon16 = (lon16 << 16) >> 16;
        double lat = lat16 / (lat_factor);
        double lon = lon16 / (lon_factor);
        return new double[]{lat, lon};
    }


}
