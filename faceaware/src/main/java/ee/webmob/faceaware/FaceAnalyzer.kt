package ee.webmob.faceaware

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FaceAnalyzer {
    private val options: FaceDetectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .setMinFaceSize(0.1f)
        .enableTracking()
        .build()

    private val faceDetector = FaceDetection.getClient(options)

    suspend fun getFace(bitmap: Bitmap, padding: Float) = suspendCoroutine<Result<Bitmap>> {
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        faceDetector.process(inputImage)
            .addOnSuccessListener { faces ->
                val faceBitmap = getFaceBitmap(faces.firstOrNull(), bitmap, padding)
                it.resume(Result.success(value = faceBitmap ?: bitmap))
            }
            .addOnFailureListener { err ->
                it.resume(Result.failure(err))
            }
    }

    private fun getFaceBitmap(face: Face?, bitmap: Bitmap, padding: Float): Bitmap? {
        if (face != null) {
            val rect = face.boundingBox.apply {
                set(
                    (left.toFloat() - padding).coerceAtLeast(0f).toInt(),
                    (top.toFloat() - padding).coerceAtLeast(0f).toInt(),
                    (right + padding.toInt()).coerceAtMost(bitmap.width),
                    (bottom + padding.toInt()).coerceAtMost(bitmap.height)
                )
            }
            return Bitmap.createBitmap(
                bitmap,
                rect.left,
                rect.top,
                rect.width(),
                rect.height()
            )
        }
        return null
    }
}