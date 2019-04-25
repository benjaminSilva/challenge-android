package com.bsoftwares.desafioamedigital

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.text.HtmlCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.text.Html
import android.widget.Toast
import com.bsoftwares.desafioamedigital.Adapters.BestSellerViewHolder
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.content_product.*
import kotlinx.android.synthetic.main.item_list_vertical.view.*
import okhttp3.*
import java.io.IOException

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        loadInformation()
        setButtons()
    }

    private fun setButtons() {
        img_backButton.setOnClickListener {
            onBackPressed()
        }
        fab_reservation.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Produto reservado com Sucesso")
            builder.setPositiveButton("Ok"){ _, _ ->
                val client = OkHttpClient()
                //Request for Banners
                var url = getString(R.string.url_forPost,intent.getParcelableExtra<BestSeller>(BestSellerViewHolder.PRODUCT_NAME_KEY).id)
                val JSON = MediaType.parse("application/json;charset=utf-8")
                val jsonObject = JsonObject()
                val requestBody = RequestBody.create(JSON,jsonObject.toString())
                var request = Request.Builder().post(requestBody).addHeader("content-type", "application/json; charset=utf-8").url(url).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Toast.makeText(applicationContext,"Failed", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body()?.string()
                        println(body)
                    }

                })
                onBackPressed()
            }
            builder.show()
        }
    }

    private fun loadInformation() {
        val product = intent.getParcelableExtra<BestSeller>(BestSellerViewHolder.PRODUCT_NAME_KEY)
        tv_productName.text = product.nome
        tv_descriptionProduct.text = HtmlCompat.fromHtml(product.descricao,HtmlCompat.FROM_HTML_MODE_LEGACY)
        tv_beforePrice.text = getString(R.string.de_preco,product.precoDe)
        tv_beforePrice.paintFlags = tv_beforePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        tv_afterPrice.text = getString(R.string.por_preco,product.precoPor.toInt())
        Picasso.with(this).load(product.urlImagem).into(img_product)
    }

}
