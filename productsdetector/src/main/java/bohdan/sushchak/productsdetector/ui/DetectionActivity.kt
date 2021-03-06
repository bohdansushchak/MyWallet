package bohdan.sushchak.productsdetector.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Size
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import bohdan.sushchak.productsdetector.R
import bohdan.sushchak.productsdetector.model.AddedProduct
import bohdan.sushchak.productsdetector.model.Classifier
import bohdan.sushchak.productsdetector.model.ClassifierQuantizedMobileNet
import bohdan.sushchak.productsdetector.model.Recognition
import bohdan.sushchak.productsdetector.utils.ImageUtils
import bohdan.sushchak.productsdetector.utils.ProductAdapter
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.bottom_sheet_result.*
import kotlin.math.roundToInt

const val PREF_WAIT_KEY = "waitkey"
const val PREF_SHOW_ACCURACY = "showAccuracy"

class DetectionActivity : CameraActivity() {

    private lateinit var classifier: Classifier
    private var sensorOrientation: Int = 0
    private lateinit var rgbFrameBitmap: Bitmap
    private lateinit var croppedBitmap: Bitmap
    private lateinit var frameToCropTransform: Matrix
    private lateinit var cropToFrameTransform: Matrix

    private lateinit var preferences: SharedPreferences

    private val addedProducts = mutableListOf<AddedProduct>()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        setSupportActionBar(myToolbar as Toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        classifier = ClassifierQuantizedMobileNet(this)

        preferences = PreferenceManager.getDefaultSharedPreferences(this@DetectionActivity)

        initSeekBar()
        initShowAccuracySwitch()
        initAddedProductView()
    }

    private fun initAddedProductView() {
        productAdapter = ProductAdapter(this, addedProducts)

        rlAddedItems.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@DetectionActivity)
        }

        productAdapter.onRemoveListener = {
            removeProduct(it)
        }

        btnClearAll.setOnClickListener {
            if(addedProducts.size > 0){
                dialogShow(
                    R.string.d_title_remove_items,
                    R.string.d_content_remove_items,
                    yes = {
                        removeAllItems()
                    })
            } else {
                Toast.makeText(this, R.string.t_list_is_empty, Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            if(addedProducts.size > 0){
                dialogShow(R.string.d_title_save_result, R.string.d_content_save_result, yes = { saveResult() })
            } else {
                Toast.makeText(this, R.string.t_nothing_to_save, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeAllItems() {
        addedProducts.clear()
        productAdapter.notifyDataSetChanged()
    }

    private fun saveResult() {
        val array = ArrayList<AddedProduct>()

        addedProducts.forEach { item ->
            array.add(item)
        }

        intent.putExtra("detectedProducts", array)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun initShowAccuracySwitch() {
        val isShowAccuracy = preferences.getBoolean(PREF_SHOW_ACCURACY, false)
        switchShowAccuracy.isChecked = isShowAccuracy
        changeAccuracyVisibility(isShowAccuracy)

        switchShowAccuracy.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().apply {
                putBoolean(PREF_SHOW_ACCURACY, isChecked)
            }.apply()
            changeAccuracyVisibility(isChecked)
        }
    }

    private fun initSeekBar() {
        val progress = preferences.getLong(PREF_WAIT_KEY, 0)
        seekBarWait.progress = progress.toInt()
        tvWaitTime.text = "${progress}ms"

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
        detectedItemFirst.accuracy = (first.confidence * 100).roundToInt().toString()
        detectedItemFirst.setOnButtonClickListener(View.OnClickListener {
            addProduct(first.title)
        })

        val second = results[1]
        detectedItemSecond.label = second.title
        detectedItemSecond.accuracy = (second.confidence * 100).roundToInt().toString()
        detectedItemSecond.setOnButtonClickListener(View.OnClickListener {
            addProduct(second.title)
        })

        val third = results[2]
        detectedItemThird.label = third.title
        detectedItemThird.accuracy = (third.confidence * 100).roundToInt().toString()
        detectedItemThird.setOnButtonClickListener(View.OnClickListener {
            addProduct(third.title)
        })

        val fourth = results[3]
        detectedItemFourth.label = fourth.title
        detectedItemFourth.accuracy = (fourth.confidence * 100).roundToInt().toString()
        detectedItemFourth.setOnButtonClickListener(View.OnClickListener {
            addProduct(fourth.title)
        })

        val fifth = results[4]
        detectedItemFifth.label = fifth.title
        detectedItemFifth.accuracy = (fifth.confidence * 100).roundToInt().toString()
        detectedItemFifth.setOnButtonClickListener(View.OnClickListener {
            addProduct(fifth.title)
        })
    }

    private fun updateProcessingTime(processingTime: Long) {
        tvInferenceTime.text = "${processingTime}ms"
    }

    override fun getFragmentContainer(): Int {
        return R.id.container
    }

    private fun addProduct(productName: String) {
        val foundProduct = addedProducts.find { it.product == productName }

        if (foundProduct == null) {
            val product = AddedProduct(product = productName, count = 1)
            addedProducts.add(product)
        } else {
            val idx = addedProducts.indexOf(foundProduct)
            addedProducts[idx] = foundProduct.apply { count = foundProduct.count + 1 }
        }

        productAdapter.notifyDataSetChanged()
    }

    private fun removeProduct(product: AddedProduct) {
        val foundProduct = addedProducts.find { it.product == product.product }!!

        if (foundProduct.count > 1) {
            val idx = addedProducts.indexOf(foundProduct)
            addedProducts[idx] = foundProduct.apply { count = foundProduct.count - 1 }
        } else {
            addedProducts.remove(foundProduct)
        }

        productAdapter.notifyDataSetChanged()
    }

    private fun changeAccuracyVisibility(isVisible: Boolean) {
        detectedItemFirst.showAccuracy = isVisible
        detectedItemSecond.showAccuracy = isVisible
        detectedItemThird.showAccuracy = isVisible
        detectedItemFourth.showAccuracy = isVisible
        detectedItemFifth.showAccuracy = isVisible
    }

    private fun dialogShow(title: Int, content: Int, yes: (() -> Unit), cancel: (() -> Unit)? = null) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(content)
        alertDialog.setPositiveButton(R.string.d_btn_yes) { _, _ ->
            yes.invoke()
        }

        alertDialog.setNegativeButton(R.string.d_btn_cancel) { _, _ ->
            cancel?.invoke()
        }

        val dialog = alertDialog.create()
        dialog.show()
    }
}