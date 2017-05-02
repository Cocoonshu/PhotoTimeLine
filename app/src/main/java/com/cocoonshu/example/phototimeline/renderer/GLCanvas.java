package com.cocoonshu.example.phototimeline.renderer;

/**
 * GLCanvas interface
 * @Author Cocoonshu
 * @date   2017-04-24
 */
public interface GLCanvas {

    void setCanvasSize(int width, int height);

    public void clearScreen();

    public void clearScreen(int color);

}
