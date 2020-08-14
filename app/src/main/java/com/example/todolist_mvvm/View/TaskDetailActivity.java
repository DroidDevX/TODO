package com.example.todolist_mvvm.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.todolist_mvvm.Data.Repository.Model.Task;
import com.example.todolist_mvvm.R;
import com.example.todolist_mvvm.ViewModel.TaskViewModel;
import com.example.todolist_mvvm.ViewModel.ViewModelFactory;

import java.util.ArrayList;

/**
* Displays a task selected from the main activity
* */
public class TaskDetailActivity extends AppCompatActivity implements ViewModelStoreOwner {
    private static final String TAG = "TaskDetailActivity";
    private static String ARGS_SELECTEDTASK="selectedTask";
    public static String ACTION_DISPLAY_SELECTED_TASK;
    public static final int REQUEST_SHOW_TASK_DETAILS=100;
    public static final int RESULT_TASK_UPDATED=200;
    public static final int RESULT_TASK_DELETED=201;

    Task selectedTask;
    EditText taskTitleTV;
    EditText taskdescriptionTV;
    TextView completedTV;
    CheckBox taskCompletedCB;

    TaskViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskTitleTV = findViewById(R.id.titleETV);
        taskdescriptionTV = findViewById(R.id.taskdescriptionETV);
        completedTV = findViewById(R.id.taskCompletedTV);
        taskCompletedCB = findViewById(R.id.taskCompletedCB);

        if(savedInstanceState!=null) {
            Log.e(TAG,"savedInstanceState() not null...restoring activity state");
            selectedTask = savedInstanceState.getParcelable(ARGS_SELECTEDTASK);
        }
        else if( getIntent().getExtras()!=null) {
            selectedTask = getIntent().getExtras().getParcelable(ACTION_DISPLAY_SELECTED_TASK);
            if(selectedTask!=null)
            Log.e(TAG,"Getting selected task from intent:"+ selectedTask.toString());

        }
        else
            Log.e(TAG,"Error: selected task intent is null");



        ViewModelProvider provider = new ViewModelProvider(this, ViewModelFactory.getSingleInstance(getApplication()));
        viewModel = provider.get(TaskViewModel.class);
        setupLiveDataObservers(viewModel);

    }


    public void setupLiveDataObservers(TaskViewModel viewModel){

        viewModel.getLiveData_Events().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s)
                {
                    case TaskViewModel.EVENT_UPDATE_TASK_SUCCESS:
                        setResult(RESULT_TASK_UPDATED);
                        finish();
                        break;
                    case TaskViewModel.EVENT_DELETE_TASKS_SUCCESS:
                        setResult(RESULT_TASK_DELETED);
                        finish();
                        break;

                    case TaskViewModel.EVENT_UPDATE_TASK_FAIL:
                        Toast.makeText(getApplicationContext(),"Update failed", Toast.LENGTH_SHORT).show();
                        break;
                    case TaskViewModel.EVENT_DELETE_TASKS_FAIL:
                        Toast.makeText(getApplicationContext(),"Deletion failed", Toast.LENGTH_SHORT).show();

                        break;

                    default:break;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(selectedTask!=null) {
            bindTaskToViews(selectedTask);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void bindTaskToViews(Task task){
        Log.e(TAG,"bindTaskToViews:"+task.toString());
        taskTitleTV.setText(task.getTitle());
        taskdescriptionTV.setText(task.getDescription());
        if(task.isCompleted())
            completedTV.setText("Task completed");
        else
            completedTV.setText("Task incomplete");

        if(selectedTask.isCompleted())
            taskCompletedCB.setChecked(true);
        else
            taskCompletedCB.setChecked(false);
    }

    public void onTaskCompletedCBClicked(View view){
        Log.e(TAG,"onTaskCompletedCBClicked");
        CheckBox taskCompletedCB = (CheckBox)view;
        if(taskCompletedCB.isChecked()) {
            completedTV.setText("Task completed");
            selectedTask.setCompleted(true);
        }
        else {
            completedTV.setText("Task incomplete");
            selectedTask.setCompleted(false);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARGS_SELECTEDTASK,selectedTask);
        super.onSaveInstanceState(outState);

    }

    public void onTaskDelete(View view){
        viewModel.deleteTask(selectedTask);
    }

    public void onTaskUpdate(View view){
        selectedTask.setTitle(taskTitleTV.getText().toString());
        selectedTask.setTitle(taskdescriptionTV.getText().toString());
        if(taskCompletedCB.isChecked())
            selectedTask.setCompleted(true);
        else
            selectedTask.setCompleted(false);
        viewModel.updateTask(selectedTask);
    }
}
