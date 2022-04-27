package io.github.greenmc.retropvp.listeners.animations;

import io.github.greenmc.retropvp.RetroPvP;
import me.despical.commons.item.ItemUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Despical
 * <p>
 * Created at 27.04.2022
 */
public class HealingAnimation {

	private BukkitTask task;

	private final ArmorStand stand;

	public HealingAnimation(Location origin) {
		this.stand = (ArmorStand) origin.getWorld().spawnEntity(origin.add(0, 1.5, 0), EntityType.ARMOR_STAND);
		this.stand.setGravity(false);
		this.stand.setVisible(false);
		this.stand.setHelmet(ItemUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZhMTA2YmQ3YzViZDNmZDA2ZDkwOGRmZjFjNzczMjVjNTIxZGM4NzM1YzAxYWFkZTc3N2YwNTY0MjFhZDkyOSJ9fX0="));
	}

	public void start(RetroPvP plugin) {
		task = new BukkitRunnable() {

			@Override
			public void run() {
				double angle = 0;
				boolean forward = true;

				if (forward) {
					angle += .15;
				} else {
					angle -= .15;
				}

				if (angle == 360) {
					forward = false;
				} else if (angle == 0){
					forward = true;
				}

				stand.setHeadPose(stand.getHeadPose().add(0, angle, 0));
			}
		}.runTaskTimer(plugin, 1, 1);
	}
}