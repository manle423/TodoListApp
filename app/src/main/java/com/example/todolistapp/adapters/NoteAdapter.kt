package com.example.todolistapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.activities.UpdateNoteActivity
import com.example.todolistapp.db.NoteDbHelper
import com.example.todolistapp.models.Note

class NoteAdapter(private var notes: List<Note>, context: Context) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val db: NoteDbHelper = NoteDbHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.updateButton.setOnClickListener {
            val i = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(i)
        }

        holder.deleteButton.setOnClickListener {
            val dlgView = AlertDialog.Builder(holder.itemView.context)
            dlgView.setTitle("Delete Note")
            dlgView.setMessage("Are you sure to delete this note")
            dlgView.setPositiveButton("Delete") { _, _ ->
                db.deleteNoteById(note.id)
                refreshData(db.getAllNotes())
                Toast.makeText(holder.itemView.context, "Note deleted", Toast.LENGTH_SHORT).show()
            }
            dlgView.setNegativeButton("Cancel") { _, _ -> }
            val dlg = dlgView.create()
            dlg.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}