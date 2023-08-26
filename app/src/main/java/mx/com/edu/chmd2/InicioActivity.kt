package mx.com.edu.chmd2

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.fondo_video.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.edu.chmd2.networking.CircularesAPI
import mx.com.edu.chmd2.networking.IChmd
import retrofit2.awaitResponse

class InicioActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null
    lateinit var iChmd: IChmd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val SHARED:String=getString(R.string.SHARED_PREF)
        sharedPreferences = getSharedPreferences(SHARED, 0)
        val userId = sharedPreferences!!.getString("userId","0")
        iChmd = CircularesAPI.getCHMDService()!!

        videoView.setOnCompletionListener { videoView.start() }
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_app)
        videoView.setVideoURI(uri)
        videoView.start()

        object : CountDownTimer(4000, 1000) {
            override fun onTick(p0: Long) {

            }
            override fun onFinish() {
                getPermisos(userId!!)
                Intent(this@InicioActivity,LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }.start()



    }



    fun getPermisos(idUsuario: String){
        val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getPermisos(idUsuario)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()!!
                try {
                    editor.putString("verCirculares", result[0].ver_circulares)
                    editor.putString("verCredencial", result[0].ver_credencial)
                    editor.putString("verMaguen", result[0].ver_miMaguen)
                    editor.putString("verUserPwd",result[0].ver_user_pwd)
                    editor.apply()
                }catch (e:java.lang.Exception){
                    editor.putString("verCirculares", "1")
                    editor.putString("verCredencial", "1")
                    editor.putString("verMaguen", "1")
                    editor.putString("verUserPwd","1")
                    editor.apply()
                }
            }else{
                Log.d("PERMISOS","error ${response.code()}")
                editor.putString("verCirculares", "1")
                editor.putString("verCredencial", "1")
                editor.putString("verMaguen", "1")
                editor.putString("verUserPwd","1")
                editor.apply()
            }
        }
    }
}