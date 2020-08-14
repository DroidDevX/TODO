package com.example.todolist_mvvm.Data.Repository.Datasource;

import com.example.todolist_mvvm.Data.Repository.Model.Task;

import java.util.ArrayList;

public interface TaskDatasource {

    ArrayList<Task> getTasks();

    int addTask(Task t);

    int deleteTask(Task t);

    int updateTask(Task t);

}
