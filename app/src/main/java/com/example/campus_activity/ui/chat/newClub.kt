package com.example.campus_activity.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.campus_activity.R
import com.example.campus_activity.ui.main.MainActivity
import com.example.campus_activity.ui.main.RoomListFragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class newClub : AppCompatActivity() {

    lateinit var clubEdt: EditText
    lateinit var adminEdt: EditText
    lateinit var btnAdd: Button
    private lateinit var editYear: EditText
    private lateinit var editBatch: EditText
    private lateinit var editRoll: EditText
    lateinit var db: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_club)

        db = FirebaseFirestore.getInstance().collection("rooms")


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

        val adminArrayList = ArrayList<String>(1)
        adminArrayList.add(rollToMail(year,batch,roll))

        val memberArrayList = ArrayList<String>(3)
        memberArrayList.add("bcs_2019009@iiitm.ac.in")
        memberArrayList.add("bcs_2019021@iiitm.ac.in")
        memberArrayList.add("bcs_2019109@iiitm.ac.in")

        if (!clubText.isEmpty() && !rollToMail(year,batch,roll).isEmpty()) {
            try {
                val members = hashMapOf<String, ArrayList<String>>()
                members.put("admin", adminArrayList)
                members.put("members", memberArrayList)
                db.document(clubText).set(members).addOnSuccessListener { void: Void? ->
                    Toast.makeText(
                        this,
                        "Successfully uploaded to the database ",
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnFailureListener { exception: java.lang.Exception ->
                    Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                 }
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Please fill up the fields ", Toast.LENGTH_LONG).show()
        }


    }

    private fun rollToMail(y: String , b : String, r : String): String {
        return (b.toLowerCase(Locale.ROOT).plus("_").plus(y).plus(r).plus("@iiitm.ac.in"))
    }


}

