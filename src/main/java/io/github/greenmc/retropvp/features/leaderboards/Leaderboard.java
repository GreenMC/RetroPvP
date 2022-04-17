package io.github.greenmc.retropvp.features.leaderboards;

import io.github.greenmc.retropvp.api.StatsStorage;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    private final String id;
    private String name;
    private int maxSize;
    private StatsStorage.StatisticType type;
    private SortingType sortingType;
    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();

    public Leaderboard(String id) {
        this(id, id, null, null, 5);
    }

    public Leaderboard(String id, String name, StatsStorage.StatisticType type, SortingType sortingType, int maxSize) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.sortingType = sortingType;
        setMaxSize(maxSize);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = Math.min(maxSize, 20);
    }

    public void setType(StatsStorage.StatisticType type) {
        this.type = type;
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StatsStorage.StatisticType getType() {
        return type;
    }

    public SortingType getSortingType() {
        return sortingType;
    }

    public int getMaxSize() {
        return maxSize;
    }
    public void setLeaderboardEntries(List<LeaderboardEntry> leaderboardEntries) {
        this.leaderboardEntries = leaderboardEntries;
    }

    public List<LeaderboardEntry> getLeaderboardEntries() {
        return leaderboardEntries;
    }

    public boolean isReady() {
        return id != null && name != null
                && type != null && sortingType != null
                && !leaderboardEntries.isEmpty();
    }

}