package ee.webmob.faceaware_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import ee.webmob.faceaware.FaceAware

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TEST_IMAGE_URL = "https://cdn.luxe.digital/media/20230120165323/best-short-haircuts-men-hairstyles-luxe-digital-1.jpg"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val faceAware = findViewById<FaceAware>(R.id.faceAware)
        Glide.with(this).load(TEST_IMAGE_URL).into(faceAware)
    }
}