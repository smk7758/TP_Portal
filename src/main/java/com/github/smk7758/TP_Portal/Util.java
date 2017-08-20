package com.github.smk7758.TP_Portal;

import org.bukkit.Location;
import org.bukkit.World;

public class Util {
	private Main main;
	String path = null;

	public Util(Main instance) {
		main = instance;
	}

	public Location getLocation() {
		World pl_world = main.getServer().getWorld(main.getConfig().getString(path + ".World"));
		double pl_x = main.getConfig().getDouble(path + ".X");
		double pl_y = main.getConfig().getDouble(path + ".Y");
		double pl_z = main.getConfig().getDouble(path + ".Z");
		float pl_Yaw = (float) main.getConfig().getDouble(path + ".Yaw");
		float pl_Pitch = (float) main.getConfig().getDouble(path + ".Pitch");
		return new Location(pl_world, pl_x, pl_y, pl_z, pl_Yaw, pl_Pitch);
	}
}
