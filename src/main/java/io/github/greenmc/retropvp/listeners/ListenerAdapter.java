package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.user.UserManager;
import org.bukkit.event.Listener;

public class ListenerAdapter implements Listener {

    protected final RetroPvP plugin;
    protected final UserManager userManager;

    public ListenerAdapter(RetroPvP plugin) {
        this.plugin = plugin;
        this.userManager = plugin.getUserManager();
    }

}