package com.bsoftwares.desafioamedigital

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import com.bsoftwares.desafioamedigital.Adapters.BestSellerViewHolder
import com.bsoftwares.desafioamedigital.Adapters.BestSellersAdapter
import com.bsoftwares.desafioamedigital.Adapters.CategoriesViewHolder
import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.content_categories.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import java.io.IOException

class CategoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        setSupportActionBar(toolbar)
        setRecyclerview()
    }

    private fun setRecyclerview() {
        recyclerView_categories_horizontal.layoutManager = LinearLayoutManager(applicationContext)
        val client = OkHttpClient()
        val url = getString(R.string.url_bestSellers)
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val bestSellers = gson.fromJson(body,BestSellers::class.java)
                runOnUiThread {
                    recyclerView_categories_horizontal.adapter = BestSellersAdapter(bestSellers.data)
                }
            }
        })
    }

}
