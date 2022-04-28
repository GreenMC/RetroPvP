package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import io.github.greenmc.retropvp.utils.Utils;
import me.despical.commons.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener extends ListenerAdapter {

	private final ItemStack healthSkull;

	public PlayerListener(RetroPvP plugin) {
		super(plugin);
		this.healthSkull = ItemUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZhMTA2YmQ3YzViZDNmZDA2ZDkwOGRmZjFjNzczMjVjNTIxZGM4NzM1YzAxYWFkZTc3N2YwNTY0MjFhZDkyOSJ9fX0=");
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		User victimUser = userManager.getUser(victim);
		victimUser.addStat(StatsStorage.StatisticType.DEATHS, 1);

		e.setKeepInventory(true);

		plugin.getServer().getScheduler().runTask(plugin, () -> {
			victim.spigot().respawn();
			victim.teleport(spawnManager.getSpawn());
		});

		int killStreak = victimUser.getStat(StatsStorage.StatisticType.KILL_STREAK);

		if (killStreak > victimUser.getStat(StatsStorage.StatisticType.MAX_STREAK)) {
			victimUser.setStat(StatsStorage.StatisticType.MAX_STREAK, killStreak);
		}

		victimUser.setStat(StatsStorage.StatisticType.KILL_STREAK, 0);

		Player killer = victim.getKiller();

		if (killer != null && !killer.equals(victim)) {

			Item item = victim.getWorld().dropItem(victim.getLocation(), healthSkull);
			item.setCustomName("§cCan Kiti");
			item.setCustomNameVisible(true);

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
	public void onPickup(PlayerPickupItemEvent e) {
		if (e.getItem().getItemStack().equals(healthSkull)) {
			e.setCancelled(true);
			e.getItem().remove();
			Utils.healPlayer(e.getPlayer());
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