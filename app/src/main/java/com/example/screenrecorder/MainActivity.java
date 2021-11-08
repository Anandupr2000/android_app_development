package com.example.screenrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.example.screenrecorder.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private static final String TAG = "ScreenCaptureActivity";

    private static final String STATE_RESULT_CODE = "result_code";
    private static final String STATE_RESULT_DATA = "result_data";

    private int mResultCode;
    private int mScreenDensity;
    private static final int REQUEST_MEDIA_PROJECTION = 1;


    private Intent mResultData;
    private Surface mSurface;
    private SurfaceView mSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            mResultCode = savedInstanceState.getInt(STATE_RESULT_CODE);
            mResultData = savedInstanceState.getParcelable(STATE_RESULT_DATA);
        }
//        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
        mSurfaceView = binding.surfaceView;
        mSurface = mSurfaceView.getHolder().getSurface();

//       call service
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.btn.getText().equals("Start")){
                    binding.btn.setText("Stop");
                    binding.textView.setText("Recording ...");
                }
                else {
                    binding.btn.setText("Start");
                    binding.textView.setText("");
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mResultData != null) {
            outState.putInt(STATE_RESULT_CODE, mResultCode);
            outState.putParcelable(STATE_RESULT_DATA, mResultData);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                Log.i(TAG, "User cancelled");
                Toast.makeText(this, "User denied screen sharing permission", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(TAG, "Starting screen capture");
            mResultCode = resultCode;
            mResultData = data;
            setUpMediaProjection();
            setUpVirtualDisplay();
        }
    }
}