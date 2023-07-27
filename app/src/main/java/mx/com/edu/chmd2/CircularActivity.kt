package mx.com.edu.chmd2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_circular.wvwDetalleCircular
import kotlinx.android.synthetic.main.toolbar_circulares.imgMovFav
import kotlinx.android.synthetic.main.toolbar_circulares.rlBack
import kotlinx.android.synthetic.main.toolbar_circulares.rlCalendar
import kotlinx.android.synthetic.main.toolbar_circulares.rlDelete
import kotlinx.android.synthetic.main.toolbar_circulares.rlFav
import kotlinx.android.synthetic.main.toolbar_circulares.rlHome
import kotlinx.android.synthetic.main.toolbar_circulares.rlNext
import kotlinx.android.synthetic.main.toolbar_circulares.rlShare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.edu.chmd2.model.Circular
import mx.com.edu.chmd2.networking.CircularesAPI
import mx.com.edu.chmd2.networking.IChmd
import retrofit2.awaitResponse


class CircularActivity : AppCompatActivity() {
    lateinit var iChmd: IChmd
    lateinit var printJob: PrintJob
    var idx=0
    var idCircular:String=""
    var lstCirculares:ArrayList<Circular> = ArrayList()
    val TODAS=1
    val FILTRA_NL=2
    val FILTRA_FAVS=3
    val FILTRA_ELIMINADAS=4
    var filtro=0
    var userId:String=""
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular)
        iChmd = CircularesAPI.getCHMDService()!!
        idCircular=intent.getStringExtra("idCircular")!!
        idx = idCircular.toInt()
        setLeida(idCircular,userId)
        val esFavorita = intent.getIntExtra("esFavorita",0)
        if(esFavorita==1)
            imgMovFav.setBackgroundResource(R.drawable.estrella_blanca)

        val SHARED:String=getString(R.string.SHARED_PREF)
        sharedPreferences = getSharedPreferences(SHARED, 0)
        wvwDetalleCircular.loadUrl(getString(R.string.BASE_URL) + getString(R.string.PATH) +"getCircularId6.php?id=" + idCircular)
        wvwDetalleCircular.settings.setSupportZoom(true)
        wvwDetalleCircular.settings.builtInZoomControls = true
        wvwDetalleCircular.settings.displayZoomControls = true
        wvwDetalleCircular.settings.domStorageEnabled = true
        wvwDetalleCircular.settings.loadsImagesAutomatically = true
        wvwDetalleCircular.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        filtro = sharedPreferences!!.getInt("filtroIterar",0)
        userId = sharedPreferences!!.getString("userId","")!!.toString()
        //userId = "2484"
        if(filtro==0 || filtro==TODAS)
            getCirculares(userId)

        if(filtro==FILTRA_NL)
            getCircularesNoLeidas(userId)

        if(filtro==FILTRA_FAVS)
            getCircularesFavoritas(userId)

        if(filtro==FILTRA_ELIMINADAS)
            getCircularesEliminadas(userId)

        rlHome.setOnClickListener {
            onBackPressed()
        }
        rlShare.setOnClickListener {
            //print(wvwDetalleCircular)
            share(getString(R.string.BASE_URL) + getString(R.string.PATH) +"getCircularId6.php?id=" + idCircular)
        }
        rlCalendar.setOnClickListener {
            val horaICS = lstCirculares[idx].horaInicialIcs
            if(horaICS != "00:00:00"){

            }
        }



        rlBack.setOnClickListener {
            idx -= 1
            if(idx<1)
                idx = 0
            val idCircular = lstCirculares[idx].idCircular
            if(lstCirculares[idx].favorita==1){
                imgMovFav.setBackgroundResource(R.drawable.estrella_blanca)
            }else{
                imgMovFav.setBackgroundResource(R.drawable.estrella_fav_icono)
            }
            setLeida(idCircular,userId)
            wvwDetalleCircular.loadUrl(getString(R.string.BASE_URL) + getString(R.string.PATH) +"getCircularId6.php?id=" + idCircular)


        }
        rlNext.setOnClickListener {
            idx += 1
            if(idx>lstCirculares.size-1)
                idx = lstCirculares.size-1
            val idCircular = lstCirculares[idx].idCircular
            if(lstCirculares[idx].favorita==1){
                imgMovFav.setBackgroundResource(R.drawable.estrella_blanca)
            }else{
                imgMovFav.setBackgroundResource(R.drawable.estrella_fav_icono)
            }
            setLeida(idCircular,userId)
            wvwDetalleCircular.loadUrl(getString(R.string.BASE_URL) + getString(R.string.PATH) +"getCircularId6.php?id=" + idCircular)

        }

        rlFav.setOnClickListener {
            if(lstCirculares[idx].favorita==1){
                lstCirculares[idx].favorita = 0
                noFavCircular(lstCirculares[idx].idCircular,userId)
                imgMovFav.setBackgroundResource(R.drawable.estrella_fav_icono)
            }else{
                lstCirculares[idx].favorita = 1
                favCircular(lstCirculares[idx].idCircular,userId)
                imgMovFav.setBackgroundResource(R.drawable.estrella_blanca)
            }
        }

        rlDelete.setOnClickListener {
            eliminarCircular(idCircular,userId)
        }

    }

    fun setFavorita(idCircular:String,usuario: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.favCircular(idCircular,usuario)!!.awaitResponse()
            if(response.isSuccessful){
                Log.d("FAVORITA",response.body().toString())
            }
        }
    }

    fun setLeida(idCircular:String,usuario: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.leerCircular(idCircular,usuario)!!.awaitResponse()
            if(response.isSuccessful){
                Log.d("FAVORITA",response.body().toString())
            }
        }
    }

    fun eliminarCircular(idCircular:String,usuario: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.eliminarCircular(idCircular,usuario)!!.awaitResponse()
            if(response.isSuccessful){
                Log.d("ELIMINADA",response.body().toString())
                withContext(Dispatchers.Main){
                    idx += 1
                    if(idx>lstCirculares.size-1)
                        idx = lstCirculares.size-1
                    val idCircular = lstCirculares[idx].idCircular
                    setLeida(idCircular,userId)
                    wvwDetalleCircular.loadUrl(getString(R.string.BASE_URL) + getString(R.string.PATH) +"getCircularId6.php?id=" + idCircular)

                }
            }
        }
    }


    fun getCirculares(usuario:String){
        lstCirculares.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getCirculares(usuario)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()!!
                result.forEach { c->
                    val circular = Circular(c!!.id,c.titulo,"","",c.fecha,c.fecha_programada,
                        c.estatus,c.leido.toInt(),c.favorito.toInt(),"",c.tema_ics,c.fecha_ics,c.hora_inicial_ics,
                        c.hora_final_ics,c.ubicacion_ics,c.adjunto.toInt(),c.nivel,c.adm,false)
                    lstCirculares.add(circular)
                }
                idx = lstCirculares.indexOfFirst {
                    it.idCircular == idCircular
                }
            }
        }
    }
    fun getCircularesFavoritas(usuario:String){
        lstCirculares.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getCirculares(usuario)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()!!
                result.forEach { c->
                    if(c!!.favorito=="1") {
                        val circular = Circular(
                            c!!.id,
                            c.titulo,
                            "",
                            "",
                            c.fecha,
                            c.fecha_programada,
                            c.estatus,
                            c.leido.toInt(),
                            c.favorito.toInt(),
                            "",
                            c.tema_ics,
                            c.fecha_ics,
                            c.hora_inicial_ics,
                            c.hora_final_ics,
                            c.ubicacion_ics,
                            c.adjunto.toInt(),
                            c.nivel,
                            c.adm,
                        false
                        )
                        lstCirculares.add(circular)
                    }

                }
                idx = lstCirculares.indexOfFirst {
                    it.idCircular == idCircular
                }
            }
        }
    }
    fun getCircularesNoLeidas(usuario:String){
        lstCirculares.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getCirculares(usuario)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()!!
                result.forEach { c->
                    if(c!!.leido=="0" && c!!.eliminado=="0") {
                        val circular = Circular(
                            c!!.id,
                            c.titulo,
                            "",
                            "",
                            c.fecha,
                            c.fecha_programada,
                            c.estatus,
                            c.leido.toInt(),
                            c.favorito.toInt(),
                            "",
                            c.tema_ics,
                            c.fecha_ics,
                            c.hora_inicial_ics,
                            c.hora_final_ics,
                            c.ubicacion_ics,
                            c.adjunto.toInt(),
                            c.nivel,
                            c.adm,
                            false
                        )
                        lstCirculares.add(circular)
                    }

                }
                idx = lstCirculares.indexOfFirst {
                    it.idCircular == idCircular
                }
            }
        }
    }
    fun getCircularesEliminadas(usuario:String){
        lstCirculares.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getCirculares(usuario)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()!!
                result.forEach { c->
                    if(c!!.eliminado=="1") {
                        val circular = Circular(
                            c!!.id,
                            c.titulo,
                            "",
                            "",
                            c.fecha,
                            c.fecha_programada,
                            c.estatus,
                            c.leido.toInt(),
                            c.favorito.toInt(),
                            "",
                            c.tema_ics,
                            c.fecha_ics,
                            c.hora_inicial_ics,
                            c.hora_final_ics,
                            c.ubicacion_ics,
                            c.adjunto.toInt(),
                            c.nivel,
                            c.adm,
                            false
                        )
                        lstCirculares.add(circular)
                    }

                }
                idx = lstCirculares.indexOfFirst {
                    it.idCircular == idCircular
                }
            }
        }
    }

    fun noFavCircular(idCircular: String, usuario: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.elimFavCircular(idCircular,usuario)!!.awaitResponse()
            if(response.isSuccessful){
                withContext(Dispatchers.Main){

                }
            }
        }
    }

    fun favCircular(idCircular: String, usuario: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.favCircular(idCircular,usuario)!!.awaitResponse()
            if(response.isSuccessful){
                withContext(Dispatchers.Main){

                }
            }
        }
    }

    fun share(u:String){
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_SUBJECT, "Comparto la circular")
        share.putExtra(Intent.EXTRA_TEXT, u)
        startActivity(Intent.createChooser(share, "Compartir esta circular"))

    }
    fun print(webView: WebView){
        val printManager = this
            .getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = idCircular + "-webpage-" + webView.url
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        printJob = printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )




    }
}