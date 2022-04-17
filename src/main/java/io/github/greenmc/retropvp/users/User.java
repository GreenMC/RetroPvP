package io.github.greenmc.retropvp.users;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.stats.StatisticType;
import io.github.greenmc.retropvp.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class User {

    private final static RetroPvP plugin = JavaPlugin.getPlugin(RetroPvP.class);

    private final Map<StatisticType, Integer> stats = new EnumMap<>(StatisticType.class);
    private final Player base;

    private UserState state;
    private FileConfiguration data;

    public User(Player player) {
        this.base = player;
        this.state = UserState.FREE;
        this.data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/players/" + player.getUniqueId() + ".yml"));
    }

    public Player getBase() {
        return base;
    }

    public String getName() {
        return base.getName();
    }

    public UUID getUUID() {
        return base.getUniqueId();
    }

    public FileConfiguration getData() {
        return data;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState newState) {
        state = newState;
    }

    public boolean isInMatch() {
        return state == UserState.IN_MATCH || state == UserState.STARTING_MATCH;
    }

    public int getStat(StatisticType statisticType) {
        Integer statistic = stats.get(statisticType);

        if (statistic == null) {
            stats.put(statisticType, 0);
            return 0;
        }

        return statistic;
    }

    public void setStat(StatisticType stat, int i) {
        stats.put(stat, i);
    }

    public void addStat(StatisticType stat, int i) {
        stats.put(stat, getStat(stat) + i);
    }

    public void sendMessage(String message) {
        base.sendMessage(Utils.getMessage(message, base));
    }

}