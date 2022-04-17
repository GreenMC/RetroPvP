package io.github.greenmc.retropvp.commands;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import io.github.greenmc.retropvp.user.UserManager;
import io.github.greenmc.retropvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final UserManager userManager;

    public StatsCommand(RetroPvP plugin) {
        this.userManager = plugin.getUserManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player statPlayer = args.length > 0 ? Bukkit.getPlayer(args[0]) : sender instanceof Player ? (Player) sender : null;
        if (statPlayer == null) {
            sender.sendMessage(Utils.getMessage("player-not-found", sender));
            return true;
        }

        if ((!statPlayer.equals(sender) && Utils.getBoolean("other-stats-require-permission")) && !sender.hasPermission("retropvp.otherstats")) {
            sender.sendMessage(Utils.getMessage("stats-command.cannot-check", sender));
            return true;
        }

        User statUser = userManager.getUser(statPlayer);
        String message = Utils.getMessage("stats-command.command", sender).replace("%statplayer%", statPlayer.getName());

        for (StatsStorage.StatisticType stat : StatsStorage.StatisticType.values()) {
            message = message.replace("%" + stat.getName() + "%", String.valueOf(statUser.getStat(stat)));
        }

        sender.sendMessage(message);
        return true;
    }

}