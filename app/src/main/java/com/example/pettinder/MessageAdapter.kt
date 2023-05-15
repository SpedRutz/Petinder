package com.example.pettinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter (
    private val MessageList: List<Message>,
    private val senderimg: String,
    private val Recieverimg: String

): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {




    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val messagecontent = itemView.findViewById<TextView>(R.id.chattingContent)
        val chattingprofileimg = itemView.findViewById<ImageView>(R.id.chattingimg)
        val Messageconstraint = itemView.findViewById<ConstraintLayout>(R.id.chattingsecConstraint)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val messageview = LayoutInflater.from(parent.context)
            .inflate(R.layout.messageitem,parent,false)

        return  MessageViewHolder(messageview)
    }

    override fun getItemCount(): Int {
        return MessageList.size
    }
    //Will switch the content of the message item and put them on right (sender) or left ( reciever)
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.messagecontent.text = MessageList[position].Content

        //allows us to manipulate constraintlayout
        val constraintSet = ConstraintSet()


        if (MessageList[position].Sender == (FirebaseAuth.getInstance().currentUser?.email ?: "")) {

            //Inflate image

            Glide.with(holder.itemView)
                .load(senderimg)
                .error(R.drawable.account_circle)
                .placeholder(R.drawable.account_circle)
                .into(holder.chattingprofileimg)


            val constraintSet = ConstraintSet()
            //If the current user is the sender of the message, align message item to the right by manipulating the constraints of the constranit layout
            constraintSet.clone(holder.Messageconstraint)
            constraintSet.clear(R.id.Messagingcardview,ConstraintSet.START)
            constraintSet.clear(R.id.chattingContent,ConstraintSet.START)
            constraintSet.connect(R.id.Messagingcardview,ConstraintSet.END,R.id.chattingsecConstraint,ConstraintSet.END,0)
            constraintSet.connect(R.id.chattingContent, ConstraintSet.END, R.id.Messagingcardview, ConstraintSet.START, 0)
            constraintSet.applyTo(holder.Messageconstraint)
        } else {

            Glide.with(holder.itemView)
                .load(Recieverimg)
                .error(R.drawable.account_circle)
                .placeholder(R.drawable.account_circle)
                .into(holder.chattingprofileimg)

            constraintSet.clone(holder.Messageconstraint)
            constraintSet.clear(R.id.Messagingcardview,ConstraintSet.END)
            constraintSet.clear(R.id.chattingContent,ConstraintSet.END)
            constraintSet.connect(R.id.Messagingcardview,ConstraintSet.START,R.id.chattingsecConstraint,ConstraintSet.START,0)
            constraintSet.connect(R.id.chattingContent, ConstraintSet.START, R.id.Messagingcardview, ConstraintSet.END, 0)
            constraintSet.applyTo(holder.Messageconstraint)
        }






    }

}


