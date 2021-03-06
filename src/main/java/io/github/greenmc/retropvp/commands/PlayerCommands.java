package io.github.greenmc.retropvp.commands;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.utils.Utils;
import me.despical.commandframework.Command;
import me.despical.commandframework.CommandArguments;
import org.bukkit.entity.Player;

public class PlayerCommands {

	private final RetroPvP plugin;

	public PlayerCommands(RetroPvP plugin) {
		this.plugin = plugin;

		plugin.getCommandFramework().registerCommands(this);
	}

	@Command(
		name = "stats",
		aliases = {"istatistikler", "istatistik", "istatistiklerim"},
		senderType = Command.SenderType.PLAYER
	)
	public void statsCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;

		if (target == null) {
			arguments.sendMessage(Utils.getMessage("player-not-found", sender));
			return;
		}

		if ((!target.equals(sender) && Utils.getBoolean("other-stats-require-permission")) && !arguments.hasPermission("retropvp.otherstats")) {
			arguments.sendMessage(Utils.getMessage("stats-command.cannot-check", sender));
			return;
		}

		String message = Utils.getMessage("stats-command.command", sender).replace("%statplayer%", target.getName());

		for (StatsStorage.StatisticType stat : StatsStorage.StatisticType.values()) {
			message = message.replace("%" + stat.getName() + "%", Integer.toString(StatsStorage.getUserStats(target, stat)));
		}

		arguments.sendMessage(message);
	}

	@Command(
		name = "ping",
		max = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void pingCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;

		if (target == null) {
			arguments.sendMessage(Utils.getMessage("player-not-found", sender));
			return;
		}

		if ((!target.equals(sender) && Utils.getBoolean("other-ping-require-permission")) && !arguments.hasPermission("retropvp.otherping")) {
			arguments.sendMessage(Utils.getMessage("ping-command.cannot-check", sender));
			return;
		}

		String message = Utils.getMessage("ping-command.command", sender)
			.replace("%pingplayer%", target.getName())
			.replace("%ping%", Integer.toString(target.spigot().getPing()));

		arguments.sendMessage(message);
	}

}