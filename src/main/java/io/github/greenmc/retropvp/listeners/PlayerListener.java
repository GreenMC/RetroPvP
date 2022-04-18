package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener extends ListenerAdapter {

    public PlayerListener(RetroPvP plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
		plugin.getScoreboardManager().createScoreboard(player);
        userManager.getUser(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
		plugin.getScoreboardManager().removeScoreboard(player);
        User user = userManager.getUser(player);

        userManager.getDatabase().saveAllStatistic(user);
        userManager.removeUser(user);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        User user = userManager.getUser(e.getEntity());
        User killer = userManager.getUser(e.getEntity().getKiller());

        e.setKeepInventory(true);
        Bukkit.getScheduler().runTask(plugin, () -> user.getPlayer().spigot().respawn());
        user.addStat(StatsStorage.StatisticType.DEATHS, 1);

		if (user.getStat(StatsStorage.StatisticType.KILL_STREAK) > user.getStat(StatsStorage.StatisticType.MAX_STREAK)) {
			user.setStat(StatsStorage.StatisticType.MAX_STREAK, user.getStat(StatsStorage.StatisticType.KILL_STREAK));
		}
        user.setStat(StatsStorage.StatisticType.KILL_STREAK, 0);

        killer.addStat(StatsStorage.StatisticType.KILLS, 1);
        killer.addStat(StatsStorage.StatisticType.KILL_STREAK, 1);

    }

}