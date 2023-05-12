package com.example.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class Data(
    val name: String,
    val age: String,
    val grade: String,
)

class Adaptador(private val data: List<Data>) :RecyclerView.Adapter<Adaptador.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.textView1)
        val edad = itemView.findViewById<TextView>(R.id.textView2)
        val grado = itemView.findViewById<TextView>(R.id.textView3)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.activity_elementos, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datos = data[position]
        holder.nombre.text = datos.name
        holder.edad.text = datos.age
        holder.grado.text = datos.grade
    }

    override fun getItemCount(): Int {
        return data.size
    }


}
