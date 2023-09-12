package mx.com.edu.chmd2.networking

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object{
        private var retrofit: Retrofit? = null
        fun getClient(url: String?): Retrofit? {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            if (retrofit == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()


                retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }
    }


    class ConnectivityCheckingInterceptor(
        private val connectivityManager: ConnectivityManager
    ) : Interceptor, ConnectivityManager.NetworkCallback() {

        private var online = false

        init {
            if (Build.VERSION.SDK_INT >= 24) {
                connectivityManager.registerDefaultNetworkCallback(this)
            }
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            if (Build.VERSION.SDK_INT < 24) {
                online = connectivityManager.activeNetworkInfo?.isConnected ?: false
            }

            if (online) {
                return chain.proceed(chain.request())
            } else {
                throw IOException("Internet connection is unavailable")
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            capabilities: NetworkCapabilities
        ) {
            online = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }



}