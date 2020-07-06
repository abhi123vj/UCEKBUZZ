package com.abh16am.ucekbuzz

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.`interface`.OnItemClick
import com.abh16am.ucekbuzz.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_module.*

class ModuleActivity : AppCompatActivity() ,OnItemClick{

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar

    private lateinit var postReference2: DatabaseReference

    var subject: String? = null
    lateinit var rowAdapter: RowAdapter
    lateinit var rows1: MutableList<RowModel>
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module)
        subject = intent.getStringExtra("subject")

        coordinatorLayout = findViewById(R.id.coordinator_layout)
       toolbar = findViewById(R.id.toolbar)
        setUpToolbar()

        progressLayout =findViewById(R.id.progressLayout)

        progressBar = findViewById(R.id.progressBar)


        progressLayout.visibility = View.VISIBLE

       recyclerView = findViewById(R.id.recycler_view)
        println("god")
        rows1 = mutableListOf()

        rowAdapter = RowAdapter(this@ModuleActivity, rows1,this)




        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )


        recyclerView.adapter = rowAdapter

        readdata()


    }
    private fun readdata() {
        postReference2 = Firebase.database.reference.child("Notes/Subject/$subject")
        println("god $subject")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rows1.clear()
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

                    rows1.add(RowModel(RowModel.COUNTRY, Semester("${data.key}", subjectList1)))
                }
                rowAdapter.notifyDataSetChanged()
                progressLayout.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        }
        postReference2.addValueEventListener(postListener)
    }
    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = subject
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onClick(value: NotesModel) {
        TODO("Not yet implemented")
    }

    override fun onClick(value: String) {
        TODO("Not yet implemented")
    }

}