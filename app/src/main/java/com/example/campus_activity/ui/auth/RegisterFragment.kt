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


    lateinit var editname : EditText
    lateinit var editpassword : EditText
    lateinit var editconpassword : EditText
    lateinit var fireBaseAuth : FirebaseAuth
    lateinit var btn : Button




    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        editname= view.findViewById(R.id.username_edit_text)
        editpassword = view.findViewById(R.id.password_edit_text)
        editconpassword= view.findViewById(R.id.confirm_password_edit_text)
        fireBaseAuth= FirebaseAuth.getInstance()

        btn = view.findViewById(R.id.register_button)
        btn.setOnClickListener {
            signupuser()
        }
        return view
    }

    private fun signupuser() {
        val email:String= editname.text.toString()
        val password : String =editpassword.text.toString()
        val confirmpass: String = editconpassword.text.toString()


        if(email.isBlank()||password.isBlank()||confirmpass.isBlank())
        {
            Toast.makeText(activity,"Blank Fields are not allowed", Toast.LENGTH_SHORT).show()
            return
        }
        if(password != confirmpass)
        {
            Toast.makeText(activity,"Password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        fireBaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful)
                {
                    Toast.makeText(activity,"User is successfully created", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(activity,"Error creating User", Toast.LENGTH_SHORT).show()
                }
            }




    }

}