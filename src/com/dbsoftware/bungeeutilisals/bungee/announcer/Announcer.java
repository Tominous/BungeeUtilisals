package com.dbsoftware.bungeeutilisals.bungee.announcer;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.tasks.GlobalAnnouncements;
import com.dbsoftware.bungeeutilisals.bungee.tasks.ServerAnnouncements;

public class Announcer {
    public static ArrayList<ScheduledTask> announcementTasks = new ArrayList<>();
    static ProxyServer proxy = ProxyServer.getInstance();
    private static BungeeUtilisals instance = BungeeUtilisals.getInstance();

    public static void loadAnnouncements() {
        setDefaults();
        if ( Announcements.announcements.getFile().getBoolean("Announcements.Enabled", true) ) {
            List<String> global = Announcements.announcements.getFile().getStringList( "Announcements.Global.Messages");
            if ( !global.isEmpty() ) {
            	if(Announcements.announcements.getFile().getBoolean( "Announcements.Global.Enabled", true)){
                int interval = Announcements.announcements.getFile().getInt( "Announcements.Global.Interval", 0 );
                if ( interval > 0 ) {
                    GlobalAnnouncements g = new GlobalAnnouncements();
                    for ( String messages : global ) {
                        g.addAnnouncement( messages );
                    }
                    ScheduledTask t = proxy.getScheduler().schedule( instance, g, interval, interval, TimeUnit.SECONDS );
                    announcementTasks.add( t );
                	}
            	}
            }
            for ( String server : proxy.getServers().keySet() ) {
                List<String> servermessages = Announcements.announcements.getFile().getStringList("Announcements." + server + ".Messages");
                if ( !servermessages.isEmpty() ) {
                	if(Announcements.announcements.getFile().getBoolean( "Announcements." + server + ".Enabled", false)){
                    int interval = Announcements.announcements.getFile().getInt( "Announcements." + server + ".Interval", 0 );
                    if ( interval > 0 ) {
                        ServerAnnouncements s = new ServerAnnouncements( proxy.getServerInfo( server ));
                        for ( String messages : servermessages ) {
                            s.addAnnouncement( messages );
                        }
                        ScheduledTask t = proxy.getScheduler().schedule( instance, s, interval, interval, TimeUnit.SECONDS );
                        announcementTasks.add( t );
                    	}
                	}
                }
            }
        }
    }

    private static void setDefaults() {
        Collection<String> check = Announcements.announcements.getFile().getSection( "Announcements" ).getKeys();
        if ( !check.contains( "Enabled" ) ) {
            Announcements.announcements.getFile().set("Announcements.Enabled", true);
        }
        if ( !check.contains( "Global" ) ) {
            Announcements.announcements.getFile().set("Announcements.Global.Enabled", true);
            Announcements.announcements.getFile().set( "Announcements.Global.Interval", 150 );
            List<String> l = new ArrayList<String>();
            l.add( "&a&lWelcome to CubedCraft network!" );
            Announcements.announcements.getFile().set( "Announcements.Global.Messages", l );
        }
        Announcements.announcements.save();
    }

    public static void reloadAnnouncements() {
        for ( ScheduledTask task : announcementTasks ) {
            task.cancel();
        }
        announcementTasks.clear();
        loadAnnouncements();
    }
}