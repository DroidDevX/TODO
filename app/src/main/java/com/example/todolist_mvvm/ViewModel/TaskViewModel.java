package com.example.todolist_mvvm.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist_mvvm.Data.Repository.Datasource.TaskDatasource;
import com.example.todolist_mvvm.Data.Repository.Model.Task;

import java.util.ArrayList;

/**
 *Returns live data which can be used to observe any operations that
 * took place between the application and a backend datasource ,such as the
 * local SQLite Database or a webserver.
 * **/
public abstract class TaskViewModel extends ViewModel
{
    public static final String EVENT_ADD_TASK_SUCCESS="addTask_Success";
    public static final String EVENT_ADD_TASK_FAIL="addTask_Fail";

    public static final String EVENT_UPDATE_TASK_SUCCESS="updateTask_Success";
    public static final String EVENT_UPDATE_TASK_FAIL="update_Fail";

    public static final String EVENT_DELETE_TASKS_SUCCESS ="deleteTask_Success";
    public static final String EVENT_DELETE_TASKS_FAIL ="deleteTask_Fail";

    public abstract LiveData<ArrayList<Task>> getLiveData_Tasks();

    public abstract  LiveData<String> getLiveData_Events();

    public abstract void getTasks();

    public abstract void addTask(Task t);

    public abstract void deleteTask(Task task);

    public abstract void updateTask(Task t);

}
