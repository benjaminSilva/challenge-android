package com.bsoftwares.desafioamedigital.Adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsoftwares.desafioamedigital.CategoriesActivity
import com.bsoftwares.desafioamedigital.Category
import com.bsoftwares.desafioamedigital.ProductActivity
import com.bsoftwares.desafioamedigital.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_horizontal.view.*

class CategoriesAdapter (val categories : List<Category>) : RecyclerView.Adapter<CategoriesViewHolder>() {
    override fun onCreateViewHolder(parent : ViewGroup, position: Int): CategoriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForColumn = layoutInflater.inflate(R.layout.item_list_horizontal,parent,false)
        return CategoriesViewHolder(cellForColumn)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position : Int) {
        val category = categories[position]
        holder.itemView.tv_category.text = category.descricao
        var urlImagem = category.urlImagem
        if(!urlImagem.contains("https"))
            urlImagem = urlImagem.replace("http","https")
        Picasso.with(holder.itemView.context).load(urlImagem).into(holder.itemView.img_category)
    }

}

class CategoriesViewHolder (v : View) : RecyclerView.ViewHolder(v){
    init {
        v.setOnClickListener {
            val intent = Intent(v.context, CategoriesActivity::class.java)
            v.context.startActivity(intent)
        }
    }
}