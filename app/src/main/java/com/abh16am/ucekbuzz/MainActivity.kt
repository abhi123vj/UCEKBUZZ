package com.abh16am.ucekbuzz

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.models.RowModel
import com.abh16am.ucekbuzz.models.Semester
import com.abh16am.ucekbuzz.models.Subject
import com.abh16am.ucekbuzz.models.Year
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    val TAG = "Tst"

    private lateinit var postReference: DatabaseReference


    lateinit var rowAdapter: RowAdapter
    lateinit var rows: MutableList<RowModel>
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        rows = mutableListOf()
        rowAdapter = RowAdapter(this, rows)

        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        recyclerView.adapter = rowAdapter

        readdata()

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
                            Log.w(
                                TAG,
                                "loadPost:onCancelled is new data ${data.key}  ${data2.key} ${data3.key}"
                            )
                        }
                        subjectList1.add(Subject("${data2.key}", yearList1))

                    }

                    rows.add(RowModel(RowModel.COUNTRY, Semester("${data.key}", subjectList1)))
                }
                rowAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        postReference.addValueEventListener(postListener)
    }


}