package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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
		player.teleport(spawnManager.getSpawn());
		plugin.getScoreboardManager().createScoreboard(player);
        userManager.getUser(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        User user = userManager.getUser(player);

		plugin.getScoreboardManager().removeScoreboard(player);
        userManager.getDatabase().saveAllStatistic(user);
        userManager.removeUser(user);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
        User user = userManager.getUser(victim);

        e.setKeepInventory(true);
        user.addStat(StatsStorage.StatisticType.DEATHS, 1);
		plugin.getServer().getScheduler().runTask(plugin, () -> {
			victim.spigot().respawn();
			victim.teleport(spawnManager.getSpawn());
		});

		if (user.getStat(StatsStorage.StatisticType.KILL_STREAK) > user.getStat(StatsStorage.StatisticType.MAX_STREAK)) {
			user.setStat(StatsStorage.StatisticType.MAX_STREAK, user.getStat(StatsStorage.StatisticType.KILL_STREAK));
		}
        user.setStat(StatsStorage.StatisticType.KILL_STREAK, 0);

		Player killer = victim.getKiller();
		if (killer != null) {
			User killerUser = userManager.getUser(killer);
			killerUser.addStat(StatsStorage.StatisticType.KILLS, 1);
			killerUser.addStat(StatsStorage.StatisticType.KILL_STREAK, 1);
		}

    }

	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

}