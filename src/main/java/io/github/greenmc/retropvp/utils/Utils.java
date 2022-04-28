package io.github.greenmc.retropvp.utils;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.features.language.LanguageManager;
import io.github.greenmc.retropvp.features.placeholders.CustomPlaceholderManager;
import me.despical.commons.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	private Utils() {}

	private static final RetroPvP plugin = JavaPlugin.getPlugin(RetroPvP.class);
	private static final CustomPlaceholderManager placeholderManager = plugin.getCustomPlaceholderManager();
	private static final LanguageManager languageManager = plugin.getLanguageManager();

	private static final boolean isPAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

	public static void noPermission(CommandSender sender) {
		sender.sendMessage(getMessage("no-permission", sender));
	}

	public static String getMessage(String string) {
		return getString(languageManager.getLanguage(), "messages." + string, null, false, false);
	}

	public static List<String> getStringList(String path) {
		return languageManager.getLanguage().getStringList(path);
	}

	public static List<String> getMessageList(String string, CommandSender from) {
		List<String> strings = new ArrayList<>();
		for (String value : languageManager.getLanguage().getStringList("messages." + string)) {
			strings.add(replacePlaceholders(value, from, false, false));
		}
		return strings;
	}

	public static List<String> getStringList(String string, CommandSender from) {
		List<String> strings = new ArrayList<>();
		for (String value : plugin.getConfig().getStringList(string)) {
			strings.add(replacePlaceholders(value, from, false, false));
		}
		return strings;
	}

	public static String getMessage(String string, CommandSender from) {
		return getString(languageManager.getLanguage(), "messages." + string, from);
	}

	public static String getMessage(String string, CommandSender from, boolean replacePersonalPlaceholders) {
		return getString(languageManager.getLanguage(), "messages." + string, from, replacePersonalPlaceholders);
	}

	public static String getMessage(String string, CommandSender from, boolean replacePersonalPlaceholders, boolean replacePAPI) {
		return getString(languageManager.getLanguage(), "messages." + string, from, replacePersonalPlaceholders, replacePAPI);
	}

	public static String getString(String string, CommandSender from) {
		return replacePlaceholders(plugin.getConfig().get(string), from, true);
	}

	public static String getString(String string, CommandSender from, boolean replacePersonalPlaceholders) {
		return replacePlaceholders(plugin.getConfig().get(string), from, replacePersonalPlaceholders);
	}

	public static String getString(FileConfiguration file, String string, CommandSender from) {
		return replacePlaceholders(file.get(string), from, true);
	}

	public static String getString(FileConfiguration file, String string, CommandSender from, boolean replacePersonalPlaceholders) {
		return replacePlaceholders(file.get(string), from, replacePersonalPlaceholders);
	}

	public static String getString(FileConfiguration file, String string, CommandSender from, boolean replacePersonalPlaceholders, boolean replacePAPI) {
		return replacePlaceholders(file.get(string), from, replacePersonalPlaceholders, replacePAPI);
	}

	public static String replacePlaceholders(Object originalMsg, CommandSender from, boolean replacePersonalPlaceholders, boolean replacePAPI) {
		if (originalMsg == null) {
			plugin.getLogger().warning(org.bukkit.ChatColor.RED + "Your language file[s] is/are corrupted or old. Please reset or update them.");
			return "";
		}

		String msg = originalMsg instanceof List ? listToString((List<?>) originalMsg) : (String) originalMsg;
		msg = placeholderManager.replacePlaceholders(Strings.format(msg)).replace("%nl%", "\n");

		if (replacePersonalPlaceholders) {
			msg = msg.replace("%player%", matchName(from));
		}
		//if (isPAPIEnabled && replacePAPI) {
			//return PlaceholderAPI.setPlaceholders(from instanceof Player ? (Player) from : null, msg);
		//}
		return msg;
	}

	public static String replacePlaceholders(Object msg, CommandSender from, boolean replacePersonalPlaceholders) {
		return replacePlaceholders(msg, from, replacePersonalPlaceholders, true);
	}

	public static String getString(String string) {
		return plugin.getConfig().getString(string);
	}

	public static String matchName(CommandSender entity) {
		return entity instanceof Player ? entity.getName() : "CONSOLE";
	}

	public static boolean getBoolean(String string, boolean def) {
		return plugin.getConfig().getBoolean(string, def);
	}

	public static boolean getBoolean(String string) {
		return plugin.getConfig().getBoolean(string);
	}

	public static boolean getLanguageBoolean(String string) {
		return languageManager.getBoolean(string);
	}

	public static int getInt(String path) {
		return plugin.getConfig().getInt(path);
	}

	public static boolean getBoolean(FileConfiguration file, String string, boolean def) {
		return file.getBoolean(string, def);
	}

	public static boolean getBoolean(FileConfiguration file, String string) {
		return file.getBoolean(string);
	}

	public static int getInt(FileConfiguration file, String path) {
		return file.getInt(path);
	}

	public static float getFloat(YamlConfiguration yaml, String path) {
		return Float.parseFloat(yaml.getString(path));
	}

	public static int getLanguageInt(String path) {
		return languageManager.getLanguage().getInt(path);
	}

	public static Location getLocation(YamlConfiguration yaml, String key) {
		return (Location) yaml.get(key);
	}

	public static boolean isSameLoc(Location loc1, Location loc2) {
		return (loc1.getBlockX() == loc2.getBlockX()) && (loc1.getBlockY() == loc2.getBlockY()) && (loc1.getBlockZ() == loc2.getBlockZ());
	}

	public static String arrayToString(String[] array, CommandSender sender, boolean replacePersonalPlaceholders, boolean replacePAPI) {
		String str = String.join(" ", array);
		return replacePlaceholders(str, sender, replacePersonalPlaceholders, replacePAPI);
	}

	public static String listToString(List<?> list) {
		StringBuilder builder = new StringBuilder();
		int max = list.size() - 1;
		int i = 0;
		for (Object s : list) {
			builder.append((String) s);
			if (i != max) {
				builder.append('\n');
				i++;
			}
		}
		return builder.toString();
	}

	public static void sendActionBar(Player player, String message) {
		if (player == null || message == null) return;
		String nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
		nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);

		//1.8.x and 1.9.x
		try {
			Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
			Object craftPlayer = craftPlayerClass.cast(player);

			Class<?> ppoc = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");
			Class<?> packet = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
			Object packetPlayOutChat;
			Class<?> chat = Class.forName("net.minecraft.server." + nmsVersion + (nmsVersion.equalsIgnoreCase("v1_8_R1") ? ".ChatSerializer" : ".ChatComponentText"));
			Class<?> chatBaseComponent = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");

			Method method = null;
			if (nmsVersion.equalsIgnoreCase("v1_8_R1")) method = chat.getDeclaredMethod("a", String.class);

			Object object = nmsVersion.equalsIgnoreCase("v1_8_R1") ? chatBaseComponent.cast(method.invoke(chat, "{'text': '" + message + "'}")) : chat.getConstructor(new Class[]{String.class}).newInstance(message);
			packetPlayOutChat = ppoc.getConstructor(new Class[]{chatBaseComponent, Byte.TYPE}).newInstance(object, (byte) 2);

			Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
			Object iCraftPlayer = handle.invoke(craftPlayer);
			Field playerConnectionField = iCraftPlayer.getClass().getDeclaredField("playerConnection");
			Object playerConnection = playerConnectionField.get(iCraftPlayer);
			Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", packet);
			sendPacket.invoke(playerConnection, packetPlayOutChat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void healPlayer(Player target) {
		target.setFoodLevel(20);
		target.setHealth(20);
		target.setFireTicks(0);
	}

	public static String getChatColor(Player player) {
		return getLast(player, "chatcolor");
	}

	public static String getLast(Player player, String perm) {
		for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
			String permission = permissionAttachmentInfo.getPermission();
			if (permission.contains(perm)) {
				return permission.substring(permission.lastIndexOf(".") + 1);
			}
		}
		return "";
	}

}