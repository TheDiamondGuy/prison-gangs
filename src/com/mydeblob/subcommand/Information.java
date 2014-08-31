package com.mydeblob.subcommand;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mydeblob.prisongangs.Gang;
import com.mydeblob.prisongangs.GangManager;
import com.mydeblob.prisongangs.Rank;

public class Information {
	private static GangManager gm = GangManager.getGangManager();
	private Player player = null;
	private CommandSender sender = null;
	private List<String> args = null;
	private String subcommand = null;
	private Gang g = null;
	
	/**
	 * The base constructor for the information class (How it gets it's information, used only in GangCommand class)
	 * @param player - The player who sent the command (Can be null)
	 * @param sender - The command sender who sent the command
	 * @param args - All the additional args of the command (Not including the subcommand, indexed at 0)
	 * @param subcommand - The subcommand
	 */
	public Information(Player player, CommandSender sender, List<String> args, String subcommand){
		this.player = player;
		this.sender = sender;
		this.args = args;
		this.subcommand = subcommand;
		g = gm.getGangWithPlayer(player);
	}
	//Please note that some of these methods are duplicates from GangManager. This is so I can use the methods easier when creating commands
	
	/**
	 * Gets the player associated with the command
	 * @return - NULL if the sender isn't a player, a player object otherwise
	 */
	public Player getPlayer(){
		return this.player;
	}
	
	/**
	 * Gets the command sender
	 * @return - CommandSender who is associated with the command
	 */
	public CommandSender getSender(){
		return this.sender;
	}
	
	/**
	 * Gets all the arguments for the command (Not including the sub command)
	 * @return - A list of all the arguments
	 */
	public List<String> getArgs(){
		return this.args;
	}
	
	/**
	 * Gets the sub command for the call
	 * @return - The sub command in type String
	 */
	public String getSubCommand(){
		return this.subcommand;
	}
	
	/**
	 * Checks if the player has a gang
	 * @return - TRUE if the player has a gang, FALSE if he doesn't, NULL if the sender isn't a player
	 */
	public boolean hasGang(){
		if(g == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Gets the gang associated with the player who typed this command
	 * @return - NULL if the sender isn't a player or the player doesn't have a gang, gang otherwise
	 */
	public Gang getGang(){
		return this.g;
	}
	
	/**
	 * Gets the gang name associated with the player who typed this command
	 * @return - NULL if the sender isn't a player or doesn't have a gang, a string of the gang name otherwise
	 */
	public String getGangName(){
		if(this.g == null){
			return null;
		}else{
			return this.g.getName();
		}
	}
	
	/**
	 * Gets the rank of the player who typed this command
	 * @return - NULL if the sender wasn't a player or doesn't have a gang, a rank otherwise
	 */
	public Rank getRank(){
		if(this.g == null){
			return null;
		}else{
			return gm.getPlayerRank(this.player, this.g);
		}
	}
	
	/**
	 * Check if the sub command has additional args
	 * @return a bool true if it does, otherwise false
	 */
	public boolean hasArgs(){
		if(this.args.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
}
