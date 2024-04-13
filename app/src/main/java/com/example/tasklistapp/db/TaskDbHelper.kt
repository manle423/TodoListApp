package com.example.tasklistapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tasklistapp.models.Task
import com.example.tasklistapp.models.TaskStatus

class TaskDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "todo_list_app_db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "tasks"
        private const val id_col = "id"
        private const val title_col = "title"
        private const val description_col = "description"
        private const val status_col = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTaskTable = "CREATE TABLE $TABLE_NAME" +
                "($id_col INTEGER PRIMARY KEY," +
                "$title_col TEXT," +
                "$description_col TEXT," +
                "$status_col TEXT CHECK ($status_col IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')))"
        db?.execSQL(createTaskTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTaskTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTaskTable)
        onCreate(db)
    }

    //#region Insert Task
    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(title_col, task.title)
            put(description_col, task.description)
            put(status_col, task.status.name)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    //#endregion

    //#region Get All Tasks
    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(id_col))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(title_col))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(description_col))
            // Chuyển đổi chuỗi trạng thái thành enum TaskStatus
            val status = when (val statusString =
                cursor.getString(cursor.getColumnIndexOrThrow(status_col))) {
                TaskStatus.PENDING.name -> TaskStatus.PENDING
                TaskStatus.IN_PROGRESS.name -> TaskStatus.IN_PROGRESS
                TaskStatus.COMPLETED.name -> TaskStatus.COMPLETED
                else -> throw IllegalArgumentException("Invalid task status: $statusString")
            }

            val task = Task(id, title, description, status)
            taskList.add(task)
        }
        cursor.close()
        db.close()
        return taskList
    }
    //#endregion

    //#region Edit Task
    fun editTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(title_col, task.title)
            put(description_col, task.description)
            put(status_col, task.status.name)
        }
        val whereClause = "$id_col = ?"
        val whereArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
    //#endregion

    //#region Get Task By Id
    @SuppressLint("Recycle")
    fun getTaskById(taskId: Int): Task {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $id_col = $taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(id_col))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(title_col))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(description_col))
        val status = when (val statusString =
            cursor.getString(cursor.getColumnIndexOrThrow(status_col))) {
            TaskStatus.PENDING.name -> TaskStatus.PENDING
            TaskStatus.IN_PROGRESS.name -> TaskStatus.IN_PROGRESS
            TaskStatus.COMPLETED.name -> TaskStatus.COMPLETED
            else -> throw IllegalArgumentException("Invalid task status: $statusString")
        }

        cursor.close()
        db.close()
        return Task(id, title, description, status)
    }
    //#endregion

    //#region Delete Task
    fun deleteTaskById(taskId: Int) {
        val db = writableDatabase
        val whereClause = "$id_col = ?"
        val whereArgs = arrayOf(taskId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
    //#endregion

}