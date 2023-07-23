package mx.com.edu.chmd2.servicios

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.legacy.content.WakefulBroadcastReceiver
import mx.com.edu.chmd2.servicios.CHMDMessagingService
class FCMBroadcastReceiver:WakefulBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val c = ComponentName(context!!.packageName,CHMDMessagingService::class.java.simpleName!!)
        startWakefulService(context!!,intent!!.setComponent(c))
        resultCode = Activity.RESULT_OK

    }
}