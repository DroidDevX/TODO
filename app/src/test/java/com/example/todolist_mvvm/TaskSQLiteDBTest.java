package com.example.todolist_mvvm;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;

import com.example.todolist_mvvm.Data.Repository.Datasource.TaskSQLDatabase;
import com.example.todolist_mvvm.Data.Repository.Model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;

import java.util.ArrayList;

//Junit and Robolectric dependencies only needed

@RunWith(RobolectricTestRunner.class)
public class TaskSQLiteDBTest {


    private TaskSQLDatabase database;

    @Before
    public void setupTest(){
        String databaseName ="TaskDatabase";
        int dbVersion =1;
        database = new TaskSQLDatabase(ApplicationProvider.getApplicationContext(),databaseName,null,dbVersion);
    }

    @After
    public void setupTestPostConditions(){
        database.close();
    }

    @Test
    public void testInsertTask(){
        Task taskToAdd = new Task(1,"Title","Description");
        int rowsInserted = database.addTask(taskToAdd);
        assertEquals(1,rowsInserted);

    }

    @Test
    public void testGetTasks(){
        //insert first
        Task taskToAdd = new Task(1,"Title","Description");
        database.addTask(taskToAdd);

        //then get
        ArrayList<Task> tasks = database.getTasks();
        assertNotNull(tasks);
        assertEquals(taskToAdd.toString(),tasks.get(0).toString());


    }

    @Test
    public void testDeleteTasks(){
        //insert, get then delete first task
        Task taskToAdd = new Task(1,"Title","Description");
        database.addTask(taskToAdd);

        int rowsDeleted = database.deleteTask(taskToAdd);
        assertEquals(1,rowsDeleted);

        ArrayList<Task> tasks = database.getTasks();
        assertNull(tasks);
    }

    @Test
    public void testUpdateTask(){
        Task originalTask = new Task(1,"Title","Description");
        Task updatedTask = new Task(1,"UpdatedTitle","Description");
        database.addTask(originalTask);

        //update task
        int rowsUpdates = database.updateTask(updatedTask);
        assertEquals(1,rowsUpdates);

        //Get updated task
        ArrayList<Task> tasks = database.getTasks();
        assertEquals(updatedTask.toString(),tasks.get(0).toString());

    }

}
