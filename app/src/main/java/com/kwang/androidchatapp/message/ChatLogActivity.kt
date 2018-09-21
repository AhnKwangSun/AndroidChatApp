package com.kwang.androidchatapp.message

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.kwang.androidchatapp.R
import com.kwang.androidchatapp.classes.ChatMessageLog
import com.kwang.androidchatapp.classes.User
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.util.ArrayList

class ChatLogActivity : AppCompatActivity() {


    val message = ArrayList<ChatMessageLog>()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        setupDummyData()
        listenForMessagees()

        send_button_chat_log.setOnClickListener {
            Log.d("ChatLog","Attempt to send message...")
            if(chat_log_editext.text.toString() == "") return@setOnClickListener
            else performSendMessage()
        }
    }

    private fun listenForMessagees() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessageLog::class.java)
                val RecyclerView = findViewById(R.id.recyclerview_chat_log) as RecyclerView
                val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)


                if(chatMessage!=null) {
                    Log.d("ChatLog", chatMessage?.text)
                    message.add(chatMessage)
                }

                RecyclerView.adapter = VerticalAdapter(message,this@ChatLogActivity)
                RecyclerView.layoutManager = layoutManager
                recyclerview_chat_log.scrollToPosition(RecyclerView.adapter.itemCount-1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {


            }
        })
    }

    private fun performSendMessage() {
        // 파이어베이스에서 메세지보내는 함수
        val fromId = FirebaseAuth.getInstance().uid
        val text = chat_log_editext.text.toString()
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if (fromId == null) return

        //val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage = ChatMessageLog(ref.key!!,text, fromId!!, toId,System.currentTimeMillis() / 1000)
        ref.setValue(chatMessage).addOnSuccessListener {
            Log.d("ChatLog","Saved our chat messagee: ${ref.key}")
            chat_log_editext.text.clear()
            recyclerview_chat_log.scrollToPosition((findViewById(R.id.recyclerview_chat_log) as RecyclerView).adapter.itemCount-1)
        }
        toRef.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)
        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }

    private fun setupDummyData(){
        /*
        var RecyclerView = findViewById(R.id.recyclerview_chat_log) as RecyclerView
        var layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        val message = ArrayList<MessageLog>()
        message.add(MessageLog("메세지를 많이많이많이많이많이많이많이많이 남겼습니다","1"))
        message.add(MessageLog("메세지를 입력해주세요","2"))
        message.add(MessageLog("메세지를 많이많이많이많이많이많이많이많이 남겼습니다","1"))
        message.add(MessageLog("메세지를 입력해주세요","2"))

        RecyclerView.adapter = VerticalAdapter(message,this@ChatLogActivity)
        RecyclerView.layoutManager = layoutManager*/
    }

    class VerticalAdapter(data: ArrayList<ChatMessageLog>, context: Context) : RecyclerView.Adapter<VerticalAdapter.MyViewHolder>() {
        private val TO = 1
        private val FROM = 2
        internal var data = ArrayList<ChatMessageLog>()
        internal var context: Context

        init {
            this.data = data
            this.context = context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            if(viewType == TO) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.char_to_row, parent, false)
                return MyViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.char_from_row, parent, false)
                return MyViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                holder.fromMessage.text = data[position].text
        }

        override fun getItemCount(): Int {
            return data.size
        }


        override fun getItemViewType(position: Int): Int {
            val fromId = FirebaseAuth.getInstance().uid
            if (data[position].fromId == fromId) return FROM
            else return TO
        }



        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //internal var image: ImageView
            internal var fromMessage: TextView

            init {
                fromMessage = itemView.findViewById(R.id.Message) as TextView
                //toMessage = itemView.findViewById(R.id.to_Message) as TextView    // 서로 다른 id로 넘기려했을 때 충돌났음. 기억해둘 것
            }
        }

    }
}
