package com.kwang.androidchatapp.message

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kwang.androidchatapp.R
import com.kwang.androidchatapp.classes.User
import com.xwray.groupie.GroupAdapter
import java.util.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val singleVerticals = ArrayList<User>()
        var RecyclerView = findViewById(R.id.recyclerview_newmessage) as RecyclerView
        var layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("NewMessage",it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        if(user.uid == FirebaseAuth.getInstance().uid) {
                            singleVerticals.add(User(user.uid, "나", user.userEmail))
                            Collections.swap(singleVerticals, 0, singleVerticals.lastIndex)
                        }
                        else singleVerticals.add(user)
                    }
                }
                RecyclerView.adapter = VerticalAdapter(singleVerticals,this@NewMessageActivity)
                RecyclerView.layoutManager = layoutManager
            }
            override fun onCancelled(p0: DatabaseError) {

            }


        }
        )
    }

    companion object {
        val USER_KEY = "UESR_KEY"
    }


    class VerticalAdapter(data: ArrayList<User>, context: Context) : RecyclerView.Adapter<VerticalAdapter.MyViewHolder>() {
        internal var data = ArrayList<User>()
        internal var context:Context

        init {
            this.data = data
            this.context = context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vertical_single_row, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            //holder.image.setImageResource(data[position].image)
            if(FirebaseAuth.getInstance().uid == data[position].uid) holder.name.text = "나"
            else holder.name.text = data[position].username
            holder.email.text = data[position].userEmail
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //internal var image: ImageView
            internal var name: TextView
            internal var email: TextView

            init {
                //image = itemView.findViewById<View>(R.id.image) as ImageView
                name = itemView.findViewById(R.id.name) as TextView
                email = itemView.findViewById(R.id.email) as TextView
                itemView.setOnClickListener {
                    var intent = Intent(context,ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,data[adapterPosition])
                    context.startActivity(intent)
                }
            }
        }
    }
}
