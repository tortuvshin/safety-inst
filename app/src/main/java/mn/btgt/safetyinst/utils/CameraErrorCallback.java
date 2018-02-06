package mn.btgt.safetyinst.utils;

import android.hardware.Camera;
import android.util.Log;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class CameraErrorCallback implements Camera.ErrorCallback {

    private static final String TAG = "CameraErrorCallback";

    @Override
    public void onError(int error, Camera camera) {
        Log.e(TAG, "Encountered an unexpected camera error: " + error);
    }
}