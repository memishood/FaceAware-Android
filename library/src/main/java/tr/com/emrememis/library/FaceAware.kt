package tr.com.emrememis.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.FaceDetector
import android.util.SparseArray
import com.google.android.gms.vision.face.Face
import java.lang.Exception
import kotlin.math.roundToInt


class FaceAware constructor(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {


    private val faceDetector: FaceDetector = FaceDetector.Builder(context)
        .setTrackingEnabled(false)
        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
        .build()

    private val frame = Frame.Builder()

    private var mBitmap: Bitmap? = null

    private var scale_X = 1.5f
    private var scale_Y = 2.5f

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FaceAware)
        scale_Y = ta.getFloat(R.styleable.FaceAware_face_elevation,2.5f)
        ta.recycle()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (drawable == null) { return }
        initializeBitmap()
    }


    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (getDrawable() == null) { return }
        initializeBitmap()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (drawable == null) { return }
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        if (drawable == null) { return }
        initializeBitmap()
    }

    private fun initializeBitmap() {

        if (mBitmap != null) {
            return
        }

        val drawable :Drawable = drawable


        val bitmap: Bitmap

        when (drawable) {
            is BitmapDrawable -> bitmap = drawable.bitmap
            is ColorDrawable -> bitmap = Bitmap.createBitmap(2,2, Bitmap.Config.ARGB_8888)
            else -> bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        mBitmap = bitmap

        if (mBitmap == null) {
            return
        }

        frame.setBitmap(mBitmap)

        val faces: SparseArray<Face> = faceDetector.detect(frame.build()) ?: return

        if (faces.size() == 0) {
            return
        }

        val face = faces.get(0)

        val newX = (face.position.x / scale_X).roundToInt()
        val newY = (face.position.y / scale_Y).roundToInt()

        var newWidth =  (face.width + (face.position.x / scale_X)).roundToInt()
        var newHeight = (face.height * scale_Y).roundToInt()

        if ((newX + newWidth) > mBitmap!!.width) {
            newWidth = mBitmap!!.width - newX
        }

        if ((newY + newHeight) > mBitmap!!.height) {
            newHeight = mBitmap!!.height - newY
        }

        try {
            val _bitmap = Bitmap.createBitmap(
                mBitmap!!,
                newX,
                newY,
                newWidth,
                newHeight
            )
            setImageBitmap(_bitmap)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onDestroy() {
        faceDetector.release()
    }

}