package com.abh16am.ucekbuzz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.abh16am.ucekbuzz.R
import com.abh16am.ucekbuzz.`interface`.OnItemClick
import com.abh16am.ucekbuzz.models.NotesModel
import com.bumptech.glide.Glide
import com.google.firebase.database.ValueEventListener


class UpdateAdapter(
    val context: Context, val itemList: ArrayList<NotesModel>,
    val listener: OnItemClick
) :
    RecyclerView.Adapter<UpdateAdapter.ModuleViewHolder>(){


    class ModuleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt: TextView = view.findViewById(R.id.name_tv)
        val toggl : ImageButton = view.findViewById(R.id.toggle_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_row, parent, false)
        return ModuleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val food = itemList[position]
        holder.txt.text = food.key
        holder.toggl.setBackgroundResource(R.drawable.ic_baseline_edit_24)
        println("dataisdad datasnapshot $food")
        holder.toggl.setOnClickListener {
           listener.onClick(food)
        }

    }
}