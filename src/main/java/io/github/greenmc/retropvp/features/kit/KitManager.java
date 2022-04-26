package io.github.greenmc.retropvp.features.kit;

import io.github.greenmc.retropvp.RetroPvP;
import me.despical.commons.configuration.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitManager {

	private final RetroPvP plugin;
	private final List<KitItem> items = new ArrayList<>();
	private final List<ItemStack> armors = new ArrayList<>();

	public KitManager(RetroPvP plugin) {
		this.plugin = plugin;
		load();
	}

	public void load() {
		FileConfiguration kitFile = ConfigUtils.getConfig(plugin, "kit");
		items.clear();
		armors.clear();
		for (String str : kitFile.getStringList("kit")) {
			String item = str.substring(0, str.indexOf(";"));
			int slot = Integer.parseInt(str.substring(str.indexOf(";") + 1, str.indexOf(":")));
			int amount = Integer.parseInt(str.substring(str.indexOf(":") + 1));
			items.add(new KitItem(new ItemStack(Material.valueOf(item), amount), slot));
		}
		for (String str : kitFile.getStringList("armors")) {
			armors.add(new ItemStack(Material.valueOf(str)));
		}
		Collections.reverse(armors);
	}

	public void giveKit(Player player) {
		player.getInventory().clear();
		for (KitItem item : items) {
			player.getInventory().setItem(item.getSlot(), item.getItem());
		}
		player.getInventory().setArmorContents(armors.toArray(new ItemStack[0]));
	}

}