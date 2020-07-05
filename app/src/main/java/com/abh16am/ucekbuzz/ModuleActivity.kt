package com.abh16am.ucekbuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ModuleActivity : AppCompatActivity() {
    val subject =intent.getStringExtra("Username")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module)

    }
}