package tr.com.emrememis.faceaware_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //faceAware.setImageResource(R.drawable.image)

        //faceAware.setImageDrawable(ActivityCompat.getDrawable(this,R.drawable.image))

        //faceAware.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.test))

        //Glide.with(this).load(R.drawable.image).into(faceAware)
    }
}
