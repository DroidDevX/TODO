package com.example.todolist_mvvm.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist_mvvm.Async.MultithreadingManager;
import com.example.todolist_mvvm.Async.LocalExecutors;
import com.example.todolist_mvvm.Data.Repository.Model.Task;
import com.example.todolist_mvvm.Data.Repository.TaskRepository;

import java.util.ArrayList;

public class TaskViewModelImpl extends TaskViewModel {
    private static final String TAG = "TaskViewModelImpl";



    TaskRepository repository;
    MutableLiveData<ArrayList<Task>> tasksLivedata;
    MutableLiveData<String> eventsLiveData;
    MultithreadingManager multithreading;

    public TaskViewModelImpl(TaskRepository repository,
                             MultithreadingManager multithreadingManager
                             ) {
        this.repository = repository;
        this.tasksLivedata = new MutableLiveData<>();
        this.eventsLiveData = new MutableLiveData<>();
        this.multithreading = multithreadingManager;
    }

    @Override
    public LiveData<ArrayList<Task>> getLiveData_Tasks() {
        return tasksLivedata;
    }

    @Override
    public LiveData<String> getLiveData_Events() {
        return eventsLiveData;
    }

    @Override
    public void getTasks() {
        Log.e(TAG,"getTasks()");
        multithreading.runInBackgroundThread(new MultithreadingManager.RunnableTask() {
            @Override
            public void run() {
                final ArrayList<Task> tasks = repository.getTasks();
                multithreading.runOnMainThread(new MultithreadingManager.RunnableTask() {
                    @Override
                    public void run() {
                        tasksLivedata.setValue(tasks);
                    }
                });
            }
        });
    }

    @Override
    public void addTask(final Task t) {
        multithreading.runInBackgroundThread(new MultithreadingManager.RunnableTask() {
            @Override
            public void run() {
                final int rowsAffected = repository.addTask(t);
                multithreading.runOnMainThread(new MultithreadingManager.RunnableTask() {
                    @Override
                    public void run() {
                        if(rowsAffected >0)
                            eventsLiveData.setValue(EVENT_ADD_TASK_SUCCESS);
                        else
                            eventsLiveData.setValue(EVENT_ADD_TASK_FAIL);
                    }
                });

            }
        });
    }


    @Override
    public void deleteTask(final Task task)
    {
        multithreading.runInBackgroundThread(new MultithreadingManager.RunnableTask() {
            @Override
            public void run() {
                final int rowsAffected = repository.deleteTask(task);
                multithreading.runOnMainThread(new MultithreadingManager.RunnableTask() {
                    @Override
                    public void run() {
                        if(rowsAffected >0)
                            eventsLiveData.setValue(EVENT_DELETE_TASKS_SUCCESS);
                        else
                            eventsLiveData.setValue(EVENT_DELETE_TASKS_FAIL);
                    }
                });
            }
        });
    }

    @Override
    public void updateTask(final Task t) {
        multithreading.runInBackgroundThread(new MultithreadingManager.RunnableTask() {
            @Override
            public void run() {
                final int rowsAffected = repository.updateTask(t);
                multithreading.runOnMainThread(new MultithreadingManager.RunnableTask() {
                    @Override
                    public void run() {
                        if(rowsAffected ==1)
                            eventsLiveData.setValue(EVENT_UPDATE_TASK_SUCCESS);
                        else
                            eventsLiveData.setValue(EVENT_UPDATE_TASK_FAIL);
                    }
                });

            }
        });
    }
}
