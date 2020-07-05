package com.abh16am.ucekbuzz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class ModuleAdapter(
    val context: Context, val itemList: ArrayList<NotesModel>,
    val listener: OnItemClick
) :
    RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>(){

    class ModuleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt: TextView = view.findViewById(R.id.itm_txt)
        val bgimg: ImageView = view.findViewById(R.id.imgcv)
        val card : CardView= view.findViewById(R.id.card)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_item_module, parent, false)
        return ModuleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val food = itemList[position]
        holder.txt.text = food.key
        Glide.with(context).load(food.value).into(holder.bgimg)
        holder.card.setOnClickListener {
            Toast.makeText(context, "Its toast! ${food.key}", Toast.LENGTH_SHORT).show()
            listener.onClick(food)

        }
    }
}