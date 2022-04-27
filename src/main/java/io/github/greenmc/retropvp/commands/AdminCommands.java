package io.github.greenmc.retropvp.commands;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import io.github.greenmc.retropvp.listeners.animations.HealingAnimation;
import io.github.greenmc.retropvp.user.User;
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
		senderType = Command.SenderType.PLAYER
	)
	public void flySpeedCommand(CommandArguments arguments) {
		int speed = arguments.getArgumentAsInt(0);

		if (!NumberUtils.isBetween(speed, 1, 10)) {
			arguments.sendMessage("Speed value must be between 1 and 10!");
			return;
		}

		Player sender = arguments.getSender(), target = arguments.isArgumentsEmpty() ? sender : plugin.getServer().getPlayer(arguments.getArgument(1));

		target.setFlySpeed(speed / 10f);
		target.sendMessage("Fly speed set to " + speed);

		if (target != sender) {
			arguments.sendMessage(target.getName() + "'s fly speed has been set to " + target.getFlySpeed());
		}
	}

	@Command(
		name = "heal",
		permission = "retropvp.heal",
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
		senderType = Command.SenderType.PLAYER
	)
	public void tpAllCommand(CommandArguments arguments) {
		Player sender = arguments.getSender();

		for (Player player : plugin.getServer().getOnlinePlayers()) {
			player.teleport(sender);
		}

		arguments.sendMessage("All players has been teleported to you.");
	}

	@Command(
		name = "spawn",
		permission = "retropvp.spawn",
		senderType = Command.SenderType.BOTH
	)
	public void spawnCommand(CommandArguments arguments) {
		Player sender = arguments.getSender(), target = !arguments.isArgumentsEmpty() ? plugin.getServer().getPlayer(arguments.getArgument(0)) : sender;
		target.teleport(plugin.getSpawnManager().getSpawn());
		target.sendMessage("You have been teleported to spawn.");

		if (target != sender) {
			arguments.sendMessage(target.getName() + " has been teleported to spawn.");
		}
	}

	@Command(
		name = "setspawn",
		permission = "retropvp.setspawn",
		senderType = Command.SenderType.PLAYER
	)
	public void setSpawnCommand(CommandArguments arguments) {
		Player player = arguments.getSender();
		player.sendMessage("Spawn has been changed.");

		plugin.getSpawnManager().setSpawn(player.getLocation());
	}

	@Command(
		name = "givekit",
		permission = "retropvp.givekit",
		min = 1,
		senderType = Command.SenderType.BOTH
	)
	public void giveKitCommand(CommandArguments arguments) {
		Player target = plugin.getServer().getPlayer(arguments.getArgument(0));
		plugin.getKitManager().giveKit(target);
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
		plugin.getSpawnManager().reloadSpawn();
		plugin.getKitManager().load();

		arguments.sendMessage("Reloaded.");
	}

	@Command(
		name = "day",
		permission = "retropvp.day",
		senderType = Command.SenderType.PLAYER
	)
	public void dayCommand(CommandArguments arguments) {
		((Player) arguments.getSender()).getWorld().setTime(1300);
	}

	@Command(
		name = "night",
		permission = "retropvp.day",
		senderType = Command.SenderType.PLAYER
	)
	public void nightCommand(CommandArguments arguments) {
		((Player) arguments.getSender()).getWorld().setTime(19000);
	}

	@Command(
		name = "board",
		permission = "retropvp.board",
		min = 1,
		senderType = Command.SenderType.PLAYER
	)
	public void boardCommand(CommandArguments arguments) {
		Player sender = arguments.getSender();
		User user = plugin.getUserManager().getUser(sender);
		user.setScoreboardMode(arguments.getArgumentAsInt(0));
	}

	@Command(
		name = "test",
		senderType = Command.SenderType.PLAYER
	)
	public void debug(CommandArguments arguments) {
		Player sender = arguments.getSender();
		HealingAnimation animation = new HealingAnimation(sender.getLocation());
		animation.start(plugin);
	}
}