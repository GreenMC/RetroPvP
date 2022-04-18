package io.github.greenmc.retropvp.features.scoreboard;

import io.github.greenmc.retropvp.features.leaderboards.LeaderboardEntry;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

	private final Scoreboard mainScoreboard;

	public ScoreboardManager() {
		this.mainScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	}

	public Scoreboard getMainScoreboard() {
		return mainScoreboard;
	}

	public void refresh() {
		for (Objective objective : mainScoreboard.getObjectives()) {
			if (objective.getName().equals("leaderboard")) {
				objective.unregister();
				break;
			}
		}
		Objective objective = mainScoreboard.registerNewObjective("leaderboard", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("§a§lTOP " + Leaderboards.getTopKills().size());
		for (LeaderboardEntry entry : Leaderboards.getTopKills()) {
			Score score = objective.getScore(new FakePlayer(entry.getName()));
			score.setScore(entry.getValue());
		}
	}

}