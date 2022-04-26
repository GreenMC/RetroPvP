package io.github.greenmc.retropvp.user.scoreboard;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import io.github.greenmc.retropvp.user.User;
import io.github.greenmc.retropvp.utils.Utils;
import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.scoreboard.common.EntryBuilder;
import me.despical.commons.scoreboard.type.Entry;
import me.despical.commons.scoreboard.type.Scoreboard;
import me.despical.commons.scoreboard.type.ScoreboardHandler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;

/**
 * @author Despical
 * <p>
 * Created at 25.04.2022
 */
public class ScoreboardManager {

	private final User user;
	private final RetroPvP plugin;

	private int mode = 1;
	private Scoreboard scoreboard;
	private BukkitTask task;

	public ScoreboardManager(User user, RetroPvP plugin) {
		this.user = user;
		this.plugin = plugin;
	}

	public void createScoreboard() {
		scoreboard = ScoreboardLib.createScoreboard(user.getPlayer()).setHandler(new ScoreboardHandler() {

			@Override
			public String getTitle(Player player) {
				return Utils.getMessage("scoreboard.title-" + mode);
			}

			@Override
			public List<Entry> getEntries(Player player) {
				return formatScoreboard();
			}
		});

		task = new BukkitRunnable() {

			@Override
			public void run() {
				mode++;

				if (mode == 4) mode = 1;
			}

		}.runTaskLaterAsynchronously(plugin, 2400);

		scoreboard.setUpdateInterval(20);
		scoreboard.activate();
	}

	public void removeScoreboard() {
		if (scoreboard == null) return;

		scoreboard.deactivate();
		scoreboard = null;

		if (task != null) {
			task.cancel();
			task = null;
		}

		mode = 1;
	}

	private List<Entry> formatScoreboard() {
		EntryBuilder builder = new EntryBuilder();

		switch (mode) {
			case 1:
				for (StatsStorage.StatisticType type : StatsStorage.StatisticType.values()) {
					builder.next(Utils.getMessage("scoreboard." + type.getName(), null), user.getStat(type));
				}

				break;
			case 2:
				for (Map.Entry<String, Integer> entry : Leaderboards.getTopKills()) {
					builder.next(entry.getKey(), entry.getValue());
				}
				break;
			case 3:
				for (Map.Entry<String, Integer> entry : Leaderboards.getTopStreaks()) {
					builder.next(entry.getKey(), entry.getValue());
				}

				break;
		}

		return builder.buildRaw();
	}

	public void switchMode(int mode) {
		this.mode = mode;
	}
}