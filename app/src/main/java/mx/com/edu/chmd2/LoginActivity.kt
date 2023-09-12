package mx.com.edu.chmd2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.lblVersion
import kotlinx.android.synthetic.main.activity_login.rlLoginGoogle
import kotlinx.android.synthetic.main.activity_login.txtEmail
import kotlinx.android.synthetic.main.activity_login.txtPassword
import kotlinx.android.synthetic.main.fondo_video.videoView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.edu.chmd2.networking.CircularesAPI
import mx.com.edu.chmd2.networking.IChmd
import retrofit2.awaitResponse


class LoginActivity : AppCompatActivity() {
    lateinit var iChmd: IChmd
    private var sharedPreferences: SharedPreferences? = null
    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN=99
    fun injectFields(){
        txtEmail.setText("programador@chmd.edu.mx")
        txtPassword.setText("1463")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pInfo: PackageInfo =
            this.packageManager.getPackageInfo(this.packageName, 0)
        val version = pInfo.versionName
        lblVersion.text = "Versión: "+pInfo.versionName
        iChmd = CircularesAPI.getCHMDService()!!
        //injectFields()
        val SHARED:String=getString(R.string.SHARED_PREF)
        sharedPreferences = getSharedPreferences(SHARED, 0)
        val loggedIn = sharedPreferences!!.getBoolean("loggedIn",false)
        val correo = sharedPreferences!!.getString("correo","")
        val verUserPwd = sharedPreferences!!.getString("verUserPwd","1")
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        validarCuenta(correo!!,loggedIn)


        Log.d("VER_C",verUserPwd.toString())
        if(verUserPwd=="0"){
            txtEmail.visibility = View.GONE
            txtPassword.visibility = View.GONE
            btnLogin.visibility = View.GONE
        }else{
            txtEmail.visibility = View.VISIBLE
            txtPassword.visibility = View.VISIBLE
            btnLogin.visibility = View.VISIBLE
        }

        videoView.setOnCompletionListener { videoView.start() }
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_app)
        videoView.setVideoURI(uri)
        videoView.start()
        //injectFields()
        btnLogin.setOnClickListener{
            val correo = txtEmail.text.toString()
            val pwd = txtPassword.text.toString()
            if(correo.isNotEmpty() && pwd.isNotEmpty())
                login(correo,pwd)
        }
        rlLoginGoogle.setOnClickListener {
            signIn()
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
                        if(result.isEmpty()){
                            withContext(Dispatchers.Main){
                                Toast.makeText(applicationContext,"Credenciales incorrectas",Toast.LENGTH_LONG).show()
                            }

                        }

                    }
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(applicationContext,"Credenciales incorrectas",Toast.LENGTH_LONG).show()
                    }

                }
            }catch (e:Exception){
                Log.e("LOGIN",e.message.toString())
            }
        }




    }

    fun login(correo:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getUsuario(correo)!!.awaitResponse()
            Log.d("RESPONSE",response.code().toString())

            if(response.code()==400){
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext,"No puedes iniciar sesión",Toast.LENGTH_LONG).show()
                }


            }

            try {
                if (response.code()==200) {
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
                            }else{
                                withContext(Dispatchers.Main){
                                    Toast.makeText(applicationContext,"Cuenta no registrada",Toast.LENGTH_LONG).show()
                                }

                            }


                            Intent(this@LoginActivity,TodasCircularesActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }


                        }
                        if(result.isEmpty()){
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    applicationContext,
                                    "Cuenta no registrada",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }
                }else{
                    mGoogleSignInClient.signOut()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Cuenta no registrada",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


            }catch (e:Exception){
                Log.e("LOGIN",e.message.toString())
            }
        }




    }


    fun validarCuenta(correo: String,loggedIn:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.validarCuenta(correo)!!.awaitResponse()
            Log.d("VALIDEZ",response.body().toString())
            if(response.isSuccessful){
                val existe = response.body()?.get(0)?.existe
                withContext(Dispatchers.Main){
                    if(existe=="1" && loggedIn)
                        Intent(this@LoginActivity,TodasCircularesActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                }

            }
        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(
                    data!!
                )
                handleSignInResult(result!!)
            } catch (ex: java.lang.Exception) {
                Log.d("ERROR_SIGNIN",ex.localizedMessage.toString())
                Toast.makeText(applicationContext, "Inicio de sesión cancelado", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d("RESULTADO",result.toString())
        if (result.isSuccess) {
            val account = result.signInAccount

            val editor = sharedPreferences!!.edit()
            editor.putString("correoRegistrado", account!!.email)
            editor.putString("nombre", account.displayName)
            Log.d("RESULTADO",account.email.toString())
            Log.d("RESULTADO",account.email.toString())
            var userPic = ""
            //Al venir la pic vacía daba error, se cerraba luego de escoger la cuenta.
            userPic = try {
                account.photoUrl.toString()
            } catch (ex: java.lang.Exception) {
                ""
            }

            editor.putString("userPic", userPic)
            editor.putString("idToken", account.idToken)
            editor.apply()
            //val intent = Intent(this@LoginActivity, ValidarPadreActivity::class.java)
            //startActivity(intent)
            login(account.email!!)

        } else {
            //Log.w(TAG, "No se pudo iniciar sesión");
            Toast.makeText(applicationContext, "No puedes iniciar sesión" + result.status.statusMessage, Toast.LENGTH_LONG)
                .show()
            //Log.w(TAG, result.getStatus().getStatusMessage());
            Log.w("RESULTADO", result.status.statusCode.toString() + "")
        }
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