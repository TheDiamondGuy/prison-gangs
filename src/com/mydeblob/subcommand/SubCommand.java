package com.mydeblob.subcommand;

import java.util.ArrayList;
import java.util.List;

import com.mydeblob.prisongangs.Rank;

public class SubCommand {
	private final String name;
    private final String permission;
    private boolean allowConsole = false;
    private Rank minRank;
    private boolean mininumRank = false;
    private int minArgs = 1; //Index starting at 1
    private boolean multiplePerms = false;
    private Execute executor = null;
    private ArrayList<String> allPermissions =  new ArrayList<String>();
    
    /**
     * Create a new sub command
     * @param name - The name of the sub command
     * @param permission - The permission for the command, null if there is no permission
     */
    public SubCommand(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }
    
    /**
     * Gets the sub commands name
     * @return String - The name of the sub command
     */
    public String getName(){
    	return this.name;
    }
    
    /**
     * Gets the sub commands permission
     * @return String - The permission for the subcommand
     */
    public String getPerm(){
    	return this.permission;
    }
    
    /**
     * Sets if console usage is allowed for this sub command
     * @return subcommand - Used for chaining methods
     */
    public SubCommand allowConsole(){
    	this.allowConsole = true;
    	return this;
    }
    
    /**
     * Gets if the sub command is allowed to be used in the console
     * @return boolean - TRUE if it can be used in console, FALSE otherwise
     */
    public boolean isConsoleAllowed(){
    	return this.allowConsole;
    }
    
    /**
     * Sets a mininum rank required in the gang
     * @param rank - The rank required to use the command
     * @return subcommand - Used for chaining methods
     */
    public SubCommand setMinRank(Rank rank){
    	this.mininumRank = true;
    	this.minRank = rank;
    	return this;
    }
    
    /**
     * Gets if the sub command requires a gang rank to perform the command
     * @return boolean - TRUE if it requires a mininum rank, FALSE otherwise
     */
    public boolean requiresRank(){
    	return this.mininumRank;
    }
    
    /**
     * Gets the mininum gang rank required to perform this command
     * @return - NULL if the command doesn't require a mininum rank, ENUM value of type RANK otherwise
     */
    public Rank getMininumRank(){
    	if(!this.requiresRank()){
    		return null;
    	}else{
    		return this.minRank;
    	}
    }
    
    /**
     * Gets the mininum args to perform the command, indexed at 1 (I.e starting at 1)
     * @return int - The mininum amount of args needed for this command, 1 by default
     */
    public int getMininumArgs(){
    	return this.minArgs;
    }
    
    /**
     * Sets the mininum amount of args required for this command, starting at 1
     * @param args - The amount of args needed (Starting at 1, i.e if I had the base command /g and the sub command /invite name I would need a mininum args of 2)
     * @return subcommand - Used for chaining methods
     */
    public SubCommand setMininumArgs(int args){
    	this.minArgs = args;
    	return this;
    }
    
    /**
     * Sets the commands executor
     * @param executor
     */
    public void setExecutor(Execute executor){
    	this.executor = executor;
    }
    
    /**
     * Gets the commands executor
     * @return - Executor if it has been set, NULL otherwise
     */
    public Execute getExecutor(){
    	return this.executor;
    }
    /**
     * Sets multiple permissions for the command
     * @param list - The permissions to be added (Don't include the default permission when you created the handler)
     * @return subcommand - Used for chaining methods
     */
    public SubCommand setMultiplePermissions(List<String> list){
    	this.multiplePerms = true;
    	this.allPermissions.add(this.permission); //We want to add the default permission in here
    	this.allPermissions.addAll(list);
    	return this;
    }
    
    /**
     * Gets wether or not there are multiple permissions
     * @return boolean - TRUE if there is multiple permissions, FALSE otherwise
     */
    public boolean hasMultiplePermissions(){
    	return this.multiplePerms;
    }
    
    /**
     * Gets all the permissions
     * @return - NULL if there isn't multiple permissions, ArrayList<String> of the permissions otherwise
     */
    public ArrayList<String> getAllPermissions(){
    	if(this.multiplePerms){
    		return this.allPermissions;
    	}else{
    		return null;
    	}
    }
    
    
}
