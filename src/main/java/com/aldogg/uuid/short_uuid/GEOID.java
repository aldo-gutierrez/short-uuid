package com.aldogg.uuid.short_uuid;

/**
 *
 * 32 bit geoId
 * 15 bit latitude
 * 17 bit longitude
 */
public class GEOID {

    private static int MIN_LATITUDE_INCLUSIVE =-56;
    private static int MAX_LATITUDE_EXCLUSIVE =+72;

    private static int MIN_LONGITUDE = -180;
    private static int MAX_LONGITUDE = 180;

    public int getGeoID(double lat, double lon) {
        int lat15 = getLat15Bits(lat);
        int lon17 = getLon17Bits(lon);
        return interleave(lon17, lat15);
    }

    protected int interleave(int lon17, int lat15) {
        int res = (lon17 >> 16) & 1;
        res = (res << 1) | ((lon17 >> 15) & 1);
        for (int k = 14; k>=0; k--) {
            res = (res << 1) | ((lat15 >> k) & 1);
            res = (res << 1) | ((lon17 >> k) & 1);
        }
        return res;
    }

    protected int getLat15Bits(double lat) {
        //15 bits for latitude
        if (lat < MIN_LATITUDE_INCLUSIVE) {
            lat = MIN_LATITUDE_INCLUSIVE;
        } else if (lat >= MAX_LATITUDE_EXCLUSIVE) {
            lat = MAX_LATITUDE_EXCLUSIVE - 0.0001;
        }
        lat = lat + -MIN_LATITUDE_INCLUSIVE;
        //7 bits for lat integer part
        int latHigh = (int) lat;
        //8 bits for lat decimal part
        int latLow = (int) ((lat - latHigh) * 256);
        return (latHigh << 8) + latLow;
    }

    protected int getLon17Bits(double lon) {
        //15 bits for latitude
        if (lon < MIN_LONGITUDE) {
            lon = MIN_LONGITUDE;
        } else if (lon > MAX_LONGITUDE) {
            lon = MAX_LONGITUDE;
        }
        return ((int) (lon * 364.088889)) & ((1 << 17) - 1);
    }
}
