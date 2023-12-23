package de.janhck.forceitembattlechallenge.constants;

public enum ItemDifficultyLevel {
    EASY, MEDIUM, HARD;

    static public final ItemDifficultyLevel[] values = values();

    public ItemDifficultyLevel prev() {
        return values[(ordinal() - 1  + values.length) % values.length];
    }

    public ItemDifficultyLevel next() {
        return values[(ordinal() + 1) % values.length];
    }
}
