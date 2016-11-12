package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FindCommand extends Command {
	
	public FindCommand() {
		super("find");
	}

	private static BungeeUtilisals instance = BungeeUtilisals.getInstance();
		
	public static void executeFindCommand(CommandSender sender, String[] args){
		find(sender, args);
	}
	
	private static void find(CommandSender sender, String[] args){
		if(args.length == 1){
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(Utils.format(instance.getConfig().getString("Main-messages.offline-player").replace("&", "§")));
			} else {
				String server = target.getServer().getInfo().getName();
				sender.sendMessage(Utils.format(instance.getConfig().getString("Main-messages.find-message").replace("&", "§").replace("%server%", server).replace("%player%", sender.getName()).replace("%target%", target.getName())));
			}
		} else {
			sender.sendMessage(Utils.format(instance.getConfig().getString("Main-messages.use-find").replace("&", "§")));
		}
	}

	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeFindCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.find", "find", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.find") || sender.hasPermission("butilisals.*")){
			executeFindCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
