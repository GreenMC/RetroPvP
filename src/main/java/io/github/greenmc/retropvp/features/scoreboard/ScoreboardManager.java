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

import java.util.*;

public class ScoreboardManager {

	private final Map<String, Integer> modes = new HashMap<>();

	private final RetroPvP plugin;
	private final Set<Scoreboard> scoreboards;

	public ScoreboardManager(RetroPvP plugin) {
		this.plugin = plugin;
		this.scoreboards = new HashSet<>();
	}

	public void createScoreboard(Player player) {
		modes.put(player.getName(), 1);
		Scoreboard scoreboard = ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {

			@Override
			public String getTitle(Player player) {
				return Utils.getMessage("scoreboard.titles." + modes.get(player.getName()));
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
		String name = player.getName();

		int mode = modes.get(name);
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

		modes.put(name, ++mode);
		if (modes.get(name) == 3) modes.put(name, 1);
		return builder.get();
	}

}