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

package com.tatumgames.stattool.helper

import android.text.InputFilter
import android.text.Spanned

/**
 * Created by Tatum on 7/24/2015.
 */
class InputFilterMinMax(private val min: Int, private val max: Int) : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(min, max, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * Method is used to check if a value is between a min, max range
     * @param a min value
     * @param b max value
     * @param c value to be determined
     * @return True if value is between min and max values, otherwise false
     */
    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c >= a && c <= b else c >= b && c <= a
    }
}
