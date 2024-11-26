package com.example.sharedpref

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sharedpref.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private val usernameData = "tes"
    private val passwordData = "123"
    private val channelId = "bel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()
        with(binding) {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (username == usernameData && password == passwordData) {
                        prefManager.setLoggedIn(true)
                        prefManager.saveUsername(username)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(this@MainActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                checkLoginStatus()
            }
            btnClear.setOnClickListener{
                prefManager.clear()
                checkLoginStatus()
            }

            // notifikasi
            btnNotif.setOnClickListener{
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                }
                else {
                    0
                }
                val intent = Intent(this@MainActivity, MainActivity::class.java)

                val pendingIntent = PendingIntent.getActivity(
                    this@MainActivity,
                    0,
                    intent,
                    flag
                )

                val logoutIntent = Intent(this@MainActivity, LogoutReceiver::class.java).apply {
                    action = "com.example.sharedpref.ACTION_LOGOUT"
                }

                val logoutPendingIntent = PendingIntent.getBroadcast(
                    this@MainActivity,
                    1,
                    logoutIntent,
                    flag
                )

                // NotificationCompat
                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifikasi PPPB")
                    .setContentText("Hallo from other side")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(0, "Logout", logoutPendingIntent)

                // Mengelola notificationManager
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        channelId,
                        "Notif PPPB",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    with(notificationManager) {
                        createNotificationChannel(notificationChannel)
                        notify(0, builder.build())
                    }
                } else {
                    notificationManager.notify(0, builder.build())
                }
            }
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            binding.llLogged.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE
        }else{
            binding.llLogged.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE
        }
    }
}


