package com.example.todolist_mvvm.Data.Repository;

import com.example.todolist_mvvm.Data.Repository.Model.Task;

import java.util.ArrayList;

public class DummyTaskRepository implements TaskRepository {

    @Override
    public ArrayList<Task> getTasks() {
        return null;
    }

    @Override
    public int addTask(Task t) {
        return 0;
    }

    @Override
    public int deleteTask(Task t) {
        return 0;
    }

    @Override
    public int updateTask(Task t) {
        return 0;
    }
}
