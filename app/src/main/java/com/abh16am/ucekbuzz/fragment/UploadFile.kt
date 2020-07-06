package com.abh16am.ucekbuzz.fragment

import android.content.Context
import android.graphics.Color
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
import kotlinx.android.synthetic.main.fragment_upload_file.*
import java.io.IOException

class UploadFile : Fragment(),OnItemClick {

   private val noteslink ="https://images2.alphacoders.com/261/thumb-1920-26102.jpg"
    private val questnlink ="https://images.pexels.com/photos/1557251/pexels-photo-1557251.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
    private lateinit var postReference: DatabaseReference

    lateinit var rowAdapter: RowAdapter
    lateinit var rows: MutableList<RowModel>
    lateinit var recyclerView: RecyclerView
    lateinit var card1 : CardView
    lateinit var card2 : CardView
    lateinit var imgv1 : ImageView
    lateinit var imgv2 : ImageView
    lateinit var cardlayer : LinearLayout
    lateinit var Et_year :EditText
    lateinit var Et_url : EditText

    lateinit var progressLayout: RelativeLayout
    lateinit var recylrLayout : LinearLayout
    lateinit var questionuploader :LinearLayout
    lateinit var Btn_Publish : Button

    lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_upload_file, container, false)
        progressLayout =view.findViewById(R.id.progressLayout)
        Btn_Publish = view.findViewById(R.id.btn_publish)
        progressBar = view.findViewById(R.id.progressBar)
        Et_year = view.findViewById(R.id.Et_year)
        Et_url = view.findViewById(R.id.Et_url)
        questionuploader = view.findViewById(R.id.questionuploaderlayout)
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
           progressLayout.visibility = View.VISIBLE


            rows = mutableListOf()
            rowAdapter = RowAdapter(activity as Context, rows,this@UploadFile,5)

            recyclerView.layoutManager = LinearLayoutManager(
                activity as Context,
                RecyclerView.VERTICAL,
                false
            )


            recyclerView.adapter = rowAdapter

            readdata()


        }
       Btn_Publish.setOnClickListener {
           if (SubjectName!="")
               if(!Et_year.text.isEmpty()&&!Et_url.text.isEmpty())
                  if( finder())
                      questionuploader.visibility = View.GONE



        }
        card1.setOnClickListener {
         //   progressLayout.visibility = View.VISIBLE


        }



        return view
    }

    private fun finder() : Boolean{
        for(d0 in rows){
            for (d1 in d0.semester.subjectList!!){
                if(SubjectName == d1.name){
                    questionpaperUpdater(d0.semester.name,SubjectName,Et_year.text.toString(),Et_url.text.toString())
                    return true
                }
                println("country 2 ${d1.name}")
            }
            println("addtoast")
        }
        return  false
    }

    private fun questionpaperUpdater( sem : String, sub : String,yr : String,url : String) {
        try {
            val database = Firebase.database
        val myRef = database.getReference("Semester/$sem/$sub/$yr")

        myRef.setValue(url)}
        catch (e: IOException){

        }
        Et_year.text.clear()
        Et_url.text.clear()


    }

    private fun editQuestions() {
        progressLayout.visibility = View.VISIBLE
    }
    private fun readdata() {
        progressLayout.visibility = View.GONE
        postReference = Firebase.database.reference.child("Semester")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rows.clear()
                // Get Post object and use the values to update the UI
                for (data in dataSnapshot.children) {

                    val subjectList1: MutableList<Subject> = mutableListOf()
                    for (data2 in data.children) {
                        val yearList1: MutableList<Year> = mutableListOf()
                        subjectList1.add(Subject("${data2.key}", yearList1))
                    }

                    rows.add(RowModel(RowModel.COUNTRY, Semester("${data.key}", subjectList1)))
                }
                rowAdapter.notifyDataSetChanged()
                cardlayer.visibility = View.GONE
                recylrLayout.visibility = View.VISIBLE
                progressLayout.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        }
        postReference.addValueEventListener(postListener)
    }

    private fun editNotes() {
        TODO("Not yet implemented")
    }

    override fun onClick(value: NotesModel) {

    }
    private var SubjectName : String = ""
    override fun onClick(value: String) {
        recylrLayout.visibility = View.GONE
        questionuploader.visibility=View.VISIBLE

        SubjectName = value
        Et_year.hint=value + " (year)"

    }

}