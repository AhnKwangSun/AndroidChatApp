package com.kwang.androidchatapp.classes

class ChatMessageLog(val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long) {
    constructor() : this("","","","",-1)
}