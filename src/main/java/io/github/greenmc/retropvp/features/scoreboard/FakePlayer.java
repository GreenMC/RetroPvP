package io.github.greenmc.retropvp.features.scoreboard;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class FakePlayer implements OfflinePlayer {

	private final String name;

	public FakePlayer(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public boolean isOnline() {
		return true;
	}

	@Override
	public UUID getUniqueId() {
		return UUID.randomUUID();
	}

	@Override
	public boolean isBanned() {
		return false;
	}

	@Override
	public void setBanned(boolean b) {}

	@Override
	public boolean isWhitelisted() {
		return false;
	}

	@Override
	public void setWhitelisted(boolean b) {}

	@Override
	public Player getPlayer() {
		return null;
	}

	@Override
	public long getFirstPlayed() {
		return 0;
	}

	@Override
	public long getLastPlayed() {
		return 0;
	}

	@Override
	public boolean hasPlayedBefore() {
		return false;
	}

	@Override
	public Location getBedSpawnLocation() {
		return null;
	}

	@Override
	public Map<String, Object> serialize() {
		return null;
	}

	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public void setOp(boolean b) {}

	@Override
	public String toString() {
		return "FakePlayer{" +
			"name='" + name + '}';
	}

}