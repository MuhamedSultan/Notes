package com.example.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.note_layout.view.*

class NoteAdapter(context : Context,notelist  : ArrayList<Note>)
    : ArrayAdapter<Note>(context,0,notelist) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      val viwe =  LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false)
        val note : Note? = getItem(position)
        if (note != null) {
            viwe.title_textview.text=note.title
            viwe.time_textView.text=note.timestamp.toString()
        }

        return viwe
    }
}