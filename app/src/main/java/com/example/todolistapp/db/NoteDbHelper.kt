package com.example.todolistapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolistapp.models.Note

class NoteDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todo_list_app_db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "notes"
        private const val id_col = "id"
        private const val title_col = "title"
        private const val content_col = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createNoteTable = "CREATE TABLE $TABLE_NAME" +
                "($id_col INTEGER PRIMARY KEY," +
                "$title_col TEXT," +
                "$content_col TEXT)"
        db?.execSQL(createNoteTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropNoteTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropNoteTable)
        onCreate(db)
    }

    //#region Insert note
    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(title_col, note.title)
            put(content_col, note.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    //#endregion

    //#region Get all notes
    fun getAllNotes(): List<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(id_col))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(title_col))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(content_col))

            val note = Note(id, title, content)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }
    //#endregion

    //#region Update note
    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(title_col, note.title)
            put(content_col, note.content)
        }
        val whereClause = "$id_col = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
    //#endregion

    //#region Get note by id
    @SuppressLint("Recycle")
    fun getNoteById(noteId: Int) : Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $id_col = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(id_col))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(title_col))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(content_col))

        cursor.close()
        db.close()
        return Note(id, title, content)
    }
    //#endregion

    //#region Delete note
    fun deleteNoteById(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$id_col = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
    //#endregion


}