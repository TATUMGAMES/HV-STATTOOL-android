package com.tatumgames.stattool.model;

import com.tatumgames.stattool.enums.Affinity;
import com.tatumgames.stattool.enums.CardType;

/**
 * Model class for Guardian statistics
 * Created by Tatum on 7/22/2015.
 */
public class Stats {

    private Affinity mAffinity;
    private CardType mCardType;
    private int mHp;
    private int mStr;
    private int mSpd;
    private int mWis;
    private int mPhyDef;
    private int mMagDef;
    private int mCrit;

    /**
     * Set affinity
     * @param affinity
     */
    public void setAffinity(Affinity affinity) {
        mAffinity = affinity;
    }

    /**
     * Set card type
     * @param cardType
     */
    public void setCardType(CardType cardType) {
        mCardType = cardType;
    }

    /**
     * set health power
     * @param hp
     */
    public void setHp(int hp) {
        mHp = hp;
    }

    /**
     * set strength
     * @param str
     */
    public void setStr(int str) {
        mStr = str;
    }

    /**
     * set speed
     * @param spd
     */
    public void setSpd(int spd) {
        mSpd = spd;
    }

    /**
     * set wisdom
     * @param wis
     */
    public void setWis(int wis) {
        mWis = wis;
    }

    /**
     * set physical defense
     * @param phyDef
     */
    public void setPhyDef(int phyDef) {
        mPhyDef = phyDef;
    }

    /**
     * set magical def
     * @param magDef
     */
    public void setMagDef(int magDef) {
        mMagDef = magDef;
    }

    /**
     * set critical percent
     * @param crit
     */
    public void setCrit(int crit) {
        mCrit = crit;
    }

    /**
     * retrieve affinity
     * @return
     */
    public Affinity getAffinity() {
        return mAffinity;
    }

    /**
     * retrieve card type
     * @return
     */
    public CardType getCardType() {
        return mCardType;
    }

    /**
     * retrieve health power
     * @return
     */
    public int getHp() {
        return mHp;
    }

    /**
     * retrieve strength
     * @return
     */
    public int getStr() {
        return mStr;
    }

    /**
     * retrieve speed
     * @return
     */
    public int getSpd() {
        return mSpd;
    }

    /**
     * retrieve wisdom
     * @return
     */
    public int getWis() {
        return mWis;
    }

    /**
     * retrieve physical defense
     * @return
     */
    public int getPhyDef() {
        return mPhyDef;
    }

    /**
     * retrieve magical defense
     * @return
     */
    public int getMagDef() {
        return mMagDef;
    }

    /**
     * retrieve critical percent
     * @return
     */
    public int getCrit() {
        return mCrit;
    }

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
