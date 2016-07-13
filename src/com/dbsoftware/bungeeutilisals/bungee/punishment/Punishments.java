package com.dbsoftware.bungeeutilisals.bungee.punishment;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;
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
import com.dbsoftware.bungeeutilisals.bungee.punishment.listeners.ChatListener;
import com.dbsoftware.bungeeutilisals.bungee.punishment.listeners.LoginListener;
import net.md_5.bungee.api.ProxyServer;

public class Punishments {
	
    private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator + "bans.yml";
    public static FileManager punishments = new FileManager( path );
    public static DatabaseManager dbmanager = BungeeUtilisals.getInstance().getDatabaseManager();
    
    public static HashMap<String, BanInfo> bans = new HashMap<String, BanInfo>();
    public static HashMap<String, MuteInfo> mutes = new HashMap<String, MuteInfo>();
    public static HashMap<String, BanIPInfo> ipbans = new HashMap<String, BanIPInfo>();

    public static void reloadPunishmentData() {
        punishments = null;
        punishments = new FileManager( path );
    }
    
    public static BanInfo getBanInfo(String player){
    	for(BanInfo info : bans.values()){
    		if(info.getPlayer().equals(player)){
    			return info;
    		}
    	}
    	return null;
    }
    
    public static BanIPInfo getIPBanInfo(String ip){
    	for(BanIPInfo info : ipbans.values()){
    		if(info.getIP().equals(ip)){
    			return info;
    		}
    	}
    	return null;
    }
    
    public static void registerPunishmentSystem(){
    	punishments = null;
    	punishments = new FileManager( path );
    	if(punishments.getFile().getBoolean("Punishments.Enabled", true)){
        	setDefaults();
        	if(punishments.getFile().getBoolean("Punishments.Commands.Ban")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new BanCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.Unban")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new UnbanCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.Kick")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new KickCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.Tempban")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new TempbanCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.BanIP")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new BanIPCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.BanInfo")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new BanInfoCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.Mute")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new MuteCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.Tempmute")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new TempmuteCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.Unmute")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new UnmuteCommand());
        	}
        	if(punishments.getFile().getBoolean("Punishments.Commands.Warn")){
            	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new WarnCommand());
        	}
        	
        	ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(), new LoginListener());
        	ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(), new ChatListener());
        	
        	ProxyServer.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), new Runnable(){
				@Override
				public void run() {
		        	for(String s : BanAPI.getBans()){
		        		BanInfo baninfo = new BanInfo(s, BanAPI.getBannedBy(s), BanAPI.getBanTime(s), BanAPI.getReason(s));
		        		bans.put(s, baninfo);
		        	}					
				}
        	});
        	
        	ProxyServer.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), new Runnable(){
				@Override
				public void run() {
		        	for(String s : BanAPI.getIPBans()){
		        		BanIPInfo banipinfo = new BanIPInfo(s, BanAPI.getIPBannedBy(s), BanAPI.getIPReason(s));
		        		ipbans.put(s, banipinfo);
		        	}					
				}
        	});
        	
        	ProxyServer.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), new Runnable(){
				@Override
				public void run() {
		        	for(String s : MuteAPI.getMutes()){
		        		MuteInfo muteinfo = new MuteInfo(s, MuteAPI.getMutedBy(s), MuteAPI.getMuteTime(s), MuteAPI.getReason(s));
		        		mutes.put(s, muteinfo);
		        	}					
				}
        	});
    	}
    }
    
    private static void setDefaults(){
        Collection<String> check = punishments.getFile().getSection( "Punishments" ).getKeys();
        if(!check.contains("Commands")){
        	punishments.getFile().set("Punishments.Commands.Ban", true);
        	punishments.getFile().set("Punishments.Commands.Unban", true);
        	punishments.getFile().set("Punishments.Commands.Kick", true);
        	punishments.getFile().set("Punishments.Commands.Tempban", true);
        	punishments.getFile().set("Punishments.Commands.BanIP", true);
        	punishments.getFile().set("Punishments.Commands.BanInfo", true);
        	punishments.getFile().set("Punishments.Commands.Mute", true);
        	punishments.getFile().set("Punishments.Commands.Tempmute", true);
        	punishments.getFile().set("Punishments.Commands.Unmute", true);
        	punishments.getFile().set("Punishments.Commands.Warn", true);
        }
        if(!check.contains("Settings")){
        	punishments.getFile().set("Punishments.Settings.Blocked-Cmds-While-Muted", Arrays.asList(new String[]{"/msg", "/w", "/tell"}));
        }
        if(!check.contains("Messages")){
        	punishments.getFile().set("Punishments.Messages.NoPermission", Arrays.asList(new String[]{"&4&lPermissions &8� &cYou don't have the permission to use this command!"}));
        }
        if(!check.contains("Ban")){
        	punishments.getFile().set("Punishments.Ban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/ban (player) (reason) &e!"}));
        	punishments.getFile().set("Punishments.Ban.Messages.KickMessage", Arrays.asList(new String[]{
							"&cBungeeUtilisals &8� &7Banned",
							" ",
							"&cReason &8� &7%reason%",
							"&cUnban at &8� &7%unbantime%",
						 	"&cBanned by &8� &7%banner%"}));
        	punishments.getFile().set("Punishments.Ban.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8� &cThat player is already banned!"}));
        	punishments.getFile().set("Punishments.Ban.Messages.Banned", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been banned! &bReason: &e%reason%"}));
        }
        if(!check.contains("Unban")){
        	punishments.getFile().set("Punishments.Unban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/unban (player)&e!"}));
        	punishments.getFile().set("Punishments.Unban.Messages.NotBanned", Arrays.asList(new String[]{"&4&lBans &8� &cThat player is not banned!"}));
        	punishments.getFile().set("Punishments.Unban.Messages.Unbanned", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been unbanned!"}));
        	punishments.getFile().set("Punishments.Unban.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8� &cThat player never joined!"}));
        }
        if(!check.contains("Kick")){
        	punishments.getFile().set("Punishments.Kick.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/kick (player) (reason) &e!"}));
        	punishments.getFile().set("Punishments.Kick.Messages.NotOnline", Arrays.asList(new String[]{"&4&lBans &8� &eThat player is not online!"}));
        	punishments.getFile().set("Punishments.Kick.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8� &7Kicked",
					" ",
					"&cReason &8� &7%reason%",
				 	"&cKicked by &8� &7%kicker%"}));
        	punishments.getFile().set("Punishments.Kick.Messages.Kicked", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been kicked from the server!"}));
        }
        if(!check.contains("Tempban")){
        	punishments.getFile().set("Punishments.Tempban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/tempban (player) (time) (reason) &e!"}));
        	punishments.getFile().set("Punishments.Tempban.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8� &cThat player is already banned!"}));
        	punishments.getFile().set("Punishments.Tempban.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8� &7Tempbanned",
					" ",
					"&cReason &8� &7%reason%",
					"&cUnban at &8� &7%unbantime%",
				 	"&cBanned by &8� &7%banner%"}));
        	punishments.getFile().set("Punishments.Tempban.Messages.Banned", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been tempbanned! &bReason: &e%reason%"}));
        	punishments.getFile().set("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use a good TimeUnit!"}));
        }
        if(!check.contains("BanIP")){
        	punishments.getFile().set("Punishments.BanIP.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/banip (player) (reason) &e!"}));
        	punishments.getFile().set("Punishments.BanIP.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8� &cThat IP is already banned!"}));
        	punishments.getFile().set("Punishments.BanIP.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8� &7IPBanned",
					" ",
					"&cReason &8� &7%reason%",
				 	"&cBanned by &8� &7%banner%"}));
        	punishments.getFile().set("Punishments.BanIP.Messages.Banned", Arrays.asList(new String[]{"&4&lBans &8� &b%ip% &ehas been banned! &bReason: &e%reason%"}));
        	punishments.getFile().set("Punishments.BanIP.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8� &cThat player never joined!"}));
        } if(!check.contains("Mute")){
        	punishments.getFile().set("Punishments.Mute.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/mute (player) (reason) &e!"}));
        	punishments.getFile().set("Punishments.Mute.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8� &cThat player is already muted!"}));
        	punishments.getFile().set("Punishments.Mute.Messages.MuteMessage", Arrays.asList(new String[]{
					"&4&lBans &8� &b%player% &ehas muted you!",
					"&4&lBans &8� &eReason: &b%reason%"}));
        	punishments.getFile().set("Punishments.Mute.Messages.Muted", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been Muted! &bReason: &e%reason%"}));
        } if(!check.contains("PlayerInfo")){
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/pinfo (player)&e!"}));
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8� &eThat player never joined before!"}));
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.Info",
					Arrays.asList(new String[]{"&4&lBans &8� &eInformation of &b%player%&e:",
					" &eIP: &b%ip%",
					" &eBans: &b%bans%",
					" &eMutes: &b%mutes%",
					" &eKicks: &b%kicks%",
					" &eWarns: &b%warns%",
					" &eCurrent Ban: &b%isbanned%",
					" &eCurrent IPBan: &b%isIPbanned%",
					" &eCurrent Mute: &b%ismuted%"}));
        	
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.isBanned", "&cBanned by %banner% for %reason%. Unban time: %time%");
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.isNotBanned", "&cNot Banned");
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.isIPBanned", "&cIPBanned by %banner% for %reason%.");
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.isNotIPBanned", "&cNot IPBanned");
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.isMuted", "&cMuted by %muter% for %reason%. Unmute time: %time%");
        	punishments.getFile().set("Punishments.PlayerInfo.Messages.isNotMuted", "&cNot Muted");
        } if(!check.contains("Unmute")){
        	punishments.getFile().set("Punishments.Unmute.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/unmute (player)&e!"}));
        	punishments.getFile().set("Punishments.Unmute.Messages.Unmuted", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been unmuted!"}));
        	punishments.getFile().set("Punishments.Unmute.Messages.NotMuted", Arrays.asList(new String[]{"&4&lBans &8� &cThat player is not muted!"}));
        } if(!check.contains("Warn")){
        	punishments.getFile().set("Punishments.Warn.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/warn (player) (reason) &e!"}));
        	punishments.getFile().set("Punishments.Warn.Messages.WarnMessage", Arrays.asList(new String[]{
					"&4&lBans &8� &b%player% &ehas warned you!",
					"&4&lBans &8� &eReason: &b%reason%"}));
        	punishments.getFile().set("Punishments.Warn.Messages.OfflinePlayer", Arrays.asList(new String[]{"&4&lBans &8� &eThat player is not online!"}));
        	punishments.getFile().set("Punishments.Mute.Messages.Warned", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been Warned! &bReason: &e%reason%"}));
        } if(!check.contains("Tempmute")){
        	punishments.getFile().set("Punishments.Tempmute.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use &b/tempmute (player) (time) (reason) &e!"}));
        	punishments.getFile().set("Punishments.Tempmute.Messages.AlreadyMuted", Arrays.asList(new String[]{"&4&lBans &8� &cThat player is already muted!"}));
        	punishments.getFile().set("Punishments.Tempmute.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8� &ePlease use a good TimeUnit!"}));
        	punishments.getFile().set("Punishments.Tempmute.Messages.MuteMessage", Arrays.asList(new String[]{
					"&4&lBans &8� &b%player% &ehas tempmuted you!",
					"&4&lBans &8� &eReason: &b%reason%",
					"&4&lBans &8� &eUnmute Time: &b%time%"}));
        	punishments.getFile().set("Punishments.Tempmute.Messages.Muted", Arrays.asList(new String[]{"&4&lBans &8� &b%player% &ehas been tempmuted! &bReason: &e%reason%"}));
        }
        punishments.save();
    }
}
