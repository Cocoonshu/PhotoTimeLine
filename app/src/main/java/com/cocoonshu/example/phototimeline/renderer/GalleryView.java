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

    public GalleryView(Context context) {
        super(context);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

}
