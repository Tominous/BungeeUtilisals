package com.dbsoftware.bungeeutilisals.bungee.listener;

import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.BanEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.IPBanEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.KickEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.MuteEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.UnbanEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.UnmuteEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.WarnEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PunishmentAlertListener implements Listener {
	
	@EventHandler
	public void onBan(BanEvent event){
		List<String> staffalert;
		if(event.getExpire() == -1L){
			staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Ban");
		} else {
			staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Tempban");
		}
		
		for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
			for(String message : staffalert){
				user.sendMessage(message.replace("%player%", event.getBanned()).replace("%banner%", event.getBanner())
						.replace("%reason%", event.getReason()).replace("%expire%", event.getDate()));
			}
		}
	}
	
	@EventHandler
	public void onKick(KickEvent event){
		List<String> staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Kick");
		
		for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
			for(String message : staffalert){
				user.sendMessage(message.replace("%player%", event.getKicked()).replace("%kicker%", event.getKicker()).replace("%reason%", event.getReason()));
			}
		}
	}
	
	@EventHandler
	public void onIPBan(IPBanEvent event){
		List<String> staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.IPBan");
		
		for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
			for(String message : staffalert){
				user.sendMessage(message.replace("%player%", event.getBanned()).replace("%banner%", event.getBanner()).replace("%reason%", event.getReason()));
			}
		}
	}
	
	@EventHandler
	public void onMute(MuteEvent event){
		List<String> staffalert;
		if(event.getExpire() == -1L){
			staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Mute");
		} else {
			staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Tempmute");
		}
		
		for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
			for(String message : staffalert){
				user.sendMessage(message.replace("%player%", event.getMuted()).replace("%muter%", event.getMuter())
						.replace("%reason%", event.getReason()).replace("%expire%", event.getDate()));
			}
		}
	}
	
	@EventHandler
	public void onWarn(WarnEvent event){
		List<String> staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Warn");
		
		for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
			for(String message : staffalert){
				user.sendMessage(message.replace("%player%", event.getWarned())
						.replace("%warner%", event.getWarner()).replace("%reason%", event.getReason()));
			}
		}
	}
	
	@EventHandler
	public void onUnmute(UnmuteEvent event){
		List<String> staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Unmute");
		
		for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
			for(String message : staffalert){
				user.sendMessage(message.replace("%player%", event.getUnmuted())
						.replace("%unmuter%", event.getUnmuter()));
			}
		}
	}
	
	@EventHandler
	public void onBan(UnbanEvent event){
		List<String> staffalert = Punishments.punishments.getFile().getStringList("StaffAlert.Unban");
		
		for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
			for(String message : staffalert){
				user.sendMessage(message.replace("%player%", event.getUnbanned())
						.replace("%unbanned%", event.getUnbanner()));
			}
		}
	}
}