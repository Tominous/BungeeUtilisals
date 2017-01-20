package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.ActionBarUtil;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.TitleUtil;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BigalertCommand extends Command {
	
	public BigalertCommand() {
		super("bigalert");{
		}
	}
	
	public static void executeBigalertCommand(CommandSender sender, String[] args){
		String prefix = ChatColor.translateAlternateColorCodes('&', BungeeUtilisals.getInstance().getConfig().getString("Prefix"));
		if (args.length >= 1) {
			if((args.length == 2) && (args[0].equalsIgnoreCase("config"))){
				if(BungeeUtilisals.getInstance().getConfig().getString("BigAlert.Messages." + args[1]) != null){
					String message = BungeeUtilisals.getInstance().getConfig().getString("BigAlert.Messages." + args[1]);
					
					if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Chat.Enabled")){
						for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
							p.sendMessage(Utils.format(prefix + ChatColor.translateAlternateColorCodes('&', message.replace("%p%", p.getName()).replaceAll("%n", " "))));
						}
					}
					if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.ActionBar.Enabled")){
						for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
							ActionBarUtil.sendActionBar(p, ChatColor.translateAlternateColorCodes('&', message.replace("%p%", p.getName()).replaceAll("%n", " ")));	
						}
					}
					
					if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Title.Enabled")){
						if(message.contains("%n")){
							String[] titles = message.split("%n");
							String title = titles[0];
							String subtitle = titles[1];
							int fadeIn = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeIn");
							int stay = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.Stay");
							int fadeOut = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeOut");
							
							for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
								
								BaseComponent[] btitle = new ComponentBuilder(title.replace("&", "§").replace("%p%", p.getName())).create();
								BaseComponent[] stitle = new ComponentBuilder(subtitle.replace("&", "§").replace("%p%", p.getName())).create();
								
								TitleUtil.sendFullTitle(p, fadeIn, stay, fadeOut, stitle, btitle);
							}
							return;
						} else {
							int fadeIn = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeIn");
							int stay = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.Stay");
							int fadeOut = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeOut");
							for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
								
								BaseComponent[] btitle = new ComponentBuilder(message.replace("&", "§").replace("%p%", p.getName())).create();
								BaseComponent[] stitle = new ComponentBuilder("").create();
								
								TitleUtil.sendFullTitle(p, fadeIn, stay, fadeOut, stitle, btitle);
							}
							return;
						}
					}
				} else {
					sender.sendMessage(Utils.format("&cThat String doesn't exist in the Config!"));
					return;
				}
			}
			if((args.length != 2) && (args[0].equalsIgnoreCase("config"))){
				return;
			}
			
			String msg = "";
			for(String s : args){
				msg = msg + s + " ";
			}
			
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Chat.Enabled")){
				for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					p.sendMessage(Utils.format(prefix + ChatColor.translateAlternateColorCodes('&', msg.replace("%p%", p.getName()).replaceAll("%n", " "))));
				}
			}
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.ActionBar.Enabled")){
				for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					ActionBarUtil.sendActionBar(p, ChatColor.translateAlternateColorCodes('&', msg.replace("%p%", p.getName()).replaceAll("%n", " ")));	
				}
			}
	        
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Title.Enabled")){
				if(msg.contains("%n")){
					String[] titles = msg.split("%n");
					String title = titles[0];
					String subtitle = titles[1];
					int fadeIn = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeIn");
					int stay = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.Stay");
					int fadeOut = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeOut");
					
					for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
						
						BaseComponent[] btitle = new ComponentBuilder(title.replace("&", "§").replace("%p%", p.getName())).create();
						BaseComponent[] stitle = new ComponentBuilder(subtitle.replace("&", "§").replace("%p%", p.getName())).create();
						
						TitleUtil.sendFullTitle(p, fadeIn, stay, fadeOut, stitle, btitle);
					}
					return;
				} else {
					int fadeIn = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeIn");
					int stay = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.Stay");
					int fadeOut = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeOut");
					for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
						
						BaseComponent[] btitle = new ComponentBuilder(msg.replace("&", "§").replace("%p%", p.getName())).create();
						BaseComponent[] stitle = new ComponentBuilder("").create();
						
						TitleUtil.sendFullTitle(p, fadeIn, stay, fadeOut, stitle, btitle);
					}
					return;
				}
			}
		} else {
			sender.sendMessage(Utils.format("&cYou need to enter at least 1 word!"));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeBigalertCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.bigalert", "bigalert", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.bigalert") || sender.hasPermission("butilisals.*")){
			executeBigalertCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}