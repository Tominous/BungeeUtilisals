package com.dbsoftware.bungeeutilisals.bungee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.dbsoftware.bungeeutilisals.bungee.announcer.Announcer;
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
import com.dbsoftware.bungeeutilisals.bungee.friends.Friends;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiAd;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiCaps;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiSpam;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiSwear;
import com.dbsoftware.bungeeutilisals.bungee.listener.ChatListener;
import com.dbsoftware.bungeeutilisals.bungee.listener.ChatLock;
import com.dbsoftware.bungeeutilisals.bungee.listener.ChatUtilities;
import com.dbsoftware.bungeeutilisals.bungee.listener.CommandListener;
import com.dbsoftware.bungeeutilisals.bungee.listener.DisconnectEvent;
import com.dbsoftware.bungeeutilisals.bungee.listener.JoinListener;
import com.dbsoftware.bungeeutilisals.bungee.listener.LoginEvent;
import com.dbsoftware.bungeeutilisals.bungee.listener.MessageLimiter;
import com.dbsoftware.bungeeutilisals.bungee.listener.PluginMessageReceive;
import com.dbsoftware.bungeeutilisals.bungee.listener.PrivateMessageListener;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.staffchat.StaffChat;
import com.dbsoftware.bungeeutilisals.bungee.utils.MySQL;
import com.dbsoftware.bungeeutilisals.bungee.utils.TPSRunnable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class BungeeUtilisals extends Plugin {

	public static BungeeUtilisals instance;
    public static DatabaseManager dbmanager;
    public boolean chatMuted = false;
    public static boolean update;
    private File configfile;
    private Configuration config;
    private ConfigData configdata;
    public HashMap<String, String> pmcache = Maps.newHashMap();
    public List<BungeeUser> users = Lists.newArrayList();
    public List<String> staff = Lists.newArrayList();
    
	public void onEnable(){
		instance = this;

		this.configfile = this.loadResource(this, "config.yml");
	    try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configfile);
			this.saveConfig();
	    } catch (IOException e) {
			e.printStackTrace();
	    }
		this.configdata = new ConfigData();
		
	    loadCommands();
	    registerEvents();
	    Announcer.loadAnnouncements();
	    StaffChat.registerStaffChat();
		
	    ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(), new TPSRunnable(), 50, TimeUnit.MILLISECONDS);
	    
	    if(getConfig().getBoolean("MySQL.Enabled")){
	    	String host = getConfig().getString("MySQL.Host", "localhost");
			int port = getConfig().getInt("MySQL.Port", 3306);
			String database = getConfig().getString("MySQL.Database", "database");
			String username = getConfig().getString("MySQL.Username", "username");
			String password = getConfig().getString("MySQL.Password", "password");
			dbmanager = new DatabaseManager(host, port, database, username, password);
			dbmanager.openConnection();
			ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(), new Runnable(){

				@Override
				public void run(){
					dbmanager.closeConnection();
					dbmanager.openConnection();
				}
				
			}, 5, 5, TimeUnit.MINUTES);				
		    Friends.registerFriendsAddons();
			Punishments.registerPunishmentSystem();
			BungeeCord.getInstance().getPluginManager().registerCommand(this, new GRankCommand());
			
			Runnable r = new Runnable(){

				@Override
				public void run() {
					try {
						staff.clear();
						ResultSet rs = MySQL.getInstance().select().table("Staffs").column("*").select();
						
						while(rs.next()){
							staff.add(rs.getString("Name").toLowerCase());
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
			};
			BungeeCord.getInstance().getScheduler().schedule(this, r, 0, 1, TimeUnit.MINUTES);
	    }

	    ProxyServer.getInstance().getLogger().info("BungeeUtilisals is now Enabled!");
	}
	
	public List<BungeeUser> getStaff(){
		List<BungeeUser> list = Lists.newArrayList();
		
		for(BungeeUser user : users){
			if(user.isStaff()){
				list.add(user);
			}
		}
		
		return list;
	}
	
	public BungeeUser getUser(ProxiedPlayer p){
		for(BungeeUser user : users){
			if(user.getPlayer().equals(p)){
				return user;
			}
		}
		return null;
	}
	
	public ConfigData getConfigData(){
		return this.configdata;
	}
	
	public boolean saveConfig(){
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean reloadConfig(){
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configfile);
			this.configdata = new ConfigData();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Configuration getConfig(){
		return this.config;
	}
	
    public DatabaseManager getDatabaseManager(){
    	return dbmanager;
    }
	
	private void loadCommands(){
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new AlertCommand());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new BigalertCommand());
		if(getConfig().getBoolean("GList.Enabled")){
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new GlistCommand());
		}
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new FindCommand());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new BgcCommand());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new ButilisalsCommand());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new ServerCommand());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new MSGCommand());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCommand());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new SpyCommand());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new LocalSpyCommand());
	    if(getConfig().getBoolean("ChatLock.ChatCommand.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new ChatCommand());
	    }

	    if(getConfig().getBoolean("ClearChat.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClearChatCommand());
	    }
	    if(getConfig().getBoolean("Hub.Enabled")){
	    	String cmd = getConfig().getString("Hub.Command");
	    	if(cmd.contains(";")){
	    		String c = cmd.split(";")[0];
	    		String ncmd = cmd.replace(c + ";", "");
	    		String[] cmds;
	    		if(ncmd.contains(";")){
	    			cmds = ncmd.split(";");
	    		} else {
	    			cmds = new String[]{ncmd};
	    		}
	    		
	    		ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand(c, cmds));
	    	} else {
		    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand(cmd));
	    	}
	    }
	    if(getConfig().getBoolean("Vote.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new VoteCommand());
	    }
	    if(getConfig().getBoolean("Store.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new StoreCommand());
	    }
	    if(getConfig().getBoolean("Rules.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new RulesCommand());
	    }
	}
	
	private void registerEvents(){
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new MessageLimiter(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new DisconnectEvent());
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new LoginEvent(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiSpam(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatLock(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatUtilities(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiSwear());
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiCaps(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiAd(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessageReceive());
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new PrivateMessageListener());
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new CommandListener());
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatListener());
	
	}
	
	public void onDisable(){
	    dbmanager.closeConnection();
	    	    
	    ProxyServer.getInstance().getLogger().info("BungeeUtilisals is now Disabled!");
	}
	
	public static BungeeUtilisals getInstance(){
		return instance;
	}
	
    public File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists()){
            folder.mkdir();
        }
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource); OutputStream out = new FileOutputStream(resourceFile)){
                	ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
    
    public class ConfigData {
    	
    	public Boolean UUIDSTORAGE, BUKKITPERMISSIONS, UPDATECHECKER;
    	public String PREFIX;
    	
    	public ConfigData(){
    		this.UUIDSTORAGE = BungeeUtilisals.getInstance().getConfig().getBoolean("UUID-Storage");
    		this.BUKKITPERMISSIONS = BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions");
    		this.UPDATECHECKER = BungeeUtilisals.getInstance().getConfig().getBoolean("UpdateChecker");
    		this.PREFIX = BungeeUtilisals.getInstance().getConfig().getString("Prefix");
    	}
    	
    }
}
