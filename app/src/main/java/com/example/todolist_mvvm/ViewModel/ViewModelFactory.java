package com.example.todolist_mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist_mvvm.Async.LocalExecutors;
import com.example.todolist_mvvm.Data.Repository.TaskRepository;
import com.example.todolist_mvvm.Util.Injection;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TaskRepository repository;
    private static ViewModelFactory sInstance;

    public static ViewModelFactory getSingleInstance(Application app){
        if(sInstance==null){
            synchronized (ViewModelFactory.class){
                if(sInstance==null)
                    sInstance= new ViewModelFactory(Injection.provideTaskRepository(app));
            }
        }
        return sInstance;
    }


    private  ViewModelFactory(TaskRepository repository){
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(TaskViewModelImpl.class))
            //noinspection unchecked
            return (T) new TaskViewModelImpl(repository, LocalExecutors.getInstance());


        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());

    }
}
