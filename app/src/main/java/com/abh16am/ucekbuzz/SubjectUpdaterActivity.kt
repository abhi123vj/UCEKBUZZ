package com.abh16am.ucekbuzz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.`interface`.OnItemClick
import com.abh16am.ucekbuzz.adapter.ModuleAdapter
import com.abh16am.ucekbuzz.adapter.UpdateAdapter
import com.abh16am.ucekbuzz.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.IOException

class SubjectUpdaterActivity : AppCompatActivity(), OnItemClick {

    val Thelist = arrayListOf<NotesModel>()

    lateinit var formlayout: ScrollView

    lateinit var cardupload: CardView

    lateinit var cardcancel: CardView

    lateinit var linearlayout: LinearLayout

    lateinit var recylerHome: RecyclerView

    lateinit var cardbuttn: CardView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: UpdateAdapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    lateinit var etFileName: EditText
    lateinit var etMod: EditText
    lateinit var etSub: EditText
    lateinit var etType: EditText
    lateinit var etUrl: EditText


    private lateinit var postReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_updater)
        recylerHome = findViewById(R.id.uploadRecyleView)
        etSub = findViewById(R.id.Et_Subject)
        etMod = findViewById(R.id.Et_Module)
        etType = findViewById(R.id.Et_Type)
        etFileName = findViewById(R.id.Et_FileName)
        etUrl = findViewById(R.id.Et_Link)
        progressLayout = findViewById(R.id.progressLayout)
        linearlayout = findViewById(R.id.ll_updater_ll)
        progressBar = findViewById(R.id.progressBar)
        formlayout = findViewById(R.id.ll_forms)
        cardupload = findViewById(R.id.cardupload)
        cardcancel = findViewById(R.id.cardcancel)
        cardbuttn = findViewById(R.id.addnewCvbtn)
        linearlayout.visibility = View.VISIBLE
        progressLayout.visibility = View.VISIBLE
        formlayout.visibility = View.GONE
        layoutManager = LinearLayoutManager(this)
        cardbuttn.setOnClickListener {
            recylerHome.visibility = View.GONE
            linearlayout.visibility = View.GONE
            formlayout.visibility = View.VISIBLE
            change(NotesModel("", ""))

        }
        cardupload.setOnClickListener {
            if (etSub.text.isNotEmpty() && etMod.text.isNotEmpty() && etType.text.isNotEmpty() && etFileName.text.isNotEmpty() && etUrl.text.isNotEmpty())
                uploadlink(
                    etSub.text.toString(),
                    etMod.text.toString(),
                    etType.text.toString(),
                    etFileName.text.toString(),
                    etUrl.text.toString()
                )

        }



        readdata()

    }

    private fun uploadlink(sub: String, mod: String, type: String, fName: String, link: String) {

        try {

            val database = Firebase.database
            val myRef = database.getReference("Notes/Subject/$sub/$mod/$type/$fName")
            myRef.setValue(link)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            println("thisda")
        } catch (e: IOException) {
            Toast.makeText(this, "Its Update failed ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            println("thisda")

        }


    }

    private fun recylerloader() {
        recyclerAdapter =
            UpdateAdapter(this@SubjectUpdaterActivity, Thelist, this@SubjectUpdaterActivity)
        recylerHome.adapter = recyclerAdapter
        recylerHome.layoutManager = layoutManager
        recylerHome.visibility = View.VISIBLE
        progressLayout.visibility = View.GONE

    }

    private fun readdata(path: String = "Notes/Subject") {
        progressLayout.visibility = View.VISIBLE
        postReference = Firebase.database.reference.child(path)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Thelist.clear()
                println("dataisda datasnapshot $dataSnapshot")
                for (data in dataSnapshot.children) {
                    val key = NotesModel(data.key.toString())
                    Thelist.add(key)
                }
                recylerloader()
                progressLayout.visibility = View.GONE

            }

            override fun onCancelled(databaseError: DatabaseError) {
                progressLayout.visibility = View.GONE
                // Getting Post failed, log a message
                // ...
            }
        }
        postReference.addValueEventListener(postListener)
    }

    private var subject = ""
    private var module = ""
    private var format = ""


    override fun onClick(value: NotesModel) {
        println("dataistht datasnapshot $value")
        if (subject == "" && module == "" && format == "") {
            subject = value.key
            readdata("Notes/Subject/$subject")
        } else if (module == "" && format == "") {
            module = value.key
            readdata("Notes/Subject/$subject/$module")
        } else if (format == "") {
            format = value.key
            readdata("Notes/Subject/$subject/$module/$format")
        } else if (subject != "" && module != "" && format != "") {
            recylerHome.visibility = View.GONE
            linearlayout.visibility = View.GONE
            formlayout.visibility = View.VISIBLE
            change(value)
        }

    }

    private fun change(value: NotesModel) {


        if (subject != "" && module != "" && format != "") {
            etFileName.setText(value.key)
            etSub.setText(subject)
            etMod.setText(module)
            etType.setText(format)
            etSub.isEnabled = false
            etMod.isEnabled = false
            etType.isEnabled = false
        } else if (module != "" && subject != "" && format == "") {
            etSub.setText(subject)
            etMod.setText(module)
            etMod.isEnabled = false
            etSub.isEnabled = false
        } else if (module == "" && subject != "" && format == "") {
            etSub.setText(subject)
            etSub.isEnabled = false
        }

    }
}