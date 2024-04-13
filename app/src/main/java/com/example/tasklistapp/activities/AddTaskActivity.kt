package com.example.tasklistapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasklistapp.databinding.ActivityAddTaskBinding
import com.example.tasklistapp.db.TaskDbHelper
import com.example.tasklistapp.models.Task
import com.example.tasklistapp.models.TaskStatus

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TaskDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskDbHelper(this)

        binding.saveButton.setOnClickListener{
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            if (title.isBlank()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            } else {
                val task = Task(0, title, description, TaskStatus.PENDING)
                db.insertTask(task)
                finish()
                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
            }
        }

    }
}