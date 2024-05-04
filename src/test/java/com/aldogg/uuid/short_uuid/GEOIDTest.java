package com.aldogg.uuid.short_uuid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GEOIDTest {

    @Test
    public void testGetLat() {
        GEOID geoid = new GEOID();
        Assertions.assertEquals(0, geoid.getLat15Bits(-57));
        Assertions.assertEquals(0, geoid.getLat15Bits(-56));
        Assertions.assertEquals(1, geoid.getLat15Bits(-55.995));
        Assertions.assertEquals(2, geoid.getLat15Bits(-55.991));
        Assertions.assertEquals((1 << 15) - 1, geoid.getLat15Bits(73));
        Assertions.assertEquals((1 << 15) - 1, geoid.getLat15Bits(72));
        Assertions.assertEquals((1 << 15) - 1, geoid.getLat15Bits(71.999));
        Assertions.assertEquals((1 << 15) - 2, geoid.getLat15Bits(71.995));
    }

    @Test
    public void testGetLon() {
        GEOID geoid = new GEOID();
        Assertions.assertEquals(0, geoid.getLon17Bits(0));
        Assertions.assertEquals(1, geoid.getLon17Bits(0.003));
        Assertions.assertEquals(2, geoid.getLon17Bits(0.006));
        Assertions.assertEquals((1 << 17) - 1, geoid.getLon17Bits(-0.003));
        Assertions.assertEquals((1 << 17) - 2, geoid.getLon17Bits(-0.006));
        Assertions.assertEquals((1 << 16), geoid.getLon17Bits(-180));
        Assertions.assertEquals((1 << 16), geoid.getLon17Bits(180));
    }

    @Test
    public void testGeoId() {
        GEOID geoid = new GEOID();
        int lat = geoid.getLat15Bits(-12.046374);
        int lon = geoid.getLon17Bits(-77.042793);
        //0b____010_1011_1111_0100
        //0b_1_1001_0010_0110_1110
        //0b11001001_10001110_10111110_01110100

        int res = geoid.getGeoID(-12.046374, -77.042793);
        Assertions.assertEquals(0b11_001001_10001110_10111110_01110100, res);
        Assertions.assertEquals(0b11_001001_10001110_10111110_01110100, geoid.getGeoID(-12.046, -77.043));
        Assertions.assertEquals(0b11_001001_10001110_10111110_01110110, geoid.getGeoID(-12.042, -77.043));
        Assertions.assertEquals(0b11_001001_10001110_10111110_01110101, geoid.getGeoID(-12.046, -77.040));
    }

}
