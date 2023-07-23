package mx.com.edu.chmd2.servicios

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.edu.chmd2.R
import mx.com.edu.chmd2.TodasCircularesActivity
import mx.com.edu.chmd2.networking.CircularesAPI
import mx.com.edu.chmd2.networking.IChmd
import retrofit2.awaitResponse

class CHMDMessagingService:FirebaseMessagingService() {
    private var ADMIN_CHANNEL_ID="chmd_channel"
    val tag = CHMDMessagingService::class.simpleName
    lateinit var iChmd: IChmd
    override fun onCreate() {
        super.onCreate()
        iChmd = CircularesAPI.getCHMDService()!!
        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "my_channel_01"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "CHMD",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TOKEN",token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(tag, message.from.toString())
        val notificationIntent = Intent(applicationContext,TodasCircularesActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pi:PendingIntent = PendingIntent.getActivity(applicationContext,0,notificationIntent,PendingIntent.FLAG_IMMUTABLE)
        val intent = Intent().apply {
            action = "Yes"
            putExtra("Aceptar",true)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        }
        val pi1:PendingIntent = PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_IMMUTABLE)

        val mBuilder:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,ADMIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.mi_maguen)
            .setContentTitle("Circulares")
            .setAutoCancel(true)
            .setContentIntent(pi)
            //Falta el add action

        val mNotificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val nChannel = NotificationChannel(ADMIN_CHANNEL_ID,"CHMD_CHANNEL",importance)
            mBuilder.setChannelId(ADMIN_CHANNEL_ID)
            mNotificationManager.createNotificationChannel(nChannel)
        }
        mNotificationManager.notify(System.currentTimeMillis().toInt(),mBuilder.build())
        if(message.data.isNotEmpty()){
            val t = message.data["title"]
            sendNotification(message)
        }

    }

    fun sendNotification(r:RemoteMessage){
        var pi:PendingIntent?=null
        val intent = Intent(this,TodasCircularesActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK

        }
        pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val nBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.mi_maguen)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .setNumber(1)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        val nm:NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(0,nBuilder.build())

    }




}