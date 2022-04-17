package io.github.greenmc.retropvp.users;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.stats.StatisticType;
import io.github.greenmc.retropvp.users.data.StatisticSaveProcess;
import me.despical.commons.configuration.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserManager {

    private final RetroPvP plugin;
    private final Set<User> userList = new HashSet<>();
    private StatisticSaveProcess statisticSaveProcess;

    public UserManager(RetroPvP plugin) {
        this.plugin = plugin;
        prepareSaveProcess();
    }

    public void prepareSaveProcess() {
        if (statisticSaveProcess != null) {
            statisticSaveProcess.cancel();
        }
        statisticSaveProcess = new StatisticSaveProcess(plugin);
        statisticSaveProcess.start();
    }

    public User getOrLoadUser(Player player) {
        if (player == null || !player.isOnline()) {
            return null;
        }

        UUID uuid = player.getUniqueId();
        for (User user : userList) {
            if (user.getUUID().equals(uuid)) {
                return user;
            }
        }

        User user = new User(player);
        loadStatistics(user);
        userList.add(user);
        return user;
    }

    public void removeUser(User user) {
        userList.remove(user);
    }

    public void loadStatistics(User user) {
        if (user == null) {
            return;
        }

        FileConfiguration data = user.getData();
        for (StatisticType statisticType : StatisticType.values()) {
            String path = "stats." + statisticType.name();
            int stat = data.getInt(path);
            user.setStat(statisticType, stat);
        }
    }

    public void saveStatistics(User user, boolean sync) {
        if (user == null) {
            return;
        }

        FileConfiguration data = user.getData();
        String path = "/players/" + user.getUUID();
        data.set("name", user.getName());
        for (StatisticType statisticType : StatisticType.values()) {
            data.set("stats." + statisticType.name(), user.getStat(statisticType));
        }
        if (!sync) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> ConfigUtils.saveConfig(plugin, data, path));
        } else {
            ConfigUtils.saveConfig(plugin, data, path);
        }
    }

    public Set<User> getUserList() {
        return userList;
    }

}