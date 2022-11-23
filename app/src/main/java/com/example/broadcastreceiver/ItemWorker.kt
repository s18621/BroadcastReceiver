package com.example.broadcastreceiver

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.*
import androidx.work.Worker
import androidx.work.WorkerParameters


class ItemWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun doWork(): Result {
        try {
            Log.i("ItemWorker", "ItemWorker -- Started")
            val itemId = inputData.getLong("itemID", 0)
            val itemName = inputData.getString("itemName")
            val channelId = inputData.getString("channelID") ?: ""
            val price = inputData.getFloat("price", 0f)
            val quantity = inputData.getInt("quantity", 0)
            val bought = inputData.getBoolean("bought", false)

            Log.i("ItemWorker", "ItemWorker vals: $itemId, $itemName, $price, $quantity, $bought")
            //Przepustka
            val addItemIntent = Intent().also {
                it.component = ComponentName("com.example.smb1", "com.example.smb1.AddActivity")
                it.putExtra("itemID", itemId)
                it.putExtra("channelID", "ItemAddChannel")
                it.putExtra("itemName", itemName)
                it.putExtra("price", price)
                it.putExtra("quantity", quantity)
                it.putExtra("bought", bought)
            }
            Log.i("ItemWorker", "ItemWorker vals after intent: $itemId, $itemName, $price, $quantity, $bought")
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                1,
                addItemIntent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            Log.i("ItemWorker", "ItemWorker Intent: $addItemIntent")

            //Notyfikacja
            val notification = NotificationCompat.Builder(
                applicationContext,
                channelId,
            ).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("New Item $itemName Added")
                .setContentText("With ID: $itemId")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            //Pokazujemy
            from(applicationContext).notify(1, notification)

        }catch (e: java.lang.Exception){
            Log.e("Error", e.printStackTrace().toString())
            return Result.failure()
        }
        return Result.success()

    }
}