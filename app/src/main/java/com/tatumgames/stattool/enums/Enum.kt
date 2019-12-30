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

package com.tatumgames.stattool.enums

class Enum {

    /**
     * Enum used for identifying card rarity
     */
    enum class CardType
    /**
     * Constructir
     *
     * @param cardType   This represents the strength and rarity of the card or Guardian
     * @param cardTypeId Id representation of card type
     */
    private constructor(private val mCardType: String, private val mCardTypeId: String) {
        SQUAD_LEADER("Squad Leader", "1"),
        COMMON("Common", "2"),
        RARE("Rare", "3"),
        EPIC("Epic", "4"),
        LEGENDARY("Legendary", "5"),
        MYTHIC("Mythic", "6");

        override fun toString(): String {
            return mCardType
        }

        /**
         * Method will return id representation of card type
         *
         * @return Id representation of card type
         */
        fun toId(): String {
            return mCardTypeId
        }
    }

    /**
     * Enum used for identifying card affinities
     */
    enum class Affinity
    /**
     * Constructor
     *
     * @param affinity   Each Guardian has strengths and weaknesses based on their affinity e.g.
     * Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @param affinityId Id representation of affinity type
     */
    private constructor(private val mAffinity: String, private val mAffinityId: String) {
        ROBOTIC("Robotic", "1"),
        PHYSICAL("Physical", "2"),
        BEAST("Beast", "3"),
        ELEMENTAL("Elemental", "4"),
        PSYCHIC("Psychic", "5"),
        BRAINIAC("Brainiac", "6");

        override fun toString(): String {
            return mAffinity
        }

        /**
         * Method will return id representation of affinity type
         *
         * @return Id representation of affinity type
         */
        fun toId(): String {
            return mAffinityId
        }
    }

    /**
     * Enum used for identifying Guardian stats
     */
    enum class Stats private constructor(private val mStats: String) {
        HEALTH("Health"),
        SPEED("Speed"),
        STRENGTH("Strength"),
        WISDOM("Wisdom"),
        PHYSICAL_RESISTANCE("PhysicalResistance"),
        MAGICAL_RESISTANCE("MagicalResistance"),
        CRITICAL_PERCENT("CriticalPercent");

        override fun toString(): String {
            return mStats
        }
    }
}
