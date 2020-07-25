package com.abh16am.ucekbuzz.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.abh16am.ucekbuzz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    lateinit var loginlayout : LinearLayout
    lateinit var signoutlayout : LinearLayout

    private lateinit var auth: FirebaseAuth
    lateinit var  email : EditText
    lateinit var password : EditText
    lateinit var button : CardView
    lateinit var signoutbutton : CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = Firebase.auth
        email = view.findViewById(R.id.Et_email)
        password = view.findViewById(R.id.Et_Password)
        button = view.findViewById(R.id.buttonCv)
        loginlayout = view.findViewById(R.id.ll_updater_ll)
        signoutlayout = view.findViewById(R.id.signoutll)
        signoutbutton = view.findViewById(R.id.signout)

        signoutbutton.setOnClickListener {
            signOut()
        }
        signoutlayout.visibility = View.GONE
        loginlayout.visibility = View.VISIBLE
        button.setOnClickListener {
        if (email.text.isNotEmpty()&&password.text.isNotEmpty())
           login(email.text.toString(),password.text.toString())
        }
        return view
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(activity as Context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            loginlayout.visibility = View.GONE
            signoutlayout.visibility = View.VISIBLE
        } else {
            loginlayout.visibility = View.VISIBLE
        }
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

}