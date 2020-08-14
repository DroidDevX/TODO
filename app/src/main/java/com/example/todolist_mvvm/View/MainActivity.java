package com.example.todolist_mvvm.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.todolist_mvvm.Data.Repository.Model.Task;
import com.example.todolist_mvvm.Data.TaskRecyclerViewAdapters.TaskAdapter;
import com.example.todolist_mvvm.R;
import com.example.todolist_mvvm.ViewModel.TaskViewModel;
import com.example.todolist_mvvm.ViewModel.ViewModelFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskOnClickListener,ViewModelStoreOwner{
    private static final String TAG = "MainActivity";
    TaskAdapter adapter;
    RecyclerView rcView;
    TaskViewModel viewModel;
    TaskAdapter.TaskOnClickListener taskOnClickListener;

    private static String ARGS_TASKS="args-tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(savedInstanceState!=null)
            adapter = new TaskAdapter(savedInstanceState.<Task>getParcelableArrayList(ARGS_TASKS),this);


        taskOnClickListener = this;
        ViewModelProvider provider = new ViewModelProvider(this, ViewModelFactory.getSingleInstance(getApplication()));
        viewModel = provider.get(TaskViewModel.class);
        setupLiveDataObservers(viewModel);
        viewModel.getTasks();

        setupRecyclerView();
    }

    public void setupLiveDataObservers(TaskViewModel viewModel){
        viewModel.getLiveData_Tasks().observe(this, new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(ArrayList<Task> tasks) {
                if(tasks!=null)
                    Log.e(TAG,"getLiveData_Tasks()->onChanged()-> Tasks: "+ tasks.toString());
                if(adapter==null) {
                    adapter = new TaskAdapter(tasks, taskOnClickListener);
                    rcView.setAdapter(adapter);
                }
                else {
                    adapter.setTasks(tasks);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void setupRecyclerView(){
        rcView = findViewById(R.id.recyclerView);
        rcView.setAdapter(adapter);
        rcView.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder draggedItem, @NonNull RecyclerView.ViewHolder target) {
                adapter.swapTasks(draggedItem.getAdapterPosition(),target.getAdapterPosition());
                adapter.notifyItemMoved(draggedItem.getAdapterPosition(),target.getAdapterPosition());
                return false;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder swipedItem, int direction) {
                viewModel.deleteTask(adapter.getTasks().get(swipedItem.getAdapterPosition()));
                adapter.removeTask(swipedItem.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        }).attachToRecyclerView(rcView);



    }

    @Override
    public void OnTaskClicked(Task t) {
        Intent i = new Intent(this,TaskDetailActivity.class);
        i.putExtra(TaskDetailActivity.ACTION_DISPLAY_SELECTED_TASK,t);
        startActivityForResult(i,TaskDetailActivity.REQUEST_SHOW_TASK_DETAILS);
    }

    public void onAddTaskClicked(View view){
        Intent i = new Intent(this,AddTaskActivity.class);
        startActivityForResult(i,AddTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(adapter!=null)
            outState.putParcelableArrayList(ARGS_TASKS,adapter.getTasks());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG,"onActivityResult()");

        switch (resultCode){

            case AddTaskActivity.RESULT_TASK_ADDED:
            {
                Log.e(TAG,"Task added");
                viewModel.getTasks();
                break;
            }
            case TaskDetailActivity.RESULT_TASK_UPDATED:{
                Log.e(TAG,"Task updated");
                viewModel.getTasks();
                break;
            }
            case TaskDetailActivity.RESULT_TASK_DELETED:{
                Log.e(TAG,"Task deleted");
                viewModel.getTasks();
                break;
            }
            default:break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}