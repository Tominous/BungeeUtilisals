package com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands;

import java.util.UUID;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Redisfind extends Command {
	
	public Redisfind() {
		super("find");
	}

	private static BungeeUtilisals instance = BungeeUtilisals.getInstance();
		
	public static void executeFindCommand(CommandSender sender, String[] args){
		find(sender, args);
	}
	
	private static void find(CommandSender sender, String[] args){
		if(args.length == 1){
			UUID uuid = BungeeUtilisals.getInstance().getRedisManager().getRedis().getUuidFromName(args[0]);
			if(uuid == null){
				sender.sendMessage(new TextComponent(instance.getConfig().getString("Main-messages.offline-player").replace("&", "�")));
			} else {
				String server = BungeeUtilisals.getInstance().getRedisManager().getRedis().getServerFor(uuid).getName();
				sender.sendMessage(new TextComponent(instance.getConfig().getString("Main-messages.find-message")
						.replace("&", "�").replace("%server%", server)
						.replace("%player%", sender.getName()).replace("%target%", args[0])));
			}
		} else {
			sender.sendMessage(new TextComponent(instance.getConfig().getString("Main-messages.use-find").replace("&", "�")));
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
