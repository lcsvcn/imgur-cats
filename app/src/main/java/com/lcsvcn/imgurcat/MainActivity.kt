package com.lcsvcn.imgurcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import okhttp3.*
import java.io.IOException

import org.json.JSONObject
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import org.w3c.dom.Text
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var responseJSON : JSONObject
    private lateinit var spinner : ProgressBar
    private lateinit var spinnerText : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        spinner = findViewById(R.id.progressBar1)
        spinnerText = findViewById(R.id.progressText1)
        spinner.setVisibility(View.VISIBLE)

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

    fun givePercentage(pos : Int, size : Int) : Int {
        var por = (pos * 100 / size)
        Log.d("DEV", "porcentagem $por")
        return (pos * 100 / size)
    }

    // Handle Success response from API
    fun handleSuccessResponse(json : String) {
        responseJSON =  JSONObject(json.trim())
        val data = responseJSON
            .getJSONArray("data")

        var imageNumber = 0
        var maxImages= 28

        val size = data.length() - 1
        for(i in 0..size) {
            try {
                var link = data.getJSONObject(i)
                    .getJSONArray("images")
                    .getJSONObject(0)
                    .getString("link")

              // avoid send gif or mp4
              if(link.endsWith(".jpg")) {
                  showCatImages(link, imageNumber, maxImages)
                  imageNumber++
              }
              if(imageNumber > maxImages) {
                  break
              }

              var porcentage = givePercentage(imageNumber, maxImages)
              spinner.setProgress(porcentage)
              spinnerText.setText(porcentage.toString() + "%")
            } catch(e : Exception) {
                Log.d("DEV", "handleSuccessResponse : $e")
            }
        }

        spinner.setVisibility(View.GONE)
        spinnerText.setVisibility(View.GONE)
    }

    // Handle Show Cat Images
    // Improve
    fun showCatImages(link : String, pos : Int, max : Int) {


        val imageViewArray = getImageViews(max)

        try {
            val myUrl = URL(link)
            val inputStream = myUrl.content as InputStream
            val drawable = Drawable.createFromStream(inputStream, null)

            imageViewArray[pos]!!.setImageDrawable(drawable)
        } catch(e : Exception) {
            Log.d("DEV", "showCatImages : $e")
        }



    }

}