package io.github.greenmc.retropvp.commands;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import me.despical.commandframework.Command;
import me.despical.commandframework.CommandArguments;
import me.despical.commons.number.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Despical
 * <p>
 * Created at 17.04.2022
 */
public class AdminCommands {

	private final RetroPvP plugin;

	public AdminCommands(RetroPvP plugin) {
		this.plugin = plugin;

		plugin.getCommandFramework().registerCommands(this);
	}

	@Command(
		name = "gmc",
		permission = "retropvp.gmc",
		senderType = Command.SenderType.PLAYER
	)
	public void gmcCommand(CommandArguments arguments) {
		Player player = arguments.getSender();
		player.setGameMode(GameMode.CREATIVE);
		player.sendMessage("Game mode has been updated to CREATIVE.");
	}

	@Command(
		name = "gms",
		permission = "retropvp.gms",
		senderType = Command.SenderType.PLAYER
	)
	public void gmsCommand(CommandArguments arguments) {
		Player player = arguments.getSender();
		player.setGameMode(GameMode.SURVIVAL);
		player.sendMessage("Game mode has been updated to SURVIVAL.");
	}

	@Command(
		name = "gmsp",
		permission = "retropvp.gmsp",
		senderType = Command.SenderType.PLAYER
	)
	public void gmspCommand(CommandArguments arguments) {
		Player player = arguments.getSender();
		player.setGameMode(GameMode.SPECTATOR);
		player.sendMessage("Game mode has been updated to SURVIVAL.");
	}

	@Command(
		name = "fly",
		permission = "retropvp.fly",
		max = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void flyCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;
		target.sendMessage("Flying mode " + (target.getAllowFlight() ? "disabled" : "enabled") + " for: " + target.getName());
		target.setAllowFlight(!target.getAllowFlight());
		target.setFlying(!target.isFlying());

		if (target != sender) {
			arguments.sendMessage(target.getName() + "'s flying mode has been " +  (target.getAllowFlight() ? "disabled" : "enabled"));
		}
	}

	@Command(
		name = "flyspeed",
		permission = "retropvp.flyspeed",
		min = 1,
		max = 2,
		senderType = Command.SenderType.PLAYER
	)
	public void flySpeedCommand(CommandArguments arguments) {
		int speed = arguments.getArgumentAsInt(0);

		if (!NumberUtils.isBetween(speed, 1, 10)) {
			arguments.sendMessage("Speed value must be between 1 and 10!");
			return;
		}

		Player sender = arguments.getSender(), target = arguments.getArgumentsLength() == 1 ? sender : plugin.getServer().getPlayer(arguments.getArgument(1));

		target.setFlySpeed(speed / 10f);
		target.sendMessage("Fly speed set to " + speed);

		if (target != sender) {
			arguments.sendMessage(target.getName() + "'s fly speed has been set to " + target.getFlySpeed());
		}
	}

	@Command(
		name = "heal",
		permission = "retropvp.heal",
		max = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void healCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;
		target.sendMessage("You have been healed.");
		target.setFoodLevel(20);
		target.setHealth(20);
		target.setFireTicks(0);

		if (target != sender) {
			arguments.sendMessage(target.getName() + " has been healed.");
		}
	}

	@Command(
		name = "feed",
		permission = "retropvp.feed",
		max = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void feedCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;
		target.sendMessage("Your hunger has been filled.");
		target.setFoodLevel(20);
		target.setSaturation(10);
		target.setExhaustion(0F);

		if (target != sender) {
			arguments.sendMessage(target.getName() + "'s hunger has been filled.");
		}
	}

	@Command(
		name = "refresh",
		permission = "retropvp.refresh",
		max = 1,
		senderType = Command.SenderType.BOTH
	)
	public void refreshLeaderboardsCommand(CommandArguments arguments) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			Leaderboards.refresh();
			arguments.sendMessage("Refreshed!");
		});
	}

	@Command(
		name = "s",
		permission = "retropvp.tphere",
		max = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void tpHereCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;
		target.sendMessage("You have been teleported to " + sender.getName() + ".");
		target.teleport(sender);

		if (target != sender) {
			arguments.sendMessage(target.getName() + " has been teleported to you.");
		}
	}

	@Command(
		name = "tpall",
		permission = "retropvp.tpall",
		max = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void tpAllCommand(CommandArguments arguments) {
		Player sender = arguments.getSender();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			player.teleport(sender);
		}
		sender.sendMessage("All players (" + players.size() + ") has been teleported to you.");
	}

	@Command(
		name = "retro",
		permission = "retropvp.reload",
		max = 1,
		senderType = Command.SenderType.BOTH
	)
	public void reloadCommand(CommandArguments arguments) {
		plugin.reloadConfig();
		plugin.getLanguageManager().load();
		arguments.sendMessage("Reloaded.");
	}

}