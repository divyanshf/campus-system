package com.example.campus_activity.ui.auth

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.campus_activity.R
import com.example.campus_activity.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RegisterFragment : Fragment()  {

    private lateinit var editYear: EditText
    private lateinit var editBatch: EditText
    private lateinit var editRoll: EditText
    private lateinit var editPassword: EditText
    private lateinit var editConPassword: EditText
    private lateinit var editName: EditText
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var btn: Button
    private lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var database : CollectionReference


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        editYear = view.findViewById(R.id.year_edit_text)
        editName = view.findViewById(R.id.name_edit_text)
        editBatch = view.findViewById(R.id.batch_edit_text)
        editRoll = view.findViewById(R.id.roll_edit_text)
        editPassword = view.findViewById(R.id.password_edit_text)
        editConPassword = view.findViewById(R.id.confirm_password_edit_text)
        fireBaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        database = firebaseFirestore.collection("users")
        btn = view.findViewById(R.id.register_button)


        btn.setOnClickListener {
            signUpUser()
        }


        return view
    }

    //  Register user
    private fun signUpUser() {
        val name: String = editName.text.toString()
        val year: String = editYear.text.toString()
        val batch: String = editBatch.text.toString()
        val roll: String = editRoll.text.toString()
        val email: String = rollToMail(year, batch, roll)
        val password: String = editPassword.text.toString()
        val confirmPass: String = editConPassword.text.toString()

        if (year.length != 4) {

            Toast.makeText(activity, "Wrong format", Toast.LENGTH_SHORT).show()
            return
        }
        if (batch.length!=3) {
            Toast.makeText(activity, "Wrong format", Toast.LENGTH_SHORT).show()
            return
        }
        if (roll.length != 3) {

            Toast.makeText(activity, "Wrong format", Toast.LENGTH_SHORT).show()
            return
        }
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            Toast.makeText(activity, "Blank Fields are not allowed", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPass) {
            Toast.makeText(activity, "Password do not match", Toast.LENGTH_SHORT).show()
            return
        }



        fireBaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val member = hashMapOf<String ,String>()
                    member.put("name",name)
                    member.put("email",email)
                    member.put("password",password)
                    member.put("roll no. ",year.plus(batch.toLowerCase()).plus("-").plus(roll))

                    database
                        .add(member)
                        .addOnSuccessListener {
                            Log.i("Memberd Success", it.toString())
                        }



                    if(batch == "adm" || batch == "ADM")
                    {
                        Toast.makeText(activity, "Registered Successfully ", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else{
                    verifyingEmail()
                    }

                    //  Set the display name of the user
                    fireBaseAuth.currentUser?.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                    )


                    

                } else {
                    Toast.makeText(activity, "Error creating User", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun rollToMail(y: String, b: String, r: String): String {
        return (b.toLowerCase(Locale.ROOT).plus("_").plus(y).plus(r).plus("@iiitm.ac.in"))
    }

    private fun verifyingEmail()
    {
        val user = fireBaseAuth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Registered Successfully : Verification mail is sent to your College Email Address", Toast.LENGTH_SHORT)
                        .show()

                }
                else
                {
                    Toast.makeText(activity, "Verification mail is not sent ", Toast.LENGTH_SHORT)
                        .show()
                }
            }


    }



}
