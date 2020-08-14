package com.example.todolist_mvvm.Data.TaskRecyclerViewAdapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mvvm.Data.Repository.Model.Task;
import com.example.todolist_mvvm.R;

import java.util.ArrayList;
import java.util.Collections;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


    private static final String TAG = "TaskAdapter";
    private static int STATE_NO_TASK_SELECTED=-1;

    public interface TaskOnClickListener{
        void OnTaskClicked(Task t);
    }


    static class TaskViewHolder extends RecyclerView.ViewHolder{

        static class TaskHighlighter {
            int highlightColor;
            int textHighlightColor;

            public TaskHighlighter(int highlightColor, int textHighlightColor) {
                this.highlightColor = highlightColor;
                this.textHighlightColor = textHighlightColor;
            }

            public void highlight( TextView titleTextView){
                titleTextView.setBackgroundColor(highlightColor);
                titleTextView.setTextColor(textHighlightColor);
            }

        }

        TextView titleTextView;
        TextView completedTV;
        View parentView;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentView = itemView;
            titleTextView = parentView.findViewById(R.id.titleTextView);
            completedTV = parentView.findViewById(R.id.completionTextView);
        }

        public void bindData(Task task){
                titleTextView.setText(task.getTitle());
                if(task.isCompleted())
                    completedTV.setText("Task completed");
                else
                    completedTV.setText("Task not completed");
        }
        public void highlightTask(TaskHighlighter highlighter){
            highlighter.highlight(titleTextView);
        }
    }

    ArrayList<Task> tasks;
    TaskViewHolder.TaskHighlighter selectHighlighter;
    TaskViewHolder.TaskHighlighter deselectHighlighter;
    TaskOnClickListener listener;
    int selectedItemPos= STATE_NO_TASK_SELECTED;

    public TaskAdapter(ArrayList<Task> tasks,TaskOnClickListener listener) {
        this.tasks = tasks;
        selectHighlighter = new TaskViewHolder.TaskHighlighter(Color.BLUE,Color.parseColor("#FFFFFF"));
        deselectHighlighter = new TaskViewHolder.TaskHighlighter(Color.WHITE,Color.BLACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new TaskViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder taskViewHolder, final int position) {
        Log.e(TAG,"onBindViewHolder:");

        taskViewHolder.bindData(tasks.get(position));
        taskViewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItemPos==position)
                    selectedItemPos = STATE_NO_TASK_SELECTED;
                else
                    selectedItemPos = position;
                notifyDataSetChanged();

                listener.OnTaskClicked(tasks.get(position));
            }
        });


    }

    public void swapTasks(int firstIndex,int secondIndex){
        Collections.swap(tasks,firstIndex,secondIndex);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void removeTask(int index){
        tasks.remove(index);
    }

    @Override
    public int getItemCount() {
        if(tasks!=null)
            return tasks.size();
        else
            return 0;
    }

    public void setTasks(ArrayList<Task> tasks){
        this.tasks = tasks;
    }
}
