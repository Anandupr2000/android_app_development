package com.example.screenrecorder;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.screenrecorder.databinding.ActivityMainBinding;

public class screenCaptureService extends Service {
    private Activity activity;
    private int mScreenDensity;
    private MediaProjectionManager mMediaProjectionManager;
    public screenCaptureService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mMediaProjectionManager = (MediaProjectionManager)
                activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private void startScreenCapture() {
        if (mSurface == null || activity == null) {
            return;
        }
        if (mMediaProjection != null) {
            setUpVirtualDisplay();
        } else if (mResultCode != 0 && mResultData != null) {
            setUpMediaProjection();
            setUpVirtualDisplay();
        } else {
            Log.i(TAG, "Requesting confirmation");
            // This initiates a prompt dialog for the user to confirm screen projection.
            startActivityForResult(
                    mMediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        }
    }
}