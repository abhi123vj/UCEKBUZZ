package com.abh16am.ucekbuzz.fragment

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.R
import com.abh16am.ucekbuzz.RowAdapter
import com.abh16am.ucekbuzz.SubjectUpdaterActivity
import com.abh16am.ucekbuzz.`interface`.OnItemClick
import com.abh16am.ucekbuzz.adapter.ModuleAdapter
import com.abh16am.ucekbuzz.models.*
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_upload_file.*
import java.io.IOException

class UploadFile : Fragment(), OnItemClick {

    private val noteslink = "https://images2.alphacoders.com/261/thumb-1920-26102.jpg"
    private val questnlink =
        "https://images.pexels.com/photos/1557251/pexels-photo-1557251.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
    private lateinit var postReference: DatabaseReference

    lateinit var rowAdapter: RowAdapter
    lateinit var rows: MutableList<RowModel>
    lateinit var recyclerView: RecyclerView
    lateinit var card1: CardView
    lateinit var card2: CardView
    lateinit var imgv1: ImageView
    lateinit var imgv2: ImageView
    lateinit var cardlayer: LinearLayout
    lateinit var Et_year: EditText
    lateinit var Et_url: EditText
    lateinit var Et_File: EditText
    lateinit var Bt_uplaod: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var recylrLayout: LinearLayout
    lateinit var questionuploader: LinearLayout
    lateinit var Btn_Publish: Button
    private lateinit var storage: FirebaseStorage
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload_file, container, false)
        storage = Firebase.storage
        progressLayout = view.findViewById(R.id.progressLayout)
        Btn_Publish = view.findViewById(R.id.btn_publish)
        Et_File = view.findViewById(R.id.Et_file_name)
        progressBar = view.findViewById(R.id.progressBar)
        Et_year = view.findViewById(R.id.Et_year)
        Et_url = view.findViewById(R.id.Et_url)
        questionuploader = view.findViewById(R.id.questionuploaderlayout)
        Bt_uplaod = view.findViewById(R.id.btn_Upload)
        card1 = view.findViewById(R.id.card)
        card2 = view.findViewById(R.id.card2)
        cardlayer = view.findViewById(R.id.cardlayout)
        imgv1 = view.findViewById(R.id.imgcvda)
        recylrLayout = view.findViewById(R.id.llayouthidn)
        imgv2 = view.findViewById(R.id.imgcvda2)
        recylrLayout.visibility = View.GONE
        progressLayout.visibility = View.GONE
        recyclerView = view.findViewById(R.id.recycler_view)
        Glide.with(activity as Context).load(noteslink).into(imgv1)
        Glide.with(activity as Context).load(questnlink).into(imgv2)
        card2.setOnClickListener {
            rows = mutableListOf()
            rowAdapter = RowAdapter(activity as Context, rows, this@UploadFile, 5)
            recyclerView.layoutManager = LinearLayoutManager(
                activity as Context,
                RecyclerView.VERTICAL,
                false
            )
            recyclerView.adapter = rowAdapter
            readdata()
        }
        Btn_Publish.setOnClickListener {
            if (SubjectName != "")
                when {
                    Et_year.text.isEmpty() -> {
                        Et_year.error = "Requires Year"
                        Et_year.requestFocus()
                        Toast.makeText(context, "Enter Question Paper details", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Et_url.text.isEmpty() -> {
                        Et_url.error = "Requires Url "
                        Et_url.requestFocus()
                        Toast.makeText(context, "Enter Question Paper Link", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> finder(Et_url.text.toString(), Et_year.text.toString())
                }
        }
        Bt_uplaod.setOnClickListener {
            if (SubjectName != "")
                if (Et_File.text.isEmpty()) {
                    Et_File.error = "Requires Details"
                    Et_File.requestFocus()
                    Toast.makeText(context, "Enter Question Paper Year", Toast.LENGTH_SHORT)
                        .show()
                } else
                    OpenFileFinder()
        }

        card1.setOnClickListener {

            val intent = Intent(activity as Context, SubjectUpdaterActivity::class.java)
            startActivity(intent)

        }
        return view
    }



    private fun OpenFileFinder() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        progressLayout.visibility = View.VISIBLE
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                uploadprofilepic(data!!.data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadprofilepic(data: Uri?) {
        val storageRef = storage.reference
        val imageRef = storageRef.child("QuestionPapers/$SubjectName/${Et_File.text}")
        val uploadTask: UploadTask = imageRef.putFile(data!!)
        val addOnSuccessListener = uploadTask.addOnFailureListener {
            progressLayout.visibility = View.GONE
        }.addOnSuccessListener {
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                progressLayout.visibility = View.GONE
                imageRef.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    finder(downloadUri.toString(), Et_File.text.toString())
                } else {
                    activity?.onBackPressed()
                    progressLayout.visibility = View.GONE
                }
            }
        }
    }


    private fun finder(url: String, Et: String): Boolean {
        for (d0 in rows) {
            for (d1 in d0.semester.subjectList!!) {
                if (SubjectName == d1.name) {
                    questionpaperUpdater(
                        d0.semester.name,
                        SubjectName,
                        Et,
                        url
                    )
                    return true
                }
            }
        }
        return false
    }

    private fun questionpaperUpdater(sem: String, sub: String, yr: String, url: String) {
        try {
            progressLayout.visibility = View.VISIBLE
            val database = Firebase.database
            val myRef = database.getReference("Semester/$sem/$sub/$yr")
            myRef.setValue(url)
        } catch (e: IOException) {
        }
        activity?.onBackPressed()
    }

    private fun readdata() {
        progressLayout.visibility = View.VISIBLE
        postReference = Firebase.database.reference.child("Semester")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rows.clear()
                for (data in dataSnapshot.children) {
                    val subjectList1: MutableList<Subject> = mutableListOf()
                    for (data2 in data.children) {
                        val yearList1: MutableList<Year> = mutableListOf()
                        subjectList1.add(Subject("${data2.key}", yearList1))
                    }
                    rows.add(RowModel(RowModel.COUNTRY, Semester("${data.key}", subjectList1)))
                }
                rowAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        progressLayout.visibility = View.GONE
        postReference.addValueEventListener(postListener)
        cardlayer.visibility = View.GONE
        recylrLayout.visibility = View.VISIBLE
    }

    override fun onClick(value: NotesModel) {

        if(value.value!="5"){
        recylrLayout.visibility = View.GONE
        questionuploader.visibility = View.VISIBLE
        SubjectName = value.key
        Et_year.hint = "${value.key} (year)"
        Et_File.hint = "${value.key} (year)"}
        if(value.value=="SubjectNAme"){
            recylrLayout.visibility = View.GONE
            questionuploader.visibility = View.VISIBLE
            SubjectName = value.key
            Et_year.hint = "${value.key} (year)"
            Et_File.hint = "${value.key} (year)"}
        }
    private var SubjectName: String = ""
}
