package bohdan.sushchak.productsdetector.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import bohdan.sushchak.productsdetector.R;
import bohdan.sushchak.productsdetector.utils.ImageUtils;
import bohdan.sushchak.productsdetector.views.AutoFitTextureView;

import java.io.IOException;
import java.util.List;

@SuppressLint("ValidFragment")
public class LegacyCameraConnectionFragment extends Fragment {

    private Camera camera;
    private Camera.PreviewCallback imageListener;
    private Size desiredSize;
    private AutoFitTextureView textureView;
    private final TextureView.SurfaceTextureListener surfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(
                        final SurfaceTexture texture, final int width, final int height) {

                    int index = getCameraId();
                    camera = Camera.open(index);

                    try {
                        Camera.Parameters parameters = camera.getParameters();
                        List<String> focusModes = parameters.getSupportedFocusModes();
                        if (focusModes != null
                                && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        }
                        List<Camera.Size> cameraSizes = parameters.getSupportedPreviewSizes();
                        Size[] sizes = new Size[cameraSizes.size()];
                        int i = 0;
                        for (Camera.Size size : cameraSizes) {
                            sizes[i++] = new Size(size.width, size.height);
                        }
                        Size previewSize =
                                CameraConnectionFragment.chooseOptimalSize(
                                        sizes, desiredSize.getWidth(), desiredSize.getHeight());
                        parameters.setPreviewSize(previewSize.getWidth(), previewSize.getHeight());
                        camera.setDisplayOrientation(90);
                        camera.setParameters(parameters);
                        camera.setPreviewTexture(texture);
                    } catch (IOException exception) {
                        camera.release();
                    }

                    camera.setPreviewCallbackWithBuffer(imageListener);
                    Camera.Size s = camera.getParameters().getPreviewSize();
                    camera.addCallbackBuffer(new byte[ImageUtils.Companion.getYUVByteSize(s.height, s.width)]);

                    textureView.setAspectRatio(s.height, s.width);

                    camera.startPreview();
                }

                @Override
                public void onSurfaceTextureSizeChanged(
                        final SurfaceTexture texture, final int width, final int height) {
                }

                @Override
                public boolean onSurfaceTextureDestroyed(final SurfaceTexture texture) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture texture) {
                }
            };
    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread backgroundThread;

    @SuppressLint("ValidFragment")
    public LegacyCameraConnectionFragment(
            final Camera.PreviewCallback imageListener, final Size desiredSize) {
        this.imageListener = imageListener;
        this.desiredSize = desiredSize;
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_connection_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        textureView = view.findViewById(R.id.textureView);
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        if (textureView.isAvailable()) {
            camera.startPreview();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopBackgroundThread();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stopCamera();
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        if (backgroundThread == null) {
            backgroundThread = new HandlerThread("CameraBackground");
            backgroundThread.start();
        }
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
        }
    }

    protected void stopCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private int getCameraId() {
        CameraInfo ci = new CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == CameraInfo.CAMERA_FACING_BACK) return i;
        }
        return -1; // No camera found
    }
}
