package bohdan.sushchak.productsdetector.model;

import android.app.Activity;

import java.io.IOException;

public class ClassifierQuantizedMobileNet extends Classifier {

    private byte[][] labelProbArray = null;

    public ClassifierQuantizedMobileNet(Activity activity) throws IOException {
        super(activity);
        this.labelProbArray = new byte[1][getNumLabels()];
    }

    @Override
    public int getImageSizeX() {
        return 224;
    }

    @Override
    public int getImageSizeY() {
        return 224;
    }

    @Override
    protected String getModelPath() {
        return "mobilenet_v1_1.0_224_quant.tflite";
    }

    @Override
    protected String getLabelPath() {
        return "labels_en.txt";
    }

    @Override
    protected void addPixelValue(int pixelValue) {
        imgData.put((byte) ((pixelValue >> 16) & 0xFF));
        imgData.put((byte) ((pixelValue >> 8) & 0xFF));
        imgData.put((byte) (pixelValue & 0xFF));
    }

    @Override
    protected float getNormalizedProbability(int labelIndex) {
        return (labelProbArray[0][labelIndex] & 0xff) / 255.0f;
    }

    @Override
    protected void runInference() {
        tflite.run(imgData, labelProbArray);
    }
}
