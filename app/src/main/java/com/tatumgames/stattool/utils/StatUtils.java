package com.tatumgames.stattool.utils;

import android.support.annotation.NonNull;

import com.tatumgames.stattool.enums.Enum;
import com.tatumgames.stattool.model.GuardianStats;

import java.util.Random;

public class StatUtils {

    private static final double[] MULTIPLIER_TIER = {1, 0.9, 0.75, 0.55, 0.4, 0.25}; // arbitrary value

    private Enum.CardType mCardType;
    private Enum.Affinity mAffinity;

    /**
     * @param cardType This represents the strength and rarity of the card or Guardian
     * @param affinity Each Guardian has strengths and weaknesses based on their affinity e.g.
     *                 Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @param asc      Ascension value
     * @return GuardianStats object with generated base stats
     */
    public GuardianStats getStats(@NonNull Enum.CardType cardType, @NonNull Enum.Affinity affinity, int asc) {
        // initialize
        mCardType = cardType;
        mAffinity = affinity;

        // populate stat object
        GuardianStats stats = new GuardianStats();
        stats.setMaxLv(getMaxLv(asc));
        stats.setMaxAsc(getMaxAsc());
        stats.setHp(randHpStatBase());
        stats.setStr(randStrStat());
        stats.setSpd(randSpdStat());
        stats.setWis(randWisStat());
        stats.setPhyDef(randPhyDefStat());
        stats.setMagDef(randMagDefStat());
        stats.setCrit(randCritPercStat());
        stats.setCardType(cardType);
        stats.setAffinity(affinity);
        return stats;
    }

    /**
     * Helper method to convert String affinity to Affinity object
     *
     * @param affinity Each Guardian has strengths and weaknesses based on their affinity e.g.
     *                 Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     * @return Object containing Affinity information
     */
    public Enum.Affinity getAffinity(String affinity) {
        if (affinity.equalsIgnoreCase(String.valueOf(Enum.Affinity.ROBOTIC))) {
            return Enum.Affinity.ROBOTIC;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Enum.Affinity.PHYSICAL))) {
            return Enum.Affinity.PHYSICAL;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Enum.Affinity.BEAST))) {
            return Enum.Affinity.BEAST;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Enum.Affinity.ELEMENTAL))) {
            return Enum.Affinity.ELEMENTAL;
        } else if (affinity.equalsIgnoreCase(String.valueOf(Enum.Affinity.PSYCHIC))) {
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
    public Enum.CardType getCardType(String cardType) {
        if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.COMMON))) {
            return Enum.CardType.COMMON;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.RARE))) {
            return Enum.CardType.RARE;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.EPIC))) {
            return Enum.CardType.EPIC;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.LEGENDARY))) {
            return Enum.CardType.LEGENDARY;
        } else if (cardType.equalsIgnoreCase(String.valueOf(Enum.CardType.MYTHIC))) {
            return Enum.CardType.MYTHIC;
        }
        return Enum.CardType.SQUAD_LEADER;
    }

    /**
     * Method is used to calculate HP base stat using cardType and affinity to generate
     * a randomized value
     *
     * <p>
     * (C) hp base: [350-400]
     * (R) hp base: [400-475]
     * (E) hp base: [475-550]
     * (L) hp base: [575-675]
     * (M) hp base: [700-850]
     * (SL) hp base: [437-512]
     * </p>
     *
     * @return HP base value
     */
    private int randHpStatBase() {
        Random rand = new Random();
        int hp = 0;

        if (mCardType.equals(Enum.CardType.COMMON)) {
            hp = rand.nextInt(51) + 350;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            hp = rand.nextInt(76) + 400;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            hp = rand.nextInt(76) + 475;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            hp = rand.nextInt(101) + 575;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            hp = rand.nextInt(151) + 700;
        } else if (mCardType.equals(Enum.CardType.SQUAD_LEADER)) {
            hp = rand.nextInt(76) + 437;
        }
        return hp + randHpByAffinity(hp);
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * base HP will be, [baseHp = randomized hp + value based on affinity]
     *
     * <p>
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     * </p>
     *
     * @param hp Randomized value to be added to create the Base HP stat
     * @return Value based on affinity
     */
    private int randHpByAffinity(int hp) {
        int hpByAffinity = 0;

        if (mAffinity.equals(Enum.Affinity.ROBOTIC)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER[2]);
        } else if (mAffinity.equals(Enum.Affinity.PHYSICAL)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER[0]);
        } else if (mAffinity.equals(Enum.Affinity.BEAST)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER[3]);
        } else if (mAffinity.equals(Enum.Affinity.ELEMENTAL)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER[1]);
        } else if (mAffinity.equals(Enum.Affinity.PSYCHIC)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER[4]);
        } else if (mAffinity.equals(Enum.Affinity.BRAINIAC)) {
            hpByAffinity = (int) (hp * MULTIPLIER_TIER[5]);
        }
        return hpByAffinity;
    }

    /**
     * Method is used to calculate Strength base stat using cardType and affinity to generate
     * a randomized value
     *
     * <p>
     * (C) str base: [40-45]
     * (R) str base: [45-55]
     * (E) str base: [50-60]
     * (L) str base: [65-80]
     * (M) str base: [90-110]
     * (SL) str base: [60-75]
     * </p>
     *
     * @return Strength base value
     */
    private int randStrStat() {
        Random rand = new Random();
        int str = 0;

        if (mCardType.equals(Enum.CardType.COMMON)) {
            str = rand.nextInt(6) + 40;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            str = rand.nextInt(11) + 45;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            str = rand.nextInt(11) + 50;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            str = rand.nextInt(16) + 65;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            str = rand.nextInt(21) + 90;
        } else if (mCardType.equals(Enum.CardType.SQUAD_LEADER)) {
            str = rand.nextInt(16) + 60;
        }
        return str + randStrByAffinity(str);
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Strength value will be, [baseStr = randomized str + value based on affinity]
     *
     * <p>
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     * </p>
     *
     * @param str Randomized value to be added to create the Base Strength stat
     * @return Value based on affinity
     */
    private int randStrByAffinity(int str) {
        int strByAffinity = 0;

        if (mAffinity.equals(Enum.Affinity.ROBOTIC)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER[0]);
        } else if (mAffinity.equals(Enum.Affinity.PHYSICAL)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER[1]);
        } else if (mAffinity.equals(Enum.Affinity.BEAST)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER[2]);
        } else if (mAffinity.equals(Enum.Affinity.ELEMENTAL)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER[3]);
        } else if (mAffinity.equals(Enum.Affinity.PSYCHIC)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER[5]);
        } else if (mAffinity.equals(Enum.Affinity.BRAINIAC)) {
            strByAffinity = (int) (str * MULTIPLIER_TIER[4]);
        }
        return strByAffinity;
    }

    /**
     * Method is used to calculate Speed base stat using cardType and affinity to generate
     * a randomized value
     *
     * <p>
     * (C) spd base: [35-40]
     * (R) spd base: [37-45]
     * (E) spd base: [42-50]
     * (L) spd base: [50-60]
     * (M) spd base: [60-75]
     * (SL) spd base: [40-48]
     * </p>
     *
     * @return Speed base value
     */
    private int randSpdStat() {
        Random rand = new Random();
        int spd = 0;

        if (mCardType.equals(Enum.CardType.COMMON)) {
            spd = rand.nextInt(6) + 35;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            spd = rand.nextInt(9) + 37;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            spd = rand.nextInt(9) + 42;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            spd = rand.nextInt(11) + 50;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            spd = rand.nextInt(16) + 60;
        } else if (mCardType.equals(Enum.CardType.SQUAD_LEADER)) {
            spd = rand.nextInt(9) + 40;
        }
        return spd + randSpdByAffinity(spd);
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Speed value will be, [baseSpd = randomized spd + value based on affinity]
     *
     * <p>
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     * </p>
     *
     * @param spd Randomized value to be added to create the Base Speed stat
     * @return Value based on affinity
     */
    private int randSpdByAffinity(int spd) {
        int spdByAffinity = 0;

        if (mAffinity.equals(Enum.Affinity.ROBOTIC)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER[5]);
        } else if (mAffinity.equals(Enum.Affinity.PHYSICAL)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER[4]);
        } else if (mAffinity.equals(Enum.Affinity.BEAST)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER[0]);
        } else if (mAffinity.equals(Enum.Affinity.ELEMENTAL)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER[4]);
        } else if (mAffinity.equals(Enum.Affinity.PSYCHIC)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER[1]);
        } else if (mAffinity.equals(Enum.Affinity.BRAINIAC)) {
            spdByAffinity = (int) (spd * MULTIPLIER_TIER[3]);
        }
        return spdByAffinity;
    }

    /**
     * Method is used to calculate Wisdom base stat using cardType and affinity to generate
     * a randomized value
     *
     * <p>
     * (C) wis base: [40-45]
     * (R) wis base: [45-55]
     * (E) wis base: [50-60]
     * (L) wis base: [65-80]
     * (M) wis base: [90-110]
     * (SL) wis base: [60-75]
     * </p>
     *
     * @return Wisdom base value
     */
    private int randWisStat() {
        Random rand = new Random();
        int wis = 0;

        if (mCardType.equals(Enum.CardType.COMMON)) {
            wis = rand.nextInt(6) + 40;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            wis = rand.nextInt(11) + 45;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            wis = rand.nextInt(11) + 50;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            wis = rand.nextInt(16) + 65;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            wis = rand.nextInt(21) + 90;
        } else if (mCardType.equals(Enum.CardType.SQUAD_LEADER)) {
            wis = rand.nextInt(16) + 60;
        }
        return wis + randWisByAffinity(wis);
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Wisdom value will be, [baseWis = randomized wis + value based on affinity]
     *
     * <p>
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     * </p>
     *
     * @param wis Randomized value to be added to create the Base Wisdom stat
     * @return Value based on affinity
     */
    private int randWisByAffinity(int wis) {
        int wisByAffinity = 0;

        if (mAffinity.equals(Enum.Affinity.ROBOTIC)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER[4]);
        } else if (mAffinity.equals(Enum.Affinity.PHYSICAL)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER[5]);
        } else if (mAffinity.equals(Enum.Affinity.BEAST)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER[5]);
        } else if (mAffinity.equals(Enum.Affinity.ELEMENTAL)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER[0]);
        } else if (mAffinity.equals(Enum.Affinity.PSYCHIC)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER[0]);
        } else if (mAffinity.equals(Enum.Affinity.BRAINIAC)) {
            wisByAffinity = (int) (wis * MULTIPLIER_TIER[0]);
        }
        return wisByAffinity;
    }

    /**
     * Method is used to calculate Physical Defense base stat using cardType and affinity to generate
     * a randomized value
     *
     * <p>
     * (C) phyDef base: [35-40]
     * (R) phyDef base: [37-45]
     * (E) phyDef base: [42-50]
     * (L) phyDef base: [50-60]
     * (M) phyDef base: [60-75]
     * (SL) phyDef base: [40-48]
     * </p>
     *
     * @return Physical Defense base value
     */
    private int randPhyDefStat() {
        Random rand = new Random();
        int phyDef = 0;

        if (mCardType.equals(Enum.CardType.COMMON)) {
            phyDef = rand.nextInt(6) + 35;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            phyDef = rand.nextInt(9) + 37;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            phyDef = rand.nextInt(9) + 42;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            phyDef = rand.nextInt(11) + 50;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            phyDef = rand.nextInt(16) + 60;
        } else if (mCardType.equals(Enum.CardType.SQUAD_LEADER)) {
            phyDef = rand.nextInt(9) + 40;
        }
        return phyDef + randPhyDefByAffinity(phyDef);
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Physical Defense value will be, [basePhyDef = randomized phyDef + value based on affinity]
     *
     * <p>
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     * </p>
     *
     * @param phyDef Randomized value to be added to create the Base Physical Defense stat
     * @return Value based on affinity
     */
    private int randPhyDefByAffinity(int phyDef) {
        int phyDefByAffinity = 0;

        if (mAffinity.equals(Enum.Affinity.ROBOTIC)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER[3]);
        } else if (mAffinity.equals(Enum.Affinity.PHYSICAL)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER[2]);
        } else if (mAffinity.equals(Enum.Affinity.BEAST)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER[4]);
        } else if (mAffinity.equals(Enum.Affinity.ELEMENTAL)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER[2]);
        } else if (mAffinity.equals(Enum.Affinity.PSYCHIC)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER[2]);
        } else if (mAffinity.equals(Enum.Affinity.BRAINIAC)) {
            phyDefByAffinity = (int) (phyDef * MULTIPLIER_TIER[1]);
        }
        return phyDefByAffinity;
    }

    /**
     * Method is used to calculate Magical Defense base stat using cardType and affinity to generate
     * a randomized value
     *
     * <p>
     * (C) magDef base: [35-40]
     * (R) magDef base: [37-45]
     * (E) magDef base: [42-50]
     * (L) magDef base: [50-60]
     * (M) magDef base: [60-75]
     * (SL) magDef base: [40-48]
     * </p>
     *
     * @return Magical Defense base value
     */
    private int randMagDefStat() {
        Random rand = new Random();
        int magDef = 0;

        if (mCardType.equals(Enum.CardType.COMMON)) {
            magDef = rand.nextInt(6) + 35;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            magDef = rand.nextInt(9) + 37;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            magDef = rand.nextInt(9) + 42;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            magDef = rand.nextInt(11) + 50;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            magDef = rand.nextInt(16) + 60;
        } else if (mCardType.equals(Enum.CardType.SQUAD_LEADER)) {
            magDef = rand.nextInt(9) + 40;
        }
        return magDef + randMagDefByAffinity(magDef);
    }

    /**
     * Method is used to return back a value based on affinity. This contributes to what the final
     * Magical Defense value will be, [baseMagDef = randomized magDef + value based on affinity]
     *
     * <p>
     * multiplier: 1, 0.9, 0.75, 0.55, 0.4, 0.25
     * Robotic stat order: Str, MagDef, HP, PhyDef, Wisdom, Speed
     * Physical stat order: HP, Str, PhyDef, MagDef, Speed, Wisdom
     * Beast stat order: Speed, MagDef, Str, HP, PhyDef, Wisdom
     * Elemental stat order: Wisdom, HP, PhyDef, Str, Speed, MagDef
     * Psychic stat order: Wisdom, Speed, PhyDef, MagDef, HP, Str
     * Brainiac stat order: Wisdom, PhyDef, MagDef, Speed, Str, HP
     * </p>
     *
     * @param magDef Randomized value to be added to create the Base Magical Defense stat
     * @return Value based on affinity
     */
    private int randMagDefByAffinity(int magDef) {
        int magDefByAffinity = 0;

        if (mAffinity.equals(Enum.Affinity.ROBOTIC)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER[1]);
        } else if (mAffinity.equals(Enum.Affinity.PHYSICAL)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER[3]);
        } else if (mAffinity.equals(Enum.Affinity.BEAST)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER[1]);
        } else if (mAffinity.equals(Enum.Affinity.ELEMENTAL)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER[5]);
        } else if (mAffinity.equals(Enum.Affinity.PSYCHIC)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER[3]);
        } else if (mAffinity.equals(Enum.Affinity.BRAINIAC)) {
            magDefByAffinity = (int) (magDef * MULTIPLIER_TIER[2]);
        }
        return magDefByAffinity;
    }

    /**
     * Method is used to calculate chance to perform critical attack. Note that crit percentage
     * is not affinity linked
     *
     * <p>
     * (C) critPerc base: [2]
     * (R) critPerc base: [4]
     * (E) critPerc base: [6]
     * (L) critPerc base: [8]
     * (M) critPerc base: [10]
     * (SL) critPerc base: [5]
     * </p>
     *
     * @return Percentage represented value to perform critical attack
     */
    private int randCritPercStat() {
        if (mCardType.equals(Enum.CardType.COMMON)) {
            return 2;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            return 4;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            return 6;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            return 8;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            return 10;
        }
        // SQUAD_LEADER return value
        return 5;
    }

    /**
     * Retrieve base max level
     *
     * @param currAsc The maximum level of a Guardian increases based on the Ascension (Asc) level
     * @return Maximum level based on cardType
     */
    private int getMaxLv(int currAsc) {
        int mMaxLv = 0;
        int ascIncValue = currAsc * 2;

        if (mCardType.equals(Enum.CardType.COMMON)) {
            mMaxLv = 20 + ascIncValue;
        } else if (mCardType.equals(Enum.CardType.RARE)) {
            mMaxLv = 24 + ascIncValue;
        } else if (mCardType.equals(Enum.CardType.EPIC)) {
            mMaxLv = 28 + ascIncValue;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY)) {
            mMaxLv = 32 + ascIncValue;
        } else if (mCardType.equals(Enum.CardType.MYTHIC)) {
            mMaxLv = 36 + ascIncValue;
        } else if (mCardType.equals(Enum.CardType.SQUAD_LEADER)) {
            mMaxLv = 34;
        }
        return mMaxLv;
    }

    /**
     * Retrieve maximum ascension level
     * <p>Squad Leader return 0 ascension by default</p>
     *
     * @return Maximum ascension level based on cardType
     */
    private int getMaxAsc() {
        // SQUAD_LEADER max ascension
        int maxAsc = 0;

        if (mCardType.equals(Enum.CardType.COMMON) ||
                mCardType.equals(Enum.CardType.RARE) ||
                mCardType.equals(Enum.CardType.EPIC)) {
            maxAsc = 3;
        } else if (mCardType.equals(Enum.CardType.LEGENDARY) ||
                mCardType.equals(Enum.CardType.MYTHIC)) {
            maxAsc = 4;
        }
        return maxAsc;
    }

}
