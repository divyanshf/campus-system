package com.example.campus_activity.ui.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.campus_activity.R
import com.example.campus_activity.data.repository.RoomsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NewClub : AppCompatActivity() {

    @Inject
    lateinit var roomsRepository: RoomsRepository

    lateinit var clubEdt: EditText
    lateinit var btnAdd: Button
    private lateinit var editYear: EditText
    private lateinit var editBatch: EditText
    private lateinit var editRoll: EditText
    lateinit var addLogo: ImageView

    val pickImage = 100
    private var imageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_club)

        clubEdt = findViewById(R.id.club_Name)
        editYear = findViewById(R.id.year_nclub_edit_text)
        editBatch =findViewById(R.id.batch_nclub_edit_text)
        editRoll = findViewById(R.id.roll_nclub_edit_text)
        btnAdd = findViewById(R.id.add_New_Club)
        addLogo = findViewById(R.id.add_logo_button)

        btnAdd.setOnClickListener {
            adding()
        }
        addLogo.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            addLogo.setImageURI(imageUri)
        }
    }

    private fun adding() {
        val clubText: String = clubEdt.text.toString()

        val year :String = editYear.text.toString()
        val batch :String = editBatch.text.toString()
        val roll :String = editRoll.text.toString()

        val admin = rollToMail(year,batch,roll)

        val memberArrayList = ArrayList<String>()

        if (clubText.isNotEmpty() && rollToMail(year,batch,roll).isNotEmpty()) {
            try {
                roomsRepository.insertRoom(clubText.toUpperCase(Locale.ROOT), admin, memberArrayList)
                finish()
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

