package com.example.todolist_mvvm.View;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.todolist_mvvm.Data.Repository.Model.Task;
import com.example.todolist_mvvm.R;
import com.example.todolist_mvvm.ViewModel.TaskViewModel;
import com.example.todolist_mvvm.ViewModel.ViewModelFactory;

public class AddTaskActivity extends AppCompatActivity implements ViewModelStoreOwner {

    public final static int REQUEST_ADD_TASK =103;
    public final static int RESULT_TASK_ADDED=203;
    TaskViewModel viewModel;

    EditText taskTitleETV;
    EditText taskDescriptionETV;
    Task task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskTitleETV = findViewById(R.id.titleETV);
        taskDescriptionETV = findViewById(R.id.taskdescriptionETV);

        ViewModelProvider provider = new ViewModelProvider(this, ViewModelFactory.getSingleInstance(getApplication()));
        viewModel = provider.get(TaskViewModel.class);

        setupLiveDataObservers(viewModel);

    }

    public boolean fieldsEmpty(){
        return (taskDescriptionETV.getText().length()==0||taskDescriptionETV.getText().length()==0);
    }

    public void setupLiveDataObservers(TaskViewModel viewModel){

        viewModel.getLiveData_Events().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s)
                {
                    case TaskViewModel.EVENT_ADD_TASK_SUCCESS:
                        setResult(RESULT_TASK_ADDED);
                        finish();
                        break;

                    case TaskViewModel.EVENT_ADD_TASK_FAIL:
                            Toast.makeText(getApplicationContext(),"Failed to add task",Toast.LENGTH_SHORT).show();
                            break;

                    default:break;
                }

            }
        });
    }

    public void onAddTaskClicked(View view) {
        if (fieldsEmpty())
            Toast.makeText(this, "Please ensure that all fields are completed", Toast.LENGTH_SHORT).show();
        else{
            task = new Task(-1,taskTitleETV.getText().toString(),taskDescriptionETV.getText().toString());
            viewModel.addTask(task);
        }
    }
}
