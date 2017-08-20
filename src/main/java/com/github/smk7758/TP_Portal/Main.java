package com.github.smk7758.TP_Portal;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public Util util = new Util(this);
	public ConsoleLog cLog = new ConsoleLog(this);

	public String PluginName = getDescription().getName();
	public String folder = getDataFolder() + File.separator;
	public boolean DebugMode = false;
	public String PluginPrefix = "[" + ChatColor.GREEN + PluginName + ChatColor.RESET + "] ";
	public String cPrefix = "[" + PluginName + "] ";
	public String pInfo = "[" + ChatColor.RED + "Info" + ChatColor.RESET + "] ";
	public String pError = "[" + ChatColor.RED + "ERROR" + ChatColor.RESET + "] ";

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {

	}

	public void tpPlayerToPortal(Player player, String portal_name) {
		Location loc_portal = util.getLocation();
		if (player.getLocation().getX() == 0 && player.getLocation().getY() == 0 && player.getLocation().getZ() == 0) {
			player.teleport(loc_portal);
		}
	}

	public boolean checkPortal(Sign sign) {
		//ゴリ押しだが、こういくのしかないのかぁと思ってやってしまった。
		//とりあえず、Block型にしてそこから型にはめてみた。
		org.bukkit.material.Sign s = (org.bukkit.material.Sign) sign.getBlock().getState().getData();
		BlockFace b = BlockFace.EAST;
		System.out.println(s.getFacing().toString());
		Block attachedBlock = sign.getBlock().getRelative(s.getAttachedFace());
		int attachedBlock_loc[] = {attachedBlock.getX(), attachedBlock.getY(), attachedBlock.getZ()};
		return false;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		System.out.println("Pass: click");
		if (block != null && (block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)
				|| block.getType().equals(Material.SIGN))) {
			System.out.println("Pass: sign");
			Sign sign = (Sign)block.getState();
			if (sign.getLine(0).equalsIgnoreCase("[Portal]")) {
				System.out.println("Pass: [Portal]");
				String portal_name = sign.getLine(1);
				if (portal_name != null && !portal_name.isEmpty()) {
					checkPortal(sign);
					if (getConfig().contains(portal_name)) { //containsをしっかりと判断するものに変更すべき。
						if (checkPortal(sign)) {
							System.out.println("Fin");
						} else {

						}
						tpPlayerToPortal(player, portal_name);
					} else {
						//存在しなかったので、作成する。
						//if (getConfig().getString(""))
					}
				} else {
					cLog.sendMessage(player, "Please, write the portal name.", 0);;
				}
			}
		}

	}
}
