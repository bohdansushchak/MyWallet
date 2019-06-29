package bohdan.sushchak.productsdetector.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Size
import android.widget.SeekBar
import bohdan.sushchak.productsdetector.R
import bohdan.sushchak.productsdetector.model.Classifier
import bohdan.sushchak.productsdetector.model.ClassifierQuantizedMobileNet
import bohdan.sushchak.productsdetector.model.Recognition
import bohdan.sushchak.productsdetector.utils.ImageUtils
import kotlinx.android.synthetic.main.bottom_sheet_result.*

const val PREF_WAIT_KEY = "waitkey"

class DetectionActivity : CameraActivity() {

    private lateinit var classifier: Classifier
    private var sensorOrientation: Int = 0
    private lateinit var rgbFrameBitmap: Bitmap
    private lateinit var croppedBitmap: Bitmap
    private lateinit var frameToCropTransform: Matrix
    private lateinit var cropToFrameTransform: Matrix

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        classifier = ClassifierQuantizedMobileNet(this)

        preferences = PreferenceManager.getDefaultSharedPreferences(this@DetectionActivity)

        initSeekBar()
    }

    private fun initSeekBar() {
        val progress = preferences.getLong(PREF_WAIT_KEY, 0)
        seekBarWait.progress = progress.toInt()

        seekBarWait.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvWaitTime.text = "${progress}ms"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    preferences.edit().apply {
                        putLong(PREF_WAIT_KEY, it.progress.toLong())
                    }.apply()
                }
            }
        })
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
        frameToCropTransform.invert(cropToFrameTransform)
    }

    override fun processImage() {
        if (this::classifier.isInitialized) {
            rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight)
            val canvas = Canvas(croppedBitmap)
            canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null)

            runInBackground {
                val startTime = SystemClock.uptimeMillis()
                val results = classifier.recognizeImage(croppedBitmap)
                val processingTime = SystemClock.uptimeMillis() - startTime

                val sleepMs = (seekBarWait.progress).toLong()
                if (sleepMs != 0L) {
                    Thread.sleep(sleepMs)
                }

                runOnUiThread {
                    updateResults(results)
                    updateProcessingTime(processingTime)
                }
            }
            readyForNextImage()
        }
    }

    private fun updateResults(results: List<Recognition>) {
        if (results.isEmpty() || results.size < Classifier.MAX_RESULTS) return

        val first = results[0]
        detectedItemFirst.label = first.title

        val second = results[1]
        detectedItemSecond.label = second.title

        val third = results[2]
        detectedItemThird.label = third.title

        val fourth = results[3]
        detectedItemFourth.label = fourth.title

        val fifth = results[4]
        detectedItemFifth.label = fifth.title
    }

    private fun updateProcessingTime(processingTime: Long) {
        tvInferenceTime.text = "${processingTime}ms"
    }

    override fun getFragmentContainer(): Int {
        return R.id.container
    }
}