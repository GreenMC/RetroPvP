package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.spawn.SpawnManager;
import io.github.greenmc.retropvp.user.UserManager;
import org.bukkit.event.Listener;

public class ListenerAdapter implements Listener {

    protected final RetroPvP plugin;
    protected final UserManager userManager;
	protected final SpawnManager spawnManager;

    public ListenerAdapter(RetroPvP plugin) {
        this.plugin = plugin;
        this.userManager = plugin.getUserManager();
		this.spawnManager = plugin.getSpawnManager();

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}