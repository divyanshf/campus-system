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
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var editYear: EditText
    private lateinit var editBatch: EditText
    private lateinit var editRoll: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnlogin: Button
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        editYear = view.findViewById(R.id.year_login_edit_text)
        editBatch = view.findViewById(R.id.batch_login_edit_text)
        editRoll = view.findViewById(R.id.roll_login_edit_text)
        txtPassword = view.findViewById(R.id.password_login_edit_text)
        btnlogin = view.findViewById(R.id.login_button)

        btnlogin.setOnClickListener {
            login()
        }
        return view
    }

    private fun login() {
        val year: String = editYear.text.toString()
        val batch: String = editBatch.text.toString()
        val roll: String = editRoll.text.toString()
        val email: String = rollToMail(year, batch, roll)
        val password: String = txtPassword.text.toString()

        if (year.length != 4) {

            Toast.makeText(activity, "Wrong format", Toast.LENGTH_SHORT).show()
            return
        }
        if (batch.length != 3) {
            Toast.makeText(activity, "Wrong format", Toast.LENGTH_SHORT).show()
            return
        }
        if (roll.length != 3) {

            Toast.makeText(activity, "Wrong format", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(activity, "Blank Fields are not allowed", Toast.LENGTH_SHORT).show()
            return
        }





        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {


                    if(batch =="adm" || batch == "ADM")
                    {
                        Toast.makeText(activity, "Admin Login Successful", Toast.LENGTH_SHORT).show()

                        //  Starting main activity Admin as user
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)


                    }
                    else {

                        val user = firebaseAuth.currentUser
                        if (user!!.isEmailVerified) {


                            Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()

                            //  Start main activity
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        } else {
                            Toast.makeText(activity, "Email is not Verified", Toast.LENGTH_SHORT)
                                .show()
                            firebaseAuth.signOut()
                        }

                    }
                } else {
                    Toast.makeText(
                        activity,
                        "Login Unsuccessful ! Wrong Email or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }

    private fun rollToMail(y: String, b: String, r: String): String {
        return (b.toLowerCase(Locale.ROOT).plus("_").plus(y).plus(r).plus("@iiitm.ac.in"))
    }

}