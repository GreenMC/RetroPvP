package io.github.greenmc.retropvp.features.kit;

import io.github.greenmc.retropvp.RetroPvP;
import me.despical.commons.configuration.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class KitManager {

	private final RetroPvP plugin;
	private final Set<KitItem> items = new HashSet<>();

	public KitManager(RetroPvP plugin) {
		this.plugin = plugin;
		load();
	}

	public void load() {
		FileConfiguration kitFile = ConfigUtils.getConfig(plugin, "kit");
		items.clear();
		for (String str : kitFile.getStringList("kit")) {
			String item = str.substring(0, str.indexOf(";"));
			int slot = Integer.parseInt(str.substring(str.indexOf(";") + 1, str.indexOf(":")));
			int amount = Integer.parseInt(str.substring(str.indexOf(":") + 1));
			items.add(new KitItem(new ItemStack(Material.valueOf(item), amount), slot));
		}
	}

	public void giveKit(Player player) {
		player.getInventory().clear();
		for (KitItem item : items) {
			player.getInventory().setItem(item.getSlot(), item.getItem());
		}
	}

}