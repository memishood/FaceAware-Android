package ee.webmob.faceaware

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class FaceAware @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "FaceAware"
    }

    private val faceAnalyzer = FaceAnalyzer()
    private var scope: CoroutineScope? = null

    private var facePadding: Float = 0f
    private var borderRadius: Float = 0f
    private var isCircle: Boolean = false

    private val clipPath = Path()
    private val clipRect = RectF()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.FaceAware, defStyleAttr, 0)
            .apply {
                facePadding = getFloat(R.styleable.FaceAware_facePadding, 0f)
                borderRadius = getDimension(R.styleable.FaceAware_borderRadius, 0f)
                isCircle = getInt(R.styleable.FaceAware_shape, 0) == 1
            }.also {
                it.recycle()
            }
    }

    private fun setImageWithFace(bm: Bitmap) {
        scope = CoroutineScope(Dispatchers.Main)
        scope?.launch {
            val imageWithFace = faceAnalyzer.getFace(bm, facePadding)

            if (imageWithFace.isFailure) {
                Log.e(TAG, "setImageWithFace: Face is not found, using default source", imageWithFace.exceptionOrNull())
            }

            super.setImageDrawable(BitmapDrawable(resources, imageWithFace.getOrNull() ?: bm))
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        bm?.let {
            setImageWithFace(it)
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if (drawable is BitmapDrawable) {
            setImageWithFace(drawable.bitmap)
        } else {
            super.setImageDrawable(drawable)
        }
    }

    override fun setImageResource(resId: Int) {
        val bm = BitmapFactory.decodeResource(resources, resId)
        setImageWithFace(bm)
    }

    override fun onDraw(canvas: Canvas) {
        if (isCircle) {
            clipPath.reset()
            clipRect.set(0f, 0f, width.toFloat(), height.toFloat())
            clipPath.addOval(clipRect, Path.Direction.CW)
            canvas.clipPath(clipPath)
        } else if (borderRadius > 0f) {
            clipPath.reset()
            clipRect.set(0f, 0f, width.toFloat(), height.toFloat())
            clipPath.addRoundRect(clipRect, borderRadius, borderRadius, Path.Direction.CW)
            canvas.clipPath(clipPath)
        }
        super.onDraw(canvas)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scope?.cancel()
    }
}