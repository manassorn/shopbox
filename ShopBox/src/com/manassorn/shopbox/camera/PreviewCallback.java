/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manassorn.shopbox.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class PreviewCallback implements Camera.PreviewCallback {

	private static final String TAG = PreviewCallback.class.getSimpleName();

	private final CameraConfigurationManager configManager;
	private Handler previewHandler;
	private int previewMessage;

	PreviewCallback(CameraConfigurationManager configManager) {
		this.configManager = configManager;
	}

	void setHandler(Handler previewHandler, int previewMessage) {
		this.previewHandler = previewHandler;
		this.previewMessage = previewMessage;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
//		Bitmap src = BitmapFactory.decodeByteArray(data, 0, data.length);
//		
//	    Matrix m = new Matrix();
//	    m.preScale(-1, 1);
//	    Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
//		
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		dst.compress(Bitmap.CompressFormat.PNG, 100, stream);
//		data = stream.toByteArray();
	    
		Log.d(TAG, ">>>onPreviewFrame");
		Point cameraResolution = configManager.getCameraResolution();
		Handler thePreviewHandler = previewHandler;
		if (cameraResolution != null && thePreviewHandler != null) {
			Message message = thePreviewHandler.obtainMessage(previewMessage, cameraResolution.x,
					cameraResolution.y, data);
			message.sendToTarget();
			previewHandler = null;
		} else {
			Log.d(TAG, "Got preview callback, but no handler or resolution available");
		}
//		try {
//			Camera.Parameters parameters = camera.getParameters();
//			Size size = parameters.getPreviewSize();
//			YuvImage image = new YuvImage(data, parameters.getPreviewFormat(), size.width,
//					size.height, null);
//			File file = new File(Environment.getExternalStorageDirectory().getPath() + "/out.jpg");
//			FileOutputStream filecon = new FileOutputStream(file);
//			boolean success = image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 90, filecon);
//		} catch (FileNotFoundException e) {
////			Toast toast = Toast.makeText(getBaseContext(), e.getMessage(), 1000);
////			toast.show();
//			Log.d(TAG, "File NOT FOUND");
//		}
	}

}
