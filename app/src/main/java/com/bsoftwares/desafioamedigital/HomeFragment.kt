package com.bsoftwares.desafioamedigital

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bsoftwares.desafioamedigital.Adapters.BestSellersAdapter
import com.bsoftwares.desafioamedigital.Adapters.CategoriesAdapter
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_categories.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recyclerView_bestSellers.layoutManager = LinearLayoutManager(context)
        getJsoninfo()
    }

    private fun getJsoninfo() {

        val client = OkHttpClient()
        //Request for Banners
        var url = getString(R.string.url_banner)
        var request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val banners = gson.fromJson(body, Banners::class.java)
                activity?.runOnUiThread {
                    createSliderBanner(banners)
                }
            }

        })

        //Request for Categories

        url = getString(R.string.url_categories)
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed")
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val categories = gson.fromJson(body,Categories::class.java)
                activity?.runOnUiThread {
                    recyclerView_categories.adapter = CategoriesAdapter(categories.data)
                }
            }
        })

        //Request for Best Sellers

        url = getString(R.string.url_bestSellers)
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val bestSellers = gson.fromJson(body,BestSellers::class.java)
                activity?.runOnUiThread {
                    recyclerView_bestSellers.adapter = BestSellersAdapter(bestSellers.data)
                    makeLayoutVisible()
                }
            }
        })

    }

    //Make Home Layout Visible
    private fun makeLayoutVisible(){
        homeLayout.visibility = View.VISIBLE
        homeProgressBar.visibility = View.INVISIBLE
        val fadeInAnimation  = AnimationUtils.loadAnimation(context,R.anim.fade_in)
        homeLayout.startAnimation(fadeInAnimation)
    }

    //Create SliderBanner

    private fun createSliderBanner(banners : Banners) {
        for (bannerData : BannerData in banners.data){
            val textSliderView = TextSliderView(context)
            textSliderView.image(bannerData.urlImagem)
                    .setOnSliderClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bannerData.linkUrl)))
                    }.scaleType = BaseSliderView.ScaleType.Fit
            sliderBanner.addSlider(textSliderView)
        }
    }
}

class Banners (val data : List<BannerData>)

class BannerData (val id:Int, val urlImagem:String,val linkUrl:String)

class Categories (val data : List<Category>)

class Category (val id:Int, val descricao: String, val urlImagem : String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(descricao)
        parcel.writeString(urlImagem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

}

class BestSellers (val data : List<BestSeller>)

class BestSeller (val nome : String, val urlImagem : String,val descricao : String ,val precoDe : Int, val precoPor : Float, val id : Int ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(urlImagem)
        parcel.writeString(descricao)
        parcel.writeInt(precoDe)
        parcel.writeFloat(precoPor)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BestSeller> {
        override fun createFromParcel(parcel: Parcel): BestSeller {
            return BestSeller(parcel)
        }

        override fun newArray(size: Int): Array<BestSeller?> {
            return arrayOfNulls(size)
        }
    }

}