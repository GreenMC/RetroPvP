package io.github.greenmc.retropvp.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class PlayerListener extends ListenerAdapter {

	private static ItemStack skull;

    public PlayerListener(RetroPvP plugin) {
        super(plugin);
		prepareSkull();
    }

	private void prepareSkull() {
		skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();

		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/e6a106bd7c5bd3fd06d908dff1c77325c521dc8735c01aade777f056421ad929").getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField;
		try
		{
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		}
		catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		skull.setItemMeta(meta);
	}

	// TODO: REMOVE THIS, JUST FOR TESTING PURPOSES
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().equals("kafa")) {
			ItemStack item = skull.clone();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§a§lCan Kiti");
			item.setItemMeta(meta);
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				Item entity = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), item);
				entity.setPickupDelay(Integer.MAX_VALUE);
			});
			e.getPlayer().getInventory().addItem(item);
		}
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