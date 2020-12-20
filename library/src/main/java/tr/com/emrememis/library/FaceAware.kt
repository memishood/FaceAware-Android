package tr.com.emrememis.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.util.forEach
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector

class FaceAware constructor(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private val path = Path()
    private val rect = RectF()

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        drawable?.let {
            val bitmap = when (it) {
                is BitmapDrawable -> it.bitmap
                is ColorDrawable -> Bitmap.createBitmap(2,2, Bitmap.Config.ARGB_8888)
                else -> Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            val frame = Frame.Builder()
                .setBitmap(bitmap)
                .build()

            val detector = FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()

            val faces = detector.detect(frame)

            faces.forEach { _ , value ->
                value?.let { face ->
                    bind(
                        face = face,
                        bitmap = bitmap
                    )
                }
            }

            detector.release()
        }
    }

    private fun bind(face: Face, bitmap: Bitmap) {
        if (face.position.y > 0 && face.position.x > 0
            && face.position.y + face.height <= bitmap.height
            && face.position.x + face.width <= bitmap.width) {
            setImageBitmap(
                Bitmap.createBitmap(
                    bitmap,
                    face.position.x.toInt(),
                    face.position.y.toInt(),
                    face.width.toInt(),
                    face.height.toInt()
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val radius = (width / 2).toFloat()
        val radius2 = (height / 2).toFloat()
        rect.left = 0f
        rect.top = 0f
        rect.right = width.toFloat()
        rect.bottom = height.toFloat()
        path.addRoundRect(rect, radius, radius2, Path.Direction.CW)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }
}