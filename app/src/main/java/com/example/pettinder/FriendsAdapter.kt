package com.example.pettinder

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


// Will get the data from the user class and display it in the friends cardview

class FriendsAdapter(

    private val friendlist: List<User>,
    private val onUserClickListener: OnUserClickListener


): RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    interface OnUserClickListener {
        fun onUserClicked(user: User, position: Int)
    }

    class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

            val friendsimg = itemView.findViewById<ImageView>(R.id.Frinedsimage)
            val friendsname = itemView.findViewById<TextView>(R.id.FriendsName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ):FriendsAdapter.FriendsViewHolder {
        val friendview = LayoutInflater.from(parent.context)
            .inflate(R.layout.friendsitem,parent,false)

        return FriendsViewHolder(friendview)
    }

    override fun onBindViewHolder(holder: FriendsAdapter.FriendsViewHolder, position: Int) {

        val currentuser = friendlist[position]

        holder.friendsname.setText(friendlist[position].displayName)

        Glide.with(holder.itemView)
            .load(friendlist[position].ProfilePicture)
            .error(R.drawable.account_circle)
            .placeholder(R.drawable.account_circle)
            .into(holder.friendsimg)

        holder.itemView.setOnClickListener {
            onUserClickListener.onUserClicked(currentuser,position)
        }


    }

    override fun getItemCount(): Int {
        return friendlist.size
    }


}