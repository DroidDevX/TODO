package com.example.todolist_mvvm.Util;

import android.app.Application;

import com.example.todolist_mvvm.Data.Repository.Datasource.TaskSQLDatabase;
import com.example.todolist_mvvm.Data.Repository.TaskRepository;
import com.example.todolist_mvvm.Data.Repository.TaskRepositoryImpl;

public  class Injection {

    private static TaskSQLDatabase provideTaskDatabase(Application application){
        return new TaskSQLDatabase(application.getApplicationContext(),"TaskDB",null,1);
    }

    public static TaskRepository provideTaskRepository(Application app){
        return new TaskRepositoryImpl(provideTaskDatabase(app));
    }

}
