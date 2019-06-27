/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

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
