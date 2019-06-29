package bohdan.sushchak.productsdetector.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.util.Size
import bohdan.sushchak.productsdetector.R
import bohdan.sushchak.productsdetector.model.Classifier
import bohdan.sushchak.productsdetector.model.ClassifierQuantizedMobileNet
import bohdan.sushchak.productsdetector.model.Recognition
import bohdan.sushchak.productsdetector.utils.ImageUtils
import kotlinx.coroutines.runBlocking

class DetectionActivity : CameraActivity() {

    private lateinit var classifier: Classifier
    private var sensorOrientation: Int = 0
    private lateinit var rgbFrameBitmap: Bitmap
    private lateinit var croppedBitmap: Bitmap
    private lateinit var frameToCropTransform: Matrix
    private lateinit var cropToFrameTransform: Matrix

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        classifier = ClassifierQuantizedMobileNet(this)
    }

    override fun onPreviewSizeChosen(size: Size, rotation: Int) {
        previewWidth = size.width
        previewHeight = size.height
        sensorOrientation = rotation - screenOrientation

        if (!this::classifier.isInitialized) return

        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
        croppedBitmap = Bitmap.createBitmap(
            classifier.imageSizeX, classifier.imageSizeY, Bitmap.Config.ARGB_8888
        )
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewWidth,
            previewHeight,
            classifier.imageSizeX,
            classifier.imageSizeY,
            sensorOrientation,
            maintainAspectRatio = true
        )

        cropToFrameTransform = Matrix()
        frameToCropTransform!!.invert(cropToFrameTransform)
    }

    override fun processImage() {
        if (this::classifier.isInitialized) {
            rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight)
            val canvas = Canvas(croppedBitmap)
            canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null)

            runInBackground {
                val results = classifier.recognizeImage(croppedBitmap)
                updateUI(results)
            }
        }
        readyForNextImage()
    }

    private fun updateUI(results: List<Recognition>) = runBlocking {

        Log.d("TAG", results.toString());
    }

    override fun getFragmentContainer(): Int {
        return R.id.container
    }

}