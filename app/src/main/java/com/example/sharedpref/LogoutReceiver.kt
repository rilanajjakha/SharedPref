package com.example.sharedpref

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class LogoutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "com.example.sharedpref.ACTION_LOGOUT") {
            val prefManager = PrefManager.getInstance(context)
            prefManager.setLoggedIn(false)
            prefManager.clear()

            val loginIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(loginIntent)

            Toast.makeText(context, "Anda telah logout", Toast.LENGTH_SHORT).show()
        }
    }
}
