package com.example.pettinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class PetAdapter(
    private val petNames: List<String>,
    private val petAges: List<String>,
    private val petImageUrls: List<String>,
    private val petGenders: List<String>,
    private val petLocations: List<String>,
    private val petDescriptions: List<String>
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {


    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

           val petPic: ImageView = itemView.findViewById(R.id.Petpic)
           val petName: TextView = itemView.findViewById(R.id.petName)
           val petAge: TextView = itemView.findViewById(R.id.petAge)
           val petGender: TextView = itemView.findViewById(R.id.petGender)
           val petLocation: TextView = itemView.findViewById(R.id.petLocation)
           val petDescription: TextView = itemView.findViewById(R.id.petDescription)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetAdapter.PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pet_item,parent,false)

        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetAdapter.PetViewHolder, position: Int) {
        holder.petName.text = petNames[position]
        holder.petAge.text = petAges[position]
        holder.petGender.text = petGenders[position]
        holder.petLocation.text = petLocations[position]
        holder.petDescription.text = petDescriptions[position]

        Glide.with(holder.itemView)
            .load(petImageUrls[position])
            .into(holder.petPic)
    }



    override fun getItemCount(): Int {
        return petNames.size
    }


}