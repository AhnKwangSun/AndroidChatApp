package com.kwang.androidchatapp.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kwang.androidchatapp.R
import com.kwang.androidchatapp.classes.ChatMessageLog
import com.kwang.androidchatapp.classes.LatestMessageRow
import com.kwang.androidchatapp.classes.User
import com.kwang.androidchatapp.regLogin.RegisterActivity
import java.util.ArrayList

class LatestMessagesActivity: AppCompatActivity(){
    var singleVerticals = ArrayList<ChatMessageLog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        listenForLatestMessages()

        val uid = FirebaseAuth.getInstance().uid
        if(uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    val latestMessagesMap = HashMap<String, ChatMessageLog>()

    private fun refreshRecyclerViewMessages() {
        var RecyclerView = findViewById(R.id.recyclerview_lateset_messages) as RecyclerView
        var layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        singleVerticals.clear()
        latestMessagesMap.values.forEach {
            singleVerticals.add(it)
            RecyclerView.adapter = LatestMessageRow(singleVerticals, this@LatestMessagesActivity)
            RecyclerView.layoutManager = layoutManager
        }
        RecyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
    }

    private  fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {                                      //EventListener가 자식의 변화를 감지
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {                              //자식의 아이템을 검색 및 추가했을 때 동작
                val chatMessage = p0.getValue(ChatMessageLog::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {                            //자식의 아이템에 변화가 있을때에 동작
                val chatMessage = p0.getValue(ChatMessageLog::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }                         //자식의 아이템의 순서가 바뀌었을때 동작
            override fun onChildRemoved(p0: DataSnapshot) {

            }                                    //자식의 아이템이 삭제되었을때 동작
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}