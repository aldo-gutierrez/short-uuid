package com.aldogg.uuid.short_uuid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeoCode32BTest {

    private static final double DELTA = 0.005; // Acceptable precision for latitude/longitude

    @Test
    void testEncodeDecodeWithCities() {
        GeoCode32B geoCode32 = new GeoCode32B();

        // Ten important but distant cities
        Object[][] cities = {
            // Latitude, Longitude
            {34.0522, -118.2437, "Los Angeles, USA"},
            {40.7128, -74.0060, "New York, USA"},
            {51.5074, 0.1278, "London, UK"},
            {48.8566, 2.3522, "Paris, France"},
            {35.6895, 139.6917, "Tokyo, Japan"},
            {-33.8688, 151.2093, "Sydney, Australia"},
            {-22.9068, -43.1729, "Rio de Janeiro, Brazil"},
            {19.0760, 72.8777, "Mumbai, India"},
            {55.7558, 37.6173,  "Moscow, Russia"},
            {-26.2041, 28.0473, "Johannesburg, South Africa"},
            {61.250, 73.4167, "Surgut, Russia"},
            {61.9585, 68.5731, "Murmansk, Russia"},
            {78.2232, 15.6569, "Longyearbyen, Norway"},
            {54.56, -67.37, "Puerto Williams, Chile"},
            {-55.05, -67.0430, "Puerto Toro, Chile"}
        };

        for (int i = 0; i < cities.length; i++) {
            double originalLat = (Double) cities[i][0];
            double originalLon = (Double) cities[i][1];

            // Encode
            int encoded = geoCode32.encode(originalLat, originalLon);

            // Decode
            double[] decoded = geoCode32.decodeToDoubleA(encoded);
            double decodedLat = decoded[0];
            double decodedLon = decoded[1];

            // Assert precision
            assertEquals(originalLat, decodedLat, DELTA,
                         "Latitude mismatch for city " + cities[i][2] + ": " + originalLat + " vs " + decodedLat);
            assertEquals(originalLon, decodedLon, DELTA,
                         "Longitude mismatch for city " + cities[i][2] + ": " + originalLon + " vs " + decodedLon);

            System.out.println("City " + cities[i][2] + ": Original Lat=" + originalLat + ", Lon=" + originalLon +
                               ", Decoded Lat=" + decodedLat + ", Lon=" + decodedLon +
                               ", Encoded=" + encoded);
        }
    }
}
