/*
 * @author     kazu0617
 * @license    MIT
 * @copyright  Copyright kazu0617 2015
 */
package com.github.smk7758.TP_Portal;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;

/**
 * 単独実行(Util不使用)のみ
 */
public class ConsoleLog {
	private Main main;
	public static final Logger log = Logger.getLogger("Minecraft");

	public ConsoleLog(Main instance) {
		main = instance;
	}

	/**
	 * コンソール(Log)にメッセージを送る。
	 * (動作に支障が無く情報を出すだけで良い時)
	 *
	 * @param msg メッセージ
	 */
	public void info(String msg) {
		log.info(main.cPrefix + msg);
	}

	/**
	 * コンソール(Log)にデバッグメッセージを送る。
	 *
	 * @param msg メッセージ
	 */
	public void debug(String msg) {
		if (main.DebugMode) log.info(main.cPrefix + "[Debug] " + msg);
	}

	/**
	 * コンソール(Log)に警告メッセージを送る。
	 * (動作に支障がある時)
	 *
	 * @param msg メッセージ
	 */
	public void warn(String msg) {
		log.warning(main.cPrefix + msg);
	}

	/**
	 * メッセージを送る。
	 *
	 * @param sender 宛先
	 * @param msg メッセージ
	 * @param mode 0 mainPrefix([mainName]) 普通のメッセージ
	 * @param mode 1 cPrefix pInfo コンソールへのメッセージ
	 * @param mode 2 cPrefix pError エラーメッセージ
	 * @param mode 3 cPrefix [Debug] デバッグメッセージ
	 */
	public void sendMessage(CommandSender sender, String msg, int mode) {
		if (mode == 0) sender.sendMessage(main.PluginPrefix + msg);
		if (mode == 1) sender.sendMessage(main.cPrefix + main.pInfo + msg);
		if (mode == 2) sender.sendMessage(main.cPrefix + main.pError + msg);
		if (mode == 3 && main.DebugMode) sender.sendMessage(main.cPrefix + "[Debug]" + msg);
	}

	public void sendPermissionErrorMessage(CommandSender sender, String permission) {
		sender.sendMessage(main.cPrefix + main.pError + "You don't have Permission.");
		if (main.DebugMode) sender.sendMessage(main.cPrefix + "[Debug] Permission:" + permission);
	}

	public void sendBroadCast(String msg) {
		main.getServer().broadcastMessage(main.PluginPrefix + msg);
	}
}
