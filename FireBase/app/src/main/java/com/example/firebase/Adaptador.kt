package com.example.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class Data(
    val text1: Int,
    val text2: String,
    val text3: String,
    val text4: String
)

class Adaptador(private val data: List<Data>) :RecyclerView.Adapter<Adaptador.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt1 = itemView.findViewById<TextView>(R.id.textView1)
        val txt2 = itemView.findViewById<TextView>(R.id.textView2)
        val txt3 = itemView.findViewById<TextView>(R.id.textView3)
        val txt4 = itemView.findViewById<TextView>(R.id.textView4)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.activity_elementos, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datos = data[position]
        holder.txt1.text = datos.text1.toString()
        holder.txt2.text = datos.text2
        holder.txt3.text = datos.text3
        holder.txt4.text = datos.text4
    }

    override fun getItemCount(): Int {
        return data.size
    }


}
