package com.dbsoftware.bungeeutilisals.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.commands.AlertCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.BgcCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.BigalertCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ButilisalsCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ChatCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ClearChatCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.FindCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.GRankCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.GlistCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.HubCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.LocalSpyCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.MSGCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ReplyCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.RulesCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ServerCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.SpyCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.StoreCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.VoteCommand;
import com.dbsoftware.bungeeutilisals.bungee.friends.FriendsCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanIPCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanInfoCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.KickCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.MuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.TempbanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.TempmuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.UnbanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.UnmuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.WarnCommand;
import com.dbsoftware.bungeeutilisals.bungee.staffchat.StaffChatCommand;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageReceive implements Listener {

	@EventHandler
	public void onMessageReceive(PluginMessageEvent event){
		if (event.getTag().equalsIgnoreCase("BungeeCord")) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
			try {
				String channel = in.readUTF();
				if(channel.equals("hasPermission")){
					String pl = in.readUTF();
					String command = in.readUTF();
					String argline = in.readUTF();
					String[] args = new String[]{};
					if(argline.isEmpty()){
						args = new String[]{};
					} else {
						args = argline.split(" ");
					}
					boolean hasperm = in.readBoolean();
					
					ProxiedPlayer p = ProxyServer.getInstance().getPlayer(pl);
					if(p == null){
						return;
					}
					CommandSender sender = (CommandSender)p;
					if(!hasperm){
						p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
						return;
					}
					if(command.equals("glag")){
						BgcCommand.executeBgcCommand(sender, args);
					} if(command.equals("localspy")){
						LocalSpyCommand.executeSpyCommand(sender, args);
					} if(command.equals("msg")){
						MSGCommand.executeMSGCommand(sender, args);
					} if(command.equals("chat")){
						ChatCommand.executeChatCommand(sender, args);
					} if(command.equals("reply")){
						ReplyCommand.executeReplyCommand(sender, args);
					} if(command.equals("spy")){
						SpyCommand.executeSpyCommand(sender, args);
					} if(command.equals("grank")){
						GRankCommand.executeGRankCommand(sender, args);
					} if(command.equals("alert")){
						AlertCommand.executeAlertCommand(sender, args);
						return;
					} if(command.equals("bigalert")){
						BigalertCommand.executeBigalertCommand(sender, args);
						return;
					} if(command.equals("cubedcraft")){
						ButilisalsCommand.executeButilisalsCommand(sender, args);
						return;
					} if(command.equals("clearchat")){
						ClearChatCommand.executeClearChatCommand(sender, args);
						return;
					} if(command.equals("find")){
						FindCommand.executeFindCommand(sender, args);
						return;
					} if(command.equals("glist")){
						GlistCommand.executeGlistCommand(sender, args);
						return;
					} if(command.equals("hub")){
						HubCommand.executeHubCommand(sender, args);
						return;
					} if(command.equals("rules")){
						RulesCommand.executeRulesCommand(sender, args);
						return;
					} if(command.equals("server")){
						ServerCommand.executeServerCommand(sender, args);
						return;
					} if(command.equals("store")){
						StoreCommand.executeStoreCommand(sender, args);
						return;
					} if(command.equals("vote")){
						VoteCommand.executeVoteCommand(sender, args);
						return;
					} if(command.equals("friend")){
						FriendsCommand.executeFriendCommand(sender, args);
						return;
					} if(command.equals("staffchat")){
						StaffChatCommand.executeStaffChatCommand(sender, args);
						return;
					} if(command.equals("ban")){
						BanCommand.executeBanCommand(sender, args);
						return;
					} if(command.equals("unban")){
						UnbanCommand.executeUnbanCommand(sender, args);
						return;
					} if(command.equals("kick")){
						KickCommand.executeKickCommand(sender, args);
						return;
					} if(command.equals("tempban")){
						TempbanCommand.executeTempBanCommand(sender, args);
						return;
					} if(command.equals("banip")){
						BanIPCommand.executeBanIPCommand(sender, args);
						return;
					} if(command.equals("baninfo")){
						BanInfoCommand.executeCheckBanCommand(sender, args);
						return;
					} if(command.equals("mute")){
						MuteCommand.executeMuteCommand(sender, args);
						return;
					} if(command.equals("unmute")){
						UnmuteCommand.executeUnmuteCommand(sender, args);
						return;
					} if(command.equals("warn")){
						WarnCommand.executeWarnCommand(sender, args);
						return;
					} if(command.equals("tempmute")){
						TempmuteCommand.executeTempmuteCommand(sender, args);
						return;
					}
				}
				if(channel.equals("getLimit")){
					String name = in.readUTF();
					ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
					if(p == null){
						return;
					}
					CommandSender sender = (CommandSender)p;
					String[] args = in.readUTF().split(" ");
					int limit = in.readInt();
					
					FriendsCommand.executeFriendAddCommand(sender, args, limit);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
}
