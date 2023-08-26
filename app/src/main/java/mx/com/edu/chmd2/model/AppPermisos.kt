package mx.com.edu.chmd2.model

class AppPermisos : ArrayList<AppPermisosItem>()

data class AppPermisosItem(
    val id_usuario: String,
    val ver_circulares: String,
    val ver_credencial: String,
    val ver_miMaguen: String,
    val ver_user_pwd:String
)