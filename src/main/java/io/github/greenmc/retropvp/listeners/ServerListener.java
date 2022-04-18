package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;

public class ServerListener extends ListenerAdapter {

	public ServerListener(RetroPvP plugin) {
		super(plugin);
	}

	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if (e.getEntity() instanceof Monster) e.setCancelled(true);
	}

}