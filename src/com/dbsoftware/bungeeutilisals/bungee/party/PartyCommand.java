package com.dbsoftware.bungeeutilisals.bungee.party;

import java.util.ArrayList;
import java.util.Arrays;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyCommand extends Command {

	public PartyCommand() {
		super("party");
	}

	public static void executePartyCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			for(String s : Party.party.getStringList("Party.Messages.Help", new ArrayList<String>())){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "�")));
			}
			return;
		}
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(TextComponent.fromLegacyText(Party.party.getString("Party.Messages.ConsoleError", "&cThe console cannot use this Command!").replace("&", "�")));
			return;
		}
		ProxiedPlayer user = (ProxiedPlayer)sender;
		if(args.length == 0){
			for(String s : Party.party.getStringList("Party.Messages.Help", Arrays.asList(new String[]{"&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r", "&c/party create &7- &c&oCreate a new party", "&c/party list &7- &c&oList the current members of your party}", "&c/party leave &7- &c&oLeave/Delete your current party", "&c/party chat &7- &c&oChat with your party members", "&c/party invite &7- &c&oInvite a player to your party", "&c/party kick &7- &c&oKick someone from your party", "&c/party admin (player) &7- &c&oPromote someone to admin in your party.", "&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r"}))){
				user.sendMessage(Utils.format(s));
			}
			return;
		}
		
		if(args.length == 1){
			if(args[0].contains("create")){
				PartyManager.createParty(user);
				return;
			} if (args[0].contains("list")){
    	  		PartyManager.partyList(user);
    	  		return;
    	  	} if(args[0].contains("leave")){
    	  		PartyManager.leave(user);
    	  		return;
    	  	} if(args[0].contains("accept")){
    	  		PartyManager.accept(user);
    	  		return;
    	  	} if(args[0].contains("help")){
    			for(String s : Party.party.getStringList("Party.Messages.Help", Arrays.asList(new String[]{"&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r", "&c/party create &7- &c&oCreate a new party", "&c/party list &7- &c&oList the current members of your party}", "&c/party leave &7- &c&oLeave/Delete your current party", "&c/party chat &7- &c&oChat with your party members", "&c/party invite &7- &c&oInvite a player to your party", "&c/party kick &7- &c&oKick someone from your party", "&c/party admin (player) &7- &c&oPromote someone to admin in your party.", "&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r"}))){
    				user.sendMessage(Utils.format(s));
    			}
    	  		return;
    	  	} if(args[0].contains("warp")){
    	  		PartyManager.warpParty(user);
    	  		return;
    	  	}
		} if(args.length == 2){
			if (args[0].contains("invite")){
				if (args.length > 1) {
					for (ProxiedPlayer x : ProxyServer.getInstance().getPlayers()) {
						if (x.getName().equalsIgnoreCase(args[1])){
							ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[1]);
							PartyManager.invite(user, p);
							return;
						}
					}
	              user.sendMessage(Utils.format(Party.party.getString("Party.Messages.OfflinePlayer", "&cThat player is not online!")));
	              return;
				} else {
					user.sendMessage(Utils.format(Party.party.getString("Party.Messages.WrongArgs", "&cPlease use /party help for more info.")));
				}
			} if(args[0].contains("admin")){
  	    		if(user.getName() == args[1]){
  	    			user.sendMessage(Utils.format(Party.party.getString("Party.Messages.AdminError", "&cYou cannot make yourself admin!")));
  	  	      		return;
  	    		} else {
  	    			PartyManager.addAdmin(user, args[1]);
  	    			return;
  	    		}
			} if(args[0].contains("kick")){
				if (args.length > 1){
					for (ProxiedPlayer x : ProxyServer.getInstance().getPlayers()) {
						if (x.getName().equalsIgnoreCase(args[1])){
							PartyManager.kick(user, x);
							return;
						}
					}
					user.sendMessage(Utils.format(Party.party.getString("Party.Messages.OfflinePlayer", "&cThat player is not online!")));
					return;
				} else {
					user.sendMessage(Utils.format(Party.party.getString("Party.Messages.WrongArgs", "&cPlease use /party help for more info.")));
					return;
	            }
			}
		} if(args.length > 1){
			if(args[0].contains("chat")){
		        String msg = "";
		        for (int i = 1; i < args.length; i++) {
		        	msg = msg + args[i] + " ";
		        }
		        PartyManager.chat(user, msg);
				return;
			} else {
    			for(String s : Party.party.getStringList("Party.Messages.Help", Arrays.asList(new String[]{"&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r", "&c/party create &7- &c&oCreate a new party", "&c/party list &7- &c&oList the current members of your party}", "&c/party leave &7- &c&oLeave/Delete your current party", "&c/party chat &7- &c&oChat with your party members", "&c/party invite &7- &c&oInvite a player to your party", "&c/party kick &7- &c&oKick someone from your party", "&c/party admin (player) &7- &c&oPromote someone to admin in your party.", "&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r"}))){
    				user.sendMessage(Utils.format(s));
    			}
    	  		return;
			}
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(Utils.format("&cOnly players can use this command!"));
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.party", "party", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.party") || sender.hasPermission("butilisals.*")){
			executePartyCommand(sender, args);
		} else {
			sender.sendMessage(Utils.format(Party.party.getString("Party.Messages.NoPermission", "&cYou don't have the permission to use this Command!")));
		}
	}
}
