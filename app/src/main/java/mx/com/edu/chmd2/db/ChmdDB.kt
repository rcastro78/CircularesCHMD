package mx.com.edu.chmd2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.com.edu.chmd2.networking.IChmd

@Database(entities = arrayOf(CircularDAO::class), version = 1)
abstract class ChmdDB:RoomDatabase() {
    companion object {
        private var INSTANCE: ChmdDB? = null
        fun getInstance(context: Context): ChmdDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    ChmdDB::class.java,
                    "chmd_db002.db"
                )
                    .build()
            }
            return INSTANCE as ChmdDB
        }
    }
    abstract val iCircularDAO: CircularDAO.ICircularDAO
}

/*
@Database(entities = arrayOf(AsistenciaDAO::class,UsuarioDAO::class,
    RutaDAO::class), version = 1)
abstract class TransporteDB : RoomDatabase() {

    companion object {
        private var INSTANCE: TransporteDB? = null
        fun getInstance(context: Context): TransporteDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    TransporteDB::class.java,
                    "transporte1a.db"
                )
                    .build()
            }
            return INSTANCE as TransporteDB
        }
    }

    abstract val iAsistenciaDAO: AsistenciaDAO.IAsistenciaDAO
    abstract val iUsuarioDAO: UsuarioDAO.IUsuarioDAO
    abstract val iRutaDAO: RutaDAO.IRutaDAO
}
* */