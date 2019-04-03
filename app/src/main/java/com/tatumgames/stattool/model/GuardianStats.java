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

import com.tatumgames.stattool.enums.Enum;

/**
 * Model class for Guardian statistics
 * Created by Tatum on 7/22/2015.
 */
public class GuardianStats {

    private Enum.Affinity mAffinity;
    private Enum.CardType mCardType;
    private int mHp, mStr, mSpd, mWis, mPhyDef, mMagDef, mCrit, mMavLv, mMaxAsc;

    /**
     * Set affinity
     *
     * @param affinity Each Guardian has strengths and weaknesses based on their affinity e.g.
     *                 Robotic, Physical, Beast, Elemental, Psychic, Brainiac
     */
    public void setAffinity(Enum.Affinity affinity) {
        mAffinity = affinity;
    }

    /**
     * Set card type
     *
     * @param cardType This represents the strength and rarity of the card or Guardian
     */
    public void setCardType(Enum.CardType cardType) {
        mCardType = cardType;
    }

    /**
     * set health power
     *
     * @param hp Randomized value to be added to create the Base HP stat
     */
    public void setHp(int hp) {
        mHp = hp;
    }

    /**
     * set strength
     *
     * @param str Randomized value to be added to create the Base Strength stat
     */
    public void setStr(int str) {
        mStr = str;
    }

    /**
     * set speed
     *
     * @param spd Randomized value to be added to create the Base Speed stat
     */
    public void setSpd(int spd) {
        mSpd = spd;
    }

    /**
     * set wisdom
     *
     * @param wis Randomized value to be added to create the Base Wisdom stat
     */
    public void setWis(int wis) {
        mWis = wis;
    }

    /**
     * set physical defense
     *
     * @param phyDef Randomized value to be added to create the Base Physical Defense stat
     */
    public void setPhyDef(int phyDef) {
        mPhyDef = phyDef;
    }

    /**
     * set magical def
     *
     * @param magDef Randomized value to be added to create the Base Magical Defense stat
     */
    public void setMagDef(int magDef) {
        mMagDef = magDef;
    }

    /**
     * set critical percent
     *
     * @param crit Randomized value to be added to create the Base Critical Percentage stat
     */
    public void setCrit(int crit) {
        mCrit = crit;
    }

    /**
     * Setter for setting maximum level
     *
     * @param mavLv Maximum level
     */
    public void setMaxLv(int mavLv) {
        mMavLv = mavLv;
    }

    /**
     * Setter for setting maximum ascension
     *
     * @param maxAsc Maximum ascension
     */
    public void setMaxAsc(int maxAsc) {
        mMaxAsc = maxAsc;
    }

    /**
     * retrieve affinity
     *
     * @return Affinity value
     */
    public Enum.Affinity getAffinity() {
        return mAffinity;
    }

    /**
     * retrieve card type
     *
     * @return Card type value
     */
    public Enum.CardType getCardType() {
        return mCardType;
    }

    /**
     * retrieve health power
     *
     * @return Health power value
     */
    public int getHp() {
        return mHp;
    }

    /**
     * retrieve strength
     *
     * @return Strength value
     */
    public int getStr() {
        return mStr;
    }

    /**
     * retrieve speed
     *
     * @return Speed value
     */
    public int getSpd() {
        return mSpd;
    }

    /**
     * retrieve wisdom
     *
     * @return Wisdom value
     */
    public int getWis() {
        return mWis;
    }

    /**
     * retrieve physical defense
     *
     * @return Physical defense value
     */
    public int getPhyDef() {
        return mPhyDef;
    }

    /**
     * retrieve magical defense
     *
     * @return Magical defense value
     */
    public int getMagDef() {
        return mMagDef;
    }

    /**
     * retrieve critical percent
     *
     * @return Critical percent value
     */
    public int getCrit() {
        return mCrit;
    }

    /**
     * Retrieve maximum possible level
     *
     * @return Maximum possible level
     */
    public int getMavLv() {
        return mMavLv;
    }

    /**
     * Retrieve maximum possible ascension
     *
     * @return Maximum possible ascension
     */
    public int getMaxAsc() {
        return mMaxAsc;
    }

    /**
     * Method is used to reset all stat attributes
     */
    public void reset() {
        mHp = 0;
        mStr = 0;
        mSpd = 0;
        mWis = 0;
        mPhyDef = 0;
        mMagDef = 0;
        mCrit = 0;
    }

}
