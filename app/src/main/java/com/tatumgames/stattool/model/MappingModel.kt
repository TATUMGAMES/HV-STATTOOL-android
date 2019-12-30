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

import android.content.Context

import com.tatumgames.stattool.R
import com.tatumgames.stattool.enums.Enum
import com.tatumgames.stattool.requests.model.MissionDetails

object MappingModel {

    /**
     * Helper method to convert String affinity to Affinity object
     *
     * @param affinity Each Guardian has strengths and weaknesses based on their affinity e.g.
     * Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @return Object containing Affinity information
     */
    fun getAffinityByAffinity(affinity: String): Enum.Affinity {
        if (affinity.equals(Enum.Affinity.ROBOTIC.toString(), ignoreCase = true)) {
            return Enum.Affinity.ROBOTIC
        } else if (affinity.equals(Enum.Affinity.PHYSICAL.toString(), ignoreCase = true)) {
            return Enum.Affinity.PHYSICAL
        } else if (affinity.equals(Enum.Affinity.BEAST.toString(), ignoreCase = true)) {
            return Enum.Affinity.BEAST
        } else if (affinity.equals(Enum.Affinity.ELEMENTAL.toString(), ignoreCase = true)) {
            return Enum.Affinity.ELEMENTAL
        } else if (affinity.equals(Enum.Affinity.PSYCHIC.toString(), ignoreCase = true)) {
            return Enum.Affinity.PSYCHIC
        }
        return Enum.Affinity.BRAINIAC
    }

    /**
     * Helper method to convert String cardType to CardType object
     *
     * @param cardType This represents the strength and rarity of the card or Guardian
     * @return Object containing cardType information
     */
    fun getCardTypeByCardType(cardType: String): Enum.CardType {
        if (cardType.equals(Enum.CardType.SQUAD_LEADER.toString(), ignoreCase = true)) {
            return Enum.CardType.SQUAD_LEADER
        } else if (cardType.equals(Enum.CardType.COMMON.toString(), ignoreCase = true)) {
            return Enum.CardType.COMMON
        } else if (cardType.equals(Enum.CardType.RARE.toString(), ignoreCase = true)) {
            return Enum.CardType.RARE
        } else if (cardType.equals(Enum.CardType.EPIC.toString(), ignoreCase = true)) {
            return Enum.CardType.EPIC
        } else if (cardType.equals(Enum.CardType.LEGENDARY.toString(), ignoreCase = true)) {
            return Enum.CardType.LEGENDARY
        }
        return Enum.CardType.MYTHIC
    }

    /**
     * Helper method to convert id affinity to Affinity object
     *
     * @param affinityId Each Guardian has strengths and weaknesses based on their affinity e.g.
     * Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @return Object containing Affinity information
     */
    fun getAffinityByAffinityId(affinityId: Int): Enum.Affinity {
        if (affinityId == 1) {
            return Enum.Affinity.ROBOTIC
        } else if (affinityId == 2) {
            return Enum.Affinity.PHYSICAL
        } else if (affinityId == 3) {
            return Enum.Affinity.BEAST
        } else if (affinityId == 4) {
            return Enum.Affinity.ELEMENTAL
        } else if (affinityId == 5) {
            return Enum.Affinity.PSYCHIC
        }
        return Enum.Affinity.BRAINIAC
    }

    /**
     * Helper method to convert id cardType to CardType object
     *
     * @param cardTypeId This represents the strength and rarity of the card or Guardian
     * @return Object containing cardType information
     */
    fun getCardTypeByCardTypeId(cardTypeId: Int): Enum.CardType {
        if (cardTypeId == 1) {
            return Enum.CardType.SQUAD_LEADER
        } else if (cardTypeId == 2) {
            return Enum.CardType.COMMON
        } else if (cardTypeId == 3) {
            return Enum.CardType.RARE
        } else if (cardTypeId == 4) {
            return Enum.CardType.EPIC
        } else if (cardTypeId == 5) {
            return Enum.CardType.LEGENDARY
        }
        return Enum.CardType.MYTHIC
    }

    /**
     * Helper method to assign appropriate affinity to Guardians based on their id
     *
     * This method is used for updating base stats
     *
     * @param guardianId The id representing Guardians
     * @return Affinity information. Will return name, e.g. Robotic, Elemental, Physical,
     * Psychic, Brainiac, Beast
     */
    fun getAffinityNameByGuardianId(guardianId: String): String {
        if (guardianId.equals("1", ignoreCase = true)) {
            // Frost Fire
            return Enum.Affinity.ROBOTIC.toString()
        } else if (guardianId.equals("2", ignoreCase = true)) {
            // Scourge
            return Enum.Affinity.ELEMENTAL.toString()
        } else if (guardianId.equals("3", ignoreCase = true)) {
            // Silent Storm
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("4", ignoreCase = true)) {
            // Aziza
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("5", ignoreCase = true)) {
            // Binary
            return Enum.Affinity.BRAINIAC.toString()
        } else if (guardianId.equals("6", ignoreCase = true)) {
            // Cougar
            return Enum.Affinity.BEAST.toString()
        } else if (guardianId.equals("7", ignoreCase = true)) {
            // Brickjaw
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("8", ignoreCase = true)) {
            // Hair Witch
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("9", ignoreCase = true)) {
            // Genesis
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("10", ignoreCase = true)) {
            // S-7
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("11", ignoreCase = true)) {
            // Pain
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("12", ignoreCase = true)) {
            // Massacre
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("13", ignoreCase = true)) {
            // Nervo
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("14", ignoreCase = true)) {
            // Dark Priestess
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("15", ignoreCase = true)) {
            // Tormented King
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("16", ignoreCase = true)) {
            // Exiled Priestess
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("17", ignoreCase = true)) {
            // Psychick
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("18", ignoreCase = true)) {
            // Amusing Sinner
            return Enum.Affinity.BRAINIAC.toString()
        } else if (guardianId.equals("19", ignoreCase = true)) {
            // Brainz
            return Enum.Affinity.BRAINIAC.toString()
        } else if (guardianId.equals("20", ignoreCase = true)) {
            // Slade
            return Enum.Affinity.BEAST.toString()
        } else if (guardianId.equals("21", ignoreCase = true)) {
            // General of Grins
            return Enum.Affinity.BEAST.toString()
        } else if (guardianId.equals("22", ignoreCase = true)) {
            // Beast Girl
            return Enum.Affinity.BEAST.toString()
        } else if (guardianId.equals("23", ignoreCase = true)) {
            // Burn Streak
            return Enum.Affinity.ELEMENTAL.toString()
        } else if (guardianId.equals("24", ignoreCase = true)) {
            // Dragon Flame
            return Enum.Affinity.ELEMENTAL.toString()
        } else if (guardianId.equals("25", ignoreCase = true)) {
            // Madam Frost
            return Enum.Affinity.ELEMENTAL.toString()
        } else if (guardianId.equals("26", ignoreCase = true)) {
            // Hot Head
            return Enum.Affinity.ELEMENTAL.toString()
        } else if (guardianId.equals("27", ignoreCase = true)) {
            // Protector
            return Enum.Affinity.ROBOTIC.toString()
        } else if (guardianId.equals("30", ignoreCase = true)) {
            // Klown Krew (TM1)
            return Enum.Affinity.BRAINIAC.toString()
        } else if (guardianId.equals("31", ignoreCase = true)) {
            // Klown Krew (TM2)
            return Enum.Affinity.BRAINIAC.toString()
        } else if (guardianId.equals("32", ignoreCase = true)) {
            // Forest Protector (TM1)
            return Enum.Affinity.BEAST.toString()
        } else if (guardianId.equals("33", ignoreCase = true)) {
            // Forst Protector (TM2)
            return Enum.Affinity.BEAST.toString()
        } else if (guardianId.equals("34", ignoreCase = true)) {
            // Hired Goon (TM1)
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("35", ignoreCase = true)) {
            // Hired Good (TM2)
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("36", ignoreCase = true)) {
            // Frost Minion (TM1)
            return Enum.Affinity.ELEMENTAL.toString()
        } else if (guardianId.equals("37", ignoreCase = true)) {
            // Frost Minion (TM2)
            return Enum.Affinity.ELEMENTAL.toString()
        } else if (guardianId.equals("38", ignoreCase = true)) {
            // Mutant (TM1)
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("39", ignoreCase = true)) {
            // Mutant (TM2)
            return Enum.Affinity.PSYCHIC.toString()
        } else if (guardianId.equals("40", ignoreCase = true)) {
            // Mutant (TM3)
            return Enum.Affinity.PHYSICAL.toString()
        } else if (guardianId.equals("41", ignoreCase = true)) {
            // Mutant (TM4)
            return Enum.Affinity.PHYSICAL.toString()
        }
        // default
        return Enum.Affinity.ROBOTIC.toString()
    }

    /**
     * Helper method to assign appropriate affinity to Guardians based on their id
     *
     * This method is used for updating base stats
     *
     * @param guardianId The id representing Guardians
     * @return Affinity information. Will return id, e.g. 1=Robotic, 2=Elemental, 3=Physical,
     * 4=Psychic, 5=Brainiac, 6=Beast
     */
    fun getAffinityIdByGuardianId(guardianId: String): String {
        if (guardianId.equals("1", ignoreCase = true)) {
            // Frost Fire
            return Enum.Affinity.ROBOTIC.toId()
        } else if (guardianId.equals("2", ignoreCase = true)) {
            // Scourge
            return Enum.Affinity.ELEMENTAL.toId()
        } else if (guardianId.equals("3", ignoreCase = true)) {
            // Silent Storm
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("4", ignoreCase = true)) {
            // Aziza
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("5", ignoreCase = true)) {
            // Binary
            return Enum.Affinity.BRAINIAC.toId()
        } else if (guardianId.equals("6", ignoreCase = true)) {
            // Cougar
            return Enum.Affinity.BEAST.toId()
        } else if (guardianId.equals("7", ignoreCase = true)) {
            // Brickjaw
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("8", ignoreCase = true)) {
            // Hair Witch
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("9", ignoreCase = true)) {
            // Genesis
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("10", ignoreCase = true)) {
            // S-7
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("11", ignoreCase = true)) {
            // Pain
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("12", ignoreCase = true)) {
            // Massacre
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("13", ignoreCase = true)) {
            // Nervo
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("14", ignoreCase = true)) {
            // Dark Priestess
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("15", ignoreCase = true)) {
            // Tormented King
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("16", ignoreCase = true)) {
            // Exiled Priestess
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("17", ignoreCase = true)) {
            // Psychick
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("18", ignoreCase = true)) {
            // Amusing Sinner
            return Enum.Affinity.BRAINIAC.toId()
        } else if (guardianId.equals("19", ignoreCase = true)) {
            // Brainz
            return Enum.Affinity.BRAINIAC.toId()
        } else if (guardianId.equals("20", ignoreCase = true)) {
            // Slade
            return Enum.Affinity.BEAST.toId()
        } else if (guardianId.equals("21", ignoreCase = true)) {
            // General of Grins
            return Enum.Affinity.BEAST.toId()
        } else if (guardianId.equals("22", ignoreCase = true)) {
            // Beast Girl
            return Enum.Affinity.BEAST.toId()
        } else if (guardianId.equals("23", ignoreCase = true)) {
            // Burn Streak
            return Enum.Affinity.ELEMENTAL.toId()
        } else if (guardianId.equals("24", ignoreCase = true)) {
            // Dragon Flame
            return Enum.Affinity.ELEMENTAL.toId()
        } else if (guardianId.equals("25", ignoreCase = true)) {
            // Madam Frost
            return Enum.Affinity.ELEMENTAL.toId()
        } else if (guardianId.equals("26", ignoreCase = true)) {
            // Hot Head
            return Enum.Affinity.ELEMENTAL.toId()
        } else if (guardianId.equals("27", ignoreCase = true)) {
            // Protector
            return Enum.Affinity.ROBOTIC.toId()
        } else if (guardianId.equals("30", ignoreCase = true)) {
            // Klown Krew (TM1)
            return Enum.Affinity.BRAINIAC.toId()
        } else if (guardianId.equals("31", ignoreCase = true)) {
            // Klown Krew (TM2)
            return Enum.Affinity.BRAINIAC.toId()
        } else if (guardianId.equals("32", ignoreCase = true)) {
            // Forest Protector (TM1)
            return Enum.Affinity.BEAST.toId()
        } else if (guardianId.equals("33", ignoreCase = true)) {
            // Forst Protector (TM2)
            return Enum.Affinity.BEAST.toId()
        } else if (guardianId.equals("34", ignoreCase = true)) {
            // Hired Goon (TM1)
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("35", ignoreCase = true)) {
            // Hired Good (TM2)
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("36", ignoreCase = true)) {
            // Frost Minion (TM1)
            return Enum.Affinity.ELEMENTAL.toId()
        } else if (guardianId.equals("37", ignoreCase = true)) {
            // Frost Minion (TM2)
            return Enum.Affinity.ELEMENTAL.toId()
        } else if (guardianId.equals("38", ignoreCase = true)) {
            // Mutant (TM1)
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("39", ignoreCase = true)) {
            // Mutant (TM2)
            return Enum.Affinity.PSYCHIC.toId()
        } else if (guardianId.equals("40", ignoreCase = true)) {
            // Mutant (TM3)
            return Enum.Affinity.PHYSICAL.toId()
        } else if (guardianId.equals("41", ignoreCase = true)) {
            // Mutant (TM4)
            return Enum.Affinity.PHYSICAL.toId()
        }
        // default
        return Enum.Affinity.ROBOTIC.toId()
    }

    /**
     * Helper method to assign appropriate cardType to Guardians based on their id
     *
     * Base stats will always either be Squad Leader or Common
     *
     * @param guardianId The id representing Guardians
     * @return CardType information. Will return name, e.g. Squad Leader, Common, Rare,
     * Epic, Legendary, Mythic
     */
    fun getCardTypeNameByGuardianId(guardianId: String): String {
        // 1 - Squad Leader
        // 2 - Common
        // 3 - Rare
        // 4 - Epic
        // 5 - Legendary
        // 6 - Mythic

        return if (Integer.parseInt(guardianId) < 7) {
            Enum.CardType.SQUAD_LEADER.toString()
        } else Enum.CardType.COMMON.toString()
    }

    /**
     * Helper method to assign appropriate cardType to Guardians based on their id
     *
     * Base stats will always either be Squad Leader or Common
     *
     * @param guardianId The id representing Guardians
     * @return CardType information. Will return id, e.g. 1=Squad Leader, 2=Common, 3=Rare,
     * 4=Epic, 5=Legendary, 6=Mythic
     */
    fun getCardTypeIdByGuardianId(guardianId: String): String {
        // 1 - Squad Leader
        // 2 - Common
        // 3 - Rare
        // 4 - Epic
        // 5 - Legendary
        // 6 - Mythic

        return if (Integer.parseInt(guardianId) < 7) {
            Enum.CardType.SQUAD_LEADER.toId()
        } else Enum.CardType.COMMON.toId()
    }

    /**
     * Helper method to assign appropriate description to Guardians based on their id
     *
     * This method is used for updating base stats
     *
     * @param context    Interface to global information about an application environment
     * @param guardianId The id representing Guardians
     * @return Description information
     */
    fun getDescriptionByGuardianId(context: Context, guardianId: String): String {
        if (guardianId.equals("1", ignoreCase = true)) {
            // Frost Fire
            return cleanString(context.resources.getString(R.string.frost_fire_desc))
        } else if (guardianId.equals("2", ignoreCase = true)) {
            // Scourge
            return cleanString(context.resources.getString(R.string.scourge_desc))
        } else if (guardianId.equals("3", ignoreCase = true)) {
            // Silent Storm
            return cleanString(context.resources.getString(R.string.silent_storm))
        } else if (guardianId.equals("4", ignoreCase = true)) {
            // Aziza
            return cleanString(context.resources.getString(R.string.aziza_desc))
        } else if (guardianId.equals("5", ignoreCase = true)) {
            // Binary
            return cleanString(context.resources.getString(R.string.binary_desc))
        } else if (guardianId.equals("6", ignoreCase = true)) {
            // Cougar
            return cleanString(context.resources.getString(R.string.cougar_desc))
        } else if (guardianId.equals("7", ignoreCase = true)) {
            // Brickjaw
            return cleanString(context.resources.getString(R.string.brick_jaw_desc))
        } else if (guardianId.equals("8", ignoreCase = true)) {
            // Hair Witch
            return cleanString(context.resources.getString(R.string.hair_witch_desc))
        } else if (guardianId.equals("9", ignoreCase = true)) {
            // Genesis
            return cleanString(context.resources.getString(R.string.genesis_desc))
        } else if (guardianId.equals("10", ignoreCase = true)) {
            // S-7
            return cleanString(context.resources.getString(R.string.s7_desc))
        } else if (guardianId.equals("11", ignoreCase = true)) {
            // Pain
            return cleanString(context.resources.getString(R.string.pain_desc))
        } else if (guardianId.equals("12", ignoreCase = true)) {
            // Massacre
            return cleanString(context.resources.getString(R.string.massacre_desc))
        } else if (guardianId.equals("13", ignoreCase = true)) {
            // Nervo
            return cleanString(context.resources.getString(R.string.nervo_desc))
        } else if (guardianId.equals("14", ignoreCase = true)) {
            // Dark Priestess
            return cleanString(context.resources.getString(R.string.dark_preistess_desc))
        } else if (guardianId.equals("15", ignoreCase = true)) {
            // Tormented King
            return cleanString(context.resources.getString(R.string.tormented_king_desc))
        } else if (guardianId.equals("16", ignoreCase = true)) {
            // Exiled Priestess
            return cleanString(context.resources.getString(R.string.exiled_priestess_desc))
        } else if (guardianId.equals("17", ignoreCase = true)) {
            // Psychick
            return cleanString(context.resources.getString(R.string.psychick_desc))
        } else if (guardianId.equals("18", ignoreCase = true)) {
            // Amusing Sinner
            return cleanString(context.resources.getString(R.string.amusing_sinner_desc))
        } else if (guardianId.equals("19", ignoreCase = true)) {
            // Brainz
            return cleanString(context.resources.getString(R.string.brainz_desc))
        } else if (guardianId.equals("20", ignoreCase = true)) {
            // Slade
            return cleanString(context.resources.getString(R.string.slade_desc))
        } else if (guardianId.equals("21", ignoreCase = true)) {
            // General of Grins
            return cleanString(context.resources.getString(R.string.general_of_grins_desc))
        } else if (guardianId.equals("22", ignoreCase = true)) {
            // Beast Girl
            return cleanString(context.resources.getString(R.string.beast_girl))
        } else if (guardianId.equals("23", ignoreCase = true)) {
            // Burn Streak
            return cleanString(context.resources.getString(R.string.burn_streak_desc))
        } else if (guardianId.equals("24", ignoreCase = true)) {
            // Dragon Flame
            return cleanString(context.resources.getString(R.string.dragon_flame_desc))
        } else if (guardianId.equals("25", ignoreCase = true)) {
            // Madam Frost
            return cleanString(context.resources.getString(R.string.madam_frost_desc))
        } else if (guardianId.equals("26", ignoreCase = true)) {
            // Hot Head
            return cleanString(context.resources.getString(R.string.hot_head_desc))
        } else if (guardianId.equals("27", ignoreCase = true)) {
            // Protector
            return cleanString(context.resources.getString(R.string.protector_desc))
        } else if (guardianId.equals("30", ignoreCase = true)) {
            // Klown Krew (TM1)
            return ""
        } else if (guardianId.equals("31", ignoreCase = true)) {
            // Klown Krew (TM2)
            return ""
        } else if (guardianId.equals("32", ignoreCase = true)) {
            // Forest Protector (TM1)
            return ""
        } else if (guardianId.equals("33", ignoreCase = true)) {
            // Forst Protector (TM2)
            return ""
        } else if (guardianId.equals("34", ignoreCase = true)) {
            // Hired Goon (TM1)
            return ""
        } else if (guardianId.equals("35", ignoreCase = true)) {
            // Hired Good (TM2)
            return ""
        } else if (guardianId.equals("36", ignoreCase = true)) {
            // Frost Minion (TM1)
            return ""
        } else if (guardianId.equals("37", ignoreCase = true)) {
            // Frost Minion (TM2)
            return ""
        } else if (guardianId.equals("38", ignoreCase = true)) {
            // Mutant (TM1)
            return ""
        } else if (guardianId.equals("39", ignoreCase = true)) {
            // Mutant (TM2)
            return ""
        } else if (guardianId.equals("40", ignoreCase = true)) {
            // Mutant (TM3)
            return ""
        } else if (guardianId.equals("41", ignoreCase = true)) {
            // Mutant (TM4)
            return ""
        }
        // default
        return ""
    }

    /**
     * Method is used to prepare String value for RESTful. It will double escape apostrophe
     * @param value The String to clean
     * @return Cleaned String value ready for RESTful
     */
    private fun cleanString(value: String): String {
        return value.replace("'".toRegex(), "\\\\'")
    }

    /**
     * Method is used to retrieve mission details
     * @param id The id representing the mission area
     * @param difficultyLevelId The id representing the mission area difficulty level e.g. Alpha, Beta, Omega
     * @return
     */
    fun getMissionDetailsById(id: String, difficultyLevelId: String): MissionDetails {
        val missionDetails = MissionDetails()
        if (difficultyLevelId.equals("1", ignoreCase = true)) {
            if (id.equals("1", ignoreCase = true)) {
                missionDetails.name = ""
                missionDetails.description = ""
            } else if (id.equals("2", ignoreCase = true)) {

            } else if (id.equals("3", ignoreCase = true)) {

            } else if (id.equals("4", ignoreCase = true)) {

            } else if (id.equals("5", ignoreCase = true)) {

            } else if (id.equals("6", ignoreCase = true)) {

            } else if (id.equals("7", ignoreCase = true)) {

            } else if (id.equals("8", ignoreCase = true)) {

            } else if (id.equals("9", ignoreCase = true)) {

            }
        }
        return missionDetails
    }

}
