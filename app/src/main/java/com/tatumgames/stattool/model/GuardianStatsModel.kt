/**
 * Copyright 2013-present Tatum Games, LLC.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.model

import com.tatumgames.stattool.enums.Enum

/**
 * Model class for Guardian statistics
 * Created by Tatum on 7/22/2015.
 */
class GuardianStatsModel {

    /**
     * retrieve affinity
     *
     * @return Affinity value
     */
    /**
     * Set affinity
     *
     * @param affinity Each Guardian has strengths and weaknesses based on their affinity e.g.
     * Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     */
    var affinity: Enum.Affinity? = null
    /**
     * retrieve card type
     *
     * @return Card type value
     */
    /**
     * Set card type
     *
     * @param cardType This represents the strength and rarity of the card or Guardian
     */
    var cardType: Enum.CardType? = null
    /**
     * retrieve health power
     *
     * @return Health power value
     */
    /**
     * set health power
     *
     * @param hp Randomized value to be added to create the Base HP stat
     */
    var hp: Int = 0
    /**
     * retrieve strength
     *
     * @return Strength value
     */
    /**
     * set strength
     *
     * @param str Randomized value to be added to create the Base Strength stat
     */
    var str: Int = 0
    /**
     * retrieve speed
     *
     * @return Speed value
     */
    /**
     * set speed
     *
     * @param spd Randomized value to be added to create the Base Speed stat
     */
    var spd: Int = 0
    /**
     * retrieve wisdom
     *
     * @return Wisdom value
     */
    /**
     * set wisdom
     *
     * @param wis Randomized value to be added to create the Base Wisdom stat
     */
    var wis: Int = 0
    /**
     * retrieve physical defense
     *
     * @return Physical defense value
     */
    /**
     * set physical defense
     *
     * @param phyDef Randomized value to be added to create the Base Physical Defense stat
     */
    var phyDef: Int = 0
    /**
     * retrieve magical defense
     *
     * @return Magical defense value
     */
    /**
     * set magical def
     *
     * @param magDef Randomized value to be added to create the Base Magical Defense stat
     */
    var magDef: Int = 0
    /**
     * retrieve critical percent
     *
     * @return Critical percent value
     */
    /**
     * set critical percent
     *
     * @param crit Randomized value to be added to create the Base Critical Percentage stat
     */
    var crit: Int = 0
    /**
     * Retrieve maximum possible level
     *
     * @return Maximum possible level
     */
    var mavLv: Int = 0
        private set
    /**
     * Retrieve maximum possible ascension
     *
     * @return Maximum possible ascension
     */
    /**
     * Setter for setting maximum ascension
     *
     * @param maxAsc Maximum ascension
     */
    var maxAsc: Int = 0

    /**
     * Setter for setting maximum level
     *
     * @param mavLv Maximum level
     */
    fun setMaxLv(mavLv: Int) {
        this.mavLv = mavLv
    }

    /**
     * Method is used to reset all stat attributes
     */
    fun reset() {
        hp = 0
        str = 0
        spd = 0
        wis = 0
        phyDef = 0
        magDef = 0
        crit = 0
    }

}
