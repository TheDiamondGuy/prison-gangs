package com.mydeblob.subcommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mydeblob.prisongangs.CommandHandler;
import com.mydeblob.prisongangs.Gang;
import com.mydeblob.prisongangs.GangManager;
import com.mydeblob.prisongangs.Lang;
import com.mydeblob.prisongangs.Rank;

public class GangCommand extends SubCommand implements CommandExecutor{

	private HashMap<String, SubCommand> subs = new HashMap<String, SubCommand>();
	public GangCommand(String name, String permission) {
		super(name, permission);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Adds a subcommand to the main command
	 * @param name - Name of the sub command
	 * @param aliases - Aliases of the sub command (Set to NULL
	 * @param permission - Main permission of the sub command (Can add more later)
	 * @return subcommand - Used for chaining
	 */
	public SubCommand addSubCommand(String name, ArrayList<String> aliases, String permission){
		SubCommand command = new SubCommand(name, permission);
		subs.put(name, command);
		if(!aliases.isEmpty() || aliases != null){
			for(String s:aliases){
				subs.put(s, command);
			}
		}
		return command;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		List<String> callArgs = new ArrayList<String>(Arrays.asList(args));
		handleCommand(sender, commandLabel, callArgs);
		return false;
	}

	public void handleCommand(CommandSender sender, String commandLabel, List<String> args){
		Player p = null;
		if(sender instanceof Player){
			p = (Player) sender;
			commandLabel = "/" + commandLabel; //Keep in mind that players have to add a slash in front of commands; consoles don't
		}
		if(args.size() == 0){ //Show them the help menu
			CommandHandler.showHelpMenu();
			return;
		}
		String name = args.get(0).toLowerCase(); //The sub command name
		SubCommand sub = subs.get(name);
		if(sub == null){
			CommandHandler.showHelpMenu();
			return;
		}
		if(sub.multiplePermissions()){
			for(String s:sub.getAllPermissions()){
				if(!sender.hasPermission(s)){
					sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS.toString());
					return;
				}
			}
		}else{
			if(!sender.hasPermission(sub.getPerm())){
				sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS.toString());
				return;
			}
		}
		if(!sub.isConsoleAllowed() && p != null){ //P will always be null if it is sent from the console
			sender.sendMessage(Lang.PREFIX.toString() + ChatColor.RED + "This command can only be performed by a player in-game!"); //I don't like hardcoded messages but... We don't want the user to change this
			return;
		}
		if(args.size() < sub.getMininumArgs()){ 
			sender.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString());
			return;
		}
		if(sub.requiresRank()){ //This will only happen if the player is a sender
			if(GangManager.getGangManager().getGangWithPlayer(p) == null){
				p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString());
				return;
			}
			if(!validRank(p, sub)){
				p.sendMessage(Lang.PREFIX.toString() + LANG_NO_PERMS_RANK_GENERAL);
				return;
			}
		}
		List<String> callArgs = new ArrayList<String>(args.subList(1, args.size())); //Remove sub command from arg list
		

	}

	public boolean validRank(Player p, SubCommand sub){
		Gang g = GangManager.getGangManager().getGangWithPlayer(p);
		Rank r = GangManager.getGangManager().getPlayerRank(p.getName(), g);
		Rank minR = sub.getMininumRank();
		switch(minR){
		case MEMBER:
			return true;
		case TRUSTED:
			if(r != Rank.MEMBER){
				return true;
			}
			return false;
		case OFFICER:
			if(r != Rank.MEMBER || r != Rank.TRUSTED){
				return true;
			}
			return false;
		case LEADER:
			if(r != Rank.MEMBER || r != Rank.TRUSTED || r != Rank.OFFICER){
				return true;
			}
			return false;
		case OWNER:
			return true;
		default: //Should never happen, but you should always have a default case
			Bukkit.getServer().getLogger().log(Level.WARNING, "[PrisonGangs] Something bad has happened. Please report this to the author! Error: 1");
			return false;
		}
	}
}