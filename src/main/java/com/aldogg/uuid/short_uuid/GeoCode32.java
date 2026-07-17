package com.aldogg.uuid.short_uuid;

public class GeoCode32 {

    public static final int E7FACTOR_I = 10000000;
    public static final double E7FACTOR_D = 10000000.0;

    public int encode(double latitude, double longitude) {
        int getLat15 = encodeLatitude15(latitude);
        int getLon17 = encodeLongitude17(longitude);
        return pack(getLon17, getLat15);
    }

    public int encode(int Latitude, int longitude) {
        return encode(longitude / E7FACTOR_D, Latitude / E7FACTOR_D);
    }

    public int[] decodeToIntA(int encoded) {
        int unpacked = unpack(encoded);
        int lat15 = unpacked & 0x7FFF; // Extract the lower 15 bits for latitude
        int lon17 = (unpacked >> 17) & 0x1FFFF; // Extract the higher 17 buts for longitude
        double lat = decodeLatitude15Unsigned(lat15);
        double lon = decodeLongitude17(lon17);
        return new int[]{(int) (lat * E7FACTOR_I), (int) (lon * E7FACTOR_I)};
    }

    public double[] decodeToDoubleA(int encoded) {
        int unpacked = unpack(encoded);
        int lat15 = unpacked & 0x7FFF; // Extract the lower 15 bits for latitude
        int lon17 = (unpacked >> 15) & 0x1FFFF; // Extract the higher 17 buts for longitude
        double lat = decodeLatitude15Unsigned(lat15);
        double lon = decodeLongitude17(lon17);
        return new double[]{lat , lon};
    }


    protected static int pack(int lon17, int lat15) {
        // 1. Get the interleaved bits for the common 15-bit range
        // We take the lower 15 bits of lon and all 15 bits of lat
        int lon15 = (lon17 >> 2) & 0x7FFF;

        // 2. Interleave:
        int packed = Morton.interleave32(lat15, lon15);

        // 3. Prepend the 2 extra LON bits (16 and 15) to the top of the 32-bit int
        packed = (packed << 2) | (lon17 & 0b11);

        return packed;
    }

    /**
     * Unpacks a 32-bit packed integer back into 17-bit Lon and 15-bit Lat.
     * @return int array where index 0 is Longitude and index 1 is Latitude.
     */
    protected static int unpack(int packed) {
        // 1. Extract the two extra Longitude bits (bits 0-1)
        int lastTwo = packed & 0b11;

        // 2. Extract the interleaved bits
        // The interleaved part is in bits 2-31
        int interleaved = packed >> 2;

        int lat15 = Morton.compact1By1From32To16(interleaved);
        int lon15 = Morton.compact1By1From32To16(interleaved >>> 1);

        // 4. Combine the bits
        int lon = lastTwo  | (lon15 << 2);

        return lon << 15 | lat15;
    }
    private static final int LAT_RANGE = 32768; // 2^15, 15 bits

    private static final int LON_RANGE = 131071; // 2^17 - 1

    /**
     * Maps longitude [-180, 180] to unsigned [0, 131071]
     */
    public static int encodeLongitude17(double lon) {
        // Clamp to physical limits
        lon = Math.max(-180.0, Math.min(180.0, lon));

        // Linear scaling: (value + offset) / total_range * max_int
        // We use 180.0 to shift [-180, 180] to [0, 360]
        double normalized = (lon + 180.0) / 360.0;

        return (int) Math.round(normalized * LON_RANGE);
    }

    private static final double LON_RANGE_D = 131071.0; // 2^17 - 1

    /**
     * Maps unsigned [0, 131071] back to longitude [-180.0, 180.0]
     */
    public static double decodeLongitude17(int encodedLon) {
        // Clamp to valid 17-bit integer bounds
        encodedLon = Math.max(0, Math.min(131071, encodedLon));

        // Reverse linear scaling: (normalized * 360.0) - 180.0
        double normalized = encodedLon / LON_RANGE_D;

        return (normalized * 360.0) - 180.0;
    }

    /**
     * Latitude Range,Span,Target %,Allocated Values
     * -90 to -55,  35∘,  3.15%,  1,032
     * -55 to -40,  15∘,  2.75%,    901
     * -40 to 60,  100∘, 90.00%, 29,491
     * 60 to 75,    15∘,  2.75%,    901
     * 75 to 90,    15∘,  1.35%,    443
     * @param lat
     * @return
     */
    public static int encodeLatitude15(double lat) {
        // Clamp to -90..90
        lat = Math.max(-90.0, Math.min(90.0, lat));

        // 1. Calculate relative progress within the specific zone
        double zoneProgress;
        double baseOffset;

        if (lat < -55) {
            zoneProgress = (lat + 90.0) / 35.0; // Range: 0 to 1
            baseOffset = 0.0;
        } else if (lat < -40) {
            zoneProgress = (lat + 55.0) / 15.0;
            baseOffset = 0.0315;
        } else if (lat < 60) {
            zoneProgress = (lat + 40.0) / 100.0;
            baseOffset = 0.0315 + 0.0275;
        } else if (lat < 75) {
            zoneProgress = (lat - 60.0) / 15.0;
            baseOffset = 0.0315 + 0.0275 + 0.90;
        } else {
            zoneProgress = (lat - 75.0) / 15.0;
            baseOffset = 0.0315 + 0.0275 + 0.90 + 0.0275;
        }

        // Map directly to unsigned 0..32767
        double mappedValue = (baseOffset + (zoneProgress * getZonePercent(lat))) * (LAT_RANGE - 1);
        return (int) Math.round(mappedValue);
    }

    private static double getZonePercent(double lat) {
        if (lat < -55) return 0.0315;
        if (lat < -40) return 0.0275;
        if (lat < 60) return 0.90;
        if (lat < 75) return 0.0275;
        return 0.0135;
    }

    // Optimization: Use a slightly more precise multiplier approach
    public static int encodeLatitude15Unsigned(double lat) {
        lat = Math.max(-90.0, Math.min(90.0, lat));

        double[] offsets = {0.0, 0.0315, 0.059, 0.959, 0.9865}; // Pre-summed offsets
        double[] percents = {0.0315, 0.0275, 0.90, 0.0275, 0.0135};
        double[] ranges = {35.0, 15.0, 100.0, 15.0, 15.0};
        double[] starts = {-90.0, -55.0, -40.0, 60.0, 75.0};

        int zone = (lat < -55) ? 0 : (lat < -40) ? 1 : (lat < 60) ? 2 : (lat < 75) ? 3 : 4;

        // Calculate normalized position (0.0 to 1.0)
        double norm = (lat - starts[zone]) / ranges[zone];
        // Scale by percentage and add base offset
        double val = (offsets[zone] + (norm * percents[zone])) * 32767.0;

        return (int) Math.round(val);
    }

    public static double decodeLatitude15Unsigned(int encodedLat) {
        // Clamp to valid 15-bit integer bounds
        encodedLat = Math.max(0, Math.min(LAT_RANGE - 1, encodedLat));

        // Define our exact integer bucket boundaries based on the allocation percentages
        int zone0Boundary = (int) Math.round(0.0315 * (LAT_RANGE - 1));
        int zone1Boundary = (int) Math.round((0.0315 + 0.0275) * (LAT_RANGE - 1));
        int zone2Boundary = (int) Math.round((0.0315 + 0.0275 + 0.90) * (LAT_RANGE - 1));
        int zone3Boundary = (int) Math.round((0.0315 + 0.0275 + 0.90 + 0.0275) * (LAT_RANGE - 1));

        double startLat;
        double zoneSpan;
        double zoneProgress;

        if (encodedLat < zone0Boundary) {
            // Zone 0: -90 to -55 (3.15% of space)
            zoneProgress = (double) encodedLat / zone0Boundary;
            startLat = -90.0;
            zoneSpan = 35.0;
        } else if (encodedLat < zone1Boundary) {
            // Zone 1: -55 to -40 (2.75% of space)
            zoneProgress = (double) (encodedLat - zone0Boundary) / (zone1Boundary - zone0Boundary);
            startLat = -55.0;
            zoneSpan = 15.0;
        } else if (encodedLat < zone2Boundary) {
            // Zone 2: -40 to 60 (90% of space)
            zoneProgress = (double) (encodedLat - zone1Boundary) / (zone2Boundary - zone1Boundary);
            startLat = -40.0;
            zoneSpan = 100.0;
        } else if (encodedLat < zone3Boundary) {
            // Zone 3: 60 to 75 (2.75% of space)
            zoneProgress = (double) (encodedLat - zone2Boundary) / (zone3Boundary - zone2Boundary);
            startLat = 60.0;
            zoneSpan = 15.0;
        } else {
            // Zone 4: 75 to 90 (1.35% of space)
            zoneProgress = (double) (encodedLat - zone3Boundary) / ((LAT_RANGE - 1) - zone3Boundary);
            startLat = 75.0;
            zoneSpan = 15.0;
        }

        // Reconstruct the latitude degree
        return startLat + (zoneProgress * zoneSpan);
    }
}
