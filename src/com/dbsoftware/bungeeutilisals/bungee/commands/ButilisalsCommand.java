package com.dbsoftware.bungeeutilisals.bungee.commands;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.announcer.Announcements;
import com.dbsoftware.bungeeutilisals.bungee.friends.Friends;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.TPSRunnable;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ButilisalsCommand extends Command {
	
	public ButilisalsCommand() {
		super("cubedcrafta", "", "cca");{
		}
	}
		
	public static void executeButilisalsCommand(CommandSender sender, String[] args){		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("reload")){
				ReloadConfig(sender);
				return;
			} else if(args[0].equalsIgnoreCase("lag")){
				lagcommand(sender);
				return;
			} else if(args[0].equalsIgnoreCase("lockchat")){
				lockchatcommand(sender);
				return;
			}
		}
		sender.sendMessage(Utils.format("&6&l[&c&lCubedCraft&6&l] &7Command List"));
		sender.sendMessage(Utils.format("&c/cubedcraft reload&7 to reload the plugin!"));
		sender.sendMessage(Utils.format("&c/cubedcraft lag&7 to get BungeeCord Status"));
		sender.sendMessage(Utils.format("&c/cubedcraft chatlock&7 to close/open chat"));
	}
	
	private static void ReloadConfig(CommandSender sender){
		BungeeUtilisals.getInstance().reloadConfig();
	    Announcements.reloadAnnouncements();
	    Friends.reloadFriendsData();
	    Punishments.reloadPunishmentData();
		sender.sendMessage(Utils.format("&6&l[&c&lCubedCraft&6&l] &7Config reloaded!"));
	}
	
	private static void lagcommand(CommandSender sender){
		Runtime run = Runtime.getRuntime();
		long maxMemory = run.maxMemory() / 1024 / 1024;
		long totalMemory = run.totalMemory() / 1024 / 1024;
		long freeMemory = run.freeMemory() / 1024 / 1024;
		
		long uptime = ManagementFactory.getRuntimeMXBean().getStartTime();
		
		SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
		String date = df2.format(new Date(uptime));
		
		double tps = TPSRunnable.getTPS();
		
		sender.sendMessage(Utils.format("&6BungeeCord statistics:"));
		sender.sendMessage(Utils.format("&6TPS: &b" + getColor(tps) + tps));
		sender.sendMessage(Utils.format("&6Max: &b" + maxMemory + " MB"));
		sender.sendMessage(Utils.format("&6Free: &b" + freeMemory + " MB"));
		sender.sendMessage(Utils.format("&6Total: &b" + totalMemory + " MB"));
		sender.sendMessage(Utils.format("&6Online since: &b" + date));
	}
	
	private static void lockchatcommand(CommandSender sender) {
	    if (BungeeUtilisals.getInstance().chatMuted){
	    	BungeeUtilisals.getInstance().chatMuted = false;
	    	sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.Unlock")));
	    	if(BungeeUtilisals.getInstance().getConfig().getBoolean("ChatLock.Broadcast.Enabled")){
	        	for(String s : BungeeUtilisals.getInstance().getConfig().getStringList("ChatLock.Broadcast.UnLock")){
	            	ProxyServer.getInstance().broadcast(Utils.format(s));
	        	}
	        }
	    } else {
	    	BungeeUtilisals.getInstance().chatMuted = true;
	    	sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.Lock")));
	    	if(BungeeUtilisals.getInstance().getConfig().getBoolean("ChatLock.Broadcast.Enabled")){
	    		for(String s : BungeeUtilisals.getInstance().getConfig().getStringList("ChatLock.Broadcast.Lock")){
	        		ProxyServer.getInstance().broadcast(Utils.format(s));
	    		}
	    	}
	    }
	 }

	static ChatColor getColor(double d){
		ChatColor color = ChatColor.GREEN;
		if (d < 15.0D) {
			color = ChatColor.RED;
		}
	    if (d >= 15.0D) {
	    	color = ChatColor.YELLOW;
	    }
	    if (d >= 18.0D) {
	    	color = ChatColor.GREEN;
	    }
	    return color;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeButilisalsCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.admin", "butilisals", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.admin") || sender.hasPermission("butilisals.*")){
			executeButilisalsCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
