package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_note.*

class Note_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        var noteintent = intent
       var notetitle= noteintent.getStringExtra("Note title")
        var notedetails=  noteintent.getStringExtra("Note details")

        Note_title.setText(notetitle)
        Note_details.setText(notedetails)
    }
}