/**
 * Copyright 2013-present Tatum Games, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by Tatum on 7/24/2015.
 */
public class ShakeEventListener implements SensorEventListener {

    /**
     * Minimum movement force to consider.
     */
    private static final int MIN_FORCE = 13;

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private static final int MIN_DIRECTION_CHANGE = 5;

    /**
     * Maximum pause between movements.
     */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;

    /**
     * Maximum allowed time for shake gesture.
     */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;

    /**
     * Time when the gesture started.
     */
    private long mFirstDirectionChangeTime = 0;

    /**
     * Time when the last movement started.
     */
    private long mLastDirectionChangeTime;

    /**
     * How many movements are considered so far.
     */
    private int mDirectionChangeCount = 0;

    /**
     * The last x position.
     */
    private float lastX = 0;

    /**
     * The last y position.
     */
    private float lastY = 0;

    /**
     * The last z position.
     */
    private float lastZ = 0;

    /**
     * OnShakeListener that is called when shake is detected.
     */
    private OnShakeListener mShakeListener;

    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        void onShake();
    }

    /**
     * Listener for if shake is registered successfully
     * @param listener
     */
    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        // get sensor data
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];

        // calculate movement
        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

        if (totalMovement > MIN_FORCE) {

            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (mFirstDirectionChangeTime == 0) {
                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
            }

            // check if the last movement was not long ago
            long lastChangeWasAgo = now - mLastDirectionChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

                // store movement data
                mLastDirectionChangeTime = now;
                mDirectionChangeCount++;

                // store last sensor data
                lastX = x;
                lastY = y;
                lastZ = z;

                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    long totalDuration = now - mFirstDirectionChangeTime;
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
                        mShakeListener.onShake();
                        resetShakeParameters();
                    }
                }

            } else {
                resetShakeParameters();
            }
        }
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private void resetShakeParameters() {
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }
}