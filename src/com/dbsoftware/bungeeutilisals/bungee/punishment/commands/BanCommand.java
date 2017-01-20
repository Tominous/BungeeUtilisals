package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.UUID;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.BanEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

	public BanCommand() {
		super("ban");
	}

	public static void executeBanCommand(CommandSender sender, String[] args) {
		String banreason = "";
		if(args.length < 2){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		
		
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if(uuid == null){
			return;
		}
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			if(BanAPI.isBanned(uuid.toString())){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.AlreadyBanned")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
		} else {
			if(BanAPI.isBanned(args[0])){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.AlreadyBanned")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		
		String kreason = "";
		String reason = "";
		for(String s : args){
			reason = reason + s + " ";
		}
		reason = reason.replaceFirst(args[0] + " ", "");
		banreason = reason;
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.KickMessage")){
			kreason = kreason + "\n" + s;
		}
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			BanAPI.addBan(sender.getName(), uuid.toString(), -1L, reason);
		} else {
			BanAPI.addBan(sender.getName(), args[0], -1L, reason);
		}
		if(p != null){
			if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
				p.disconnect(Utils.format(kreason.replace("%banner%", BanAPI.getBannedBy(uuid.toString()))
						.replace("%unbantime%", "Never")
						.replace("%reason%", BanAPI.getReason(uuid.toString()))));
			} else {
				p.disconnect(Utils.format(kreason.replace("%banner%", BanAPI.getBannedBy(p.getName())).replace("%unbantime%", "Never").replace("%reason%", BanAPI.getReason(p.getName()))));
			}
		}
		PlayerInfo info;
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			info = new PlayerInfo(uuid.toString());
		} else {
			info = new PlayerInfo(args[0]);
		}
		BanEvent event = new BanEvent(sender.getName(), args[0], -1L, reason);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		info.addBan();
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.Banned")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", banreason)));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeBanCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.ban", "ban", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.ban") || sender.hasPermission("butilisals.*")){
			executeBanCommand(sender, args);		
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
