/**
 * Copyright 2013-present Tatum Games, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.listener

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

/**
 * Created by Tatum on 7/24/2015.
 */
class ShakeEventListener : SensorEventListener {

    /**
     * Time when the gesture started.
     */
    private var mFirstDirectionChangeTime: Long = 0

    /**
     * Time when the last movement started.
     */
    private var mLastDirectionChangeTime: Long = 0

    /**
     * How many movements are considered so far.
     */
    private var mDirectionChangeCount = 0

    /**
     * The last x position.
     */
    private var lastX = 0f

    /**
     * The last y position.
     */
    private var lastY = 0f

    /**
     * The last z position.
     */
    private var lastZ = 0f

    /**
     * OnShakeListener that is called when shake is detected.
     */
    private var mShakeListener: OnShakeListener? = null

    /**
     * Interface for shake gesture.
     */
    interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        fun onShake()
    }

    /**
     * Listener for if shake is registered successfully
     * @param listener
     */
    fun setOnShakeListener(listener: OnShakeListener) {
        mShakeListener = listener
    }

    override fun onSensorChanged(se: SensorEvent) {
        // get sensor data
        val x = se.values[0]
        val y = se.values[1]
        val z = se.values[2]

        // calculate movement
        val totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ)

        if (totalMovement > MIN_FORCE) {

            // get time
            val now = System.currentTimeMillis()

            // store first movement time
            if (mFirstDirectionChangeTime == 0L) {
                mFirstDirectionChangeTime = now
                mLastDirectionChangeTime = now
            }

            // check if the last movement was not long ago
            val lastChangeWasAgo = now - mLastDirectionChangeTime
            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

                // store movement data
                mLastDirectionChangeTime = now
                mDirectionChangeCount++

                // store last sensor data
                lastX = x
                lastY = y
                lastZ = z

                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    val totalDuration = now - mFirstDirectionChangeTime
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
                        mShakeListener!!.onShake()
                        resetShakeParameters()
                    }
                }

            } else {
                resetShakeParameters()
            }
        }
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private fun resetShakeParameters() {
        mFirstDirectionChangeTime = 0
        mDirectionChangeCount = 0
        mLastDirectionChangeTime = 0
        lastX = 0f
        lastY = 0f
        lastZ = 0f
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // do nothing
    }

    companion object {

        /**
         * Minimum movement force to consider.
         */
        private val MIN_FORCE = 13

        /**
         * Minimum times in a shake gesture that the direction of movement needs to
         * change.
         */
        private val MIN_DIRECTION_CHANGE = 5

        /**
         * Maximum pause between movements.
         */
        private val MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200

        /**
         * Maximum allowed time for shake gesture.
         */
        private val MAX_TOTAL_DURATION_OF_SHAKE = 400
    }
}