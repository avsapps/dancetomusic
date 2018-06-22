package me.yaraju.dancetomusic;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by avan on 06-02-2018.
 */

public class SoundDetector {
    private final Activity activity;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 42;
    /* data source */
    private SoundMeter mSensor;
    /** config state **/
    private int mThreshold = 1;
    /** running state **/
    private boolean mRunning = false;
    /* constants */
    private static final int POLL_INTERVAL = 300;
    private Handler mHandler = new Handler();

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");

            start();
        }
    };
    private Map<Integer, SoundChangeListener> soundChangeListenerMap;

    private boolean initalized = false;

    public SoundDetector(Activity activity) {
        this.activity = activity;
        soundChangeListenerMap = new HashMap<>();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                // TODO(araju): Give reason here instead of just asking again
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_RECORD_AUDIO is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            init();
        }
    }

    // Create runnable thread to Monitor Voice
    private Runnable mPollTask = new Runnable() {
        public void run() {
            if (mSensor == null) return;
            double amp = mSensor.getAmplitude();
            //Log.i("Noise", "runnable mPollTask");
//            updateDisplay("Monitoring Voice...", amp);

            if ((amp > mThreshold)) {
                onSoundStarted();
            } else {
                onSoundStopped();
            }

            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    public void addSoundChangeListener(SoundChangeListener listener) {
        soundChangeListenerMap.put(listener.getListenerId(), listener);
    }
    private void onSoundStopped() {
        for (SoundChangeListener listener: soundChangeListenerMap.values()) {
            if (listener.isReady()) listener.onSoundStopped();
        }
    }

    private void onSoundStarted() {
        for (SoundChangeListener listener: soundChangeListenerMap.values()) {
            if (listener.isReady()) {
                listener.onSoundStarted();
            }
        }
    }

    private void init() {
        if (mSensor == null) mSensor = new SoundMeter(activity);
        initalized = true;
    }

    private void start() {
        if (!initalized) {
            processNotInitializedError();
            return;
        }
        //Log.i("Noise", "==== start ===");

        if (mSensor != null) mSensor.start();

        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    public void stop() {
        if (!initalized) {
            processNotInitializedError();
            return;
        }
        Log.i("Noise", "==== Stop Noise Monitoring===");
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        if (mSensor!= null) mSensor.stop();
        mRunning = false;
    }

    public void resume() {
        if (!initalized) {
            processNotInitializedError();
            return;
        }
        if (!mRunning && mSensor != null) {
            mRunning = true;
            start();
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!initalized) init();
                    resume();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void processNotInitializedError() {
        Log.e("SoundDetector", "SoundDetector has not been initialized yet!! init() must be called before using.");
    }
}
