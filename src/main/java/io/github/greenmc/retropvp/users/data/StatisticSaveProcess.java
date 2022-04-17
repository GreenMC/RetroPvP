package io.github.greenmc.retropvp.users.data;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.leaderboards.LeaderboardManager;
import io.github.greenmc.retropvp.utils.Utils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class StatisticSaveProcess extends BukkitRunnable {

    private final RetroPvP plugin;

    public StatisticSaveProcess(RetroPvP plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getLogger().info("Oyuncu verilerinin otomatik kayıt süreci başlatılıyor...");
        int i = plugin.getConfig().getInt("database.auto-save-interval");
        long time = (i * 60L) * 20;
        runTaskTimerAsynchronously(plugin, time, time);
    }

    @Override
    public void run() {
        ConsoleCommandSender console = plugin.getServer().getConsoleSender();
        console.sendMessage(Utils.getMessage("saving-statistics", console));
        plugin.saveAllUserStatistics(false);
        LeaderboardManager leaderboardManager = plugin.getLeaderboardManager();
        leaderboardManager.sortEveryLeaderboard();
        console.sendMessage(Utils.getMessage("main-command.saved-stats", console));
    }

}