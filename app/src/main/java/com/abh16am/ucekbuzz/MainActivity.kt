package com.abh16am.ucekbuzz

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.adapter.ModuleAdapter
import com.abh16am.ucekbuzz.fragment.NotesFragment
import com.abh16am.ucekbuzz.fragment.QuestionPaper
import com.abh16am.ucekbuzz.fragment.UploadFile
import com.abh16am.ucekbuzz.models.RowModel
import com.abh16am.ucekbuzz.models.Semester
import com.abh16am.ucekbuzz.models.Subject
import com.abh16am.ucekbuzz.models.Year
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigation_view)

        setUpToolbar()
        openHome()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null)
                previousMenuItem?.isChecked = false
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {

                R.id.Notes -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.QuestionPapers -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            QuestionPaper()
                        )
                        .commit()
                    supportActionBar?.title = "Question Papers"
                    drawerLayout.closeDrawers()

                }
                R.id.uploadFile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            UploadFile()
                        )
                        .commit()
                    supportActionBar?.title = "Upload"
                    drawerLayout.closeDrawers()

                }

            }

            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "UCEK BUZZ"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START)

        return super.onOptionsItemSelected(item)
    }

    fun openHome() {
        val fragment = NotesFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Notes"
        navigationView.setCheckedItem(R.id.Notes)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is NotesFragment -> openHome()
            else -> super.onBackPressed()
        }
    }

}
