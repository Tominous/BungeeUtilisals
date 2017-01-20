package com.dbsoftware.bungeeutilisals.bungee.punishment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;

import net.md_5.bungee.api.ProxyServer;

public class MuteAPI {

	private static DatabaseManager dbmanager = BungeeUtilisals.getInstance().getDatabaseManager();

	public static List<String> getMutes() {
		List<String> servers = new ArrayList<String>();
		try {
			PreparedStatement preparedStatement = dbmanager.getConnection().prepareStatement("SELECT * FROM `Mutes`;");

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				servers.add(rs.getString("Muted"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return servers;
	}

	public static boolean isMuted(String player) {
		MuteInfo info = Punishments.getMuteInfo(player);
		if(info != null){
			return true;
		}
		try {
			PreparedStatement preparedStatement = dbmanager.getConnection().prepareStatement("SELECT `Reason` FROM `Mutes` WHERE `Muted` = ?;");
			preparedStatement.setString(1, player);

			ResultSet rs = preparedStatement.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void addMute(final String muted_by, final String muted, final Long mute_time, final String reason) {
		ProxyServer.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), new Runnable(){
			public void run() {
				try {
					PreparedStatement preparedStatement = dbmanager.getConnection().prepareStatement("INSERT INTO Mutes(`MutedBy`, `Muted`, `MuteTime`, `Reason`) VALUES (?, ?, ?, ?);");
					preparedStatement.setString(1, muted_by);
					preparedStatement.setString(2, muted);
					preparedStatement.setLong(3, mute_time);
					preparedStatement.setString(4, reason);

					preparedStatement.executeUpdate();
					
					Punishments.mutes.add(new MuteInfo(muted, muted_by, mute_time, reason));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void removeMute(final String player) {
		ProxyServer.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), new Runnable(){
			public void run() {
				try {
					PreparedStatement preparedStatement = dbmanager.getConnection()
							.prepareStatement("DELETE FROM `Mutes` WHERE `Muted` = ?;");
					preparedStatement.setString(1, player);

					preparedStatement.executeUpdate();
					MuteInfo info = Punishments.getMuteInfo(player);
					if(info != null){
						Punishments.mutes.remove(info);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static String getMutedBy(String player) {
		MuteInfo info = Punishments.getMuteInfo(player);
		if(info != null){
			return info.getBy();
		}
		String playername = "";
		try {
			PreparedStatement preparedStatement = dbmanager.getConnection().prepareStatement("SELECT `MutedBy` FROM `Mutes` WHERE `Muted` = ?;");
			preparedStatement.setString(1, player);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				playername = rs.getString("MutedBy");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return playername;
	}

	public static Long getMuteTime(String player) {
		MuteInfo info = Punishments.getMuteInfo(player);
		if(info != null){
			return info.getTime();
		}
		try {
			PreparedStatement preparedStatement = dbmanager.getConnection().prepareStatement("SELECT `MuteTime` FROM `Mutes` WHERE `Muted` = ?;");
			preparedStatement.setString(1, player);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return rs.getLong("MuteTime");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1L;
	}

	public static String getReason(String player) {
		MuteInfo info = Punishments.getMuteInfo(player);
		if(info != null){
			return info.getReason();
		}
		String reason = "";
		try {
			PreparedStatement preparedStatement = dbmanager.getConnection().prepareStatement("SELECT `Reason` FROM `Mutes` WHERE `Muted` = ?;");
			preparedStatement.setString(1, player);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				reason = rs.getString("Reason");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reason;
	}
}