package com.example.todolist_mvvm.Async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalExecutors implements MultithreadingManager{


    private static LocalExecutors sInstance;
    Handler UIThreadHandler;
    Executor threadPoolExecutor;
    int MAX_CORES;
    int MIN_CORES;

    private LocalExecutors(){
        UIThreadHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };

        MIN_CORES =1;
        MAX_CORES = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor
                = Executors.newSingleThreadExecutor();
    }

    public static LocalExecutors getInstance(){
        synchronized(LocalExecutors.class){
            if(sInstance==null)
                sInstance = new LocalExecutors();
        }
        return sInstance;
    }

    public void runTask(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }

    public void runTaskOnUIThread(Runnable runnable){
        UIThreadHandler.post(runnable);
    }

    @Override
    public void runInBackgroundThread(final RunnableTask t) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                t.run();
            }
        });
    }

    @Override
    public void runOnMainThread(final RunnableTask t) {
        UIThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                t.run();
            }
        });
    }
}
