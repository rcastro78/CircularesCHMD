package mx.com.edu.chmd2.networking

class CircularesAPI {
    companion object {
        private const val WEBSERVICE_TRANSPORTE_URL = "https://www.chmd.edu.mx/WebAdminCirculares/ws/"
        fun getCHMDService(): IChmd? {
            return RetrofitClient.getClient(WEBSERVICE_TRANSPORTE_URL)?.create(IChmd::class.java)
        }

    }
}