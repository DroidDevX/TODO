package com.example.todolist_mvvm.Data.MockData;

import com.example.todolist_mvvm.Data.Repository.Model.Task;

import java.util.ArrayList;

/**
 * Provides mocked data to the application for testing purposes
 * */
public class TaskProvider {

    public static ArrayList<Task> getRandomTasks(){
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Task(1,"Chores","Wash the dishes"));
        tasks.add(new Task(2,"Meet with Darcy","Discuss plans to host birthday part for Mathew"));
        tasks.add(new Task(3,"Chores","Take out the trash"));
        tasks.add(new Task(4,"Buy groceries","Carrots, tomatoes, cabbages, rice"));

        return tasks;
    }
}
