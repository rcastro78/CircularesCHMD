package mx.com.edu.chmd2.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.com.edu.chmd2.CircularActivity
import mx.com.edu.chmd2.R
import mx.com.edu.chmd2.TodasCircularesActivity
import mx.com.edu.chmd2.model.Circular
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CircularesAdapter(var lstCirculares:ArrayList<Circular>? = null, var c: Context?=null,
                        private val checkboxStateList: MutableList<Boolean>?=null)
    : RecyclerView.Adapter<CircularesAdapter.ViewHolder>()
{
    var seleccionados:ArrayList<String> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lblCircular : TextView = view.findViewById(R.id.lblCircular)
        val lblFecha : TextView = view.findViewById(R.id.lblFecha)
        val lblPara : TextView = view.findViewById(R.id.lblPara)
        val rlNoLeido: RelativeLayout = view.findViewById(R.id.rlNoLeido)
        val chkSeleccion: CheckBox = view.findViewById(R.id.chkSelectCircular)
        val imgClip:ImageView = view.findViewById(R.id.imgClip)
        val imgCalendar:ImageView = view.findViewById(R.id.imgCalendar)
        val rlAdicional:LinearLayout = view.findViewById(R.id.rlAdicional)
        val rlItemFav:RelativeLayout = view.findViewById(R.id.rlItemFav)
        val llContainer:LinearLayout = view.findViewById(R.id.llContainer)
        val imgFavorita:CheckBox = view.findViewById(R.id.imgFavorita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_circular, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return lstCirculares!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tf = Typeface.createFromAsset(holder.lblCircular.context.assets,"fonts/GothamRoundedMedium_21022.ttf")
        var items = lstCirculares!![position]
        //Ultimo recurso si no lo logro
        holder.setIsRecyclable(false)
        holder.lblCircular.text = items.encabezado
        holder.lblFecha.text = convertirFecha(items.fecha1)
        val para = items.nivel
        holder.lblPara.text = para

        if(items.leida==0)
            holder.rlNoLeido.visibility = View.VISIBLE

        if(items.leida==1)
            holder.rlNoLeido.visibility = View.GONE

        if(items.adjunto==1)
            holder.imgClip.visibility = View.VISIBLE
        else
            holder.imgClip.visibility = View.GONE

        if(items.horaInicialIcs=="00:00:00")
            holder.imgCalendar.visibility = View.GONE
        else
            holder.imgCalendar.visibility = View.VISIBLE

        if(items.adjunto==0 && items.horaInicialIcs=="00:00:00")
            holder.rlAdicional.visibility = View.GONE

        holder.imgFavorita.isChecked = items.favorita==1

        holder.lblCircular.typeface = tf
        holder.lblFecha.typeface = tf
        holder.lblPara.typeface = tf
        holder.chkSeleccion.isChecked = items.isSelected
        holder.chkSeleccion.tag = position

        holder.chkSeleccion.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                seleccionados.add(items.idCircular)
            }else{
                seleccionados.remove(items.idCircular)
            }
            if(c is TodasCircularesActivity){
                (c as TodasCircularesActivity).cambiarBarra(seleccionados)
            }
        }

        /*
        holder.imgFavorita.setOnCheckedChangeListener { _, isChecked ->

                if (c is TodasCircularesActivity) {
                    Log.d("MARCADA_FAV", items.idCircular)
                    if (!isChecked) {
                        items.favorita = 1
                        (c as TodasCircularesActivity).marcarFavoritas(
                            items.idCircular,
                            (c as TodasCircularesActivity).userId
                        )
                    }

                    if (isChecked) {
                        items.favorita = 0
                        (c as TodasCircularesActivity).marcarNoFavoritas(
                            items.idCircular,
                            (c as TodasCircularesActivity).userId
                        )
                    }
                }

        }
*/
        holder.imgFavorita.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                (c as TodasCircularesActivity).marcarFavoritasSilent(
                    items.idCircular,
                    (c as TodasCircularesActivity).userId
                )
            }else{
                (c as TodasCircularesActivity).marcarNoFavoritasSilent(
                    items.idCircular,
                    (c as TodasCircularesActivity).userId
                )
            }
        }





        holder.llContainer.setOnClickListener {
           Intent(c,CircularActivity::class.java).also {
               it.putExtra("idCircular",items.idCircular)
               it.putExtra("esFavorita",items.favorita)
               c!!.startActivity(it)
           }
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun convertirFecha(fechaString: String): String {
        val formatoDiaSemana = SimpleDateFormat("EEEE", Locale.getDefault())
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val fecha = formatoEntrada.parse(fechaString) ?: return ""

        val fechaActual = Calendar.getInstance()
        val fechaPasada = Calendar.getInstance().apply {
            time = fecha
        }

        val diferenciaMillis = fechaActual.timeInMillis - fechaPasada.timeInMillis
        val diferenciaDias = diferenciaMillis / (1000 * 60 * 60 * 24)

        return when {
            diferenciaDias < 1 -> "Hoy"
            diferenciaDias < 7 -> formatoDiaSemana.format(fechaPasada.time)
            diferenciaDias < 14 -> "Hace 1 semana"
            diferenciaDias < 21 -> "Hace 2 semanas"
            diferenciaDias < 30 -> "Hace ${diferenciaDias / 7} semanas"
            diferenciaDias < 365 -> "Hace ${diferenciaDias / 30} mes(es)"
            else -> "Hace ${diferenciaDias / 365} año(s)"
        }
    }
}