package com.abh16am.ucekbuzz.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.ModuleActivity
import com.abh16am.ucekbuzz.R
import com.abh16am.ucekbuzz.`interface`.OnItemClick
import com.abh16am.ucekbuzz.adapter.ModuleAdapter
import com.abh16am.ucekbuzz.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class NotesFragment : Fragment(), OnItemClick {


    lateinit var recylerHome: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: ModuleAdapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    val tempnames = arrayListOf<NotesModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("back value is  $sem and $batch")
                if (sem!="")
                 {  sem=""
                     readdata("Notes/bgsem")}
                else if(batch!="")
                  { batch=""
                      readdata("Notes/bg")}
                else{
                    isEnabled = false
                    activity?.onBackPressed()
                }

            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        setHasOptionsMenu(true)

        recylerHome = view.findViewById(R.id.recycler_view)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(activity)

        readdata("Notes/bg")

        return view
    }

    private fun readdata(path: String) {


        val postReference = Firebase.database.reference.child(path)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tempnames.clear()
                // Get Post object and use the values to update the UI
                for (data in dataSnapshot.children) {
                    println("thisda key ${data.key} value ${data.value}")
                    val tempobjct = NotesModel("${data.key}", "${data.value}")
                    tempnames.add(tempobjct)
                    recyclerAdapter =
                        ModuleAdapter(activity as Context, tempnames, this@NotesFragment)

                    recylerHome.adapter = recyclerAdapter

                    recylerHome.layoutManager = layoutManager

                }


                println("thisda key $tempnames")


                progressLayout.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        }
        postReference.addValueEventListener(postListener)


    }

    var batch: String = ""
    var sem : String =""
    var sub : String=""

    override fun onClick(value: NotesModel) {
        println("thisda value is  $value")

        if (value.key == "ECE" || value.key == "CSE" || value.key== "IT") {
            batch = value.key
            readdata("Notes/bgsem")
            println("thisda value is  $value")
            return
        }

        if(value.key=="S1 & S2"||value.key=="S3"||value.key=="S4"||value.key=="S5"||value.key=="S6"||value.key=="S7"||value.key=="S8"&&batch!=""){
            sem = value.key
            readdata("Notes/Branch/$batch/$sem")
            return
        }

        if(value.value!=""&&sem!=""){
            println("thisda1 value is this doooo${value.key}")
            sub =value.key
            val intent = Intent (this@NotesFragment.context, ModuleActivity::class.java)
            intent.putExtra("subject",sub)
            startActivity(intent)
            return
        }

        println("thisda1 value is this  ${value.key}")


    }

    override fun onClick(value: String) {
        TODO("Not yet implemented")
    }


}