package com.dbsoftware.bungeeutilisals.bungee;

import java.sql.ResultSet;
import com.dbsoftware.bungeeutilisals.bungee.utils.MySQL;
import com.dbsoftware.bungeeutilisals.bungee.utils.MySQL.WhereType;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeUser {
	
	ProxiedPlayer p;
	BungeeRank rank;
	Boolean socialspy,localspy;
	
	public BungeeUser(ProxiedPlayer p){
		this.p = p;
		this.socialspy = false;
		this.localspy = false;
		loadRank();
	}
	
	public void sendMessage(String message){
		this.getPlayer().sendMessage(Utils.format(message));
	}
	
	public ProxiedPlayer getPlayer(){
		return p;
	}
	
	public void setPlayer(ProxiedPlayer p){
		this.p = p;
	}
	
	public Boolean isSocialSpy(){
		return socialspy;
	}
	
	public void setSocialSpy(Boolean b){
		this.socialspy = b;
	}
	
	public Boolean isLocalSpy(){
		return localspy;
	}
	
	public void setLocalSpy(Boolean b){
		this.localspy = b;
	}
	
	public Boolean isStaff(){
		return rank.equals(BungeeRank.STAFF);
	}
	
	public static void setRank(final String name, final BungeeRank rank){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				try {
					if(rank.equals(BungeeRank.STAFF)){
						if(!MySQL.getInstance().select().table("Staffs").column("Name").wheretype(WhereType.EQUALS).where("Name").wherereq(name.toLowerCase()).select().next()){
							MySQL.getInstance().insert().single().table("Staffs").column("Name").value(name.toLowerCase()).insert();
						}
					} else {
						if(MySQL.getInstance().select().table("Staffs").column("Name").wheretype(WhereType.EQUALS).where("Name").wherereq(name.toLowerCase()).select().next()){
							MySQL.getInstance().delete().table("Staffs").where("Name").equals(name.toLowerCase()).delete();
						}
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			
		};
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), r);
	}
	
	public void loadRank(){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				try {
					ResultSet rs = MySQL.getInstance().select().table("Staffs").column("Name").wheretype(WhereType.EQUALS).where("Name").wherereq(p.getName().toLowerCase()).select();
					if(rs.next()){
						rank = BungeeRank.STAFF;
					} else {
						rank = BungeeRank.GUEST;
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), r);
	}
	
	public BungeeRank getRank(){
		return rank;
	}
}
