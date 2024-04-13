package com.example.tasklistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklistapp.activities.AddTaskActivity
import com.example.tasklistapp.adapters.TaskAdapter
import com.example.tasklistapp.databinding.ActivityMainBinding
import com.example.tasklistapp.db.TaskDbHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: TaskDbHelper
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskDbHelper(this)
        taskAdapter = TaskAdapter(db.getAllTasks(), this)

        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = taskAdapter

        binding.addButton.setOnClickListener {
            val i = Intent(this, AddTaskActivity::class.java)
            startActivity(i)
        }

    }

    override fun onResume() {
        super.onResume()
        taskAdapter.refreshData(db.getAllTasks())
    }
}