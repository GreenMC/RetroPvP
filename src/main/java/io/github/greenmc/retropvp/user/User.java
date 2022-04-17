package io.github.greenmc.retropvp.user;

import io.github.greenmc.retropvp.api.StatsStorage;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class User {

    private final Player player;
    private final Map<StatsStorage.StatisticType, Integer> stats;

    public User(Player player) {
        this.player = player;
        this.stats = new EnumMap<>(StatsStorage.StatisticType.class);
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public void setStat(StatsStorage.StatisticType stat, int value) {
        stats.put(stat, value);
    }

    public int getStat(StatsStorage.StatisticType statisticType) {
        Integer statistic = stats.get(statisticType);

        if (statistic == null) {
            stats.put(statisticType, 0);
            return 0;
        }

        return statistic;
    }

    public void addStat(StatsStorage.StatisticType stat, int value) {
        setStat(stat, getStat(stat) + value);
    }

}