package bohdan.sushchak.productsdetector.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.*
import androidx.fragment.app.Fragment
import bohdan.sushchak.productsdetector.R
import kotlinx.android.synthetic.main.camera_connection_fragment.*

class CameraFragment : Fragment() {

    private var cameraDevice: CameraDevice? = null
    private lateinit var cameraId: String
    private lateinit var imageDimension: Size
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var cameraSession: CameraCaptureSession

    private var backgroundHandler: Handler? = null
    private var handlerThread: HandlerThread? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.camera_connection_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private val surfaceListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(
            surface: SurfaceTexture?,
            width: Int,
            height: Int
        ) {
            try {
                openCamera()
            } catch (e: Exception) {

            }
        }
    }



    private fun initViews() {
        textureView.surfaceTextureListener = surfaceListener
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        if (activity == null) throw IllegalStateException("Activity cannot be null")

        val cameraManager = activity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]

        val cc = cameraManager.getCameraCharacteristics(cameraId)
        val map = cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]

        cameraManager.openCamera(cameraId, cameraStateCallback, null)
    }

    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            startCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    @SuppressLint("Recycle")
    private fun startCameraPreview() {
        val texture = textureView.surfaceTexture
        texture.setDefaultBufferSize(imageDimension.width, imageDimension.height)

        val surface = Surface(texture)

        captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)

        cameraDevice!!.createCaptureSession(mutableListOf(surface), cameraCaptureSession, null)
    }

    private val cameraCaptureSession = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
        }

        override fun onConfigured(session: CameraCaptureSession) {
            if (cameraDevice == null) {
                return
            }
            cameraSession = session
            updatePreview()
        }
    }

    private fun updatePreview() {
        if (cameraDevice == null) {
            return
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        cameraSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()

        if (textureView.isAvailable) {
            try {
                openCamera()
            } catch (e: Exception) {

            }
        } else {
            textureView.surfaceTextureListener = surfaceListener
        }
    }

    override fun onPause() {
        try {
            stopBackgroundThread()
        } catch (e: Exception) {

        }
        super.onPause()
    }

    private fun startBackgroundThread() {
        handlerThread = HandlerThread("Camera background")
        handlerThread!!.start()

        backgroundHandler = Handler(handlerThread!!.looper)
    }

    private fun stopBackgroundThread() {
        handlerThread?.quitSafely()
        handlerThread?.join()

        backgroundHandler = null
        handlerThread = null
    }

}