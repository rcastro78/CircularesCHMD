package mx.com.edu.chmd2.model

class Vigencia : ArrayList<VigenciaItem>()
data class VigenciaItem(
    val id: String,
    val texto: String,
    val vigencia: String
)