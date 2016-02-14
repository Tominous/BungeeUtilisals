package com.dbsoftware.bungeeutilisals.bungee.tabmanager;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PostLoginEvent event){
		ProxiedPlayer p = event.getPlayer();
		
		String count = (BungeeUtilisals.getInstance().useRedis() ? String.valueOf(BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayerCount()) : 
			String.valueOf(ProxyServer.getInstance().getPlayers().size()));
		
		String sheader = (TabUpdateTask.headers.get(TabUpdateTask.headercount)).replace("%newline%", "\n").replace("%p%", p.getName())
				.replace("%globalonline%", count).replace("%server%", p.getServer().getInfo().getName());
		String sfooter = (TabUpdateTask.footers.get(TabUpdateTask.footercount)).replace("%newline%", "\n").replace("%p%", p.getName())
				.replace("%globalonline%",count).replace("%server%", p.getServer().getInfo().getName());

		BaseComponent[] header = TextComponent.fromLegacyText(sheader);
		BaseComponent[] footer = TextComponent.fromLegacyText(sfooter);

		p.setTabHeader(header, footer);
	}
}