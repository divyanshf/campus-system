package com.example.campus_activity.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.campus_activity.R
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {


    lateinit var editYear: EditText
    lateinit var editBatch: EditText
    lateinit var editRoll: EditText
    lateinit var editPassword: EditText
    lateinit var editConPassword: EditText
    lateinit var editName : EditText
    lateinit var fireBaseAuth: FirebaseAuth
    lateinit var btn: Button


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
            signupuser()
        }
        return view
    }

    private fun signupuser() {

        val name : String = editName.text.toString()
        val year :String = editYear.text.toString()
        val batch :String = editBatch.text.toString()
        val roll :String = editRoll.text.toString()
        val email: String = rollToMail(year , batch , roll)
        val password: String = editPassword.text.toString()
        val confirmpass: String = editConPassword.text.toString()


        if (name.isBlank()||email.isBlank() || password.isBlank() || confirmpass.isBlank()) {
            Toast.makeText(activity, "Blank Fields are not allowed", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmpass) {
            Toast.makeText(activity, "Password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        fireBaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(activity, "User is successfully created", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(activity, "Error creating User", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun rollToMail(y: String , b : String, r : String): String {

        return (b.toLowerCase().plus("_").plus(y).plus(r).plus("@iiitm.ac.in"))
    }

}