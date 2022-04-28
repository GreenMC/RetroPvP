package io.github.greenmc.retropvp.listeners;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.utils.Utils;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends ListenerAdapter {

	private final Chat chat;

	public ChatListener(RetroPvP plugin) {
		super(plugin);
		this.chat = plugin.getChat();
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		Player player = e.getPlayer();
		e.setMessage("§" + Utils.getChatColor(player) + e.getMessage()
			.replace('&', ' '));
		String format = Utils.getString("chat-format", e.getPlayer())
			.replace("%display_name%", chat.getPlayerPrefix(player) + " " + player.getName())
			.replace("%message%", e.getMessage());
		plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', format));
	}

}