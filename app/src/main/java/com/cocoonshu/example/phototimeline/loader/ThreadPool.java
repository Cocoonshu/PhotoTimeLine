package com.cocoonshu.example.phototimeline.loader;

import android.os.HandlerThread;

import com.cocoonshu.example.phototimeline.utils.DataUtils;
import com.cocoonshu.example.phototimeline.utils.Debugger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @Author Cocoonshu
 * @Date   2017-05-02
 */
public class ThreadPool {
    private static final String TAG                             = "ThreadPool";
    private static final String SERIAL_RELOAD_HANDLER_THREAD    = "SerialReloadHandlerThread";
    private static final String CONCURRENT_WORK_THREAD          = "ConcurrentWorkThread";
    private static final int    CONCURRENT_WORK_THREAD_PROPERTY = android.os.Process.THREAD_PRIORITY_BACKGROUND;
    private static final int    DEFAULT_WORK_THREAD_COUNT       = 8;

    private HandlerThread   mSerialReloadHandlerThread = null;
    private ExecutorService mWorkExecutors             = null;

    public ThreadPool() {

    }

    public void setup() {
        startSerialReloadHandlerThread();
    }

    public void resume() {
        startWorkExecutor();
    }

    public void pause() {
        stopWorkExecutor();
    }

    public void terminal() {
        stopSerialReloadHandlerThread();
    }

    public void startSerialReloadHandlerThread() {
        if (mSerialReloadHandlerThread == null) {
            mSerialReloadHandlerThread = new HandlerThread(SERIAL_RELOAD_HANDLER_THREAD);
        }
        mSerialReloadHandlerThread.start();
    }

    public void stopSerialReloadHandlerThread() {
        if (mSerialReloadHandlerThread != null) {
            mSerialReloadHandlerThread.quitSafely();
            mSerialReloadHandlerThread = null;
        }
    }

    public void startWorkExecutor() {
        if (mWorkExecutors == null || mWorkExecutors.isShutdown() || mWorkExecutors.isTerminated()) {
            int threadSize = DataUtils.getSuggestionThreadSize(DEFAULT_WORK_THREAD_COUNT);
            mWorkExecutors = Executors.newFixedThreadPool(threadSize, new WorkThreadFactory());
            Debugger.i(TAG, "[startWorkExecutor] work thread size = " + threadSize);
        }
    }

    public void stopWorkExecutor() {
        if (mWorkExecutors != null) {
            Debugger.i(TAG, "[stopWorkExecutor]");
            mWorkExecutors.shutdown();
            mWorkExecutors = null;
        }
    }

    public HandlerThread getSerialReloadHandlerThread() {
        return mSerialReloadHandlerThread;
    }

    public ExecutorService getWorkExecutor() {
        return mWorkExecutors;
    }

    /**
     * Work executor thread factory
     */
    private class WorkThreadFactory implements ThreadFactory {

        private Thread.UncaughtExceptionHandler mCrashHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                Debugger.e(TAG, "[uncaughtException] Work thread " + thread + " crashed!", e);
            }
        };

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(CONCURRENT_WORK_THREAD);
            thread.setPriority(CONCURRENT_WORK_THREAD_PROPERTY);
            thread.setUncaughtExceptionHandler(mCrashHandler);
            return thread;
        }

    }
}
