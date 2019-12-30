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

package com.tatumgames.stattool.utils

import com.tatumgames.stattool.enums.Enum
import com.tatumgames.stattool.model.GuardianStatsModel

import java.util.Random

class StatUtils {

    private var mCardType: Enum.CardType? = null
    private var mAffinity: Enum.Affinity? = null

    /**
     * Retrieve maximum ascension level
     *
     * Squad Leader return 0 ascension by default
     *
     * @return Maximum ascension level based on cardType
     */
    private// SQUAD_LEADER max ascension
    val maxAsc: Int
        get() {
            var maxAsc = 0

            if (mCardType == Enum.CardType.COMMON ||
                    mCardType == Enum.CardType.RARE ||
                    mCardType == Enum.CardType.EPIC) {
                maxAsc = 3
            } else if (mCardType == Enum.CardType.LEGENDARY || mCardType == Enum.CardType.MYTHIC) {
                maxAsc = 4
            }
            return maxAsc
        }

    /**
     * @param cardType This represents the strength and rarity of the card or Guardian
     * @param affinity Each Guardian has strengths and weaknesses based on their affinity e.g.
     * Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @param asc      Ascension value
     * @return GuardianStatsModel object with generated base stats
     */
    fun getStats(cardType: Enum.CardType, affinity: Enum.Affinity, asc: Int): GuardianStatsModel {
        // initialize
        mCardType = cardType
        mAffinity = affinity

        // populate stat object
        val stats = GuardianStatsModel()
        stats.setMaxLv(getMaxLv(asc))
        stats.maxAsc = maxAsc
        stats.hp = randHpStatBase()
        stats.str = randStrStat()
        stats.spd = randSpdStat()
        stats.wis = randWisStat()
        stats.phyDef = randPhyDefStat()
        stats.magDef = randMagDefStat()
        stats.crit = randCritPercStat()
        stats.cardType = cardType
        stats.affinity = affinity
        return stats
    }

    /**
     * Method is used to calculate HP base stat using cardType and affinity to generate
     * a randomized value
     *
     *
     *
     * (C) hp base: [350-400]
     * (R) hp base: [400-475]
     * (E) hp base: [475-550]
     * (L) hp base: [575-675]
     * (M) hp base: [700-850]
     * (SL) hp base: [437-512]
     *
     *
     * @return HP base value
     */
    private fun randHpStatBase(): Int {
        val rand = Random()
        var hp = 0

        if (mCardType == Enum.CardType.COMMON) {
            hp = rand.nextInt(51) + 350
        } else if (mCardType == Enum.CardType.RARE) {
            hp = rand.nextInt(76) + 400
        } else if (mCardType == Enum.CardType.EPIC) {
            hp = rand.nextInt(76) + 475
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            hp = rand.nextInt(101) + 575
        } else if (mCardType == Enum.CardType.MYTHIC) {
            hp = rand.nextInt(151) + 700
        } else if (mCardType == Enum.CardType.SQUAD_LEADER) {
            hp = rand.nextInt(76) + 437
        }
        return hp + randHpByAffinity(hp)
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * base HP will be, [baseHp = randomized hp + value based on affinity]
     *
     *
     *
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     *
     *
     * @param hp Randomized value to be added to create the Base HP stat
     * @return Value based on affinity
     */
    private fun randHpByAffinity(hp: Int): Int {
        var hpByAffinity = 0

        if (mAffinity == Enum.Affinity.ROBOTIC) {
            hpByAffinity = (hp * MULTIPLIER_TIER[2]).toInt()
        } else if (mAffinity == Enum.Affinity.PHYSICAL) {
            hpByAffinity = (hp * MULTIPLIER_TIER[0]).toInt()
        } else if (mAffinity == Enum.Affinity.BEAST) {
            hpByAffinity = (hp * MULTIPLIER_TIER[3]).toInt()
        } else if (mAffinity == Enum.Affinity.ELEMENTAL) {
            hpByAffinity = (hp * MULTIPLIER_TIER[1]).toInt()
        } else if (mAffinity == Enum.Affinity.PSYCHIC) {
            hpByAffinity = (hp * MULTIPLIER_TIER[4]).toInt()
        } else if (mAffinity == Enum.Affinity.BRAINIAC) {
            hpByAffinity = (hp * MULTIPLIER_TIER[5]).toInt()
        }
        return hpByAffinity
    }

    /**
     * Method is used to calculate Strength base stat using cardType and affinity to generate
     * a randomized value
     *
     *
     *
     * (C) str base: [40-45]
     * (R) str base: [45-55]
     * (E) str base: [50-60]
     * (L) str base: [65-80]
     * (M) str base: [90-110]
     * (SL) str base: [60-75]
     *
     *
     * @return Strength base value
     */
    private fun randStrStat(): Int {
        val rand = Random()
        var str = 0

        if (mCardType == Enum.CardType.COMMON) {
            str = rand.nextInt(6) + 40
        } else if (mCardType == Enum.CardType.RARE) {
            str = rand.nextInt(11) + 45
        } else if (mCardType == Enum.CardType.EPIC) {
            str = rand.nextInt(11) + 50
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            str = rand.nextInt(16) + 65
        } else if (mCardType == Enum.CardType.MYTHIC) {
            str = rand.nextInt(21) + 90
        } else if (mCardType == Enum.CardType.SQUAD_LEADER) {
            str = rand.nextInt(16) + 60
        }
        return str + randStrByAffinity(str)
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Strength value will be, [baseStr = randomized str + value based on affinity]
     *
     *
     *
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     *
     *
     * @param str Randomized value to be added to create the Base Strength stat
     * @return Value based on affinity
     */
    private fun randStrByAffinity(str: Int): Int {
        var strByAffinity = 0

        if (mAffinity == Enum.Affinity.ROBOTIC) {
            strByAffinity = (str * MULTIPLIER_TIER[0]).toInt()
        } else if (mAffinity == Enum.Affinity.PHYSICAL) {
            strByAffinity = (str * MULTIPLIER_TIER[1]).toInt()
        } else if (mAffinity == Enum.Affinity.BEAST) {
            strByAffinity = (str * MULTIPLIER_TIER[2]).toInt()
        } else if (mAffinity == Enum.Affinity.ELEMENTAL) {
            strByAffinity = (str * MULTIPLIER_TIER[3]).toInt()
        } else if (mAffinity == Enum.Affinity.PSYCHIC) {
            strByAffinity = (str * MULTIPLIER_TIER[5]).toInt()
        } else if (mAffinity == Enum.Affinity.BRAINIAC) {
            strByAffinity = (str * MULTIPLIER_TIER[4]).toInt()
        }
        return strByAffinity
    }

    /**
     * Method is used to calculate Speed base stat using cardType and affinity to generate
     * a randomized value
     *
     *
     *
     * (C) spd base: [35-40]
     * (R) spd base: [37-45]
     * (E) spd base: [42-50]
     * (L) spd base: [50-60]
     * (M) spd base: [60-75]
     * (SL) spd base: [40-48]
     *
     *
     * @return Speed base value
     */
    private fun randSpdStat(): Int {
        val rand = Random()
        var spd = 0

        if (mCardType == Enum.CardType.COMMON) {
            spd = rand.nextInt(6) + 35
        } else if (mCardType == Enum.CardType.RARE) {
            spd = rand.nextInt(9) + 37
        } else if (mCardType == Enum.CardType.EPIC) {
            spd = rand.nextInt(9) + 42
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            spd = rand.nextInt(11) + 50
        } else if (mCardType == Enum.CardType.MYTHIC) {
            spd = rand.nextInt(16) + 60
        } else if (mCardType == Enum.CardType.SQUAD_LEADER) {
            spd = rand.nextInt(9) + 40
        }
        return spd + randSpdByAffinity(spd)
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Speed value will be, [baseSpd = randomized spd + value based on affinity]
     *
     *
     *
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     *
     *
     * @param spd Randomized value to be added to create the Base Speed stat
     * @return Value based on affinity
     */
    private fun randSpdByAffinity(spd: Int): Int {
        var spdByAffinity = 0

        if (mAffinity == Enum.Affinity.ROBOTIC) {
            spdByAffinity = (spd * MULTIPLIER_TIER[5]).toInt()
        } else if (mAffinity == Enum.Affinity.PHYSICAL) {
            spdByAffinity = (spd * MULTIPLIER_TIER[4]).toInt()
        } else if (mAffinity == Enum.Affinity.BEAST) {
            spdByAffinity = (spd * MULTIPLIER_TIER[0]).toInt()
        } else if (mAffinity == Enum.Affinity.ELEMENTAL) {
            spdByAffinity = (spd * MULTIPLIER_TIER[4]).toInt()
        } else if (mAffinity == Enum.Affinity.PSYCHIC) {
            spdByAffinity = (spd * MULTIPLIER_TIER[1]).toInt()
        } else if (mAffinity == Enum.Affinity.BRAINIAC) {
            spdByAffinity = (spd * MULTIPLIER_TIER[3]).toInt()
        }
        return spdByAffinity
    }

    /**
     * Method is used to calculate Wisdom base stat using cardType and affinity to generate
     * a randomized value
     *
     *
     *
     * (C) wis base: [40-45]
     * (R) wis base: [45-55]
     * (E) wis base: [50-60]
     * (L) wis base: [65-80]
     * (M) wis base: [90-110]
     * (SL) wis base: [60-75]
     *
     *
     * @return Wisdom base value
     */
    private fun randWisStat(): Int {
        val rand = Random()
        var wis = 0

        if (mCardType == Enum.CardType.COMMON) {
            wis = rand.nextInt(6) + 40
        } else if (mCardType == Enum.CardType.RARE) {
            wis = rand.nextInt(11) + 45
        } else if (mCardType == Enum.CardType.EPIC) {
            wis = rand.nextInt(11) + 50
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            wis = rand.nextInt(16) + 65
        } else if (mCardType == Enum.CardType.MYTHIC) {
            wis = rand.nextInt(21) + 90
        } else if (mCardType == Enum.CardType.SQUAD_LEADER) {
            wis = rand.nextInt(16) + 60
        }
        return wis + randWisByAffinity(wis)
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Wisdom value will be, [baseWis = randomized wis + value based on affinity]
     *
     *
     *
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     *
     *
     * @param wis Randomized value to be added to create the Base Wisdom stat
     * @return Value based on affinity
     */
    private fun randWisByAffinity(wis: Int): Int {
        var wisByAffinity = 0

        if (mAffinity == Enum.Affinity.ROBOTIC) {
            wisByAffinity = (wis * MULTIPLIER_TIER[4]).toInt()
        } else if (mAffinity == Enum.Affinity.PHYSICAL) {
            wisByAffinity = (wis * MULTIPLIER_TIER[5]).toInt()
        } else if (mAffinity == Enum.Affinity.BEAST) {
            wisByAffinity = (wis * MULTIPLIER_TIER[5]).toInt()
        } else if (mAffinity == Enum.Affinity.ELEMENTAL) {
            wisByAffinity = (wis * MULTIPLIER_TIER[0]).toInt()
        } else if (mAffinity == Enum.Affinity.PSYCHIC) {
            wisByAffinity = (wis * MULTIPLIER_TIER[0]).toInt()
        } else if (mAffinity == Enum.Affinity.BRAINIAC) {
            wisByAffinity = (wis * MULTIPLIER_TIER[0]).toInt()
        }
        return wisByAffinity
    }

    /**
     * Method is used to calculate Physical Defense base stat using cardType and affinity to generate
     * a randomized value
     *
     *
     *
     * (C) phyDef base: [35-40]
     * (R) phyDef base: [37-45]
     * (E) phyDef base: [42-50]
     * (L) phyDef base: [50-60]
     * (M) phyDef base: [60-75]
     * (SL) phyDef base: [40-48]
     *
     *
     * @return Physical Defense base value
     */
    private fun randPhyDefStat(): Int {
        val rand = Random()
        var phyDef = 0

        if (mCardType == Enum.CardType.COMMON) {
            phyDef = rand.nextInt(6) + 35
        } else if (mCardType == Enum.CardType.RARE) {
            phyDef = rand.nextInt(9) + 37
        } else if (mCardType == Enum.CardType.EPIC) {
            phyDef = rand.nextInt(9) + 42
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            phyDef = rand.nextInt(11) + 50
        } else if (mCardType == Enum.CardType.MYTHIC) {
            phyDef = rand.nextInt(16) + 60
        } else if (mCardType == Enum.CardType.SQUAD_LEADER) {
            phyDef = rand.nextInt(9) + 40
        }
        return phyDef + randPhyDefByAffinity(phyDef)
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Physical Defense value will be, [basePhyDef = randomized phyDef + value based on affinity]
     *
     *
     *
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     *
     *
     * @param phyDef Randomized value to be added to create the Base Physical Defense stat
     * @return Value based on affinity
     */
    private fun randPhyDefByAffinity(phyDef: Int): Int {
        var phyDefByAffinity = 0

        if (mAffinity == Enum.Affinity.ROBOTIC) {
            phyDefByAffinity = (phyDef * MULTIPLIER_TIER[3]).toInt()
        } else if (mAffinity == Enum.Affinity.PHYSICAL) {
            phyDefByAffinity = (phyDef * MULTIPLIER_TIER[2]).toInt()
        } else if (mAffinity == Enum.Affinity.BEAST) {
            phyDefByAffinity = (phyDef * MULTIPLIER_TIER[4]).toInt()
        } else if (mAffinity == Enum.Affinity.ELEMENTAL) {
            phyDefByAffinity = (phyDef * MULTIPLIER_TIER[2]).toInt()
        } else if (mAffinity == Enum.Affinity.PSYCHIC) {
            phyDefByAffinity = (phyDef * MULTIPLIER_TIER[2]).toInt()
        } else if (mAffinity == Enum.Affinity.BRAINIAC) {
            phyDefByAffinity = (phyDef * MULTIPLIER_TIER[1]).toInt()
        }
        return phyDefByAffinity
    }

    /**
     * Method is used to calculate Magical Defense base stat using cardType and affinity to generate
     * a randomized value
     *
     *
     *
     * (C) magDef base: [35-40]
     * (R) magDef base: [37-45]
     * (E) magDef base: [42-50]
     * (L) magDef base: [50-60]
     * (M) magDef base: [60-75]
     * (SL) magDef base: [40-48]
     *
     *
     * @return Magical Defense base value
     */
    private fun randMagDefStat(): Int {
        val rand = Random()
        var magDef = 0

        if (mCardType == Enum.CardType.COMMON) {
            magDef = rand.nextInt(6) + 35
        } else if (mCardType == Enum.CardType.RARE) {
            magDef = rand.nextInt(9) + 37
        } else if (mCardType == Enum.CardType.EPIC) {
            magDef = rand.nextInt(9) + 42
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            magDef = rand.nextInt(11) + 50
        } else if (mCardType == Enum.CardType.MYTHIC) {
            magDef = rand.nextInt(16) + 60
        } else if (mCardType == Enum.CardType.SQUAD_LEADER) {
            magDef = rand.nextInt(9) + 40
        }
        return magDef + randMagDefByAffinity(magDef)
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Magical Defense value will be, [baseMagDef = randomized magDef + value based on affinity]
     *
     *
     *
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     *
     *
     * @param magDef Randomized value to be added to create the Base Magical Defense stat
     * @return Value based on affinity
     */
    private fun randMagDefByAffinity(magDef: Int): Int {
        var magDefByAffinity = 0

        if (mAffinity == Enum.Affinity.ROBOTIC) {
            magDefByAffinity = (magDef * MULTIPLIER_TIER[1]).toInt()
        } else if (mAffinity == Enum.Affinity.PHYSICAL) {
            magDefByAffinity = (magDef * MULTIPLIER_TIER[3]).toInt()
        } else if (mAffinity == Enum.Affinity.BEAST) {
            magDefByAffinity = (magDef * MULTIPLIER_TIER[1]).toInt()
        } else if (mAffinity == Enum.Affinity.ELEMENTAL) {
            magDefByAffinity = (magDef * MULTIPLIER_TIER[5]).toInt()
        } else if (mAffinity == Enum.Affinity.PSYCHIC) {
            magDefByAffinity = (magDef * MULTIPLIER_TIER[3]).toInt()
        } else if (mAffinity == Enum.Affinity.BRAINIAC) {
            magDefByAffinity = (magDef * MULTIPLIER_TIER[2]).toInt()
        }
        return magDefByAffinity
    }

    /**
     * Method is used to calculate chance to perform critical attack. Note that crit percentage
     * is not affinity linked
     *
     *
     *
     * (C) critPerc base: [2]
     * (R) critPerc base: [4]
     * (E) critPerc base: [6]
     * (L) critPerc base: [8]
     * (M) critPerc base: [10]
     * (SL) critPerc base: [5]
     *
     *
     * @return Percentage represented value to perform critical attack
     */
    private fun randCritPercStat(): Int {
        if (mCardType == Enum.CardType.COMMON) {
            return 2
        } else if (mCardType == Enum.CardType.RARE) {
            return 4
        } else if (mCardType == Enum.CardType.EPIC) {
            return 6
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            return 8
        } else if (mCardType == Enum.CardType.MYTHIC) {
            return 10
        }
        // SQUAD_LEADER return value
        return 5
    }

    /**
     * Retrieve base max level
     *
     * @param currAsc The maximum level of a Guardian increases based on the Ascension (Asc) level
     * @return Maximum level based on cardType
     */
    private fun getMaxLv(currAsc: Int): Int {
        var mMaxLv = 0
        val ascIncValue = currAsc * 2

        if (mCardType == Enum.CardType.COMMON) {
            mMaxLv = 20 + ascIncValue
        } else if (mCardType == Enum.CardType.RARE) {
            mMaxLv = 24 + ascIncValue
        } else if (mCardType == Enum.CardType.EPIC) {
            mMaxLv = 28 + ascIncValue
        } else if (mCardType == Enum.CardType.LEGENDARY) {
            mMaxLv = 32 + ascIncValue
        } else if (mCardType == Enum.CardType.MYTHIC) {
            mMaxLv = 36 + ascIncValue
        } else if (mCardType == Enum.CardType.SQUAD_LEADER) {
            mMaxLv = 34
        }
        return mMaxLv
    }

    companion object {

        private val MULTIPLIER_TIER = doubleArrayOf(1.0, 0.9, 0.75, 0.55, 0.4, 0.25) // arbitrary value
    }

}
