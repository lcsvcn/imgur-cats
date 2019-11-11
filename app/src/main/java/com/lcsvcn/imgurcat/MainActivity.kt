package com.lcsvcn.imgurcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import okhttp3.*
import java.io.IOException

import com.squareup.picasso.Picasso
import org.json.JSONObject




class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var responseJSON : JSONObject
    private var fetchLinks : Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getImages("https://api.imgur.com/3/gallery/search/?q=cats")
    }

    fun getImages(url: String) {

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Client-ID 1ceddedc03a5d71")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("DEV", "FALHOU, erro: " + e.toString() )
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("DEV", "SUCESSO")
                handleSuccessResponse(response.body()!!.string())
            }
        })
    }

    fun handleSuccessResponse(json : String) {
        responseJSON =  JSONObject(json.trim())
        val data = responseJSON
            .getJSONArray("data")

        val size = data.length() - 1

        for(i in 0..size) {
            try {
                var link = data.getJSONObject(i)
                    .getJSONArray("images")
                    .getJSONObject(0)
                    .getString("link")

              if(link.endsWith(".jpg"))
                    //setImages(link)

            } catch(e : Exception) {
                Log.d("DEV", "OPS $e")
            }
        }
    }

    // TODO PRINT IMAGES WITHOUT ERROR AND GENERATE IMAGEVIEW
    fun setImages(link : String) {
        var imageView1 = findViewById<ImageView>(R.id.catImg1)

        val picasso = Picasso.get()
        Log.d("DEV", link)

        picasso.load(link)
            .resize(250, 250)
            .into(imageView1)

    }

}
