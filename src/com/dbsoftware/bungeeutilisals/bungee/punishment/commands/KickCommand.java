package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.Arrays;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

	public KickCommand() {
		super("kick");
	}

	public static void executeKickCommand(CommandSender sender, String[] args) {
		if(args.length < 2){
			for(String s : Punishments.punishments.getStringList("Punishments.Kick.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/kick (player) (reason) &e!"}))){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "�")));
			}
			return;
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if(p == null){
			for(String s : Punishments.punishments.getStringList("Punishments.Kick.Messages.NotOnline", Arrays.asList(new String[]{"&4&lBans &8� &eThat player is not online!"}))){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		String kreason = "";
		String reason = "";
		for(String s : args){
			reason = reason + s + " ";
		}
		reason = reason.replace(p.getName() + " ", "");
		for(String s : Punishments.punishments.getStringList("Punishments.Kick.Messages.KickMessage", Arrays.asList(new String[]{
				"&cBungeeUtilisals &8� &7Kicked",
				" ",
				"&cReason &8� &7%reason%",
			 	"&cKicked by &8� &7%kicker%"}))){
			kreason = kreason + "\n" + s;
		}
          
		PlayerInfo pinfo = new PlayerInfo(p.getName());
		pinfo.addKick();
		p.disconnect(Utils.format(kreason.replace("%kicker%", sender.getName()).replace("%reason%", reason)));
		for(String s : Punishments.punishments.getStringList("Punishments.Kick.Messages.Kicked", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been kicked from the server!"}))){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeKickCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.kick", "kick", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.kick") || sender.hasPermission("butilisals.*")){
			executeKickCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getStringList("Punishments.Messages.NoPermission", Arrays.asList(new String[]{"&4&lPermissions &8� &cYou don't have the permission to use this command!"}))){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
