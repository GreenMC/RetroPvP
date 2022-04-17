package io.github.greenmc.retropvp.commands;

import io.github.greenmc.retropvp.RetroPvP;
import me.despical.commandframework.Command;
import me.despical.commandframework.CommandArguments;
import me.despical.commons.number.NumberUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

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
		permission = "retropvp.admin",
		senderType = Command.SenderType.PLAYER
	)
	public void gmcCommand(CommandArguments arguments) {
		Player player = arguments.getSender();
		player.setGameMode(GameMode.CREATIVE);
		player.sendMessage("Game mode has been updated to CREATIVE.");
	}

	@Command(
		name = "gms",
		permission = "retropvp.admin",
		senderType = Command.SenderType.PLAYER
	)
	public void gmsCommand(CommandArguments arguments) {
		Player player = arguments.getSender();
		player.setGameMode(GameMode.SURVIVAL);
		player.sendMessage("Game mode has been updated to SURVIVAL.");
	}

	@Command(
		name = "gmsp",
		permission = "retropvp.admin",
		senderType = Command.SenderType.PLAYER
	)
	public void gmspCommand(CommandArguments arguments) {
		Player player = arguments.getSender();
		player.setGameMode(GameMode.SPECTATOR);
		player.sendMessage("Game mode has been updated to SURVIVAL.");
	}

	@Command(
		name = "fly",
		permission = "retropvp.admin",
		max = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void flyCommand(CommandArguments arguments) {
		Player target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : arguments.getSender();
		target.sendMessage("Flying mode " + (target.getAllowFlight() ? "disabled" : "enabled") + " for: " + target.getName());
		target.setAllowFlight(!target.getAllowFlight());
	}

	@Command(
		name = "flyspeed",
		permission = "retropvp.admin",
		min = 1,
		max = 2,
		senderType = Command.SenderType.PLAYER
	)
	public void flySpeedCommand(CommandArguments arguments) {
		int speed = arguments.getArgumentAsInt(0);
		Player player = arguments.getSender();

		if (!NumberUtils.isBetween(speed, 1, 10)) {
			arguments.sendMessage("Speed value must be between 1 and 10!");
			return;
		}

		player.setFlySpeed(speed / 10f);
		player.sendMessage("Fly speed set to " + speed);
	}

	@Command(
		name = "heal",
		permission = "retropvp.admin",
		min = 1,
		max = 2,
		senderType = Command.SenderType.PLAYER
	)
	public void healCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;
		target.sendMessage("You have been healed.");

		if (target != sender) {
			arguments.sendMessage(target.getName() + " have been healed.");
		}
	}

	@Command(
		name = "feed",
		permission = "retropvp.admin",
		min = 1,
		max = 2,
		senderType = Command.SenderType.PLAYER
	)
	public void feedCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;
		target.sendMessage("Your hunger has been filled.");

		if (target != sender) {
			arguments.sendMessage(target.getName() + "'s hunger has been filled.");
		}
	}
}