package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note.*
import kotlinx.android.synthetic.main.add_note.view.*
import kotlinx.android.synthetic.main.delete.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent as Intent

class MainActivity : AppCompatActivity() {
    var mRef : DatabaseReference?=null
    var mnotelist : ArrayList<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance()
        mRef = database.getReference("Notes")
        mnotelist = ArrayList()

        add_note.setOnClickListener {
            showDialogeAddNote()

        }

        list_view.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var mynotes = mnotelist?.get(position)
                var title = mynotes?.title
                var note = mynotes?.note
                var noteIntent = Intent(this,Note_Activity::class.java)
                noteIntent.putExtra("Note title",title)
                noteIntent.putExtra("Note details",note)
                startActivity(noteIntent)

            }
        list_view.onItemLongClickListener=
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val alertbuilder   = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.delete,null)
                alertbuilder.setView(view)
            val alertdialoge = alertbuilder.create()
            alertdialoge.show()

                var mynotes = mnotelist?.get(position)
                val note_title = mynotes?.title
                val note_details = mynotes?.note
              view.title_delete.setText(note_title)
              view.note_delete.setText(note_details)

                view.Update.setOnClickListener {

                    var chiledRef = mRef?.child(mynotes?.id.toString())
                    var title = view.title_delete.text.toString()
                    var note = view.note_delete.text.toString()
                    val afterUpdate = Note(mynotes?.id!!,title ,note,getCurrentDate())
                    chiledRef?.setValue(afterUpdate)
                    alertdialoge.dismiss()
                }
                view.Delete.setOnClickListener {
                    mRef?.child(mynotes?.id.toString())?.removeValue()
                    alertdialoge.dismiss()
                }
                false
            }
    }
      override fun  onStart() {
          super.onStart()
          mRef?.addValueEventListener(object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                  mnotelist?.clear()
                  for (n in snapshot!!.children) {
                      var note = n.getValue(Note::class.java)
                      mnotelist?.add(note!!)
                  }
                  val noteadapter = NoteAdapter(applicationContext, mnotelist!!)
                  list_view.adapter=noteadapter
              }

              override fun onCancelled(error: DatabaseError) {
                  TODO("Not yet implemented")
              }

          })

      }


    fun showDialogeAddNote (){
        val alertbulder = AlertDialog.Builder(this)
        val view =layoutInflater.inflate(R.layout.add_note,null)
        alertbulder.setView(view)
        val alertDialog = alertbulder.create()
        alertDialog.show()

        view.add.setOnClickListener {
            var title = view.title_edit_text.text.toString()
            var note = view.note_edit_text.text.toString()
            if (title.isNotEmpty() && note.isNotEmpty()){
                var id = mRef!!.push().key
                val mynotes = Note(id.toString(),title,note,getCurrentDate())
                    mRef!!.child(id.toString()).setValue(mynotes)
                alertDialog.dismiss()
            }else {
               var toast = Toast.makeText(this,"Title and Deatails Required",Toast.LENGTH_SHORT)
                toast.show()

            }
        }

    }
    fun getCurrentDate(): String {
        val calender =Calendar.getInstance()
        val midformat = SimpleDateFormat("EEEE hh:mm a")
        val strDate = midformat.format(calender.time)
        return strDate
    }

}