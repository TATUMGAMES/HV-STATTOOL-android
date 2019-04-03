package com.tatumgames.stattool.enums;

public class Enum {

    /**
     * Enum used for identifying card rarity
     */
    public enum CardType {
        COMMON, RARE, EPIC, LEGENDARY, MYTHIC, SQUAD_LEADER;
    }

    /**
     * Enum used for identifying card affinities
     */
    public enum Affinity {
        ROBOTIC, PHYSICAL, BEAST, ELEMENTAL, PSYCHIC, BRAINIAC;
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
