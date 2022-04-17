package io.github.greenmc.retropvp.features.stats;

public enum StatisticType {

    KILLS("kills", "kill", 0),
    DEATHS("death", "death", 0),
    KILL_STREAK("kill_streak", "streak", 0),
    MAX_STREAK("max_streak", "record", 0);

    private final String name;
    private final String shortName;
    private final int defaultValue;

    StatisticType(String name, String shortName, int defaultValue) {
        this.name = name;
        this.shortName = shortName;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public String getShortName() {
        return shortName;
    }

    public static StatisticType getByShort(String shortName) {
        for (StatisticType value : values()) {
            if (value.getShortName().equalsIgnoreCase(shortName)) {
                return value;
            }
        }
        return null;
    }

}