package com.lcsvcn.imgurcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import okhttp3.*
import java.io.IOException

import org.json.JSONObject
import android.graphics.drawable.Drawable
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var responseJSON : JSONObject



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getCatImages("https://api.imgur.com/3/gallery/search/?q=cats")
    }

    fun getImageViews(size : Int) : Array<ImageView?> {
        var imageViewArray : Array<ImageView?> = arrayOfNulls<ImageView>(size)

        for(i in 0..size) {
            try {
                imageViewArray[i] = findViewById<ImageView>(
                    resources.getIdentifier(
                        "catImg$i", "id",
                        packageName
                    )
                )
            } catch (e: Exception) {
                Log.d("DEV", "OPS $e")
            }
        }

        return imageViewArray
    }
    
    // Handle Get Cat Images from Imgur
    fun getCatImages(url: String) {
        // Create the request for url
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Client-ID 1ceddedc03a5d71")
            .build()

        // Call method GET
        client.newCall(request).enqueue(object : Callback {
            // Fail to get response
            override fun onFailure(call: Call, e: IOException) {
                Log.d("DEV", "FALHOU, erro: " + e.toString() )
            }

            // Success to get response
            override fun onResponse(call: Call, response: Response) {
                Log.d("DEV", "SUCESSO")
                handleSuccessResponse(response.body()!!.string())
            }
        })
    }


    // Handle Success response from API
    fun handleSuccessResponse(json : String) {
        responseJSON =  JSONObject(json.trim())
        val data = responseJSON
            .getJSONArray("data")

        var imageNumber = 0

        val size = data.length() - 1

        for(i in 0..size) {
            try {
                var link = data.getJSONObject(i)
                    .getJSONArray("images")
                    .getJSONObject(0)
                    .getString("link")

              // avoid send gif or mp4
              if(link.endsWith(".jpg")) {
                  showCatImages(link, imageNumber)
                  imageNumber++
              }

            } catch(e : Exception) {
                Log.d("DEV", "OPS $e")
            }
        }
    }

    // Handle Show Cat Images
    // Improve
    fun showCatImages(link : String, pos : Int) {


        val imageViewArray = getImageViews(24)

        try {
            val myUrl = URL(link)
            val inputStream = myUrl.content as InputStream
            val drawable = Drawable.createFromStream(inputStream, null)
            imageViewArray[pos]!!.setImageDrawable(drawable)
        } catch(e : Exception) {
            Log.d("DEV", "OPS $e")
        }

    }

}