package com.kwang.androidchatapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kwang.androidchatapp.classes.User
import java.util.ArrayList

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
                        singleVerticals.add(user)
                    }
                }
                RecyclerView.adapter = VerticalAdapter(singleVerticals)
                RecyclerView.layoutManager = layoutManager
            }
            override fun onCancelled(p0: DatabaseError) {

            }


        }
        )
    }

    class VerticalAdapter(data: ArrayList<User>) : RecyclerView.Adapter<VerticalAdapter.MyViewHolder>() {
        internal var data = ArrayList<User>()

        init {
            this.data = data
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vertical_single_row, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            //holder.image.setImageResource(data[position].image)
            holder.name.text = data[position].username
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
            }
        }
    }
}
