package com.example.campus_activity.ui.chat

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campus_activity.R
import com.example.campus_activity.data.repository.RoomsRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NewClub : AppCompatActivity() {

    @Inject
    lateinit var roomsRepository: RoomsRepository

    lateinit var clubEdt: EditText
    lateinit var adminEdt: EditText
    lateinit var btnAdd: Button
    private lateinit var editYear: EditText
    private lateinit var editBatch: EditText
    private lateinit var editRoll: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_club)

        clubEdt = findViewById(R.id.club_Name)
        editYear = findViewById(R.id.year_nclub_edit_text)
        editBatch =findViewById(R.id.batch_nclub_edit_text)
        editRoll = findViewById(R.id.roll_nclub_edit_text)
        btnAdd = findViewById(R.id.add_New_Club)

        btnAdd.setOnClickListener {
            adding()
        }

    }

    private fun adding() {
        val clubText: String = clubEdt.text.toString()

        val year :String = editYear.text.toString()
        val batch :String = editBatch.text.toString()
        val roll :String = editRoll.text.toString()

        val admin = rollToMail(year,batch,roll)

        val memberArrayList = ArrayList<String>(3)
        memberArrayList.add("bcs_2019009@iiitm.ac.in")
        memberArrayList.add("bcs_2019021@iiitm.ac.in")
        memberArrayList.add("bcs_2019109@iiitm.ac.in")

        if (clubText.isNotEmpty() && rollToMail(year,batch,roll).isNotEmpty()) {
            try {
                roomsRepository.insertRoom(clubText, admin, memberArrayList)
                Log.i("Rooms", "here")
                finish()
                Log.i("Rooms", "here")
                Toast.makeText(this, "New club added", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Please fill up the fields ", Toast.LENGTH_LONG).show()
        }

    }

    private fun rollToMail(y: String , b : String, r : String): String {
        return (b.toLowerCase(Locale.ROOT).plus("_").plus(y).plus(r).plus("@iiitm.ac.in"))
    }

}

