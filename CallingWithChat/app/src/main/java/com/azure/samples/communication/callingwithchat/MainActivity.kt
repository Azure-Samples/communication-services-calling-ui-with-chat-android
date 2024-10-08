package com.azure.samples.communication.callingwithchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.demoGroupButton).setOnClickListener {
            startActivity(Intent(this, GroupCallWithChatActivity::class.java))
        }

        findViewById<Button>(R.id.demoTeamsButton).setOnClickListener {
            startActivity(Intent(this, TeamsMeetingActivity::class.java))
        }
    }
}