package com.kwang.androidchatapp.message

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kwang.androidchatapp.R
import com.kwang.androidchatapp.classes.MessageLog
import com.kwang.androidchatapp.classes.User
import java.util.ArrayList

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = username

        var RecyclerView = findViewById(R.id.recyclerview_chat_log) as RecyclerView
        var layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        val message = ArrayList<MessageLog>()
        message.add(MessageLog("메세지를 많이많이많이많이많이많이많이많이 남겼습니다","1"))
        message.add(MessageLog("메세지를 입력해주세요","2"))
        message.add(MessageLog("메세지를 많이많이많이많이많이많이많이많이 남겼습니다","1"))
        message.add(MessageLog("메세지를 입력해주세요","2"))

        RecyclerView.adapter = VerticalAdapter(message,this@ChatLogActivity)
        RecyclerView.layoutManager = layoutManager

    }
    class VerticalAdapter(data: ArrayList<MessageLog>, context: Context) : RecyclerView.Adapter<VerticalAdapter.MyViewHolder>() {
        private val TO = 1
        private val FROM = 2
        internal var data = ArrayList<MessageLog>()
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
                holder.fromMessage.text = data[position].message
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun getItemViewType(position: Int): Int {
            if (data[position].flag == "1") return TO
            else return FROM
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
