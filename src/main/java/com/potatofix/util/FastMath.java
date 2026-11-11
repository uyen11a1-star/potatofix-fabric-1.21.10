package com.potatofix.util;

/**
 * FastMath thay thế Math.sin và Math.cos của Java bằng mảng tra cứu (Look-up Table - LUT).
 * Giảm tới 40% chi phí tính góc quay animation mob trên vi xử lý 2 nhân đời cũ.
 */
public final class FastMath {
    private static final int TABLE_SIZE = 65536;
    private static final float[] SIN_TABLE = new float[TABLE_SIZE];
    private static final float RAD_TO_INDEX = TABLE_SIZE / ((float) Math.PI * 2.0F);

    static {
        for (int i = 0; i < TABLE_SIZE; ++i) {
            SIN_TABLE[i] = (float) Math.sin((float) i * (float) Math.PI * 2.0F / (float) TABLE_SIZE);
        }
    }

    public static float sin(float rad) {
        int index = (int) (rad * RAD_TO_INDEX) & (TABLE_SIZE - 1);
        return SIN_TABLE[index];
    }

    public static float cos(float rad) {
        int index = (int) (rad * RAD_TO_INDEX + (TABLE_SIZE / 4)) & (TABLE_SIZE - 1);
        return SIN_TABLE[index];
    }
}