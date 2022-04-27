package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener extends ListenerAdapter {

	public PlayerListener(RetroPvP plugin) {
		super(plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		player.teleport(spawnManager.getSpawn());

		User user = userManager.getUser(player);
		user.createScoreboard();
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		User user = userManager.getUser(event.getPlayer());
		user.removeScoreboard();
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

		int killStreak = user.getStat(StatsStorage.StatisticType.KILL_STREAK);

		if (killStreak > user.getStat(StatsStorage.StatisticType.MAX_STREAK)) {
			user.setStat(StatsStorage.StatisticType.MAX_STREAK, killStreak);
		}

		user.setStat(StatsStorage.StatisticType.KILL_STREAK, 0);

		Player killer = victim.getKiller();

		if (killer != null && !killer.equals(victim)) {
			User killerUser = userManager.getUser(killer);
			killerUser.addStat(StatsStorage.StatisticType.KILLS, 1);
			killerUser.addStat(StatsStorage.StatisticType.KILL_STREAK, 1);

			killStreak = killerUser.getStat(StatsStorage.StatisticType.KILL_STREAK);

			if (killStreak > killerUser.getStat(StatsStorage.StatisticType.MAX_STREAK)) {
				killerUser.setStat(StatsStorage.StatisticType.MAX_STREAK, killStreak);
			}
		}
	}

	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlockPlaced().getType().equals(Material.FIRE)) {
			ItemStack item = e.getItemInHand();
			if (item.getDurability() != 64) {
				item.setDurability((short) (item.getDurability() + 16));
			}
			plugin.getServer().getScheduler().runTaskLater(plugin, () -> e.getBlockPlaced().setType(Material.AIR), 140);
		}
	}

}