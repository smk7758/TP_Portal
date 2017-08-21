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
	}

	public void onDisable() {

	}

	public Location getPortalLocation(String path) {
		World pl_world = getServer().getWorld(getConfig().getString(path + ".World"));
		double pl_x = getConfig().getDouble(path + ".X");
		double pl_y = getConfig().getDouble(path + ".Y");
		double pl_z = getConfig().getDouble(path + ".Z");
		float pl_Yaw = (float) getConfig().getDouble(path + ".Yaw");
		float pl_Pitch = (float) getConfig().getDouble(path + ".Pitch");
		return new Location(pl_world, pl_x, pl_y, pl_z, pl_Yaw, pl_Pitch);
	}

	public void tpPlayerToPortal(Player player, String portal_name) {
		Location loc_portal = getPortalLocation("XXXXX"); // みかん味
		if (player.getLocation().getBlockX() == 0 && player.getLocation().getBlockY() == 0 && player.getLocation().getBlockZ() == 0) {
			player.teleport(loc_portal);
		}
	}

	public boolean checkPortal(Sign sign) {
		//ゴリ押しだが、こういくのしかないのかぁと思ってやってしまった。
		//とりあえず、Block型にしてそこから型にはめてみた。
		org.bukkit.material.Sign s = (org.bukkit.material.Sign) sign.getBlock().getState().getData();
		Block attachedBlock = sign.getBlock().getRelative(s.getAttachedFace());
		//起点を取得。左上とする。
		int starting_point_loc[] = {attachedBlock.getX(), attachedBlock.getY()+2, attachedBlock.getZ()};
//		int decrease_number_x = 0;
//		if (s.getFacing().equals(BlockFace.NORTH)) decrease_number_x = 0;
//		else if (s.getFacing().equals(BlockFace.EAST)) decrease_number_x = 0;
//		else if (s.getFacing().equals(BlockFace.SOUTH)) decrease_number_x = 0;
//		else if (s.getFacing().equals(BlockFace.WEST)) decrease_number_x = 0;
		World world = sign.getWorld();
		//Y軸方向
		for (int i=0; i<5; i++) {
			//X軸方向
			for (int j=0; j<4; j++) {
				Location loc0 = new Location(world, starting_point_loc[0]+j, starting_point_loc[1]-i, starting_point_loc[2]);
				cLog.debug(loc0.toString());
				if (!loc0.getBlock().getType().equals(Material.IRON_BLOCK)) return false;
			}
		}
		return true;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		//看板かの判別
		if (block == null || !(block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN))) return;
		cLog.debug("Pass: sign");
		Sign sign = (Sign)block.getState();
		//[Portal] の判別]
		if (!sign.getLine(0).equalsIgnoreCase("[Portal]")) return;
		cLog.debug("Pass: [Portal]");
		String portal_name = sign.getLine(1);
		if (portal_name == null || portal_name.isEmpty()) {
			cLog.sendMessage(player, "Please, write the portal name.", 0);
			return;
		}
//		if (getConfig().contains(portal_name)) { //containsをしっかりと判断するものに変更すべき。
			if (checkPortal(sign)) {
				cLog.debug("Pass: checkPortal");
				tpPlayerToPortal(player, portal_name);
			} else {
				//ポータルの基準を満たしていない。
				cLog.sendMessage(player, "The portal is not good.", 2);
			}
//		} else {
//			//存在しなかったので、作成する。
//			//if (getConfig().getString(""))
//		}
	}
}
