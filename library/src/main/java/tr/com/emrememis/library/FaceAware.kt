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
import kotlin.math.roundToInt


class FaceAware constructor(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private val faceDetector: FaceDetector = FaceDetector.Builder(context)
        .setTrackingEnabled(false)
        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
        .build()

    private val frame: Frame.Builder? = Frame.Builder()

    private var faceElevation = 10

    private var isCropped = false

    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.FaceAware)
        faceElevation = ta.getInt(R.styleable.FaceAware_face_elevation,10)
        ta.recycle()

        if (faceElevation < 10) {
            faceElevation = 10
        }

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

        if (isCropped) {
            return
        }

        isCropped = true

        val bitmap: Bitmap

        when (val drawable = drawable) {
            is BitmapDrawable -> bitmap = drawable.bitmap
            is ColorDrawable -> bitmap = Bitmap.createBitmap(2,2, Bitmap.Config.ARGB_8888)
            else -> bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        if (bitmap == null || frame == null) {
            return
        }

        frame.setBitmap(bitmap)

        val faces: SparseArray<Face> = faceDetector.detect(frame.build()) ?: return

        if (faces.size() == 0) {
            return
        }

        val face = faces.get(0)

        var faceX = ( face.position.x - faceElevation ).roundToInt()
        var faceY = ( face.position.y - faceElevation ).roundToInt()

        if (faceY <= 0) {
            faceY = 0
        }

        if (faceX <= 0) {
            faceX = 0
        }

        var faceWidth = ( face.width + faceElevation * 2 ).roundToInt()
        var faceHeight = ( face.height + faceElevation * 2 ).roundToInt()


        if ((faceX + faceWidth) > bitmap.width) {
            faceWidth = bitmap.width - faceX
        }

        if ((faceY + faceHeight) > bitmap.height) {
            faceHeight = bitmap.height - faceY
        }

        try {
            setImageBitmap(Bitmap.createBitmap(bitmap, faceX,faceY,faceWidth,faceHeight))
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onDestroy() {
        faceDetector.release()
    }
}