package com.kwang.androidchatapp.classes

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kwang.androidchatapp.R
import com.kwang.androidchatapp.message.ChatLogActivity
import com.kwang.androidchatapp.message.NewMessageActivity
import java.util.ArrayList

class LatestMessageRow(data: ArrayList<ChatMessageLog>, context: Context) : RecyclerView.Adapter<LatestMessageRow.MyViewHolder>() {
    internal var data = ArrayList<ChatMessageLog>()
    internal var context: Context
    var chatPartnerUser = ArrayList<User>()

    init {
        this.data = data
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.latest_message_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.image.setImageResource(data[position].image)
        val chatPartnerId: String
        if(FirebaseAuth.getInstance().uid == data[position].fromId) {
            chatPartnerId = data[position].toId
        } else {
            chatPartnerId = data[position].fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser.add(p0.getValue(User::class.java)!!)
                holder.name.text = chatPartnerUser[position].username
                holder.message.text = data[position].text
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //internal var image: ImageView
        internal var name: TextView
        internal var message: TextView

        init {
            //image = itemView.findViewById<View>(R.id.image) as ImageView
            name = itemView.findViewById(R.id.latest_Username) as TextView
            message = itemView.findViewById(R.id.latest_Message) as TextView
            itemView.setOnClickListener {
                var intent = Intent(context,ChatLogActivity::class.java)
                intent.putExtra(NewMessageActivity.USER_KEY, this@LatestMessageRow.chatPartnerUser[adapterPosition])
                context.startActivity(intent)
            }
        }
    }
}