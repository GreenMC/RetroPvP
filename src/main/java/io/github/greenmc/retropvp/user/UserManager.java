package io.github.greenmc.retropvp.user;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.user.data.FileStats;
import io.github.greenmc.retropvp.user.data.UserDatabase;
import me.despical.commons.util.LogUtils;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserManager {

    private final Set<User> users;
    private final UserDatabase database;

    public UserManager(RetroPvP plugin) {
        this.users = new HashSet<>();
        this.database = new FileStats();

        plugin.getServer().getOnlinePlayers().stream().map(this::getUser).forEach(this::loadStatistics);
    }

    public User getUser(Player player) {
        UUID uuid = player.getUniqueId();

        for (User user : users) {
            if (user.getUniqueId().equals(uuid)) {
                return user;
            }
        }

        LogUtils.log("Registering new user {0} ({1})", uuid, player.getName());

        User user = new User(player);
        loadStatistics(user);
        users.add(user);
        return user;
    }

    public Set<User> getUsers() {
    	return new HashSet<>(users);
	}

    public void loadStatistics(User user) {
        database.loadStatistics(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public UserDatabase getDatabase() {
        return database;
    }
}