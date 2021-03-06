/*
 * Copyright (C) 2008 ZXing authors
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

package com.manassorn.shopbox.barcode;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.manassorn.shopbox.camera.CameraManager;

/**
 * This thread does all the heavy lifting of decoding the images.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class DecodeThread extends Thread {

	public static final String BARCODE_BITMAP = "barcode_bitmap";
	public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

	static final Collection<BarcodeFormat> PRODUCT_FORMATS;
	static final Collection<BarcodeFormat> ONE_D_FORMATS;
	static final Collection<BarcodeFormat> QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
	static final Collection<BarcodeFormat> DATA_MATRIX_FORMATS = EnumSet
			.of(BarcodeFormat.DATA_MATRIX);
	static {
		PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A, BarcodeFormat.UPC_E,
				BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.RSS_14,
				BarcodeFormat.RSS_EXPANDED);
		ONE_D_FORMATS = EnumSet.of(BarcodeFormat.CODE_39, BarcodeFormat.CODE_93,
				BarcodeFormat.CODE_128, BarcodeFormat.ITF, BarcodeFormat.CODABAR);
		ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
	}

	private final Map<DecodeHintType, Object> hints;
	private Handler decodeHandler;
	private final CountDownLatch handlerInitLatch;
	private Handler activityHandler;
	private CameraManager cameraManager;

	public DecodeThread(Handler activityHandler, CameraManager cameraManager,
			Collection<BarcodeFormat> decodeFormats, Map<DecodeHintType, ?> baseHints,
			String characterSet, ResultPointCallback resultPointCallback) {
		this.activityHandler = activityHandler;
		this.cameraManager = cameraManager;

		handlerInitLatch = new CountDownLatch(1);

		hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		if (baseHints != null) {
			hints.putAll(baseHints);
		}

		decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
		decodeFormats.addAll(DecodeThread.ONE_D_FORMATS);
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}
		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
	}

	public Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return decodeHandler;
	}

	@Override
	public void run() {
		Looper.prepare();
		decodeHandler = new DecodeHandler(activityHandler, cameraManager, hints);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
