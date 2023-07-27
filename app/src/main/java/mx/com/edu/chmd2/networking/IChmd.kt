package mx.com.edu.chmd2.networking

import mx.com.edu.chmd2.model.AppPermisos
import mx.com.edu.chmd2.model.Cifrado
import mx.com.edu.chmd2.model.Circulares
import mx.com.edu.chmd2.model.InicioSesion
import mx.com.edu.chmd2.model.InicioSesionItem
import mx.com.edu.chmd2.model.Vigencia
import org.checkerframework.framework.qual.FromStubFile
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IChmd {


    @GET("validarEmail.php")
    fun validarCuenta(@Query("correo") correo: String?): Call<String?>?

    @GET("cifrar.php")
    fun getCifrado(@Query("idUsuario") idUsuario: String?): Call<Cifrado?>?

    @GET("getUsuarioEmail.php")
    fun iniciarSesion(@Query("correo") correo: String?,@Query("pwd") pwd: String?): Call<InicioSesion?>?

    @GET("getUsuarioEmail.php")
    fun getUsuario(@Query("correo") correo: String?): Call<InicioSesion?>?

    @FormUrlEncoded
    @POST("leerCircular.php")
    fun leerCircular(
        @Field("circular_id") circular_id: String?,
        @Field("usuario_id") usuario_id: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("noleerCircular.php")
    fun noLeerCircular(
        @Field("circular_id") circular_id: String?,
        @Field("usuario_id") usuario_id: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("eliminarCircular.php")
    fun eliminarCircular(
        @Field("circular_id") circular_id: String?,
        @Field("usuario_id") usuario_id: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("elimFavCircular.php")
    fun elimFavCircular(
        @Field("circular_id") circular_id: String?,
        @Field("usuario_id") usuario_id: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("favCircular.php")
    fun favCircular(
        @Field("circular_id") circular_id: String?,
        @Field("usuario_id") usuario_id: String?
    ): Call<String?>?

    @GET("getCirculares_iOS.php")
    fun getCirculares(@Query("usuario_id") usuario_id: String?): Call<List<Circulares?>?>?

    @FormUrlEncoded
    @POST("actualizaFoto.php")
    fun postActualizarFoto(
        @Field("image") encodedImage: String?,
        @Field("usuario_id") usuario_id: String?
    ): Call<Void?>?

    @GET("getVigencia.php")
    fun getVigencia(@Query("idUsuario") idUsuario: String?):Call<Vigencia?>?

    @GET("getAppPermisos.php")
    fun getPermisos(@Query("id_usuario") idUsuario: String?):Call<AppPermisos?>?

    @FormUrlEncoded
    @POST("registrarDispositivo.php")
    fun registraDispositivo(@Field("correo") correo: String?, @Field("device_token") device_token:String?,
    @Field("plataforma") plataforma:String?,@Field("id_usuario") id_usuario:String?): Call<Void?>?



}