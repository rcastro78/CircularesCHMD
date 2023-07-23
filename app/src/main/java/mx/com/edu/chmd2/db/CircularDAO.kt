package mx.com.edu.chmd2.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
@Entity(tableName = CircularDAO.TABLE_NAME)
class CircularDAO(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "idCircular")  val  idCircular:String,
    @ColumnInfo(name = "nombre")  val  nombre:String,
    @ColumnInfo(name = "leida")  val  leida:Int,
    @ColumnInfo(name = "favorita")  val  favorita:Int,
    @ColumnInfo(name = "eliminada")  val  eliminada:Int,
    @ColumnInfo(name = "idUsuario")  val  idUsuario:String,
    @ColumnInfo(name = "createdAt")  val  createdAt:String,
    @ColumnInfo(name = "fechaIcs")  val  fecha_ics:String,
    @ColumnInfo(name = "adjunto")  val  adjunto:String,
    @ColumnInfo(name = "nivel")  val  nivel:String
) {

    companion object {
        const val TABLE_NAME = "Circular"
    }
    @Dao
    interface ICircularDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(c:CircularDAO)

        @Query("DELETE FROM $TABLE_NAME")
        fun eliminaTodasCirculares()

        @Query("SELECT * FROM $TABLE_NAME where idUsuario=:idUsuario")
        fun getCirculares(idUsuario: String):List<CircularDAO>

        @Query("SELECT * FROM $TABLE_NAME where idUsuario=:idUsuario AND leida=1")
        fun getCircularesNoLeidas(idUsuario: String):List<CircularDAO>

        @Query("SELECT * FROM $TABLE_NAME where idUsuario=:idUsuario AND favorita=1")
        fun getCircularesFavoritas(idUsuario: String):List<CircularDAO>

        @Query("SELECT * FROM $TABLE_NAME where idUsuario=:idUsuario AND eliminada=1 ")
        fun getCircularesEliminadas(idUsuario: String):List<CircularDAO>

    }
}

