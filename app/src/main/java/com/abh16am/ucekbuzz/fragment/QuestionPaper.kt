package com.abh16am.ucekbuzz.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.R
import com.abh16am.ucekbuzz.RowAdapter
import com.abh16am.ucekbuzz.`interface`.OnItemClick
import com.abh16am.ucekbuzz.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class QuestionPaper : Fragment() ,OnItemClick{
    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    private lateinit var postReference: DatabaseReference


    lateinit var rowAdapter: RowAdapter
    lateinit var rows: MutableList<RowModel>
    lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question_paper, container, false)
        progressLayout =view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE
        recyclerView = view.findViewById(R.id.recycler_view)
        rows = mutableListOf()
        rowAdapter = RowAdapter(activity as Context, rows,this)

        recyclerView.layoutManager = LinearLayoutManager(
            activity as Context,
            RecyclerView.VERTICAL,
            false
        )


        recyclerView.adapter = rowAdapter

        readdata()

        return view

        // populateData()


    }


    private fun readdata() {
        postReference = Firebase.database.reference.child("Semester")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rows.clear()
                // Get Post object and use the values to update the UI
                for (data in dataSnapshot.children) {

                    val subjectList1: MutableList<Subject> = mutableListOf()
                    for (data2 in data.children) {
                        val yearList1: MutableList<Year> = mutableListOf()
                        for (data3 in data2.children) {
                            yearList1.add(Year("${data3.key}","${data3.value}"))

                        }
                        subjectList1.add(Subject("${data2.key}", yearList1))

                    }

                    rows.add(RowModel(RowModel.COUNTRY, Semester("${data.key}", subjectList1)))
                }
                rowAdapter.notifyDataSetChanged()
                progressLayout.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        }
        postReference.addValueEventListener(postListener)
    }

    override fun onClick(value: NotesModel) {
        TODO("Not yet implemented")
    }

    override fun onClick(value: String) {
        TODO("Not yet implemented")
    }


}


