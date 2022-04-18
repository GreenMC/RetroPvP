package io.github.greenmc.retropvp.features.scoreboard;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.features.leaderboards.LeaderboardEntry;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import io.github.greenmc.retropvp.user.User;
import io.github.greenmc.retropvp.utils.Utils;
import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.scoreboard.type.Entry;
import me.despical.commons.scoreboard.type.Scoreboard;
import me.despical.commons.scoreboard.type.ScoreboardHandler;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ScoreboardManager {

	private int mode = 1;

	private final RetroPvP plugin;
	private final Set<Scoreboard> scoreboards;

	public ScoreboardManager(RetroPvP plugin) {
		this.plugin = plugin;
		this.scoreboards = new HashSet<>();
	}

	public void createScoreboard(Player player) {
		Scoreboard scoreboard = ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {

			@Override
			public String getTitle(Player player) {
				return Utils.getMessage("scoreboard.titles." + mode);
			}

			@Override
			public List<Entry> getEntries(Player player) {
				return formatScoreboard(player);
			}
		});

		scoreboard.setUpdateInterval(2400); // 2 min
		scoreboard.activate();
		scoreboards.add(scoreboard);
	}

	public void removeScoreboard(Player player) {
		for (Scoreboard board : scoreboards) {
			if (board.getHolder().equals(player)) {
				scoreboards.remove(board);
				board.deactivate();
				return;
			}
		}
	}

	public void stopAllScoreboards() {
		scoreboards.forEach(Scoreboard::deactivate);
		scoreboards.clear();
	}

	private List<Entry> formatScoreboard(Player player) {
		EntryBuilder builder = new EntryBuilder();

		if (mode == 4) mode = 1;

		switch (mode) {
			case 1:
				User user = plugin.getUserManager().getUser(player);
				for (StatsStorage.StatisticType type : StatsStorage.StatisticType.values()) {
					builder.put(Utils.getMessage("scoreboard." + type.getName(), null), user.getStat(type));
				}

				break;
			case 2:
				for (LeaderboardEntry entry : Leaderboards.getTopKills()) {
					builder.put(entry.getName(), entry.getValue());
				}
				break;
			case 3:
				for (LeaderboardEntry entry : Leaderboards.getTopStreaks()) {
					builder.put(entry.getName(), entry.getValue());
				}
				break;
		}

		mode++;
		return builder.get();
	}

}