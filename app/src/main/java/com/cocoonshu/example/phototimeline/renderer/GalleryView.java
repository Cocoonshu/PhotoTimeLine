package com.cocoonshu.example.phototimeline.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Author Cocoonshu
 * @Date   2017-04-13
 */
public class GalleryView extends AbsGalleryView {

    private GLCanvas mGLCanvas = null;

    public GalleryView(Context context) {
        super(context);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mGLCanvas = new AdvanceGL20Canvas(config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mGLCanvas.setCanvasSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mGLCanvas.clearScreen(0xFFF5F5F5);
    }

}
