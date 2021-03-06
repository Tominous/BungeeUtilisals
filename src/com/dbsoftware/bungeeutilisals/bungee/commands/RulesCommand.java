package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RulesCommand extends Command {
		
	public RulesCommand() {
			super("rules");{
		}
	}
	
	private static BungeeUtilisals instance = (BungeeUtilisals)BungeeUtilisals.getInstance();

	public static void executeRulesCommand(CommandSender sender, String[] args){
		TextComponent click = new TextComponent( ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("Rules.Text").replace("%player%", sender.getName())) );
		click.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("Rules.Hover").replace("%player%", sender.getName()))).create() ) );
		click.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, instance.getConfig().getString("Rules.Site") ) );
		
		sender.sendMessage(Utils.format(instance.getConfig().getString("Rules.Header")));
		
		for (String rules : instance.getConfig().getStringList("Rules.Rules")) {
			sender.sendMessage(Utils.format(rules.replace("%player%", sender.getName())));
		}
		
		sender.sendMessage(click);
	      
		sender.sendMessage(Utils.format(instance.getConfig().getString("Rules.Footer")));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeRulesCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.rules", "rules", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.rules") || sender.hasPermission("butilisals.*")){
			executeRulesCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}