package mx.com.edu.chmd2

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_mi_maguen.wvwMaguen

class MiMaguenActivity : AppCompatActivity() {

    fun configToolbar(){
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val imgHome = toolbar.findViewById<ImageView>(R.id.imgHome)
        imgHome.setOnClickListener {
            onBackPressed()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_maguen)
        configToolbar()

        val url = "https://www.chmd.edu.mx/pruebascd/icloud/"
        val ua = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0"

        wvwMaguen.settings.setSupportZoom(true)
        wvwMaguen.settings.builtInZoomControls = true
        wvwMaguen.settings.displayZoomControls = true
        wvwMaguen.settings.domStorageEnabled = true
        wvwMaguen.settings.loadsImagesAutomatically = true
        wvwMaguen.settings.userAgentString = ua
        wvwMaguen.settings.javaScriptEnabled = true
        wvwMaguen.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        wvwMaguen.loadUrl(url)
        wvwMaguen.webViewClient = object : WebViewClient() {
            var authComplete=false
            var authCode=""
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url!!.contains("?code=") && !authComplete) {
                    val uri = Uri.parse(url)
                    authCode = uri.getQueryParameter("code").toString()
                    authComplete = true
                } else {
                    //Toast.makeText(getApplicationContext(),"ERROR:"+url.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }



    }
}