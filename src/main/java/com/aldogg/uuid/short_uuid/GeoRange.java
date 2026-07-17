package com.aldogg.uuid.short_uuid;

public class GeoRange {
    public static class Range {
        public final int min;
        public final int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * Precalculates the range of packed coordinates that fall
     * within the same "grid cell" at the given bit-resolution.
     *
     * Visualizing the Resolution (The Shift Values)
     * If you are deciding which shift value to pass to this function:
     * ShiftResolution LevelApprox. Grid Size
     * 0 Base Precision~400m
     * 1 Neighborhood~800m
     * 3 Local District~3.2km
     * 5 City Sector~12.8km
     * 7 Region/Metro~50km
     */
    public static Range getRange(int packedCoord, int shift) {
        // 1. Clear the lower 'shift' bits to get the base of the cell
        int base = (packedCoord >> shift) << shift;

        // 2. The min is the base
        int min = base;

        // 3. The max is the base + all lower bits set to 1
        // (1 << shift) - 1 creates a mask of 'shift' ones
        int max = base | ((1 << shift) - 1);

        return new Range(min, max);
    }



}
