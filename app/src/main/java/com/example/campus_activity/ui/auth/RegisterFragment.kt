package com.example.campus_activity.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.campus_activity.R
import com.example.campus_activity.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var editYear: EditText
    private lateinit var editBatch: EditText
    private lateinit var editRoll: EditText
    private lateinit var editPassword: EditText
    private lateinit var editConPassword: EditText
    private lateinit var editName: EditText
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var btn: Button

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
                    Toast.makeText(activity, "User is successfully created", Toast.LENGTH_SHORT)
                        .show()

                    //  Set the display name of the user
                    fireBaseAuth.currentUser?.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                    )

                    //  Start main activity
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, "Error creating User", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun rollToMail(y: String, b: String, r: String): String {
        return (b.toLowerCase(Locale.ROOT).plus("_").plus(y).plus(r).plus("@iiitm.ac.in"))
    }


}