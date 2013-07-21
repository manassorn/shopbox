package com.manassorn.shopbox;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class CameraInfoActivity extends Activity implements SurfaceHolder.Callback {
	Camera camera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_info);
		
	    int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
	    StringBuilder sb = new StringBuilder();
	    sb.append("Display Rotation: " + surfaceRotation(rotation) + "\n");
	    
	    int numOfCam = Camera.getNumberOfCameras();
	    sb.append("Number of Camera: " + numOfCam + "\n");
	    
	    if(numOfCam > 0) {
	    	int cameraId = 0;
		    Camera.CameraInfo info = new Camera.CameraInfo();
		    Camera.getCameraInfo(cameraId, info); 
		    sb.append("Camera Orientation: " + info.orientation + "\n");
		    sb.append("Camera Facing: " + cameraFacing(info.facing) + "\n");

			camera = Camera.open(cameraId);
			Camera.Parameters parameters = camera.getParameters();
			sb.append("Camera Parameters: \n");
			sb.append(parameters.flatten().replaceAll(";", "\n"));
			
			setCameraDisplayOrientation(this, cameraId, camera);
		    
		    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.addCallback(this);
		    
		    TextView textView = (TextView) findViewById(R.id.text);
		    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		    textView.setText(sb.toString());
	    }
	    
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		camera.release();
	}

	static String cameraFacing(int facing) {
		switch(facing) {
			case Camera.CameraInfo.CAMERA_FACING_FRONT:
				return "CAMERA_FACING_FRONT";
		}
		return "CAMERA_FACING_BACK";
	}
	
	static String surfaceRotation(int rotation) {
		switch(rotation) {
			case Surface.ROTATION_0:
				return "ROTATION_0";
			case Surface.ROTATION_90:
				return "ROTATION_90";
			case Surface.ROTATION_180:
				return "ROTATION_180";
			case Surface.ROTATION_270:
				return "ROTATION_270";
		}
		return "UNKNOWN";
	}
	
	public static int setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {      
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;      
		switch (rotation) {
			case Surface.ROTATION_0: degrees = 0; break;          
			case Surface.ROTATION_90: degrees = 90; break;          
			case Surface.ROTATION_180: degrees = 180; break;          
			case Surface.ROTATION_270: degrees = 270; break;      
		}
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);  
		return result;
	} 

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
