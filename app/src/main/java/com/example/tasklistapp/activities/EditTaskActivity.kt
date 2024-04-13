package com.example.tasklistapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasklistapp.R
import com.example.tasklistapp.adapters.TaskAdapter
import com.example.tasklistapp.databinding.ActivityEditTaskBinding
import com.example.tasklistapp.db.TaskDbHelper
import com.example.tasklistapp.models.Task
import com.example.tasklistapp.models.TaskStatus

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var db: TaskDbHelper
    private var taskID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskDbHelper(this)

        taskID = intent.getIntExtra("task_id", -1)
        if (taskID == -1) {
            finish()
            return
        }

        val task = db.getTaskById(taskID)
        binding.editTitleEditText.setText(task.title)
        binding.editDescriptionEditText.setText(task.description)
        //check status in DB
        when (task.status) {
            TaskStatus.PENDING -> binding.pendingRadioButton.isChecked = true
            TaskStatus.IN_PROGRESS -> binding.inProgressRadioButton.isChecked = true
            TaskStatus.COMPLETED -> binding.completedRadioButton.isChecked = true
        }
        binding.editSaveButton.setOnClickListener {
            val newTitle = binding.editTitleEditText.text.toString()
            val newDescription = binding.editDescriptionEditText.text.toString()

            val newStatus = when (binding.statusRadioGroup.checkedRadioButtonId) {
                R.id.pendingRadioButton -> TaskStatus.PENDING
                R.id.inProgressRadioButton -> TaskStatus.IN_PROGRESS
                R.id.completedRadioButton -> TaskStatus.COMPLETED
                else -> task.status
            }

            val editedTask = Task(taskID, newTitle, newDescription, newStatus)

            db.editTask(editedTask)

            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }


    }


}