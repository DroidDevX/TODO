package com.example.todolist_mvvm.Data.Repository.Datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolist_mvvm.Data.Repository.Model.Task;

import java.util.ArrayList;

public class TaskSQLDatabase extends SQLiteOpenHelper implements TaskDatasource {

    public TaskSQLDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String query ="CREATE TABLE \"TASK\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"TITLE\"\tTEXT NOT NULL,\n" +
                "\t\"DESCRIPTION\"\tTEXT NOT NULL,\n" +
                "\t\"COMPLETED\"\tINTEGER NOT NULL\n" +
                ");";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    @Override
    public ArrayList<Task> getTasks() {
        String [] selectedColumns =null;//Null just means all columns
        String tableName ="TASK";
        String WHERE_CLAUSE =null;
        String[] WHERE_ARGS =null;
        String sortOrder =null;//default is ascending

        Cursor c =getReadableDatabase().query(tableName,selectedColumns,WHERE_CLAUSE,WHERE_ARGS,null,null,sortOrder);
        boolean atLeastOneRecordSelected = c.moveToFirst();

        ArrayList<Task> requestedTasks = null;
        if(atLeastOneRecordSelected){
            requestedTasks = new ArrayList<>();
            do {
                //Conversion;
                int taskID = c.getInt(0);
                String title = c.getString(1);
                String description = c.getString(2);
                int isCompleted =  c.getInt(3);
                Task t = new Task(taskID,title,description);
                if(isCompleted==1)
                    t.setCompleted(true);
                else
                    t.setCompleted(false);
                requestedTasks.add(t);
            }while (c.moveToNext());
        }
        c.close();
        close();

        return requestedTasks;
    }

    @Override
    public int addTask(Task t) {
        ContentValues values = new ContentValues();
        values.put("TITLE",t.getTitle());
        values.put("DESCRIPTION",t.getDescription());

        int completed;
        if (t.isCompleted())
            completed=1;
        else
            completed=0;
        values.put("COMPLETED",completed);

        int numRowsInserted =(int)getWritableDatabase().insert("TASK",null,values);

        close();
        return numRowsInserted;
    }


    @Override
    public int deleteTask(Task t) {
        String tableName ="TASK";
        String WHERE_CLAUSE ="ID=?";
        String[] WHERE_ARGS =new String[]{String.valueOf(t.getId())};
        return getWritableDatabase().delete(tableName,WHERE_CLAUSE,WHERE_ARGS);
    }

    @Override
    public int updateTask(Task t) {
        ContentValues values = new ContentValues();
        values.put("TITLE",t.getTitle());
        values.put("DESCRIPTION",t.getDescription());

        int completed;
        if (t.isCompleted())
            completed=1;
        else
            completed=0;
        values.put("COMPLETED",completed);

        String dbTableName ="TASK";
        String WHERE ="ID=?";
        String []WHERE_ARGS=new String[]{String.valueOf(t.getId())};

        return getWritableDatabase().update(dbTableName,values,WHERE,WHERE_ARGS);

    }
}
