package bohdan.sushchak.productsdetector.model;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class Classifier {

    private static final int MAX_RESULTS = 3;
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;

    private final int[] intValues = new int[getImageSizeX() * getImageSizeY()];
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    private MappedByteBuffer tfliteModel;
    private List<String> labels;
    protected Interpreter tflite;
    protected ByteBuffer imgData = null;

    protected Classifier(Activity activity) throws IOException {
        tfliteModel = loadModelFile(activity);
        tfliteOptions.setNumThreads(1);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        labels = loadLabelList(activity);
        imgData =
                ByteBuffer.allocateDirect(
                        DIM_BATCH_SIZE
                                * getImageSizeX()
                                * getImageSizeY()
                                * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
    }

    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labels = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(getLabelPath())));
        String line;
        while ((line = reader.readLine()) != null) {
            labels.add(line);
        }
        reader.close();
        return labels;
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        for (int i = 0; i < getImageSizeX(); ++i) {
            for (int j = 0; j < getImageSizeY(); ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }
    }

    public List<Recognition> recognizeImage(final Bitmap bitmap) {
        convertBitmapToByteBuffer(bitmap);
        runInference();

        PriorityQueue<Recognition> pq =
                new PriorityQueue<Recognition>(
                        3,
                        (lhs, rhs) -> {
                            // Intentionally reversed to put high confidence at the head of the queue.
                            return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                        });
        for (int i = 0; i < labels.size(); ++i) {
            pq.add(
                    new Recognition(
                            "" + i,
                            labels.size() > i ? labels.get(i) : "unknown",
                            getNormalizedProbability(i)));
        }
        final ArrayList<Recognition> recognitions = new ArrayList<Recognition>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }

    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
        tfliteModel = null;
    }

    public abstract int getImageSizeX();

    public abstract int getImageSizeY();

    protected abstract String getModelPath();

    protected abstract String getLabelPath();

    protected abstract void addPixelValue(int pixelValue);

    protected abstract float getNormalizedProbability(int labelIndex);

    protected abstract void runInference();

    protected int getNumLabels() {
        return labels.size();
    }
}
