package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge;

public enum ForceItemBattleChallengeMode {

    DEFAULT, ALL_SAME_ITEMS;

    static public final ForceItemBattleChallengeMode[] values = values();

    public ForceItemBattleChallengeMode prev() {
        return values[(ordinal() - 1  + values.length) % values.length];
    }

    public ForceItemBattleChallengeMode next() {
        return values[(ordinal() + 1) % values.length];
    }

}
