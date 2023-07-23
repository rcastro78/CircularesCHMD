package mx.com.edu.chmd2

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.txtEmail
import kotlinx.android.synthetic.main.activity_login.txtPassword
import kotlinx.android.synthetic.main.fondo_video.videoView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.edu.chmd2.db.ChmdDB
import mx.com.edu.chmd2.db.CircularDAO
import mx.com.edu.chmd2.model.Circular
import mx.com.edu.chmd2.model.Usuario
import mx.com.edu.chmd2.networking.CircularesAPI
import mx.com.edu.chmd2.networking.IChmd
import retrofit2.awaitResponse
import java.lang.Exception
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    lateinit var iChmd: IChmd
    private var sharedPreferences: SharedPreferences? = null
    fun injectFields(){
        txtEmail.setText("programador@chmd.edu.mx")
        txtPassword.setText("1463")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        iChmd = CircularesAPI.getCHMDService()!!
        val SHARED:String=getString(R.string.SHARED_PREF)
        sharedPreferences = getSharedPreferences(SHARED, 0)
       /* if(sharedPreferences!!.getBoolean("loggedIn",false)){
            Intent(this@LoginActivity,TodasCircularesActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }*/
        videoView.setOnCompletionListener { videoView.start() }
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_app)
        videoView.setVideoURI(uri)
        videoView.start()
        injectFields()
        btnLogin.setOnClickListener{
            val correo = txtEmail.text.toString()
            val pwd = txtPassword.text.toString()
            if(correo.isNotEmpty() && pwd.isNotEmpty())
                login(correo,pwd)
        }



    }

    fun login(correo:String, pwd:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.iniciarSesion(correo,pwd)!!.awaitResponse()
            try {
                if (response.isSuccessful) {
                    val result = response.body()
                    withContext(Dispatchers.Main) {
                        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                        result!!.forEach { r->
                            editor.putString("correo", r.correo)
                            editor.putString("userId", r.id)
                            editor.putString("familia", r.familia)
                            editor.putString("vigencia", r.vigencia)
                            editor.putString("nombre", r.nombre)
                            editor.putString("responsable",r.responsable)
                            editor.putString("foto",r.fotografia)
                            editor.putBoolean("loggedIn",true)
                            if(r.nuevaFoto.isNotEmpty()) {
                                editor.putString("nuevaFoto", r.nuevaFoto)
                            }else{
                                editor.putString("nuevaFoto", "")
                            }
                            editor.apply()
                            if (r.id.isNotEmpty()) {
                                getPermisos(r.id)
                            }


                                Intent(this@LoginActivity,TodasCircularesActivity::class.java).also {
                                    startActivity(it)
                                    finish()
                                }


                        }

                    }
                }
            }catch (e:Exception){
                Log.e("LOGIN",e.message.toString())
            }
        }




    }
    /*
    fun getCirculares(usuario:String){
        val db = ChmdDB.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            db.iCircularDAO.eliminaTodasCirculares()
            val response = iChmd.getCirculares(usuario)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()!!
                result.forEach { c->
                    if(c!!.eliminado=="0") {
                        val circular = CircularDAO(0,c.id, c.titulo,
                            c.leido.toInt(),c.favorito.toInt(),c.eliminado.toInt(),
                            c.id_usuario,c.created_at,c.fecha_ics,c.adjunto,c.nivel)
                        db.iCircularDAO.insert(circular)
                    }

                }
                withContext(Dispatchers.Main){
                    Intent(this@LoginActivity,TodasCircularesActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }

            }
        }
    }
*/

    fun getPermisos(idUsuario: String){
        val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
       CoroutineScope(Dispatchers.IO).launch {
           val response = iChmd.getPermisos(idUsuario)!!.awaitResponse()
           if(response.isSuccessful){
               val result = response.body()!!

               editor.putString("verCirculares",result[0].ver_circulares)
               editor.putString("verCredencial",result[0].ver_credencial)
               editor.putString("verMaguen",result[0].ver_miMaguen)
               editor.apply()
           }else{
               Log.d("PERMISOS","error ${response.code()}")
           }
       }
    }
}