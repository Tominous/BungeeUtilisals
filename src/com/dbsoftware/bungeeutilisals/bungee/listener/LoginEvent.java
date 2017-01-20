package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import net.md_5.bungee.api.plugin.Listener;

public class LoginEvent implements Listener {

	public BungeeUtilisals plugin;
	  
	public LoginEvent(BungeeUtilisals plugin){
		this.plugin = plugin;
	}

}
