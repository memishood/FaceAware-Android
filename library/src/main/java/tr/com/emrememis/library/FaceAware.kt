package tr.com.emrememis.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.util.forEach
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector

class FaceAware constructor(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private val path = Path()
    private val rect = RectF()

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)

        drawable ?: return

        val bitmap = when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is ColorDrawable -> Bitmap.createBitmap(2,2, Bitmap.Config.ARGB_8888)
            else -> Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val detector = FaceDetector.Builder(context)
            .setTrackingEnabled(false)
            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
            .build()

        val frame = Frame.Builder()
            .setBitmap(bitmap)
            .build()

        val faces = detector.detect(frame)

        if (faces.isNotEmpty()) {

            faces.forEach { _ , value ->
                if (value != null) {
                    bind(value, bitmap)
                }
            }

        }

        detector.release()

    }

    private fun bind(value: Face, bitmap: Bitmap) {
        if (value.position.y + value.height <= bitmap.height
            && value.position.x + value.width <= bitmap.width) {

            val image = Bitmap.createBitmap(
                bitmap,
                value.position.x.toInt(),
                value.position.y.toInt(),
                value.width.toInt(),
                value.height.toInt()
            )

            setImageBitmap(image)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val radius = (height / 2).toFloat()

        rect.left = 0f
        rect.top = 0f
        rect.right = width.toFloat()
        rect.bottom = height.toFloat()

        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.clipPath(path)

        super.onDraw(canvas)
    }
}