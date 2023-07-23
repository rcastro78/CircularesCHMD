package mx.com.edu.chmd2

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.activity_credencial.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.edu.chmd2.networking.CircularesAPI
import mx.com.edu.chmd2.networking.IChmd
import retrofit2.awaitResponse
import java.lang.Exception

class CredencialActivity : AppCompatActivity() {
    private val BASE_URL_FOTO = "http://chmd.chmd.edu.mx:65083/CREDENCIALES/padres/"
    private val BASE_URL_FOTO2 = "https://www.chmd.edu.mx/WebAdminCirculares/ws/"
    private val URL_FIRMA = "https://www.chmd.edu.mx/imagenesapp/img/firma.jpg"
    private var sharedPreferences: SharedPreferences? = null
    lateinit var iChmd: IChmd
    var userId:String=""
    fun configToolbar(){
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val imgHome = toolbar.findViewById<ImageView>(R.id.imgHome)
        imgHome.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credencial)
        configToolbar()
        iChmd = CircularesAPI.getCHMDService()!!
        val SHARED:String=getString(R.string.SHARED_PREF)
        sharedPreferences = getSharedPreferences(SHARED, 0)

        userId = sharedPreferences!!.getString("userId","")!!.toString()
        //userId = "2484"
        getVigencia(userId)
        lblNombrePadre.text = sharedPreferences!!.getString("nombre","")
        lblPadre.text = sharedPreferences!!.getString("responsable","")
        val rutaFoto2 = sharedPreferences!!.getString("nuevaFoto","").toString()
        Glide.with(this)
            .load(URL_FIRMA)
            .placeholder(R.drawable.logo2)
            .error(R.drawable.logo2)
            .into(firma)



        if(rutaFoto2.length>0){
            val urlFoto = BASE_URL_FOTO2+rutaFoto2
            Glide.with(this)
                .load(urlFoto)
                .placeholder(R.drawable.icon_non_profile)
                .error(R.drawable.icon_non_profile)
                //.transform(new Transformacion(90))
                .into(imgFotoPadre)
        }else{
            var foto = sharedPreferences!!.getString("foto","")!!
                .replace("C:\\\\IDCARDDESIGN\\\\CREDENCIALES\\\\padres\\\\","")
            val urlFoto = BASE_URL_FOTO+foto
            Glide.with(this)
                .load(urlFoto)
                .placeholder(R.drawable.icon_non_profile)
                .error(R.drawable.icon_non_profile)
                //.transform(new Transformacion(90))
                .into(imgFotoPadre)
        }
        generarCodigoQRCifrado(sharedPreferences!!.getString("userId","0").toString())

    }

    fun generarCodigoQRCifrado(userId:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getCifrado(userId)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()?.get(0)!!.cifrado
                val bmp = crearQR(result)
                withContext(Dispatchers.Main){
                    imgQR.setImageBitmap(bmp)
                }
            }
        }
    }



    private fun crearQR(str: String?): Bitmap? {
        val result: BitMatrix
        result = try {
            MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE, 200, 200, null
            )
        } catch (iae: IllegalArgumentException) {
            return null
        }
        val w: Int = result.getWidth()
        val h: Int = result.getHeight()
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, 200, 0, 0, w, h)
        return bitmap
    }

    fun getVigencia(userId: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getVigencia(userId)!!.awaitResponse()
            if(response.isSuccessful){
                try {
                    val result = response.body()?.get(0)?.texto
                    withContext(Dispatchers.Main) {
                        lblVigencia.text = result
                    }
                }catch (e:Exception){
                    lblVigencia.text = "Sin Vigencia"

                }
            }
        }
    }

}