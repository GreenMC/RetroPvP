package io.github.greenmc.retropvp.features.spawn;

import io.github.greenmc.retropvp.RetroPvP;
import me.despical.commons.configuration.ConfigUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnManager {

	private final RetroPvP plugin;

	private FileConfiguration spawnFile;
	private Location spawn;

	public SpawnManager(RetroPvP plugin) {
		this.plugin = plugin;
		reloadSpawn();
	}

	public void loadSpawn() {
		spawn = null;
		try {
			spawn = (Location) spawnFile.get("spawn");
		} catch (NullPointerException ignored) {
			plugin.getLogger().warning("Spawn location does not exist.");
		}
		if (spawn == null) {
			spawn = plugin.getServer().getWorlds().get(0).getSpawnLocation();
		}
	}

	public void reloadSpawn() {
		spawnFile = ConfigUtils.getConfig(plugin, "spawn");
		loadSpawn();
	}

	public void save() {
		spawnFile.set("spawn", spawn);
		ConfigUtils.saveConfig(plugin, spawnFile, "spawn");
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

}