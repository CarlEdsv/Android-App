package com.example.firebase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/*data class TareaData(
    val id:Int,
    val titulo:String,
    val descripcion:String,
    val fechaVencimiento:String
)*/

class SqliteHelper(context: Context): SQLiteOpenHelper(context, "BD", null,1) {

    companion object {
        private const val tblTareas = "tarea" //tabla de tareas

        // campos de la tabla de tareas
        private const val campIdTareas = "id"
        private const val campTitulo = "titulo"
        private const val campDescripcionTareas = "descripcion"
        private const val campFechaVencimiento = "fechaVencimiento"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val tblTarea = "CREATE TABLE $tblTareas"+""+
                "($campIdTareas INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$campTitulo TEXT,"+
                "$campDescripcionTareas TEXT,"+
                "$campFechaVencimiento TEXT)"
        db!!.execSQL(tblTarea)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val modTable2 = "DROP TABLE IF EXISTS $tblTareas"
        db!!.execSQL(modTable2)
        onCreate(db)
    }

    fun addTareas(titulo:String, descripcion: String, fechaVencimiento: String){
        val datos = ContentValues()
        datos.put(campTitulo, titulo)
        datos.put(campDescripcionTareas,descripcion)
        datos.put(campFechaVencimiento, fechaVencimiento)
        val db = this.writableDatabase
        db.insert(tblTareas, null, datos)
        db.close()
    }

    fun getTarea(): List<Data> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $tblTareas",null)
        val tareass = mutableListOf<Data>()
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            val fechaVencimeinto = cursor.getString(cursor.getColumnIndexOrThrow("fechaVencimiento"))
            val tarea = Data(id,titulo,descripcion,fechaVencimeinto)
            tareass.add(tarea)
        }
        cursor.close()
        db.close()
        return tareass
    }

    fun numTareas(): String {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $tblTareas", null)
        var numRecords = 0
        if (cursor.moveToFirst()) {
            numRecords = cursor.getInt(0)
        }
        val numRecordsString = numRecords.toString()
        return numRecordsString
    }
}

