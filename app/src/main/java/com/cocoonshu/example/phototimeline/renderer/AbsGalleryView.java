package com.cocoonshu.example.phototimeline.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.TextureView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Author Cocoonshu
 * @Date   2017-04-13
 */
public abstract class AbsGalleryView extends GLSurfaceView implements GLSurfaceView.Renderer {

    public AbsGalleryView(Context context) {
        this(context, null);
    }

    public AbsGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(false);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

}
