/**
 * Copyright 2013-present Tatum Games, LLC.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.enums;

public class Enum {

    /**
     * Enum used for identifying card rarity
     */
    public enum CardType {
        SQUAD_LEADER("Squad Leader", "1"),
        COMMON("Common", "2"),
        RARE("Rare", "3"),
        EPIC("Epic", "4"),
        LEGENDARY("Legendary", "5"),
        MYTHIC("Mythic", "6");

        private final String mCardType, mCardTypeId;

        /**
         * Constructir
         *
         * @param cardType   This represents the strength and rarity of the card or Guardian
         * @param cardTypeId Id representation of card type
         */
        CardType(String cardType, String cardTypeId) {
            mCardType = cardType;
            mCardTypeId = cardTypeId;
        }

        @Override
        public String toString() {
            return mCardType;
        }

        /**
         * Method will return id representation of card type
         *
         * @return Id representation of card type
         */
        public String toId() {
            return mCardTypeId;
        }
    }

    /**
     * Enum used for identifying card affinities
     */
    public enum Affinity {
        ROBOTIC("Robotic", "1"),
        PHYSICAL("Physical", "2"),
        BEAST("Beast", "3"),
        ELEMENTAL("Elemental", "4"),
        PSYCHIC("Psychic", "5"),
        BRAINIAC("Brainiac", "6");

        private final String mAffinity, mAffinityId;

        /**
         * Constructor
         *
         * @param affinity   Each Guardian has strengths and weaknesses based on their affinity e.g.
         *                   Robotic, Physical, Beast, Elemental, Psychic, Brainiac
         * @param affinityId Id representation of affinity type
         */
        Affinity(String affinity, String affinityId) {
            mAffinity = affinity;
            mAffinityId = affinityId;
        }

        @Override
        public String toString() {
            return mAffinity;
        }

        /**
         * Method will return id representation of affinity type
         *
         * @return Id representation of affinity type
         */
        public String toId() {
            return mAffinityId;
        }
    }

    /**
     * Enum used for identifying Guardian stats
     */
    public enum Stats {
        HEALTH("Health"),
        SPEED("Speed"),
        STRENGTH("Strength"),
        WISDOM("Wisdom"),
        PHYSICAL_RESISTANCE("PhysicalResistance"),
        MAGICAL_RESISTANCE("MagicalResistance"),
        CRITICAL_PERCENT("CriticalPercent");

        private final String mStats;

        Stats(String stats) {
            mStats = stats;
        }

        @Override
        public String toString() {
            return mStats;
        }
    }
}
