package com.example.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlin.system.exitProcess

class ItemNotifier : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("ItemNotifier", "ItemNotifier ${intent.action.toString()} ")

        if (intent.action == "com.example.smb1.action.AddActivity") {

            //Kanał notyfikacji
            Log.i("ItemNotifier", "ItemNotifier inside IF")

            val channelId = createChannel(context)
            val itemId = intent.getLongExtra("itemID",0)
            val itemName = intent.getStringExtra("itemName")
            val quantity = intent.getIntExtra("quantity", 0)
            val price = intent.getFloatExtra("price", 0f)
            val bought = intent.getBooleanExtra("bought", false)


            val data = workDataOf("itemID" to itemId, "itemName" to itemName, "channelID" to channelId, "quantity" to quantity, "price" to price, "bought" to bought, "channelID" to channelId)

            val worker = OneTimeWorkRequestBuilder<ItemWorker>().setInputData(data).build()

            WorkManager.getInstance(context).enqueue(worker)

        } else {
            Toast.makeText(context, "Error in Action!", Toast.LENGTH_LONG).show()
        }
    }

    private fun createChannel(context: Context): String {
        val id = "ItemAddChannel"
        val channel = NotificationChannel(
            id,
            "Item Add Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        Log.i("ItemNotifier", "ItemNotifier create Channel with id : $id")
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
        return id
    }

}