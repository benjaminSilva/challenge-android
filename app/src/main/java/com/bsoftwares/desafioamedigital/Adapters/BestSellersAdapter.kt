package com.bsoftwares.desafioamedigital.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bsoftwares.desafioamedigital.BestSeller
import com.bsoftwares.desafioamedigital.ProductActivity
import com.bsoftwares.desafioamedigital.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_vertical.view.*



class BestSellersAdapter (val bestSellers : List<BestSeller>) : RecyclerView.Adapter<BestSellerViewHolder>(){

    override fun onCreateViewHolder(parent : ViewGroup, position: Int): BestSellerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForColumn = layoutInflater.inflate(R.layout.item_list_vertical,parent,false)
        return BestSellerViewHolder(cellForColumn)
    }


    override fun getItemCount(): Int {
        return bestSellers.size
    }

    override fun onBindViewHolder(holder: BestSellerViewHolder, position : Int) {
        val bestSeller = bestSellers[position]
        holder.itemView.tv_bestSeller_name.text = bestSeller.nome
        holder.itemView.tv_bestSeller_oldPrice.text = holder.itemView.context?.getString(R.string.de_preco,bestSeller.precoDe)
        holder.itemView.tv_bestSeller_oldPrice.paintFlags = holder.itemView.tv_bestSeller_oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.itemView.tv_bestSeller_newPrice.text = holder.itemView.context?.getString(R.string.por_preco,bestSeller.precoPor.toInt())
        Picasso.with(holder.itemView.context).load(bestSeller.urlImagem).into(holder.itemView.img_bestSeller)
        holder.bestSeller = bestSeller
    }
}

class BestSellerViewHolder( v : View, var bestSeller : BestSeller? = null) : RecyclerView.ViewHolder(v) {

    companion object{
        val PRODUCT_NAME_KEY = "PRODUCT_NAME"
    }
    init {
        v.setOnClickListener {
            val intent = Intent(v.context,ProductActivity::class.java)
            intent.putExtra(PRODUCT_NAME_KEY,bestSeller)
            v.context.startActivity(intent)
        }
    }
}