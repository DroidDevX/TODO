package com.example.todolist_mvvm.Async;


/**
 * Subclasses provide their own implementation on how to perform work on the background thread
 * and also on the main thread
 * */
public interface MultithreadingManager {

    public interface RunnableTask{
        void run();
    }

    void runInBackgroundThread(RunnableTask t);
    void runOnMainThread(RunnableTask t);
}
