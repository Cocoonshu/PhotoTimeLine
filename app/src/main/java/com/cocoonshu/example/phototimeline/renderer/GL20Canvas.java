package com.cocoonshu.example.phototimeline.renderer;

import android.graphics.Color;
import android.opengl.GLES20;

import com.cocoonshu.example.phototimeline.utils.ColorUtils;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * GLCanvas instance implemented by OpenGLES 20
 * @Author Cocoonshu
 * @date   2017-04-24
 */
public class GL20Canvas implements GLCanvas {

    private EGLConfig mEGLConfig  = null;
    private int       mClearColor = 0x00000000;

    public GL20Canvas(EGLConfig config) {
        mEGLConfig = config;
    }

    @Override
    public void setCanvasSize(int width, int height) {

    }

    @Override
    public void clearScreen() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void clearScreen(int color) {
        if (mClearColor != color) {
            mClearColor = color;
            GLES20.glClearColor(
                    ColorUtils.getRedF(mClearColor),
                    ColorUtils.getGreenF(mClearColor),
                    ColorUtils.getBlurF(mClearColor),
                    ColorUtils.getAlphaF(mClearColor));
        }
        clearScreen();
    }

}
