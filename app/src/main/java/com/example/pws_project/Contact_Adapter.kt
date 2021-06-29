package com.example.pws_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Contact_Adapter(items : List<Contact_Class>,ctx: Context) : RecyclerView.Adapter<Contact_Adapter.ViewHolder>(){

    private var list = items
    private var context = ctx

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Contact_Adapter.ViewHolder, position: Int) {
        holder.cont_name.text = list[position].c_name
        holder.cont_number.text = list[position].c_number
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Contact_Adapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_contact,parent,false))
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val cont_name:TextView = v.findViewById(R.id.txt_Con_Name)
        val cont_number:TextView = v.findViewById(R.id.txt_Con_Number)
    }
}