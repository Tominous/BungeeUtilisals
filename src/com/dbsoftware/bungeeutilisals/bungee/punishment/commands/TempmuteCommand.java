package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.punishment.TimeUnit;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempmuteCommand extends Command {

	public TempmuteCommand() {
		super("tempmute");
	}

	public static void executeTempmuteCommand(CommandSender sender, String[] args) {
		String mutereason = "";
		if(args.length < 3){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "�")));
			}
			return;
		}
		if(MuteAPI.isMuted(args[0])){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.AlreadyMuted")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if(p != null){
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "").replace(args[1] + " ", "");
			mutereason = reason;
				
			Long time = -1L;
			try {
				time = TimeUnit.parseDateDiff(args[1], true);
			} catch(NumberFormatException e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			} catch(Exception e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			if(time == -1L){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			
			SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
			String date = df2.format(new Date(time));
			
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.MuteMessage")){
				p.sendMessage(Utils.format(s.replace("%player%", sender.getName()).replace("%reason%", mutereason).replace("%time%", date)));
			}
			
			MuteAPI.addMute(sender.getName(), p.getName(), time, reason);
		} else {
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "").replace(args[1] + " ", "");
			mutereason = reason;

			Long time = -1L;
			try {
				time = TimeUnit.parseDateDiff(args[1], true);
			} catch(NumberFormatException e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			} catch(Exception e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			if(time == -1L){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			MuteAPI.addMute(sender.getName(), args[0], time, reason);
		}
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		pinfo.addMute();
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.Muted")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", mutereason)));
		}
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeTempmuteCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.tempmute", "tempmute", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.tempmute") || sender.hasPermission("butilisals.*")){
			executeTempmuteCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}