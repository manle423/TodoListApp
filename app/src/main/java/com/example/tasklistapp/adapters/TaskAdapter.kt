package com.example.tasklistapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklistapp.R
import com.example.tasklistapp.activities.EditTaskActivity
import com.example.tasklistapp.db.TaskDbHelper
import com.example.tasklistapp.models.Task
import com.example.tasklistapp.models.TaskStatus

class TaskAdapter(private var tasks: List<Task>, context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val db: TaskDbHelper = TaskDbHelper(context)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val editButton: ImageView = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val statusString = when (task.status) {
            TaskStatus.PENDING -> "Pending"
            TaskStatus.IN_PROGRESS -> "In Process"
            TaskStatus.COMPLETED -> "Complete"
        }
        holder.titleTextView.text = task.title
        holder.descriptionTextView.text = task.description
        holder.statusTextView.text = statusString

        holder.editButton.setOnClickListener {
            val i = Intent(holder.itemView.context, EditTaskActivity::class.java).apply {
                putExtra("task_id", task.id)
            }
            holder.itemView.context.startActivity(i)
        }

        holder.deleteButton.setOnClickListener {
            val dlgView = AlertDialog.Builder(holder.itemView.context)
            dlgView.setTitle("Delete Task")
            dlgView.setMessage("Are you sure to delete this task")
            dlgView.setPositiveButton("Delete") { _, _ ->
                db.deleteTaskById(task.id)
                refreshData(db.getAllTasks())
                Toast.makeText(holder.itemView.context, "Task deleted", Toast.LENGTH_SHORT).show()
            }
            dlgView.setNegativeButton("Cancel") { _, _ -> }
            val dlg = dlgView.create()
            dlg.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}