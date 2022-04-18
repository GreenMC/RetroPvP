package io.github.greenmc.retropvp.features.scoreboard;

import io.github.greenmc.retropvp.RetroPvP;
	import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.scoreboard.common.EntryBuilder;
import me.despical.commons.scoreboard.type.Entry;
import me.despical.commons.scoreboard.type.Scoreboard;
import me.despical.commons.scoreboard.type.ScoreboardHandler;
import me.despical.commons.util.Collections;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ScoreboardManager {

	int mode;

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
				return "plugin.getChatManager().message(Scoreboard.Title)";
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
		List<String> lines;

		if (mode == 3) mode = 0;
		mode++;

		switch (mode) {
			case 1:
				lines = Collections.listOf("a");
				break;
			case 2:
				lines = Collections.listOf("b");
				break;
			case 3:
				lines = Collections.listOf("c");
				break;
			default:
				lines = Collections.listOf("d");
				break;
		}

		for (String line : lines) {
			builder.next(formatScoreboardLine(line, player));
		}

		return builder.build();
	}

	private String formatScoreboardLine(String line, Player player) {
		String formattedLine = line;

		formattedLine = StringUtils.replace(formattedLine, "%%", "");

		return "";
	}
}