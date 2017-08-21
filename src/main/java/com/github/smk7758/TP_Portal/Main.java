package com.github.smk7758.TP_Portal;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public ConsoleLog cLog = new ConsoleLog(this);

	public final String PluginName = getDescription().getName();
	public final String folder = getDataFolder() + File.separator;
	public boolean DebugMode = true;
	public String PluginPrefix = "[" + ChatColor.GREEN + PluginName + ChatColor.RESET + "] ";
	public String cPrefix = "[" + PluginName + "] ";
	public String pInfo = "[" + ChatColor.RED + "Info" + ChatColor.RESET + "] ";
	public String pError = "[" + ChatColor.RED + "ERROR" + ChatColor.RESET + "] ";

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		reloadConfig();
		DebugMode = getConfig().getBoolean("DebugMode");
	}

	public void onDisable() {
	}

	public boolean validatePortalFromConfig(String portal_name) {
		String path = "PortalList." + portal_name;
		reloadConfig();
		if (getConfig().contains(path + ".World") && getConfig().contains(path + ".X") && getConfig().contains(path + ".Y")
				&& getConfig().contains(path + ".Z")) return true;
		else return false;
	}

	public void savePortalSignLocationFromConfig(String portal_name, Sign sign) {
		String path = "PortalList." + portal_name;
		getConfig().set(path + ".World", sign.getWorld().getName());
		getConfig().set(path + ".X", sign.getX());
		getConfig().set(path + ".Y", sign.getY());
		getConfig().set(path + ".Z", sign.getZ());
		saveConfig();
	}

	// みかん味
	public Location loadPortalSignLocationFromConfig(String portal_name) {
		String path = "PortalList." + portal_name;
		reloadConfig();
		World pl_world = getServer().getWorld(getConfig().getString(path + ".World"));
		int pl_x = getConfig().getInt(path + ".X");
		int pl_y = getConfig().getInt(path + ".Y");
		int pl_z = getConfig().getInt(path + ".Z");
		return new Location(pl_world, pl_x, pl_y, pl_z);
	}

	//何に使ってるんですか！？↓
//	public void tpPlayerToPortal(Player player, Location portal_loc) {
//	}

	// todo: 方向違いに対応する。
	// todo: 空白部分に対応する。
	public boolean checkPortal(Sign sign) {
		// ゴリ押しだが、こういくのしかないのかぁと思ってやってしまった。
		// とりあえず、Block型にしてそこから型にはめてみた。
		org.bukkit.material.Sign s = (org.bukkit.material.Sign) sign.getBlock().getState().getData();
		Block attachedBlock = sign.getBlock().getRelative(s.getAttachedFace());
		// 起点を取得。左上とする。
		int starting_point_loc[] = { attachedBlock.getX(), attachedBlock.getY() + 2, attachedBlock.getZ() };
		// int decrease_number_x = 0;
		// if (s.getFacing().equals(BlockFace.NORTH)) decrease_number_x = 0;
		// else if (s.getFacing().equals(BlockFace.EAST)) decrease_number_x = 0;
		// else if (s.getFacing().equals(BlockFace.SOUTH)) decrease_number_x = 0;
		// else if (s.getFacing().equals(BlockFace.WEST)) decrease_number_x = 0;
		World world = sign.getWorld();
		// Y軸方向
		for (int i = 0; i < 5; i++) {
			// X軸方向
			for (int j = 0; j < 4; j++) {
				Location loc0 = new Location(world, starting_point_loc[0] + j, starting_point_loc[1] - i, starting_point_loc[2]);
				cLog.debug(loc0.toString());
				if (!loc0.getBlock().getType().equals(Material.IRON_BLOCK)) return false;
			}
		}
		cLog.debug("checkPortal!");
		return true;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		// 看板かの判別
		if (block == null || !(block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)
				|| block.getType().equals(Material.SIGN))) return;
		cLog.debug("Pass: sign");
		Sign sign = (Sign) block.getState();
		// [Portal] の判別]
		if (!sign.getLine(0).equalsIgnoreCase("[Portal]")) return;
		cLog.debug("Pass: [Portal]");
		String portal_name = sign.getLine(1);
		if (portal_name != null && !portal_name.isEmpty()) {
			if (checkPortal(sign)) {
				cLog.debug("Pass: checkPortal");
				if (validatePortalFromConfig(portal_name)) {
					player.teleport(loadPortalSignLocationFromConfig(portal_name));
					cLog.debug("Pass; teleport!!");
					//todo: 相手側のポータルの判定。
					return;
				} else {
					// 存在しなかったので、作成する。
					savePortalSignLocationFromConfig(portal_name, sign);
					cLog.debug("Made portal location to config.");
				}
			} else {
				// ポータルの基準を満たしていない。
				cLog.sendMessage(player, "The portal is not properly.", 2);
			}
		} else {
			cLog.sendMessage(player, "Please, write the portal name.", 0);
		}
		return;
	}
}
