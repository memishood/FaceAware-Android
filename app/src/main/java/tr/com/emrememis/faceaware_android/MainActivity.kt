package tr.com.emrememis.faceaware_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        faceAware.setImageResource(R.drawable.test)
//
//        faceAware.setImageDrawable(ActivityCompat.getDrawable(this,R.drawable.test))
//
//        faceAware.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.test))

//        Glide.with(this).load(R.drawable.test).into(faceAware)

    }

    override fun onDestroy() {
        faceAware.onDestroy()
        super.onDestroy()
    }
}
