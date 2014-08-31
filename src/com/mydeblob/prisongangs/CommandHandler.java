package com.mydeblob.prisongangs;

import java.util.Arrays;

import com.mydeblob.subcommand.Execute;
import com.mydeblob.subcommand.GangCommand;
import com.mydeblob.subcommand.Information;



public class CommandHandler{
	private PrisonGangs plugin;
	public static final GangManager gm = GangManager.getGangManager();
	public static final FileManager f = FileManager.getFileManager();
	public CommandHandler(PrisonGangs plugin){
		this.plugin = plugin;
	}

	public void setupCommands(){
		GangCommand gCmd = new GangCommand();
		plugin.getCommand("gang").setExecutor(gCmd); //Register /gang as the base command; g is defined in the plugin.yml as an alias
		
		/**
		 * KDR Command
		 */
		gCmd.addSubCommand("kdr", null, "gangs.user")
		.setMultiplePermissions(Arrays.asList("gangs.kdr", "gangs.admin"))
		.setExecutor(new Execute(){
			public void execute(Information info){
				
			}
		});
	}

}