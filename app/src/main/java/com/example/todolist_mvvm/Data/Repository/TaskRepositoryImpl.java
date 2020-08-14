package com.example.todolist_mvvm.Data.Repository;

import com.example.todolist_mvvm.Data.Repository.Datasource.TaskDatasource;
import com.example.todolist_mvvm.Data.Repository.Model.Task;

import java.util.ArrayList;


public class TaskRepositoryImpl implements TaskRepository {


    TaskDatasource localDatabase;

    public TaskRepositoryImpl(TaskDatasource localDatabase) {
        this.localDatabase = localDatabase;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return localDatabase.getTasks();
    }

    @Override
    public int addTask(Task t) {
        return localDatabase.addTask(t);
    }

    @Override
    public int deleteTask(Task t) {
        return localDatabase.deleteTask(t);
    }

    @Override
    public int updateTask(Task t) {
        return localDatabase.updateTask(t);
    }
}
