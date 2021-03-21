package com.example.campus_activity.ui.chat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.campus_activity.R
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.repository.RoomsRepository
import com.example.campus_activity.data.viewmodels.RoomsViewModel
import com.example.campus_activity.ui.handler.ImageHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NewClub : AppCompatActivity() {

    @Inject
    lateinit var roomsRepository: RoomsRepository
    @Inject
    lateinit var imageHelper: ImageHelper

    private val roomsViewModel : RoomsViewModel by viewModels()

    lateinit var clubEdt: EditText
    lateinit var btnAdd: Button
    private lateinit var editYear: EditText
    private lateinit var editBatch: EditText
    private lateinit var editRoll: EditText
    lateinit var addLogo: ImageView

//    val pickImage = 100
//    val CROP_PIC = 200
    private var imageUri : Uri? = null
    private var downloadUri : Uri? = null

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
            val intent = imageHelper.pickImage()
            startActivityForResult(intent, imageHelper.PICK_IMAGE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == imageHelper.PICK_IMAGE) {
            imageUri = data?.data
            try {
                val cropIntent = imageHelper.cropImage(imageUri!!, 1, 1)
                startActivityForResult(cropIntent, imageHelper.CROP_IMAGE)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        else if(resultCode == RESULT_OK && requestCode == imageHelper.CROP_IMAGE){
            try {
                imageUri = data?.data
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                addLogo.setImageBitmap(bitmap)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun adding() {
        val clubText: String = clubEdt.text.toString().toUpperCase(Locale.ROOT)

        val year :String = editYear.text.toString()
        val batch :String = editBatch.text.toString()
        val roll :String = editRoll.text.toString()

        val admin = rollToMail(year,batch,roll)

        val memberArrayList = ArrayList<String>()

        if (clubText.isNotEmpty() && rollToMail(year,batch,roll).isNotEmpty()) {
            try {
                uploadImage(clubText, admin, memberArrayList)
//                roomsRepository.insertImage(clubText, clubText.toUpperCase(Locale.ROOT), admin, memberArrayList, imageUri!!)
//                finish()
//                Toast.makeText(this, "New club added", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Please fill up the fields ", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadImage(name:String, rollNmber:String, members:ArrayList<String>){
        if(imageUri != null){
            roomsViewModel.uploadImage(name, imageUri!!).observe(this, {
                when (it) {
                    is Result.Progress -> {
                        Toast.makeText(this, "Upload In Progress. . .", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        Toast.makeText(this, "Image Uploaded !", Toast.LENGTH_SHORT).show()
                        downloadUri = it.result
                        Log.i("Download", downloadUri.toString())
                        uploadClub(name, rollNmber, members)
                    }
                    is Result.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "This shouldn't have happened!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        else{
            uploadClub(name, rollNmber, members)
        }
    }

    private fun uploadClub(name:String, rollNmber:String, members:ArrayList<String>){
        roomsViewModel.uploadClub(name, rollNmber, members, downloadUri)
        finish()
        Toast.makeText(this, "Club Added!", Toast.LENGTH_SHORT).show()
    }

    private fun rollToMail(y: String , b : String, r : String): String {
        return (b.toLowerCase(Locale.ROOT).plus("_").plus(y).plus(r).plus("@iiitm.ac.in"))
    }

}

