package tr.com.emrememis.faceaware_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tr.com.emrememis.faceaware_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
         binding.faceAware.setImageResource(R.drawable.image)

         binding.faceAware.setImageDrawable(ActivityCompat.getDrawable(this,R.drawable.image))

         binding.faceAware.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.test))

         Glide.with(this).load(R.drawable.image).into(binding.faceAware)
        * */
    }
}