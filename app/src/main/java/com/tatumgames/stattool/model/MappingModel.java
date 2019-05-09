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

package com.tatumgames.stattool.model;

import android.content.Context;

import com.tatumgames.stattool.R;
import com.tatumgames.stattool.enums.Enum;
import com.tatumgames.stattool.requests.model.MissionDetails;

public class MappingModel {

    /**
     * Helper method to convert String affinity to Affinity object
     *
     * @param affinity Each Guardian has strengths and weaknesses based on their affinity e.g.
     *                 Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @return Object containing Affinity information
     */
    public static Enum.Affinity getAffinityByAffinity(String affinity) {
        if (affinity.equalsIgnoreCase(Enum.Affinity.ROBOTIC.toString())) {
            return Enum.Affinity.ROBOTIC;
        } else if (affinity.equalsIgnoreCase(Enum.Affinity.PHYSICAL.toString())) {
            return Enum.Affinity.PHYSICAL;
        } else if (affinity.equalsIgnoreCase(Enum.Affinity.BEAST.toString())) {
            return Enum.Affinity.BEAST;
        } else if (affinity.equalsIgnoreCase(Enum.Affinity.ELEMENTAL.toString())) {
            return Enum.Affinity.ELEMENTAL;
        } else if (affinity.equalsIgnoreCase(Enum.Affinity.PSYCHIC.toString())) {
            return Enum.Affinity.PSYCHIC;
        }
        return Enum.Affinity.BRAINIAC;
    }

    /**
     * Helper method to convert String cardType to CardType object
     *
     * @param cardType This represents the strength and rarity of the card or Guardian
     * @return Object containing cardType information
     */
    public static Enum.CardType getCardTypeByCardType(String cardType) {
        if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.SQUAD_LEADER))) {
            return Enum.CardType.SQUAD_LEADER;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.COMMON))) {
            return Enum.CardType.COMMON;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.RARE))) {
            return Enum.CardType.RARE;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.EPIC))) {
            return Enum.CardType.EPIC;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.LEGENDARY))) {
            return Enum.CardType.LEGENDARY;
        }
        return Enum.CardType.MYTHIC;
    }

    /**
     * Helper method to convert id affinity to Affinity object
     *
     * @param affinityId Each Guardian has strengths and weaknesses based on their affinity e.g.
     *                   Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @return Object containing Affinity information
     */
    public static Enum.Affinity getAffinityByAffinityId(int affinityId) {
        if (affinityId == 1) {
            return Enum.Affinity.ROBOTIC;
        } else if (affinityId == 2) {
            return Enum.Affinity.PHYSICAL;
        } else if (affinityId == 3) {
            return Enum.Affinity.BEAST;
        } else if (affinityId == 4) {
            return Enum.Affinity.ELEMENTAL;
        } else if (affinityId == 5) {
            return Enum.Affinity.PSYCHIC;
        }
        return Enum.Affinity.BRAINIAC;
    }

    /**
     * Helper method to convert id cardType to CardType object
     *
     * @param cardTypeId This represents the strength and rarity of the card or Guardian
     * @return Object containing cardType information
     */
    public static Enum.CardType getCardTypeByCardTypeId(int cardTypeId) {
        if (cardTypeId == 1) {
            return Enum.CardType.SQUAD_LEADER;
        } else if (cardTypeId == 2) {
            return Enum.CardType.COMMON;
        } else if (cardTypeId == 3) {
            return Enum.CardType.RARE;
        } else if (cardTypeId == 4) {
            return Enum.CardType.EPIC;
        } else if (cardTypeId == 5) {
            return Enum.CardType.LEGENDARY;
        }
        return Enum.CardType.MYTHIC;
    }

    /**
     * Helper method to assign appropriate affinity to Guardians based on their id
     * <p>This method is used for updating base stats</p>
     *
     * @param guardianId The id representing Guardians
     * @return Affinity information. Will return name, e.g. Robotic, Elemental, Physical,
     * Psychic, Brainiac, Beast
     */
    public static String getAffinityNameByGuardianId(String guardianId) {
        if (guardianId.equalsIgnoreCase("1")) {
            // Frost Fire
            return Enum.Affinity.ROBOTIC.toString();
        } else if (guardianId.equalsIgnoreCase("2")) {
            // Scourge
            return Enum.Affinity.ELEMENTAL.toString();
        } else if (guardianId.equalsIgnoreCase("3")) {
            // Silent Storm
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("4")) {
            // Aziza
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("5")) {
            // Binary
            return Enum.Affinity.BRAINIAC.toString();
        } else if (guardianId.equalsIgnoreCase("6")) {
            // Cougar
            return Enum.Affinity.BEAST.toString();
        } else if (guardianId.equalsIgnoreCase("7")) {
            // Brickjaw
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("8")) {
            // Hair Witch
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("9")) {
            // Genesis
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("10")) {
            // S-7
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("11")) {
            // Pain
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("12")) {
            // Massacre
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("13")) {
            // Nervo
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("14")) {
            // Dark Priestess
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("15")) {
            // Tormented King
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("16")) {
            // Exiled Priestess
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("17")) {
            // Psychick
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("18")) {
            // Amusing Sinner
            return Enum.Affinity.BRAINIAC.toString();
        } else if (guardianId.equalsIgnoreCase("19")) {
            // Brainz
            return Enum.Affinity.BRAINIAC.toString();
        } else if (guardianId.equalsIgnoreCase("20")) {
            // Slade
            return Enum.Affinity.BEAST.toString();
        } else if (guardianId.equalsIgnoreCase("21")) {
            // General of Grins
            return Enum.Affinity.BEAST.toString();
        } else if (guardianId.equalsIgnoreCase("22")) {
            // Beast Girl
            return Enum.Affinity.BEAST.toString();
        } else if (guardianId.equalsIgnoreCase("23")) {
            // Burn Streak
            return Enum.Affinity.ELEMENTAL.toString();
        } else if (guardianId.equalsIgnoreCase("24")) {
            // Dragon Flame
            return Enum.Affinity.ELEMENTAL.toString();
        } else if (guardianId.equalsIgnoreCase("25")) {
            // Madam Frost
            return Enum.Affinity.ELEMENTAL.toString();
        } else if (guardianId.equalsIgnoreCase("26")) {
            // Hot Head
            return Enum.Affinity.ELEMENTAL.toString();
        } else if (guardianId.equalsIgnoreCase("27")) {
            // Protector
            return Enum.Affinity.ROBOTIC.toString();
        } else if (guardianId.equalsIgnoreCase("30")) {
            // Klown Krew (TM1)
            return Enum.Affinity.BRAINIAC.toString();
        } else if (guardianId.equalsIgnoreCase("31")) {
            // Klown Krew (TM2)
            return Enum.Affinity.BRAINIAC.toString();
        } else if (guardianId.equalsIgnoreCase("32")) {
            // Forest Protector (TM1)
            return Enum.Affinity.BEAST.toString();
        } else if (guardianId.equalsIgnoreCase("33")) {
            // Forst Protector (TM2)
            return Enum.Affinity.BEAST.toString();
        } else if (guardianId.equalsIgnoreCase("34")) {
            // Hired Goon (TM1)
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("35")) {
            // Hired Good (TM2)
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("36")) {
            // Frost Minion (TM1)
            return Enum.Affinity.ELEMENTAL.toString();
        } else if (guardianId.equalsIgnoreCase("37")) {
            // Frost Minion (TM2)
            return Enum.Affinity.ELEMENTAL.toString();
        } else if (guardianId.equalsIgnoreCase("38")) {
            // Mutant (TM1)
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("39")) {
            // Mutant (TM2)
            return Enum.Affinity.PSYCHIC.toString();
        } else if (guardianId.equalsIgnoreCase("40")) {
            // Mutant (TM3)
            return Enum.Affinity.PHYSICAL.toString();
        } else if (guardianId.equalsIgnoreCase("41")) {
            // Mutant (TM4)
            return Enum.Affinity.PHYSICAL.toString();
        }
        // default
        return Enum.Affinity.ROBOTIC.toString();
    }

    /**
     * Helper method to assign appropriate affinity to Guardians based on their id
     * <p>This method is used for updating base stats</p>
     *
     * @param guardianId The id representing Guardians
     * @return Affinity information. Will return id, e.g. 1=Robotic, 2=Elemental, 3=Physical,
     * 4=Psychic, 5=Brainiac, 6=Beast
     */
    public static String getAffinityIdByGuardianId(String guardianId) {
        if (guardianId.equalsIgnoreCase("1")) {
            // Frost Fire
            return Enum.Affinity.ROBOTIC.toId();
        } else if (guardianId.equalsIgnoreCase("2")) {
            // Scourge
            return Enum.Affinity.ELEMENTAL.toId();
        } else if (guardianId.equalsIgnoreCase("3")) {
            // Silent Storm
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("4")) {
            // Aziza
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("5")) {
            // Binary
            return Enum.Affinity.BRAINIAC.toId();
        } else if (guardianId.equalsIgnoreCase("6")) {
            // Cougar
            return Enum.Affinity.BEAST.toId();
        } else if (guardianId.equalsIgnoreCase("7")) {
            // Brickjaw
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("8")) {
            // Hair Witch
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("9")) {
            // Genesis
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("10")) {
            // S-7
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("11")) {
            // Pain
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("12")) {
            // Massacre
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("13")) {
            // Nervo
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("14")) {
            // Dark Priestess
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("15")) {
            // Tormented King
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("16")) {
            // Exiled Priestess
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("17")) {
            // Psychick
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("18")) {
            // Amusing Sinner
            return Enum.Affinity.BRAINIAC.toId();
        } else if (guardianId.equalsIgnoreCase("19")) {
            // Brainz
            return Enum.Affinity.BRAINIAC.toId();
        } else if (guardianId.equalsIgnoreCase("20")) {
            // Slade
            return Enum.Affinity.BEAST.toId();
        } else if (guardianId.equalsIgnoreCase("21")) {
            // General of Grins
            return Enum.Affinity.BEAST.toId();
        } else if (guardianId.equalsIgnoreCase("22")) {
            // Beast Girl
            return Enum.Affinity.BEAST.toId();
        } else if (guardianId.equalsIgnoreCase("23")) {
            // Burn Streak
            return Enum.Affinity.ELEMENTAL.toId();
        } else if (guardianId.equalsIgnoreCase("24")) {
            // Dragon Flame
            return Enum.Affinity.ELEMENTAL.toId();
        } else if (guardianId.equalsIgnoreCase("25")) {
            // Madam Frost
            return Enum.Affinity.ELEMENTAL.toId();
        } else if (guardianId.equalsIgnoreCase("26")) {
            // Hot Head
            return Enum.Affinity.ELEMENTAL.toId();
        } else if (guardianId.equalsIgnoreCase("27")) {
            // Protector
            return Enum.Affinity.ROBOTIC.toId();
        } else if (guardianId.equalsIgnoreCase("30")) {
            // Klown Krew (TM1)
            return Enum.Affinity.BRAINIAC.toId();
        } else if (guardianId.equalsIgnoreCase("31")) {
            // Klown Krew (TM2)
            return Enum.Affinity.BRAINIAC.toId();
        } else if (guardianId.equalsIgnoreCase("32")) {
            // Forest Protector (TM1)
            return Enum.Affinity.BEAST.toId();
        } else if (guardianId.equalsIgnoreCase("33")) {
            // Forst Protector (TM2)
            return Enum.Affinity.BEAST.toId();
        } else if (guardianId.equalsIgnoreCase("34")) {
            // Hired Goon (TM1)
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("35")) {
            // Hired Good (TM2)
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("36")) {
            // Frost Minion (TM1)
            return Enum.Affinity.ELEMENTAL.toId();
        } else if (guardianId.equalsIgnoreCase("37")) {
            // Frost Minion (TM2)
            return Enum.Affinity.ELEMENTAL.toId();
        } else if (guardianId.equalsIgnoreCase("38")) {
            // Mutant (TM1)
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("39")) {
            // Mutant (TM2)
            return Enum.Affinity.PSYCHIC.toId();
        } else if (guardianId.equalsIgnoreCase("40")) {
            // Mutant (TM3)
            return Enum.Affinity.PHYSICAL.toId();
        } else if (guardianId.equalsIgnoreCase("41")) {
            // Mutant (TM4)
            return Enum.Affinity.PHYSICAL.toId();
        }
        // default
        return Enum.Affinity.ROBOTIC.toId();
    }

    /**
     * Helper method to assign appropriate cardType to Guardians based on their id
     * <p>Base stats will always either be Squad Leader or Common</p>
     *
     * @param guardianId The id representing Guardians
     * @return CardType information. Will return name, e.g. Squad Leader, Common, Rare,
     * Epic, Legendary, Mythic
     */
    public static String getCardTypeNameByGuardianId(String guardianId) {
        // 1 - Squad Leader
        // 2 - Common
        // 3 - Rare
        // 4 - Epic
        // 5 - Legendary
        // 6 - Mythic

        if (Integer.parseInt(guardianId) < 7) {
            return Enum.CardType.SQUAD_LEADER.toString();
        }
        return Enum.CardType.COMMON.toString();
    }

    /**
     * Helper method to assign appropriate cardType to Guardians based on their id
     * <p>Base stats will always either be Squad Leader or Common</p>
     *
     * @param guardianId The id representing Guardians
     * @return CardType information. Will return id, e.g. 1=Squad Leader, 2=Common, 3=Rare,
     * 4=Epic, 5=Legendary, 6=Mythic
     */
    public static String getCardTypeIdByGuardianId(String guardianId) {
        // 1 - Squad Leader
        // 2 - Common
        // 3 - Rare
        // 4 - Epic
        // 5 - Legendary
        // 6 - Mythic

        if (Integer.parseInt(guardianId) < 7) {
            return Enum.CardType.SQUAD_LEADER.toId();
        }
        return Enum.CardType.COMMON.toId();
    }

    /**
     * Helper method to assign appropriate description to Guardians based on their id
     * <p>This method is used for updating base stats</p>
     *
     * @param context    Interface to global information about an application environment
     * @param guardianId The id representing Guardians
     * @return Description information
     */
    public static String getDescriptionByGuardianId(Context context, String guardianId) {
        if (guardianId.equalsIgnoreCase("1")) {
            // Frost Fire
            return cleanString(context.getResources().getString(R.string.frost_fire_desc));
        } else if (guardianId.equalsIgnoreCase("2")) {
            // Scourge
            return cleanString(context.getResources().getString(R.string.scourge_desc));
        } else if (guardianId.equalsIgnoreCase("3")) {
            // Silent Storm
            return cleanString(context.getResources().getString(R.string.silent_storm));
        } else if (guardianId.equalsIgnoreCase("4")) {
            // Aziza
            return cleanString(context.getResources().getString(R.string.aziza_desc));
        } else if (guardianId.equalsIgnoreCase("5")) {
            // Binary
            return cleanString(context.getResources().getString(R.string.binary_desc));
        } else if (guardianId.equalsIgnoreCase("6")) {
            // Cougar
            return cleanString(context.getResources().getString(R.string.cougar_desc));
        } else if (guardianId.equalsIgnoreCase("7")) {
            // Brickjaw
            return cleanString(context.getResources().getString(R.string.brick_jaw_desc));
        } else if (guardianId.equalsIgnoreCase("8")) {
            // Hair Witch
            return cleanString(context.getResources().getString(R.string.hair_witch_desc));
        } else if (guardianId.equalsIgnoreCase("9")) {
            // Genesis
            return cleanString(context.getResources().getString(R.string.genesis_desc));
        } else if (guardianId.equalsIgnoreCase("10")) {
            // S-7
            return cleanString(context.getResources().getString(R.string.s7_desc));
        } else if (guardianId.equalsIgnoreCase("11")) {
            // Pain
            return cleanString(context.getResources().getString(R.string.pain_desc));
        } else if (guardianId.equalsIgnoreCase("12")) {
            // Massacre
            return cleanString(context.getResources().getString(R.string.massacre_desc));
        } else if (guardianId.equalsIgnoreCase("13")) {
            // Nervo
            return cleanString(context.getResources().getString(R.string.nervo_desc));
        } else if (guardianId.equalsIgnoreCase("14")) {
            // Dark Priestess
            return cleanString(context.getResources().getString(R.string.dark_preistess_desc));
        } else if (guardianId.equalsIgnoreCase("15")) {
            // Tormented King
            return cleanString(context.getResources().getString(R.string.tormented_king_desc));
        } else if (guardianId.equalsIgnoreCase("16")) {
            // Exiled Priestess
            return cleanString(context.getResources().getString(R.string.exiled_priestess_desc));
        } else if (guardianId.equalsIgnoreCase("17")) {
            // Psychick
            return cleanString(context.getResources().getString(R.string.psychick_desc));
        } else if (guardianId.equalsIgnoreCase("18")) {
            // Amusing Sinner
            return cleanString(context.getResources().getString(R.string.amusing_sinner_desc));
        } else if (guardianId.equalsIgnoreCase("19")) {
            // Brainz
            return cleanString(context.getResources().getString(R.string.brainz_desc));
        } else if (guardianId.equalsIgnoreCase("20")) {
            // Slade
            return cleanString(context.getResources().getString(R.string.slade_desc));
        } else if (guardianId.equalsIgnoreCase("21")) {
            // General of Grins
            return cleanString(context.getResources().getString(R.string.general_of_grins_desc));
        } else if (guardianId.equalsIgnoreCase("22")) {
            // Beast Girl
            return cleanString(context.getResources().getString(R.string.beast_girl));
        } else if (guardianId.equalsIgnoreCase("23")) {
            // Burn Streak
            return cleanString(context.getResources().getString(R.string.burn_streak_desc));
        } else if (guardianId.equalsIgnoreCase("24")) {
            // Dragon Flame
            return cleanString(context.getResources().getString(R.string.dragon_flame_desc));
        } else if (guardianId.equalsIgnoreCase("25")) {
            // Madam Frost
            return cleanString(context.getResources().getString(R.string.madam_frost_desc));
        } else if (guardianId.equalsIgnoreCase("26")) {
            // Hot Head
            return cleanString(context.getResources().getString(R.string.hot_head_desc));
        } else if (guardianId.equalsIgnoreCase("27")) {
            // Protector
            return cleanString(context.getResources().getString(R.string.protector_desc));
        } else if (guardianId.equalsIgnoreCase("30")) {
            // Klown Krew (TM1)
            return "";
        } else if (guardianId.equalsIgnoreCase("31")) {
            // Klown Krew (TM2)
            return "";
        } else if (guardianId.equalsIgnoreCase("32")) {
            // Forest Protector (TM1)
            return "";
        } else if (guardianId.equalsIgnoreCase("33")) {
            // Forst Protector (TM2)
            return "";
        } else if (guardianId.equalsIgnoreCase("34")) {
            // Hired Goon (TM1)
            return "";
        } else if (guardianId.equalsIgnoreCase("35")) {
            // Hired Good (TM2)
            return "";
        } else if (guardianId.equalsIgnoreCase("36")) {
            // Frost Minion (TM1)
            return "";
        } else if (guardianId.equalsIgnoreCase("37")) {
            // Frost Minion (TM2)
            return "";
        } else if (guardianId.equalsIgnoreCase("38")) {
            // Mutant (TM1)
            return "";
        } else if (guardianId.equalsIgnoreCase("39")) {
            // Mutant (TM2)
            return "";
        } else if (guardianId.equalsIgnoreCase("40")) {
            // Mutant (TM3)
            return "";
        } else if (guardianId.equalsIgnoreCase("41")) {
            // Mutant (TM4)
            return "";
        }
        // default
        return "";
    }

    /**
     * Method is used to prepare String value for RESTful. It will double escape apostrophe
     * @param value The String to clean
     * @return Cleaned String value ready for RESTful
     */
    private static String cleanString(String value) {
        return value.replaceAll("'", "\\\\'");
    }

    /**
     * Method is used to retrieve mission details
     * @param id The id representing the mission area
     * @param difficultyLevelId The id representing the mission area difficulty level e.g. Alpha, Beta, Omega
     * @return
     */
    public static MissionDetails getMissionDetailsById(String id, String difficultyLevelId) {
        MissionDetails missionDetails = new MissionDetails();
        if (difficultyLevelId.equalsIgnoreCase("1")) {
            if (id.equalsIgnoreCase("1")) {
                missionDetails.name = "";
                missionDetails.description = "";
            } else if (id.equalsIgnoreCase("2")) {

            } else if (id.equalsIgnoreCase("3")) {

            } else if (id.equalsIgnoreCase("4")) {

            } else if (id.equalsIgnoreCase("5")) {

            } else if (id.equalsIgnoreCase("6")) {

            } else if (id.equalsIgnoreCase("7")) {

            } else if (id.equalsIgnoreCase("8")) {

            } else if (id.equalsIgnoreCase("9")) {

            }
        }
        return missionDetails;
    }

}
