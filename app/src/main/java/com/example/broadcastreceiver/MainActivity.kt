package com.example.broadcastreceiver

import android.app.Activity
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.KeyEventDispatcher.Component

class MainActivity : Activity() {

    private lateinit var receiver: ItemNotifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "Receiver Created!", Toast.LENGTH_LONG).show()
        receiver = ItemNotifier()
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "register Receiver")
        registerReceiver(
            receiver,
            IntentFilter("com.example.smb1.action.AddActivity")
        )
    }


}