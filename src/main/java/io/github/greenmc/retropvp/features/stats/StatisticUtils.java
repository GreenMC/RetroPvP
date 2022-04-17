package io.github.greenmc.retropvp.features.stats;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.leaderboards.LeaderboardEntry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class StatisticUtils {

    private static final RetroPvP plugin = JavaPlugin.getPlugin(RetroPvP.class);

    public static void getStats(StatisticType stat, Consumer<List<LeaderboardEntry>> consumer) {
        if (stat == null | consumer == null) {
            return;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File base = new File(plugin.getDataFolder() + "/players/");
            List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();

            List<File> users;
            try {
                users = new ArrayList<>(Arrays.asList(base.listFiles()));
            } catch (NullPointerException ignored) {
                plugin.getLogger().warning("Hiç kayıtlı flat oyuncu istatistiği yok.");
                consumer.accept(leaderboardEntries);
                return;
            }

            for (File file : users) {
                FileConfiguration data = YamlConfiguration.loadConfiguration(file);
                String name = data.getString("name");
                int value = data.getInt("stats." + stat.name());
                LeaderboardEntry entry = new LeaderboardEntry(name, value);
                leaderboardEntries.add(entry);
            }
            consumer.accept(leaderboardEntries);
        });
    }

}
