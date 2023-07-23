package mx.com.edu.chmd2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.fondo_video.*
class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        videoView.setOnCompletionListener { videoView.start() }
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_app)
        videoView.setVideoURI(uri)
        videoView.start()
        fabLogin.setOnClickListener {
            //Iniciar sesion
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}