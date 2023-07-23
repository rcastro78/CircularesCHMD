package mx.com.edu.chmd2

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_todas_circulares.*
import kotlinx.android.synthetic.main.toolbar_circulares_mod.*
import kotlinx.android.synthetic.main.toolbar_circulares_nav.rlCerrarSesion
import kotlinx.android.synthetic.main.toolbar_circulares_nav.rlCred
import kotlinx.android.synthetic.main.toolbar_circulares_nav.rlMaguen
import kotlinx.android.synthetic.main.toolbar_inferior.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.edu.chmd2.adapter.CircularesAdapter
import mx.com.edu.chmd2.db.ChmdDB
import mx.com.edu.chmd2.model.Circular
import mx.com.edu.chmd2.networking.CircularesAPI
import mx.com.edu.chmd2.networking.IChmd
import retrofit2.awaitResponse

class TodasCircularesActivity : AppCompatActivity() {
    var seleccionados:ArrayList<String> = ArrayList()
    lateinit var iChmd: IChmd
    val TODAS=1
    val FILTRA_NL=2
    val FILTRA_FAVS=3
    val FILTRA_ELIMINADAS=4
    var verCirculares:String=""
    var verCredencial:String=""
    var verMiMaguen:String=""
    private var sharedPreferences: SharedPreferences? = null
    var lstCirculares:ArrayList<Circular> = ArrayList()
    var userId:String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todas_circulares)
        val SHARED:String=getString(R.string.SHARED_PREF)
        sharedPreferences = getSharedPreferences(SHARED, 0)
        iChmd = CircularesAPI.getCHMDService()!!
        userId = sharedPreferences!!.getString(userId,"")!!.toString()
        val correo = sharedPreferences!!.getString("correo","")!!.toString()

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener {tk->
                Log.d("TOKEN",tk)
                registraDisp(correo,"Android OS "+Build.VERSION.RELEASE,tk,userId)
            }
            .addOnFailureListener {
                Log.e("TOKEN",it.message.toString())
            }


        userId = "2484"
        verCredencial = sharedPreferences!!.getString("verCredencial","0").toString()
        verCirculares = sharedPreferences!!.getString("verCirculares","0").toString()
        verMiMaguen = sharedPreferences!!.getString("verMaguen","0").toString()


        rlFavs.setOnClickListener{
          if(verCirculares.equals("1")) {
           vwFavs.visibility = View.VISIBLE
           vwTodas.visibility = View.GONE
           vwEliminadas.visibility = View.GONE
           vwNoLeidas.visibility = View.GONE
           getCircularesFavoritas(userId)
            val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putInt("filtroIterar",FILTRA_FAVS)
            editor.apply()
        }else{
            Toast.makeText(applicationContext,"No tienes permiso",Toast.LENGTH_LONG).show()
        }
        }
        rlTodas.setOnClickListener {
            if(verCirculares.equals("1")) {
                vwFavs.visibility = View.GONE
                vwTodas.visibility = View.VISIBLE
                vwEliminadas.visibility = View.GONE
                vwNoLeidas.visibility = View.GONE
                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putInt("filtroIterar", TODAS)
                editor.apply()
                getCirculares(userId)
            }else{
                Toast.makeText(applicationContext,"No tienes permiso",Toast.LENGTH_LONG).show()
            }
        }

        rlNoLeidas.setOnClickListener {
            if(verCirculares.equals("1")) {
            vwFavs.visibility = View.GONE
            vwTodas.visibility = View.GONE
            vwEliminadas.visibility = View.GONE
            vwNoLeidas.visibility = View.VISIBLE
            val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putInt("filtroIterar",FILTRA_NL)
            editor.apply()
            getCircularesNoLeidas(userId)
            }else{
                Toast.makeText(applicationContext,"No tienes permiso",Toast.LENGTH_LONG).show()
            }
        }

        rlEliminadas.setOnClickListener {
            if(verCirculares.equals("1")) {
            vwFavs.visibility = View.GONE
            vwTodas.visibility = View.GONE
            vwEliminadas.visibility = View.VISIBLE
            vwNoLeidas.visibility = View.GONE
            val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putInt("filtroIterar",FILTRA_ELIMINADAS)
            editor.apply()
            getCircularesEliminadas(userId)
            }else{
                Toast.makeText(applicationContext,"No tienes permiso",Toast.LENGTH_LONG).show()
            }
        }

        rlNoLeer.setOnClickListener {
            seleccionados.forEach { s->
                marcarNoLeidas(s,userId)
            }
            seleccionados.clear()
            cambiarBarra(seleccionados)
        }

        rlLeer.setOnClickListener {
            seleccionados.forEach { s->
                Log.d("SELECCIONADAS",s)
                marcarLeidas(s,userId)
            }
            seleccionados.clear()
            cambiarBarra(seleccionados)
        }

        rlMarcarFavoritas.setOnClickListener {
            seleccionados.forEach { s->
                Thread.sleep(200)
                Log.d("SELECCIONADAS",s)
                marcarFavoritas(s,userId)
            }
            seleccionados.clear()
            cambiarBarra(seleccionados)
        }

        rlEliminar.setOnClickListener {
            seleccionados.forEach { s->
                Log.d("SELECCIONADAS",s)
                eliminar(s,userId)
            }
            seleccionados.clear()
            cambiarBarra(seleccionados)
        }

        rlCred.setOnClickListener {
            //verCredencial = sharedPreferences!!.getString("verCredencial","0").toString()
            if(verCredencial=="1") {
            Intent(this,CredencialActivity::class.java).also {
                startActivity(it)
            }
            }else{
                Toast.makeText(applicationContext,"No tienes permiso",Toast.LENGTH_LONG).show()
            }

        }
        rlMaguen.setOnClickListener {
            //verMiMaguen = sharedPreferences!!.getString("verMaguen","0").toString()
            if(verMiMaguen.equals("1")) {

            Intent(this,MiMaguenActivity::class.java).also {
                startActivity(it)
            }
            }else{
                Toast.makeText(applicationContext,"No tienes permiso",Toast.LENGTH_LONG).show()
            }
        }

        rlCerrarSesion.setOnClickListener {
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
                val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putBoolean("loggedIn",false)
                editor.apply()
                finish()
            }
        }





    }

    override fun onResume() {
        super.onResume()


        val filtro = sharedPreferences!!.getInt("filtroIterar",0)
        if(filtro==0 || filtro==TODAS)
            getCirculares(userId)

        if(filtro==FILTRA_NL)
            getCircularesNoLeidas(userId)

        if(filtro==FILTRA_FAVS)
            getCircularesFavoritas(userId)

        if(filtro==FILTRA_ELIMINADAS)
            getCircularesEliminadas(userId)
    }

    override fun onPause() {
        super.onPause()
        lstCirculares.clear()
        rvCirculares.adapter = null
    }


    fun cambiarBarra(ids:ArrayList<String>){
        seleccionados = ids
        if(ids.size>0){
            action_home_tool_bar2.visibility = View.VISIBLE
            action_home_tool_bar.visibility = View.GONE
        }else{
            action_home_tool_bar2.visibility = View.GONE
            action_home_tool_bar.visibility = View.VISIBLE
        }
    }

    fun getCirculares(usuario:String){
        lstCirculares.clear()

        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.getCirculares(usuario)!!.awaitResponse()
            if(response.isSuccessful){
                val result = response.body()!!
                result.forEach { c->
                    if(c!!.eliminado=="0") {
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
                withContext(Dispatchers.Main){



                    val adapter = CircularesAdapter(lstCirculares,this@TodasCircularesActivity)
                    val itemTouchCallback: ItemTouchHelper.SimpleCallback = object :
                        ItemTouchHelper.SimpleCallback(
                            0,
                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        ) {

                        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {

                            return .7f
                        }

                        override fun onChildDraw(
                            c: Canvas,
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            dX: Float,
                            dY: Float,
                            actionState: Int,
                            isCurrentlyActive: Boolean
                        ) {
                            super.onChildDraw(
                                c,
                                recyclerView,
                                viewHolder,
                                dX,
                                dY,
                                actionState,
                                isCurrentlyActive
                            )
                            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                                viewHolder.itemView.translationX = dX / 5
                                val p = Paint()
                                p.color = Color.parseColor("#D32F2F")
                                val background = RectF(
                                    viewHolder.itemView.right.toFloat().plus(dX / 5) ?: 0.0f,
                                    viewHolder.itemView.top.toFloat() ?: 0.0f,
                                    viewHolder.itemView.right.toFloat() ?: 0.0f,
                                    viewHolder.itemView.bottom.toFloat() ?: 0.0f
                                )
                                c?.drawRect(background, p)

                                val itemView = viewHolder.itemView
                                val itemHeight = itemView.height
                                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.clear)
                                val deleteIconTop: Float =
                                    itemView.top + (itemHeight - 32.0f) / 2.0f
                                val deleteIconMargin: Float = (itemHeight - 32.0f) / 2.0f
                                val deleteIconLeft: Float =
                                    itemView.right - deleteIconMargin - 32.0f
                                val deleteIconRight: Float = itemView.right - deleteIconMargin
                                val deleteIconBottom: Float = deleteIconTop + 32
                                val b = RectF(deleteIconLeft,deleteIconTop,deleteIconRight,deleteIconBottom)

                                c.drawBitmap(bitmap, null, b, p)
                            } else {
                                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            }


                        }
                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val position = viewHolder.adapterPosition
                            when (direction) {
                                ItemTouchHelper.LEFT -> {
                                    //Eliminar la circular cuando se alcance el umbral
                                    //Luego, refrescar la lista
                                    val idCircular = lstCirculares[position].idCircular
                                    eliminar(idCircular,userId)
                                    getCirculares(userId)

                                }
                                ItemTouchHelper.RIGHT -> {
                                    Log.d("MOVIMIENTO","hacia la derecha")
                                }
                            }
                        }

                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean = false

                    }

                    rvCirculares.layoutManager = LinearLayoutManager(this@TodasCircularesActivity)

                    val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
                    itemTouchHelper.attachToRecyclerView(rvCirculares)

                    rvCirculares.adapter = adapter
                }
            }
        }
    }

    fun getCircularesDB(usuario: String){
        val db = ChmdDB.getInstance(this)
        lstCirculares.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val circularesDB = db.iCircularDAO.getCirculares(usuario)
            circularesDB.forEach{c->
                val circular = Circular(c!!.idCircular,
                    c.nombre,
                    "",
                    "",
                    c.fecha_ics,
                    "",
                    "",
                    c.leida.toInt(),
                    c.favorita.toInt(),
                    "",
                    "",
                    c.fecha_ics,
                    "",
                    "",
                    "",
                    c.adjunto.toInt(),
                    "",
                    c.nivel,
                    false)
            }

            withContext(Dispatchers.Main){
                val adapter = CircularesAdapter(lstCirculares,this@TodasCircularesActivity)
                rvCirculares.layoutManager = LinearLayoutManager(this@TodasCircularesActivity)
                rvCirculares.adapter = adapter
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
                    if(c!!.favorito.equals("1") && c!!.eliminado.equals("0")) {
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
                withContext(Dispatchers.Main){

                    val adapter = CircularesAdapter(lstCirculares,this@TodasCircularesActivity)
                    rvCirculares.layoutManager = LinearLayoutManager(this@TodasCircularesActivity)
                    rvCirculares.adapter = adapter
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
                    if(c!!.favorito.equals("0") && c!!.leido.equals("0") && c!!.eliminado.equals("0")) {
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
                withContext(Dispatchers.Main){
                    val adapter = CircularesAdapter(lstCirculares,this@TodasCircularesActivity)
                    rvCirculares.layoutManager = LinearLayoutManager(this@TodasCircularesActivity)
                    rvCirculares.adapter = adapter
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
                    if(c!!.eliminado.equals("1")) {
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
                withContext(Dispatchers.Main){
                    val adapter = CircularesAdapter(lstCirculares,this@TodasCircularesActivity)
                    rvCirculares.layoutManager = LinearLayoutManager(this@TodasCircularesActivity)
                    rvCirculares.adapter = adapter
                }
            }
        }
    }


    fun marcarNoLeidas(id:String,uid:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.noLeerCircular(id,uid)!!.awaitResponse()
            if(response.isSuccessful){

            }
            withContext(Dispatchers.Main){
                val filtro = sharedPreferences!!.getInt("filtroIterar",0)
                if(filtro==0 || filtro==TODAS)
                    getCirculares(userId)

                if(filtro==FILTRA_NL)
                    getCircularesNoLeidas(userId)

                if(filtro==FILTRA_FAVS)
                    getCircularesFavoritas(userId)

                if(filtro==FILTRA_ELIMINADAS)
                    getCircularesEliminadas(userId)
            }
        }
    }
    fun marcarLeidas(id:String,uid:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.leerCircular(id,uid)!!.awaitResponse()
            if(response.isSuccessful){

            }
            withContext(Dispatchers.Main){
                val filtro = sharedPreferences!!.getInt("filtroIterar",0)
                if(filtro==0 || filtro==TODAS)
                    getCirculares(userId)

                if(filtro==FILTRA_NL)
                    getCircularesNoLeidas(userId)

                if(filtro==FILTRA_FAVS)
                    getCircularesFavoritas(userId)

                if(filtro==FILTRA_ELIMINADAS)
                    getCircularesEliminadas(userId)
            }
        }
    }
    fun marcarFavoritas(id:String,uid:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.favCircular(id,uid)!!.awaitResponse()
            if(response.isSuccessful){

            }
            withContext(Dispatchers.Main){
                val filtro = sharedPreferences!!.getInt("filtroIterar",0)
                if(filtro==0 || filtro==TODAS)
                    getCirculares(userId)

                if(filtro==FILTRA_NL)
                    getCircularesNoLeidas(userId)

                if(filtro==FILTRA_FAVS)
                    getCircularesFavoritas(userId)

                if(filtro==FILTRA_ELIMINADAS)
                    getCircularesEliminadas(userId)
            }
        }
    }
    fun marcarNoFavoritasSilent(id:String,uid:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.elimFavCircular(id,uid)!!.awaitResponse()
            if(response.isSuccessful){

            }
            withContext(Dispatchers.Main){
                val filtro = sharedPreferences!!.getInt("filtroIterar",0)
                if(filtro==0 || filtro==TODAS)
                    getCirculares(userId)

                if(filtro==FILTRA_NL)
                    getCircularesNoLeidas(userId)

                if(filtro==FILTRA_FAVS)
                    getCircularesFavoritas(userId)

                if(filtro==FILTRA_ELIMINADAS)
                    getCircularesEliminadas(userId)
            }
        }
    }
    fun marcarFavoritasSilent(id:String,uid:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.favCircular(id,uid)!!.awaitResponse()
            if(response.isSuccessful){

            }
            withContext(Dispatchers.Main){

            }
        }
    }

    fun eliminar(id:String,uid:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.eliminarCircular(id,uid)!!.awaitResponse()
            if(response.isSuccessful){

            }
            withContext(Dispatchers.Main){
                val filtro = sharedPreferences!!.getInt("filtroIterar",0)
                if(filtro==0 || filtro==TODAS)
                    getCirculares(userId)

                if(filtro==FILTRA_NL)
                    getCircularesNoLeidas(userId)

                if(filtro==FILTRA_FAVS)
                    getCircularesFavoritas(userId)

                if(filtro==FILTRA_ELIMINADAS)
                    getCircularesEliminadas(userId)
            }
        }
    }

    fun registraDisp(correo:String, plataforma:String, token:String,userId:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = iChmd.registraDispositivo(correo,token,plataforma,userId)!!.awaitResponse()
            if(response.isSuccessful){
                Log.d("TOKEN _reg_","Registrado")
            }
        }
    }
}