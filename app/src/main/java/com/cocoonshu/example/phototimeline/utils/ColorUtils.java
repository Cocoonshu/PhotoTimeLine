package com.cocoonshu.example.phototimeline.utils;

/**
 * @Author Cocoonshu
 * @Date   2017-04-13
 */
public class ColorUtils {

    public static float getAlphaF(int color) {
        return ((color & 0xFF000000) >> 24) / 255f;
    }

    public static float getRedF(int color) {
        return ((color & 0x00FF0000) >> 16) / 255f;
    }

    public static float getGreenF(int color) {
        return ((color & 0x0000FF00) >> 8) / 255f;
    }

    public static float getBlurF(int color) {
        return ((color & 0x000000FF)) / 255f;
    }

    public static int toAlphaX(float alpha) {
        return (((int) (alpha * 255f)) & 0xFF) << 24;
    }

    public static int toRedX(float red) {
        return (((int) (red * 255f)) & 0xFF) << 16;
    }

    public static int toGreenX(float green) {
        return (((int) (green * 255f)) & 0xFF) << 8;
    }

    public static int toBlueX(float blue) {
        return (((int) (blue * 255f)) & 0xFF);
    }

}
